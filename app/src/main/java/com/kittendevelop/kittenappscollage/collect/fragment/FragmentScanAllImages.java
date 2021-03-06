package com.kittendevelop.kittenappscollage.collect.fragment;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.kittendevelop.kittenappscollage.R;
import com.kittendevelop.kittenappscollage.draw.fragment.SavedKollagesFragmentDraw;
import com.kittendevelop.kittenappscollage.helpers.App;
import com.kittendevelop.kittenappscollage.helpers.Massages;
import com.kittendevelop.kittenappscollage.helpers.dbPerms.Permis;
import com.kittendevelop.kittenappscollage.helpers.dbPerms.WorkDBPerms;
import com.kittendevelop.kittenappscollage.helpers.rx.ThreadTransformers;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

public class FragmentScanAllImages extends Fragment {

    private HashMap<String,ArrayList<String>>listImagesToFolder;

    private HashMap<String,String>listFolds;

    private HashMap<String,Long>listMutable;

    private ArrayList<String> storage;

    private boolean blockScan;


    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        blockScan = false;
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void scanDevice(){
        if(!blockScan){
            scanFoldAll();
        }
    }

    public void setBlock(boolean block){
        blockScan = block;
    }

    @SuppressLint("CheckResult")
    private void scanFoldAll(){
        definitionStorage();
        if(getListImagesInFolders()==null) initListImagesInFolders();
        else clearListImagesInFolder();
                      Observable.create((ObservableOnSubscribe<HashMap<String, ArrayList<String>>>) emitter -> {
                          scanAvailablePermissions(emitter);
                      }).compose(new ThreadTransformers.InputOutput<>())
//                        .doOnComplete(() ->
//                                Massages.SHOW_MASSAGE(getContext(), "Ты можешь увеличить список доступных папок в галерее"))

                              .subscribe(stringArrayListHashMap -> setListImagesInFolders(stringArrayListHashMap));
            }




    private void scanAvailablePermissions(ObservableEmitter<HashMap<String, ArrayList<String>>> emitter) {
            for (Permis p : WorkDBPerms.get().allItems()) {
                addImgsInCursor(p,emitter);
                emitter.onNext(getListImagesInFolders());
            }
            emitter.onNext(getListImagesInFolders());
            emitter.onComplete();
    }

    private void addImgsInCursor(Permis p,ObservableEmitter<HashMap<String, ArrayList<String>>> emitter) {

        final String keyAndPerm = p.uriPerm;
        final String name = p.name;
        final String id = ""+p.id;
             Cursor cursor = null;
        try {
            cursor = getContext().getContentResolver().query(
                    question(),
                    new String[]{MediaStore.Images.Media._ID, MediaStore.Images.Media.DATE_MODIFIED, MediaStore.Images.Media.MIME_TYPE},
                    MediaStore.Images.Media.BUCKET_ID + " = ?",
                    new String[]{id},
                    MediaStore.Images.Media.DATE_MODIFIED);

            int iterator = 0;
            if (cursor.getCount() == 0) {
                WorkDBPerms.get(getContext()).delItem(keyAndPerm);
                return;
            }
            final int col_id = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
            final int col_mime = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.MIME_TYPE);
            final int col_date = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_MODIFIED);

            while (cursor.moveToNext()) {
                final String type = cursor.getString(col_mime);
                if (type.equals("image/png") || type.equals("image/jpeg") || type.equals("image/jpg")) {
                    final String img = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cursor.getLong(col_id)).toString();
                    final long date = cursor.getLong(col_date);
                    addInScan(keyAndPerm, img, name, keyAndPerm, date);
                    if (iterator % 10 == 0) emitter.onNext(getListImagesInFolders());
                }
                iterator++;
            }
            /*а надо ли?*/
        }finally {
            if(cursor!=null)cursor.close();
        }


    }

    protected void addInScan(String key, String img, String fold, String permis,long date){
        addDateMod(key,date);
        if(getListImagesInFolders().containsKey(key)){
            getListImagesInFolders().get(key).add(img);
        } else {
            ArrayList<String> imgs = new ArrayList<>();
            imgs.add(img);
            getListImagesInFolders().put(key, imgs);
            getListFolds().put(key,fold);
        }
    }

    public void setSavingInMedia(Uri uri){

    }

    /*android 9 storage system*/
//    public void setSavingInStorageCollage(Uri uri, String report, String delimiter,long date){
//    }


    protected Uri question(){
        Uri uri = null;
        if(App.checkVersion())uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        else uri = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL);
        return uri;
    }

    /*определяем тома в устройстве*/
    private void definitionStorage(){
        File[] files = ContextCompat.getExternalFilesDirs(getContext(), null);
        storage = new ArrayList<>();
        for (int i=0;i<files.length;i++){
            storage.add(files[i].getAbsolutePath().split("Android")[0]);
        }
    }

    protected void addDateMod(String key, long date){
        if(getListMutable().containsKey(key)){
            long d = getListMutable().get(key);
            if(date>d)getListMutable().put(key,d);
        }else {
            getListMutable().put(key,date);
        }
    }



    protected void addImgCollect(String key, String img, String fold, String permis,long date){
        addDateMod(key,date);
        if(getListImagesInFolders().containsKey(key)){
            getListImagesInFolders().get(key).add(img);
            getListMutable().put(key,date);
        } else {
            ArrayList<String> imgs = new ArrayList<>();
            imgs.add(img);
            getListImagesInFolders().put(key, imgs);
            getListFolds().put(key,fold);
        }
    }

    protected void setListFolds(HashMap<String,String>list){
        listFolds = list;
    }

    protected void setListMutable(HashMap<String,Long>list){
        listMutable = list;
    }

    protected void setListImagesInFolders(HashMap<String,ArrayList<String>> list){
         listImagesToFolder = list;
    }

    public HashMap<String, Long> getListMutable() {
        return listMutable;
    }

    protected HashMap<String,ArrayList<String>> getListImagesInFolders(){
        return listImagesToFolder;
    }

    protected HashMap<String,String>getListFolds(){
        return listFolds;
    }

    protected void clearListImagesInFolder(){
        listImagesToFolder.clear();
        listFolds.clear();
        listMutable.clear();
    }
    protected void initListImagesInFolders(){
        listImagesToFolder = new HashMap<>();
        listFolds = new HashMap<>();
        listMutable = new HashMap<>();

    }

}
