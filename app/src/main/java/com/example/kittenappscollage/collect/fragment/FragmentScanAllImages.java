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

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class FragmentScanAllImages extends Fragment {

    private RecyclerView recycler;

    private ArrayList<String>listFoldersImg,listAllImg ;
    private HashMap<String,ArrayList<String>>listImagesToFolder;

    public FragmentScanAllImages() {
        listFoldersImg = new ArrayList<>();
        listAllImg = new ArrayList<>();
        listImagesToFolder = new HashMap<>();

    }

    @SuppressLint("Recycle")
    @RequiresApi(api = Build.VERSION_CODES.Q)
    public void scanDevice(){
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
        while (cursor.moveToNext()) {

            listAllImg.add(cursor.getString(col_path));
            listFoldersImg.add(cursor.getString(col_fold));

            String[] data = cursor.getString(col_fold).split("[.]");
            String pref = data[data.length-1].toLowerCase();
            boolean pik = pref.equals("png")||pref.equals("jpeg")||pref.equals("jpg");
            if(listImagesToFolder.containsKey(cursor.getString(col_fold))){
                if(pik)listImagesToFolder.get(cursor.getString(col_fold)).add(cursor.getString(col_path));
            } else {
                if(pik) {
                    ArrayList<String> imgs = new ArrayList<>();
                    imgs.add(cursor.getString(col_path));
                    listImagesToFolder.put(cursor.getString(col_fold), imgs);
                }
            }
        }
    }


    public void mut(File file){
        getContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
    }
}
