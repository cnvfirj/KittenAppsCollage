package com.example.kittenappscollage.collect.fragment;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Intent;
import android.content.UriPermission;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.Fragment;

import com.example.kittenappscollage.draw.fragment.SavedKollagesFragmentDraw;
import com.example.kittenappscollage.helpers.App;
import com.example.kittenappscollage.helpers.ListenMedia;
import com.example.kittenappscollage.helpers.Massages;
import com.example.kittenappscollage.helpers.dbPerms.Permis;
import com.example.kittenappscollage.helpers.dbPerms.WorkDBPerms;
import com.example.kittenappscollage.helpers.rx.ThreadTransformers;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

import static com.example.kittenappscollage.helpers.Massages.LYTE;

public class FragmentScanAllImages extends Fragment {

    private HashMap<String,ArrayList<String>>listImagesToFolder;

    private HashMap<String,String>listFolds;

//    private HashMap<String,String>listPerms;

    private HashMap<String,Long>listMutable;

    private ArrayList<String> storage;

    private boolean blockScan;

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
                      Observable.create((ObservableOnSubscribe<HashMap<String, ArrayList<String>>>) emitter -> {
                          scanAvailablePermissions(emitter);
                      }).compose(new ThreadTransformers.InputOutput<>())
                        .doOnComplete(() ->
                                Massages.SHOW_MASSAGE(getContext(), "Все доступные папки просканированы. Ты можешь увеличить список доступных папок в галерее"))
                              .subscribe(stringArrayListHashMap -> setListImagesInFolders(stringArrayListHashMap));
            }



    private void scanAvailablePermissions(ObservableEmitter<HashMap<String, ArrayList<String>>> emitter) {

        for (Permis p: WorkDBPerms.get().allItems()){
            DocumentFile df = DocumentFile.fromTreeUri(getContext(), Uri.parse(p.uriPerm));
            if (df.exists() && df.isDirectory()) {
               addImgsInFold(p,df,emitter);
            } else {
                WorkDBPerms.get(getContext()).setAction(WorkDBPerms.DELETE,p.uriPerm);
            }
        }
        emitter.onNext(getListImagesInFolders());
        emitter.onComplete();
    }

    private void addImgsInFold(Permis p,DocumentFile df,ObservableEmitter<HashMap<String, ArrayList<String>>> emitter){
        final String keyAndPerm = df.getUri().toString();
        final String name = df.getName();
        int iterator = 0;
        for (DocumentFile f:df.listFiles()){
            if(f.isFile()) {
                final String type = f.getType();
                if (type.equals("image/png") || type.equals("image/jpeg") || type.equals("image/jpg")) {
                    final long date = f.lastModified();
                    if(!getListImagesInFolders().containsKey(keyAndPerm)){
                        addInScan(keyAndPerm, f.getUri().toString(), name, keyAndPerm, date);
                        iterator++;
                    }else {
                        if(!getListImagesInFolders().get(keyAndPerm).contains(f.getUri().toString())){
                            addInScan(keyAndPerm, f.getUri().toString(), name, keyAndPerm, date);
                            if (iterator % 10 == 0) emitter.onNext(getListImagesInFolders());
                            iterator++;
                        }
                    }
                }
            }
        }

        if(iterator==0){
               WorkDBPerms.get(getContext()).delItem(p.uriPerm);
        }
        emitter.onNext(getListImagesInFolders());
    }

    private void addInScan(String key, String img, String fold, String permis,long date){
        addDateMod(key,date);
        if(getListImagesInFolders().containsKey(key)){
            getListImagesInFolders().get(key).add(img);
            getListMutable().put(key,date);
        } else {
            ArrayList<String> imgs = new ArrayList<>();
            imgs.add(img);
            getListImagesInFolders().put(key, imgs);
            getListFolds().put(key,fold);
//            getListPerms().put(key,permis);
        }
    }

    /*android 9 storage system*/
    public void setSavingInStorageCollage(Uri uri, String report, String delimiter){
        String[]split = report.split(delimiter);
        DocumentFile fold = DocumentFile.fromTreeUri(getContext(),Uri.parse(split[SavedKollagesFragmentDraw.INDEX_URI_PERM_FOLD]));

        addImgCollect(
                fold.getUri().toString(),
                split[SavedKollagesFragmentDraw.INDEX_URI_DF_IMG],
                fold.getName(),
                fold.getUri().toString(),
                fold.lastModified());
        setListImagesInFolders(getListImagesInFolders());
    }



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
//            getListPerms().put(key,permis);
        }
    }


    protected void correctAdapterPostSave(){
        /*если в момент сохранения окрыт подкаталог галереи,
        * то при создании директории индекс открыто не изменяется
        * и отображается предыдущая по порядку*/
    }

    protected void setListImagesInFolders(HashMap<String,ArrayList<String>> list){
         listImagesToFolder = list;
    }

    public HashMap<String, Long> getListMutable() {
        return listMutable;
    }

    protected ArrayList<String>getNamesStorage(){
        return storage;
    }

    protected HashMap<String,ArrayList<String>> getListImagesInFolders(){
        return listImagesToFolder;
    }

    protected HashMap<String,String>getListFolds(){
        return listFolds;
    }

    protected void initListImagesInFolders(){
        listImagesToFolder = new HashMap<String, ArrayList<String>>();
        listFolds = new HashMap<String,String>();
        listMutable = new HashMap<String,Long>();

    }

}
