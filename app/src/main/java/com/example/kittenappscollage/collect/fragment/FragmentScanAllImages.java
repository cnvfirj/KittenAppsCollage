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

import static com.example.kittenappscollage.helpers.Massages.LYTE;

public class FragmentScanAllImages extends Fragment {

    private HashMap<String,ArrayList<String>>listImagesToFolder;

    private HashMap<String,String>listFolds;

    private HashMap<String,String>listPerms;

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
            scanFoldAll(cursor());
        }
    }

    public void setBlock(boolean block){
        blockScan = block;
    }

    @SuppressLint("CheckResult")
    private void scanFoldAll(Cursor cursor){
        definitionStorage();
        if(getListImagesInFolders()==null) initListImagesInFolders();
        else clearListImagesInFolders();

        Observable.create(new ObservableOnSubscribe<HashMap<String, ArrayList<String>>>() {
            @Override
            public void subscribe(ObservableEmitter<HashMap<String, ArrayList<String>>> emitter) throws Exception {
//                cursor.moveToFirst();

                final int col_id_img = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
                final int col_id_fold = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_ID);
                final int col_name_fold = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
                final int col_mime = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.MIME_TYPE);
                while (cursor.moveToNext()) {
                    final String mime = cursor.getString(col_mime);
                    if (mime.equals("image/png") || mime.equals("image/jpg") || mime.equals("image/jpeg")) {
                        final String id_img = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cursor.getLong(col_id_img)).toString();
                        final String id_fold = ""+cursor.getLong(col_id_fold);
                        final String name_fold = cursor.getString(col_name_fold);
                        ContentPermis cp = getContentPermis(id_fold);
                        if(cp.system!=null&&cp.system.equals(ActionsContentPerms.SYS_DF)){
                            if(!getListImagesInFolders().containsKey(id_fold)){
                                if(cp.visible==View.VISIBLE) {
                                    getListPerms().put(id_fold,cp.uriPerm);
                                    getListFolds().put(id_fold,name_fold);
//                                    scanFoldStorageAPI29(getListImagesInFolders(),cp.uriDocFile,id_fold);
                                    emitter.onNext(scanFoldStorageAPI29(getListImagesInFolders(),cp.uriDocFile,id_fold));
                                }
                            }
                        }else {
                            if(getListImagesInFolders().containsKey(id_fold)){
                                getListImagesInFolders().get(id_fold).add(id_img);
                            }else {

                                if(cp.visible==View.VISIBLE) {
                                    ArrayList<String> imgs = new ArrayList<>();
                                    imgs.add(id_img);
                                    getListImagesInFolders().put(id_fold, imgs);
                                    getListFolds().put(id_fold,name_fold);
                                    getListPerms().put(id_fold,cp.uriPerm);
                                }
                            }
                        }
                    }
                }
                emitter.onNext(getListImagesInFolders());
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
        if(cp==null){
            cp = new ContentPermis(key);
            cp.system = ActionsContentPerms.ZHOPA;
            cp.uriPerm = ActionsContentPerms.NON_PERM;
            cp.storage = ActionsContentPerms.NON_LOC_STOR;
            cp.uriDocFile = ActionsContentPerms.ZHOPA;
            cp.visible = View.VISIBLE;
            ActionsContentPerms.create(getContext()).insertCP(cp);
        }
        return cp;
    }

    /*android 9 storage system*/
    public void setSavingInStorageCollage(Uri uri, String report, String delimiter){
        String[]split = report.split(delimiter);
        Cursor cursor = getContext().getContentResolver().query(uri,projection(),null,null,null);
        final int col_id_fold = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_ID);

        final int col_name_fold = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
        cursor.moveToFirst();
        final String id_fold = ""+cursor.getLong(col_id_fold);
        final String name_fold = cursor.getString(col_name_fold);

        ActionsContentPerms.create(getContext().getApplicationContext()).queryItemDB(
                id_fold,
                split[SavedKollagesFragmentDraw.INDEX_URI_PERM_FOLD],
                split[SavedKollagesFragmentDraw.INDEX_URI_DF_FOLD],
                ActionsContentPerms.SYS_DF,
                1,
                View.VISIBLE);

        addImgCollect(
                id_fold,
                split[SavedKollagesFragmentDraw.INDEX_URI_DF_IMG],
                name_fold,
                split[SavedKollagesFragmentDraw.INDEX_URI_PERM_FOLD]);
    }

    /*android 9 file system*/
    public void setSavingInFileCollage(Uri uri,String pathImg,String pathFold){

        Cursor cursor = getContext().getContentResolver().query(uri,projection(),null,null,null);

        final int col_id_fold = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_ID);
        final int col_name_fold = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
        cursor.moveToFirst();
        final String id_fold = "" + cursor.getLong(col_id_fold);
        final String nameFold = cursor.getString(col_name_fold);

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
                ActionsContentPerms.GRAND);
    }

//    @SuppressLint("CheckResult")
//    private void correctSavingFoldInStorage(String[]split){
//        Observable.create(new ObservableOnSubscribe<HashMap<String, ArrayList<String>>>() {
//            @Override
//            public void subscribe(ObservableEmitter<HashMap<String, ArrayList<String>>> emitter) throws Exception {
//                String udf = split[SavedKollagesFragmentDraw.INDEX_URI_DF_FOLD];
//                String key = split[SavedKollagesFragmentDraw.INDEX_PATH_FOLD];
//                emitter.onNext(scanFoldStorageAPI29(getListImagesInFolders(),udf,key));
//                emitter.onComplete();
//            }
//        }).compose(new ThreadTransformers.InputOutput<>())
//                .subscribe(new Consumer<HashMap<String, ArrayList<String>>>() {
//                    @Override
//                    public void accept(HashMap<String, ArrayList<String>> stringArrayListHashMap) throws Exception {
//                        setListImagesInFolders(stringArrayListHashMap);
//                    }
//                });
//    }

    private Cursor cursor(){
        return getContext().getContentResolver().query(question(),projection(),null,null,sort());
    }

    protected Uri question(){
        Uri uri = null;
        if(App.checkVersion())uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        else uri = MediaStore.Images.Media.getContentUri(
                MediaStore.VOLUME_EXTERNAL);
        return uri;
    }

    private String[]projection(){
        return new String[]{
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.BUCKET_ID,
                MediaStore.Images.Media.MIME_TYPE,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME};
    }

    private String sort(){
        return MediaStore.Images.Media.BUCKET_ID;
    }

    /*определяем тома в устройстве*/
    private void definitionStorage(){

        File[] files = ContextCompat.getExternalFilesDirs(getContext(), null);
        storage = new ArrayList<>();
        for (int i=0;i<files.length;i++){
            storage.add(files[i].getAbsolutePath().split("Android")[0]);
        }
    }

    protected void addImgCollect(String key, String img, String fold, String permis){

        if(getListImagesInFolders().containsKey(key)){
            getListImagesInFolders().get(key).add(img);
        } else {
            correctAdapterPostSave();
            ArrayList<String> imgs = new ArrayList<>();
            imgs.add(img);
            getListImagesInFolders().put(key, imgs);
            getListFolds().put(key,fold);
            getListPerms().put(key,permis);
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

    protected HashMap<String,String>getListPerms(){
        return listPerms;
    }

    protected void clearListImagesInFolders(){
        listImagesToFolder.clear();
        listFolds.clear();
        listPerms.clear();
    }

    protected void initListImagesInFolders(){
        listImagesToFolder = new HashMap<>();
        listFolds = new HashMap<>();
        listPerms = new HashMap<>();

    }

}
