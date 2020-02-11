package com.example.kittenappscollage.collect.fragment;

import android.annotation.SuppressLint;
import android.content.ContentUris;
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
import com.example.kittenappscollage.helpers.db.aller.ActionsContentPerms;
import com.example.kittenappscollage.helpers.db.aller.ContentPermis;
import com.example.kittenappscollage.helpers.rx.ThreadTransformers;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;

import static com.example.kittenappscollage.draw.fragment.SavedKollagesFragmentDraw.INDEX_PATH_FOLD;
import static com.example.kittenappscollage.draw.fragment.SavedKollagesFragmentDraw.INDEX_PATH_IMG;
import static com.example.kittenappscollage.draw.fragment.SavedKollagesFragmentDraw.INDEX_URI_DF_IMG;
import static com.example.kittenappscollage.draw.fragment.SavedKollagesFragmentDraw.INDEX_URI_PERM_FOLD;
import static com.example.kittenappscollage.helpers.Massages.LYTE;

public class FragmentScanAllImages extends Fragment {

    private HashMap<String,ArrayList<String>>listImagesToFolder;

    private HashMap<String,String>listFolds;

    private HashMap<String, Integer>listStorages;

    private HashMap<String,String>listPerms;

    private ArrayList<String> storage;

    private boolean blockScan;

    public void scanDevice(){
        if(!blockScan){
            if(App.checkVersion())checkStepsSearchCursor();
        }
    }

