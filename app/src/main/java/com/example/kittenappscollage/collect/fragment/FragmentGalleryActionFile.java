package com.example.kittenappscollage.collect.fragment;

import android.content.ActivityNotFoundException;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import androidx.core.content.FileProvider;
import androidx.documentfile.provider.DocumentFile;

import com.example.kittenappscollage.helpers.Massages;
import com.example.kittenappscollage.helpers.RequestFolder;
import com.example.kittenappscollage.helpers.db.aller.ActionsContentPerms;
import com.example.kittenappscollage.helpers.db.aller.ContentPermis;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import static android.provider.MediaStore.VOLUME_EXTERNAL;
import static com.example.kittenappscollage.collect.adapters.ListenLoadFoldAdapter.ROOT_ADAPTER;
import static com.example.kittenappscollage.helpers.Massages.LYTE;

public class FragmentGalleryActionFile extends FragmentGalleryAction {

    @Override
    protected void renameFoldFile(String name,String key){
        super.renameFoldFile(name, key);
        LYTE("FragmentGalleryActionFile rename "+key);
        renameFold(name,key);

    }

    @Override
    protected void applyDeleteSelectedFile(){
        super.applyDeleteSelectedFile();

    }

    @Override
    protected void deletedFoldFile(String fold){
        super.deletedFoldFile(fold);

    }



    protected void clearLists(String key){
        getListPerms().remove(key);
        getListImagesInFolders().remove(key);
        getListFolds().remove(key);
    }

    protected void removeImgInCollect(String key,String img){
        getListImagesInFolders().get(key).remove(img);
    }

    private void renameFold(String name, String key){
        File fold = createNewFold(name,key);
        ArrayList<String>images = (ArrayList<String>)getListImagesInFolders().get(key).clone();
        Cursor cursor = null;
        for(String img:images){
            try {
                cursor = getContext().getContentResolver().query(Uri.parse(img),getPathAndNameImg(),null,null,null);
                cursor.moveToFirst();
                final int col_name_img = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);
                final String image = fold.getAbsolutePath()+"/"+cursor.getString(col_name_img);
                if(transfer(new File(image),getContext().getContentResolver().openInputStream(Uri.parse(img)))){
                    Uri uri = report(image);
                    getContext().getContentResolver().delete(Uri.parse(img),null,null);
                }else {
                    Massages.SHOW_MASSAGE(getContext(),"Не удалось переместить "+cursor.getString(col_name_img));
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private Uri report(String img){
        String mime = "image/";
        if(img.endsWith(".png"))mime+="png";
        else if(img.endsWith(".jpeg"))mime+="jpeg";
        else if(img.endsWith(".jpg"))mime+="jpg";
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.MIME_TYPE, mime);
        values.put(MediaStore.MediaColumns.DATA, img);
        return idImage(values);
    }

    private Uri idImage(ContentValues values){
        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.Q)return getContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        return getContext(). getContentResolver().insert(MediaStore.Images.Media.getContentUri(VOLUME_EXTERNAL), values);
    }

    private boolean transfer(File newImg, InputStream is) throws IOException {
            OutputStream os = new FileOutputStream(newImg);
            copyStream(is,os);
            is.close();
            os.close();
            return newImg.exists();
    }

    private  void copyStream(InputStream input, OutputStream output)
            throws IOException
    {
        byte[] buffer = new byte[1024]; // Adjust if you want
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1)
        {
            output.write(buffer, 0, bytesRead);
        }
    }

    private File createNewFold(String name, String key){
        final Uri uImg = Uri.parse(getListImagesInFolders().get(key).get(0));
        Cursor cursor = getContext().getContentResolver().query(uImg,getPathAndNameImg(),selection(),args(getListFolds().get(key)),null);

        final int col_data_img = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        final int col_name_img = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);

        cursor.moveToFirst();
        String oldPathFold = cursor.getString(col_data_img).split(cursor.getString(col_name_img))[0];
        File oldFileFold = new File(oldPathFold);
        String newPathFold = oldFileFold.getParent()+"/"+name;
        File newFileFold = new File(newPathFold);
        if(!newFileFold.exists()){
            if(!newFileFold.mkdir())return null;
        }
        return newFileFold;
    }


    private String[] getPathAndNameImg(){
        return new String[]{
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DISPLAY_NAME
        };
    }

    private String[] getIdAndNameFold(){
        return new String[]{
                MediaStore.Images.Media.BUCKET_ID,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME
        };
    }

    private String selection(){
        return MediaStore.Images.Media.BUCKET_DISPLAY_NAME+" = ? ";
    }

    private String[] args(String name){
        return new String[]{name};
    }

    protected Uri reportMedia(ContentValues cv){
        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.Q)return getContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cv);
        else return getContext().getContentResolver().insert(MediaStore.Images.Media.getContentUri(VOLUME_EXTERNAL), cv);
    }

}
