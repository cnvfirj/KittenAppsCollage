package com.example.kittenappscollage.collect.fragment;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.database.Cursor;
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
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.Fragment;

import com.example.kittenappscollage.draw.fragment.SavedKollagesFragmentDraw;
import com.example.kittenappscollage.helpers.App;
import com.example.kittenappscollage.helpers.ListenMedia;
import com.example.kittenappscollage.helpers.RequestFolder;
import com.example.kittenappscollage.helpers.db.aller.ActionsContentPerms;
import com.example.kittenappscollage.helpers.db.aller.ContentPermis;
import com.example.kittenappscollage.helpers.rx.ThreadTransformers;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;

import static android.provider.MediaStore.VOLUME_EXTERNAL;
import static com.example.kittenappscollage.draw.fragment.SavedKollagesFragmentDraw.INDEX_PATH_FOLD;
import static com.example.kittenappscollage.draw.fragment.SavedKollagesFragmentDraw.INDEX_PATH_IMG;
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
         if(!blockScan)check();
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
    private void check(){
        Observable.create((ObservableOnSubscribe<HashMap<String, ArrayList<String>>>) emitter -> {
          if(App.checkVersion())emitter.onNext(scanAPI21(getListImagesInFolders(), getListFolds()));
          else emitter.onNext(scanAPI29(getListImagesInFolders(), getListFolds()));
          emitter.onComplete();
        }).compose(new ThreadTransformers.NewThread<>())
          .subscribe(stringArrayListHashMap -> {
              setListImagesInFolders(stringArrayListHashMap);
          });
    }


    /*android > 9*/
    private HashMap<String,ArrayList<String>>scanAPI29(HashMap<String,ArrayList<String>>list,HashMap<String,String>folds){
        definitionStorage();
        if(getListImagesInFolders()==null)initListImagesInFolders();
        else clearListImagesInFolders();
        String[]projection = {MediaStore.Images.Media._ID,
                MediaStore.Images.Media.BUCKET_ID,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Images.Media.DISPLAY_NAME};


        Uri content = null;
        if(App.checkVersion())content = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        else content = MediaStore.Images.Media.getContentUri(VOLUME_EXTERNAL);

        Cursor cursor = getContext().getContentResolver().query(content,projection,null,null,null);
        int col_id,col_name,col_fold,col_fold_id;
        col_id = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
        col_name = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);
        col_fold = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
        col_fold_id = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_ID);

        cursor.moveToFirst();
        while (cursor.moveToNext()){
            long id = cursor.getLong(col_id);
            String name = cursor.getString(col_name);//name img
            String fold = cursor.getString(col_fold);//name fold
            String uriImg = ContentUris.withAppendedId(content,id).toString();//uri img
            long f_id = cursor.getLong(col_fold_id);
            String uriFold = ContentUris.withAppendedId(content,f_id).toString();//uri fold


        }


//        String query = MediaStore.Images.Media.DISPLAY_NAME + " = ?";
//        String nameImg = "2020_41_6541.png";
//        Cursor search = getContext().getContentResolver().query(content,new String[]{MediaStore.Images.Media._ID},query,new String[]{nameImg},null);
//        col_id = search.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
//
//        search.moveToFirst();
//        int index =0;
//        LYTE("start ");
//        while (search.moveToNext()){
//            LYTE("index "+index);
//            long id = cursor.getLong(col_id);
//            LYTE("id "+id);
////            String name = cursor.getString(col_name);//name img
////            LYTE("name "+name);
////            String fold = cursor.getString(col_fold);//name fold
////            LYTE("fold "+fold);
////            Uri uri = ContentUris.withAppendedId(content,id);//uri img
////            long f_id = cursor.getLong(col_fold_id);
////            LYTE("fold id "+f_id);
////            Uri fUri = ContentUris.withAppendedId(content,f_id);//uri fold
//            index++;
//        }
        return list;
    }


    /*android 9*/

    @SuppressLint("Recycle")
    private HashMap<String,ArrayList<String>> scanAPI21(HashMap<String,ArrayList<String>>list,HashMap<String,String>folds){

        definitionStorage();
        if(list==null) initListImagesInFolders();
        clearListImagesInFolders();
        String[] projection = {
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME};
//        String selection = MediaStore.Images.Media.MIME_TYPE+" = ?";
//        String[]selectionArgs = new String[]{
//                "image/png"
////                ,"image/jpg"
////                ,"image/jpeg"
//        };
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        Cursor cursor = getContext().getContentResolver().query(uri, projection, null,
                null, null);

        int col_path, col_fold;
        col_path = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        col_fold = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
        cursor.moveToFirst();

        while (cursor.moveToNext()) {
            String path = cursor.getString(col_path).toLowerCase();
            LYTE("path "+path);
            String key = cursor.getString(col_path).split(cursor.getString(col_fold))[0]+cursor.getString(col_fold);

            boolean pik = path.endsWith(".png")||path.endsWith(".jpeg")||path.endsWith(".jpg");
            if(list.containsKey(key)){
                if(pik) {
                    list.get(key).add(cursor.getString(col_path));
                }
            } else {
                if(pik&&addPerm(key)) {
                    ArrayList<String> imgs = new ArrayList<>();
                    imgs.add(cursor.getString(col_path));
                    list.put(key, imgs);
                    folds.put(key,cursor.getString(col_fold));
                        for(int i=0;i<getNamesStorage().size();i++) {
                            if(key.contains(getNamesStorage().get(i))){
                                getIndexesStorage().put(key, getNamesStorage().indexOf(getNamesStorage().get(i)));
                            }
                        }
                }
            }
        }
        return list;
    }

    private boolean addPerm(String fold){
        boolean visible = true;
        if(ActionsContentPerms.create(getContext()).check(fold)){
            ContentPermis cp = ActionsContentPerms.create(getContext()).getItem(fold);
            if(cp.visible==View.INVISIBLE)visible = false;
            getListPerms().put(fold,cp.uriPerm);
        }else {
            ActionsContentPerms.create(getContext()).queryItemDB(
                    fold,ActionsContentPerms.NON_PERM,
                    ActionsContentPerms.ZHOPA,ActionsContentPerms.ZHOPA,
                    ActionsContentPerms.NON_LOC_STOR, View.VISIBLE);
            getListPerms().put(fold,ActionsContentPerms.NON_PERM);
        }
        return visible;
    }

    /*определяем тома в устройстве*/
    private void definitionStorage(){

        File[] files = ContextCompat.getExternalFilesDirs(getContext(), null);
        storage = new ArrayList<>();
        for (int i=0;i<files.length;i++){
            storage.add(files[i].getAbsolutePath().split("Android")[0]);
        }
    }
    /*android 9 storage system*/
    public void setSavingInStorageCollage(Uri uri, String report, String delimiter){
        String[] split = report.split(delimiter);

        getListPerms().put(split[INDEX_PATH_FOLD],split[INDEX_URI_PERM_FOLD]);
        addImgCollect(split[INDEX_PATH_FOLD],split[INDEX_PATH_IMG]);
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
        addImgCollect(key,path);
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