    public void setBlock(boolean block){
        blockScan = block;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        blockScan = false;
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @SuppressLint("CheckResult")
    public void checkStepsSearchCursor(){
        definitionStorage();
        if(getListImagesInFolders()==null) initListImagesInFolders();
        else clearListImagesInFolders();
        Observable.create(new ObservableOnSubscribe<Cursor>() {
            @Override
            public void subscribe(ObservableEmitter<Cursor> emitter) throws Exception {
                emitter.onNext(getCursorApi29());
                emitter.onComplete();
            }
        }).compose(new ThreadTransformers.InputOutput<>())
                .subscribe(new Consumer<Cursor>() {
                    @Override
                    public void accept(Cursor cursor) throws Exception {
                        scanFoldApi29(cursor);
                    }
                });
    }

    /*android 9 storage system*/
    public void setSavingInStorageCollage(Uri uri, String report, String delimiter){
        String[] split = report.split(delimiter);
        getListPerms().put(split[INDEX_PATH_FOLD],split[INDEX_URI_PERM_FOLD]);
        String system = split[SavedKollagesFragmentDraw.INDEX_TYPE_SYSTEM];
        if(system.equals(ActionsContentPerms.ZHOPA)){
         correctSavingFoldInStorage(split);
        }else addImgCollect(split[INDEX_PATH_FOLD],split[INDEX_URI_DF_IMG]);
    }

    /*android > 9 or storage*/
    public void setSavingInStorageCollage(Uri uri){

    }

    /*android 9 file system*/
    public void setSavingInFileCollage(String path,String key){
        ActionsContentPerms.create(getContext()).queryItemDB(
                key,
                ActionsContentPerms.GRAND,
                ActionsContentPerms.ZHOPA,
                ActionsContentPerms.SYS_FILE,
                ActionsContentPerms.NON_LOC_STOR,
                View.VISIBLE);
        getListPerms().put(key,ActionsContentPerms.GRAND);
        addImgCollect(key,Uri.parse(path).toString());
    }

    @SuppressLint("CheckResult")
    private void correctSavingFoldInStorage(String[]split){
        Observable.create(new ObservableOnSubscribe<HashMap<String, ArrayList<String>>>() {
            @Override
            public void subscribe(ObservableEmitter<HashMap<String, ArrayList<String>>> emitter) throws Exception {
                String udf = split[SavedKollagesFragmentDraw.INDEX_URI_DF_FOLD];
                String key = split[SavedKollagesFragmentDraw.INDEX_PATH_FOLD];
                emitter.onNext(scanFoldStorageAPI29(getListImagesInFolders(),udf,key));
                emitter.onComplete();
            }
        }).compose(new ThreadTransformers.InputOutput<>())
                .subscribe(new Consumer<HashMap<String, ArrayList<String>>>() {
                    @Override
                    public void accept(HashMap<String, ArrayList<String>> stringArrayListHashMap) throws Exception {
                        setListImagesInFolders(stringArrayListHashMap);
                    }
                });
    }
    @SuppressLint("CheckResult")
    private void scanFoldApi29(Cursor cursor){
       Observable.create(new ObservableOnSubscribe<HashMap<String, ArrayList<String>>>() {
           @Override
           public void subscribe(ObservableEmitter<HashMap<String, ArrayList<String>>> emitter) throws Exception {
               final int col_path = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
               final int col_fold = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
               final int col_mime = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.MIME_TYPE);
               String etalon = "";
               cursor.moveToFirst();
               while (cursor.moveToNext()){
                   final String mime = cursor.getString(col_mime);
                   final String key = cursor.getString(col_path).split(cursor.getString(col_fold))[0]+cursor.getString(col_fold);
                   if(mime.equals("image/png")||mime.equals("image/jpeg")||mime.equals("image/jpg")){

                       ContentPermis cp = getContentPermis(key);
                       if(cp.system.equals(ActionsContentPerms.SYS_DF)&&
                               !cp.uriPerm.equals(ActionsContentPerms.GRAND)&&
                               !cp.uriPerm.equals(ActionsContentPerms.NON_PERM)) {
                           if(!getListImagesInFolders().containsKey(key)){
                               getListPerms().put(key,cp.uriPerm);
                               getIndexesStorage().put(key,cp.storage);
                               getListFolds().put(key,cursor.getString(col_fold));
                               if(cp.visible==View.VISIBLE)emitter.onNext(scanStorageAPI29(getListImagesInFolders(), cp, key));
                           }
                       }else if(cp.storage==0){
                           if(!getListImagesInFolders().containsKey(key)){
                               getListPerms().put(key,cp.uriPerm);
                               getIndexesStorage().put(key,cp.storage);
                               getListFolds().put(key,cursor.getString(col_fold));
                               if(cp.visible==View.VISIBLE)emitter.onNext(scanFileAPI29(getListImagesInFolders(),key));
                           }
                       }else {
                           if(!getListImagesInFolders().containsKey(key)){
                               getListPerms().put(key,cp.uriPerm);
                               getIndexesStorage().put(key,cp.storage);
                               getListFolds().put(key,cursor.getString(col_fold));
                               if(cp.visible==View.VISIBLE){
                                   putNonPermStorageFiles(cursor.getString(col_path),cursor.getString(col_fold),cp);
                               }
                           }else {
                               getListImagesInFolders().get(key).add(Uri.parse(cursor.getString(col_path)).toString());
                           }
                       }

                   }

               }
               emitter.onNext(getListImagesInFolders());
               emitter.onComplete();
           }
       }).compose(new ThreadTransformers.Processor<>())
               .subscribe(new Consumer<HashMap<String, ArrayList<String>>>() {
                   @Override
                   public void accept(HashMap<String, ArrayList<String>> stringArrayListHashMap) throws Exception {
                       setListImagesInFolders(stringArrayListHashMap);
                   }
               });

    }

    private Cursor getCursorApi29(){
        String[] projection = {
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Images.Media.MIME_TYPE};

        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        Cursor cursor = getContext().getContentResolver().query(uri, projection, null,
                null, null);
        return cursor;
    }

    private void putNonPermStorageFiles(String path, String fold, ContentPermis cp){
        ArrayList<String>images = new ArrayList<>();
        images.add(Uri.parse(path).toString());
        getListImagesInFolders().put(cp.keyPath, images);
        getListFolds().put(cp.keyPath,fold);
        getIndexesStorage().put(cp.keyPath,cp.storage);
    }

