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
import com.example.kittenappscollage.helpers.db.aller.ActionsContentPerms;
import com.example.kittenappscollage.helpers.db.aller.ContentPermis;
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
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

import static com.example.kittenappscollage.helpers.Massages.LYTE;

public class FragmentScanAllImages extends Fragment {

    private HashMap<String,ArrayList<String>>listImagesToFolder;

    private HashMap<String,String>listFolds;

    private HashMap<String,String>listPerms;

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
        final int takeFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION;
        for (UriPermission u : getContext().getContentResolver().getPersistedUriPermissions()) {
            DocumentFile df = DocumentFile.fromTreeUri(getContext(), u.getUri());
            if (df.exists() && df.isDirectory()) {
               addImgsInFold(u,df,emitter);
            } else {
                getContext().getContentResolver().releasePersistableUriPermission(u.getUri(), takeFlags);
            }
        }
        emitter.onNext(getListImagesInFolders());
        emitter.onComplete();
    }

    private void addImgsInFold(UriPermission u, DocumentFile df,ObservableEmitter<HashMap<String, ArrayList<String>>> emitter){
        final String keyAndPerm = df.getUri().toString();
        final String name = df.getName();
        int iterator = 0;
//        getListImagesInFolders().remove(keyAndPerm);
        for (DocumentFile f:df.listFiles()){
            if(f.isFile()) {
                final String type = f.getType();
                if (type.equals("image/png") || type.equals("image/jpeg") || type.equals("image/jpg")) {
                    final long date = f.lastModified();
                    if(!getListImagesInFolders().containsKey(keyAndPerm)){
                        addInScan(keyAndPerm, f.getUri().toString(), name, keyAndPerm, date);
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
            int takeFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION;
            try {
                getContext().getContentResolver().releasePersistableUriPermission(u.getUri(), takeFlags);
            }catch (SecurityException e){
               LYTE("FragmentScanAllImages SecurityException e "+e.toString());
            }
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
            getListPerms().put(key,permis);
        }
    }

    /*android 9 storage system*/
    public void setSavingInStorageCollage(Uri uri, String report, String delimiter){
        String[]split = report.split(delimiter);
        DocumentFile fold = DocumentFile.fromTreeUri(getContext(),Uri.parse(split[SavedKollagesFragmentDraw.INDEX_URI_PERM_FOLD]));
        ActionsContentPerms.create(getContext().getApplicationContext()).queryItemDB(
                fold.getUri().toString(),
                fold.getUri().toString(),
                split[SavedKollagesFragmentDraw.INDEX_URI_DF_FOLD],
                ActionsContentPerms.SYS_DF,
                1,
                View.VISIBLE);
        addImgCollect(
                fold.getUri().toString(),
                split[SavedKollagesFragmentDraw.INDEX_URI_DF_IMG],
                fold.getName(),
                fold.getUri().toString(),
                fold.lastModified());
        setListImagesInFolders(getListImagesInFolders());
    }

    /*android 9 file system*/
    public void setSavingInFileCollage(Uri uri,String pathImg,String pathFold){

        Cursor cursor = getContext().getContentResolver().query(uri,projection(),null,null,null);

        final int col_id_fold = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_ID);
        final int col_name_fold = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
        final int col_date_mod = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_MODIFIED);
        cursor.moveToFirst();
        final String id_fold = "" + cursor.getLong(col_id_fold);
        final String nameFold = cursor.getString(col_name_fold);
        final long date_mod = cursor.getLong(col_date_mod);

        ActionsContentPerms.create(getContext()).queryItemDB(
                id_fold,
                ActionsContentPerms.GRAND,
                ActionsContentPerms.ZHOPA,
                ActionsContentPerms.SYS_FILE,
                0,
                View.VISIBLE);

        addImgCollect(
                id_fold,
                uri.toString(),
                nameFold,
                ActionsContentPerms.GRAND,
                date_mod);
        setListImagesInFolders(getListImagesInFolders());
    }

    protected Uri question(){
        Uri uri = null;
        if(App.checkVersion())uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        else uri = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL);
        return uri;
    }

    private String[]projection(){
        return new String[]{
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.MIME_TYPE,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Images.Media.DATE_MODIFIED};
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
            correctAdapterPostSave();
            ArrayList<String> imgs = new ArrayList<>();
            imgs.add(img);
            getListImagesInFolders().put(key, imgs);
            getListFolds().put(key,fold);
            getListPerms().put(key,permis);
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

    protected HashMap<String,String>getListPerms(){
        return listPerms;
    }

    protected void clearListImagesInFolders(){
        listImagesToFolder.clear();
        listFolds.clear();
        listPerms.clear();
        listMutable.clear();
    }

    protected void initListImagesInFolders(){
        listImagesToFolder = new HashMap<String, ArrayList<String>>();
        listFolds = new HashMap<String,String>();
        listPerms = new HashMap<String,String>();
        listMutable = new HashMap<String,Long>();

    }

}
