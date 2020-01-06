package com.example.kittenappscollage.collect.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kittenappscollage.helpers.RequestFolder;
import com.example.kittenappscollage.helpers.rx.ThreadTransformers;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;

import static com.example.kittenappscollage.helpers.Massages.LYTE;

public class FragmentScanAllImages extends Fragment {

    private HashMap<String,ArrayList<String>>listImagesToFolder;

    public void scanDevice(){
         check();
    }

    @SuppressLint("CheckResult")
    private void check(){
        Observable.create((ObservableOnSubscribe<HashMap<String, ArrayList<String>>>) emitter -> {
          emitter.onNext(scan(getListImagesInFolders()));
          emitter.onComplete();
        }).compose(new ThreadTransformers.NewThread<>())
          .subscribe(stringArrayListHashMap -> setListImagesInFolders(stringArrayListHashMap));
    }

     @SuppressLint("Recycle")
    private HashMap<String,ArrayList<String>> scan(HashMap<String,ArrayList<String>>list){
         if(getListImagesInFolders()==null)initListImagesInFolders();
         else getListImagesInFolders().clear();
        String[] projection = {
                MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME };

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
            String[] data = cursor.getString(col_path).split("[.]");
            String pref = data[data.length-1].toLowerCase();
            boolean pik = pref.equals("png")||pref.equals("jpeg")||pref.equals("jpg");

            if(list.containsKey(cursor.getString(col_fold))){
                if(pik) list.get(cursor.getString(col_fold)).add(cursor.getString(col_path));
            } else {
                if(pik) {
                    ArrayList<String> imgs = new ArrayList<>();
                    imgs.add(cursor.getString(col_path));
                    list.put(cursor.getString(col_fold), imgs);
                }
            }
        }
        return list;
    }

    public void setSavingCollage(String path){
        String key = RequestFolder.getNameFoldCollages();
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

    protected HashMap<String,ArrayList<String>> getListImagesInFolders(){
        return listImagesToFolder;
    }

    protected void clearListImagesInFolders(){
        listImagesToFolder.clear();
    }

    protected void initListImagesInFolders(){
        listImagesToFolder = new HashMap<>();
    }

}