    private HashMap<String, ArrayList<String>>scanFileAPI29(HashMap<String,ArrayList<String>>list, String key){
        File[]files = new File(key).listFiles();
        ArrayList<String>images = new ArrayList<>();
        for (File f:files){
            images.add(Uri.parse(f.getAbsolutePath()).toString());
        }
        list.put(key,images);

        return list;
    }

    private HashMap<String,ArrayList<String>>scanStorageAPI29(HashMap<String,ArrayList<String>>list, ContentPermis cp, String key){
        DocumentFile df = DocumentFile.fromTreeUri(getContext(),Uri.parse(cp.uriDocFile));
        DocumentFile[]files = df.listFiles();
        ArrayList<String>images = new ArrayList<>();
        for(DocumentFile f:files){
            images.add(f.getUri().toString());
        }
        list.put(key,images);
        return list;
    }

    private HashMap<String,ArrayList<String>>scanFoldStorageAPI29(HashMap<String,ArrayList<String>>list,String udf,String key){
        list.remove(key);
        DocumentFile df = DocumentFile.fromTreeUri(getContext(),Uri.parse(udf));
        DocumentFile[]files = df.listFiles();
        ArrayList<String>images = new ArrayList<>();
        for(DocumentFile f:files){
            images.add(f.getUri().toString());
        }
        list.put(key,images);
        return list;
    }

    private ContentPermis getContentPermis(String key){
        ContentPermis cp = ActionsContentPerms.create(getContext()).getItem(key);
        if(cp!=null)return cp;
        else {
            ActionsContentPerms.create(getContext()).queryItemDB(
                    key,
                    ActionsContentPerms.NON_PERM,
                    ActionsContentPerms.ZHOPA,
                    ActionsContentPerms.ZHOPA,
                    ActionsContentPerms.NON_LOC_STOR,
                    View.VISIBLE);

            return ActionsContentPerms.create(getContext()).getItem(key);
        }
    }


    /*определяем тома в устройстве*/
    private void definitionStorage(){

        File[] files = ContextCompat.getExternalFilesDirs(getContext(), null);
        storage = new ArrayList<>();
        for (int i=0;i<files.length;i++){
            storage.add(files[i].getAbsolutePath().split("Android")[0]);
        }
    }

    private void correctListFold(String[]split){

    }

       private void addImgCollect(String key, String path){
        if(getListImagesInFolders().containsKey(key)){
            if(!getListImagesInFolders().get(key).contains(path))getListImagesInFolders().get(key).add(path);
        } else {
            correctAdapterPostSave();
            ArrayList<String> imgs = new ArrayList<>();
            imgs.add(path);
            getListImagesInFolders().put(key, imgs);

            String[]split = key.split("[/]");

            getListFolds().put(key,split[split.length-1]);

            for(int i=0;i<getNamesStorage().size();i++) {
                if(key.contains(getNamesStorage().get(i))){
                    getIndexesStorage().put(key, getNamesStorage().indexOf(getNamesStorage().get(i)));
                }
            }
        }
        setListImagesInFolders(getListImagesInFolders());
    }
    protected void correctAdapterPostSave(){
        /*если в момент сохранения окрыт подкаталог галереи,
        * то при создании директории индекс открыто не изменяется
        * и отображается предыдущая по порядку*/
    }

    protected void setListImagesInFolders(HashMap<String,ArrayList<String>> list){
         listImagesToFolder = list;
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

    /*индекс хранилища из getNamesStorage()*/
    protected HashMap<String,Integer>getIndexesStorage(){
        return listStorages;
    }

    protected HashMap<String,String>getListPerms(){
        return listPerms;
    }

    protected void clearListImagesInFolders(){
        listImagesToFolder.clear();
        listFolds.clear();
        listStorages.clear();
        listPerms.clear();
    }

    protected void initListImagesInFolders(){
        listImagesToFolder = new HashMap<>();
        listFolds = new HashMap<>();
        listStorages = new HashMap<>();
        listPerms = new HashMap<>();

    }

}
