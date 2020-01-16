package com.example.kittenappscollage.collect.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.kittenappscollage.helpers.RequestFolder;
import com.example.kittenappscollage.helpers.rx.ThreadTransformers;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;

import static com.example.kittenappscollage.helpers.Massages.LYTE;

public class FragmentScanAllImages extends Fragment {

    private HashMap<String,ArrayList<String>>listImagesToFolder;

    private HashMap<String,String>listFolds;

    private HashMap<String,String>listPartition;

    private HashMap<String, Integer>listStorages;

    private ArrayList<String> storage;

    public void scanDevice(){
         check();
//        content();
    }

    @SuppressLint("CheckResult")
    private void check(){
        Observable.create((ObservableOnSubscribe<HashMap<String, ArrayList<String>>>) emitter -> {
          emitter.onNext(scan(getListImagesInFolders(), getListFolds()));
          emitter.onComplete();
        }).compose(new ThreadTransformers.NewThread<>())
          .subscribe(stringArrayListHashMap -> setListImagesInFolders(stringArrayListHashMap));
    }

        @SuppressLint("Recycle")
    private HashMap<String,ArrayList<String>> scan(HashMap<String,ArrayList<String>>list,HashMap<String,String>folds){
         definitionStorage();
         if(getListImagesInFolders()==null)initListImagesInFolders();
         else getListImagesInFolders().clear();
        String[] projection = {
                MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME};

        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        Cursor cursor = getActivity().getContentResolver().query(uri, projection, null,
                null, null);

        int col_path, col_fold;
        col_path = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        col_fold = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);

        cursor.moveToFirst();
        if(list==null)initListImagesInFolders();
        clearListImagesInFolders();
        while (cursor.moveToNext()) {
            String path = cursor.getString(col_path).toLowerCase();


            String key = cursor.getString(col_path).split(cursor.getString(col_fold))[0]+cursor.getString(col_fold);;
            boolean pik = path.endsWith(".png")||path.endsWith(".jpeg")||path.endsWith("jpg");
            if(list.containsKey(key)){
                if(pik) {
                    list.get(key).add(cursor.getString(col_path));
                }
            } else {
                if(pik) {
                    ArrayList<String> imgs = new ArrayList<>();
                    imgs.add(cursor.getString(col_path));
                    list.put(key, imgs);
                    folds.put(key,cursor.getString(col_fold));
                        for(int i=0;i<getNamesStorage().size();i++) {
                            if(key.contains(getNamesStorage().get(i))){
                                getIndexesStorage().put(key, getNamesStorage().indexOf(getNamesStorage().get(i)));
                                getListPartition().put(key,getNamesStorage().get(i));
                            }

                        }

                }
            }
        }
        return list;
    }

    /*определяем тома в устройстве*/
    private void definitionStorage(){
        File[] files = ContextCompat.getExternalFilesDirs(getContext(), null);
        storage = new ArrayList<>();
        for (int i=0;i<files.length;i++){
            storage.add(files[i].getAbsolutePath().split("Android")[0]);
        }
    }

    public void setSavingCollage(String path){
        String key = RequestFolder.getFolderImages();
        if(listImagesToFolder.containsKey(key)){
            listImagesToFolder.get(key).add(path);
        } else {
                ArrayList<String> imgs = new ArrayList<>();
                imgs.add(path);
            listImagesToFolder.put(key, imgs);
        }
        setListImagesInFolders(listImagesToFolder);
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

    protected HashMap<String,String>getListPartition(){
        return listPartition;
    }
    /*индекс хранилища из getNamesStorage()*/
    protected HashMap<String,Integer>getIndexesStorage(){
        return listStorages;
    }

    protected void clearListImagesInFolders(){
        listImagesToFolder.clear();
        listFolds.clear();
        listStorages.clear();
        listPartition.clear();
    }

    protected void initListImagesInFolders(){
        listImagesToFolder = new HashMap<>();
        listFolds = new HashMap<>();
        listStorages = new HashMap<>();
        listPartition = new HashMap<>();
    }

//    private void  content(){
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
//        intent.setType("image/jpeg");
//        intent.setType("image/pjpeg");
//        intent.setType("image/png");
//        startActivityForResult(intent, FragmentGallery.REQUEST_READ_STORAGE);
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode==FragmentGallery.REQUEST_READ_STORAGE){
//            if(resultCode == Activity.RESULT_OK && data!= null && data.getData() != null){
//                definitionStorage();
//                if(getListImagesInFolders()==null)initListImagesInFolders();
//                else getListImagesInFolders().clear();
//
//                Uri uri = data.getData();
//
//                String[] projection = {
//                        MediaStore.MediaColumns.DATA,
//                        MediaStore.Images.Media.BUCKET_DISPLAY_NAME};
//
//                Cursor cursor = getActivity().getContentResolver().query(uri, projection, null,
//                        null, null);
//
//                int col_path, col_fold;
//                col_path = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
//                col_fold = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
//
//                cursor.moveToFirst();
//
//                while (cursor.moveToNext()) {
//                    String key = cursor.getString(col_path).split(cursor.getString(col_fold))[0]+cursor.getString(col_fold);;
//
//                    if(getListImagesInFolders().containsKey(key)){
//
//                            getListImagesInFolders().get(key).add(cursor.getString(col_path));
//
//                    } else {
//                            ArrayList<String> imgs = new ArrayList<>();
//                            imgs.add(cursor.getString(col_path));
//                            getListImagesInFolders().put(key, imgs);
//                            getListFolds().put(key,cursor.getString(col_fold));
//                            for(int i=0;i<getNamesStorage().size();i++) {
//                                if(key.contains(getNamesStorage().get(i))){
//                                    getIndexesStorage().put(key, getNamesStorage().indexOf(getNamesStorage().get(i)));
//                                    getListPartition().put(key,getNamesStorage().get(i));
//                                }
//
//                            }
//
//                        }
//                    }
//
//                setListImagesInFolders(getListImagesInFolders());
//                }
//            }
//        }

}
