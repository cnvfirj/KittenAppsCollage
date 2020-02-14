package com.example.kittenappscollage.collect.fragment;


import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.view.View;

import com.example.kittenappscollage.helpers.Massages;
import com.example.kittenappscollage.helpers.db.aller.ActionsContentPerms;
import com.example.kittenappscollage.helpers.rx.ThreadTransformers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

import static android.provider.MediaStore.VOLUME_EXTERNAL;

public class FragmentGalleryActionFile extends FragmentGalleryAction {

    @Override
    protected void renameFoldFile(String name,String key){
        super.renameFoldFile(name, key);
        invisibleMenu();
        threadRename(name,key);

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
        if(getListImagesInFolders().containsKey(key)) {
            getListImagesInFolders().remove(key);
            getListPerms().remove(key);
            getListFolds().remove(key);
            getListMutable().remove(key);
        }
    }

    @SuppressLint("CheckResult")
    private void threadRename(String name, String key){
        Observable.create((ObservableOnSubscribe<HashMap<String, ArrayList<String>>>) emitter -> renameFold(name,key, emitter)).compose(new ThreadTransformers.InputOutput<>())
                .subscribe(stringArrayListHashMap -> setListImagesInFolders(stringArrayListHashMap));
    }

    private void renameFold(String name, String key, ObservableEmitter<HashMap<String, ArrayList<String>>> emitter){
        File fold = createNewFold(name,key);
        ArrayList<String>images = (ArrayList<String>)getListImagesInFolders().get(key).clone();
        Cursor cursor = null;
        for(int i=0;i<images.size();i++){
            try {
                cursor = getContext().getContentResolver().query(Uri.parse(images.get(i)),getPathAndNameImg(),null,null,null);
                cursor.moveToFirst();
                final int col_name_img = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);
                final String image = fold.getAbsolutePath()+"/"+cursor.getString(col_name_img);

                if(transfer(new File(image),getContext().getContentResolver().openInputStream(Uri.parse(images.get(i))))){
                    addImage(report(image));
                    /*отслеживаем итератор*/
                    if(i==images.size()-1) delImage(Uri.parse(images.get(i)),-5);
                    else delImage(Uri.parse(images.get(i)),i);

                    emitter.onNext(getListImagesInFolders());
                }else {
                    Massages.SHOW_MASSAGE(getContext(),"Не удалось переместить "+cursor.getString(col_name_img));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        emitter.onNext(getListImagesInFolders());
        emitter.onComplete();

    }

    /*удаляем все изображения из галереи
    * в последней итерации удаляем разрешение на редактирование*/
    private void delImage(Uri uri, int i){
        Cursor cursor  = getContext().getContentResolver().query(uri,getIdAndNameFold(),null,null,null);
        cursor.moveToFirst();
        final int col_id_fold = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_ID);
        final String id_fold = ""+cursor.getLong(col_id_fold);

        if(i==0)clearLists(id_fold);
        if(i<0)ActionsContentPerms.create(getContext()).deleteItemDB(id_fold);
        getContext().getContentResolver().delete(uri,null,null);

    }

    /*добавляем изображение в галерею*/
    private void addImage(Uri uri){
        Cursor cursor  = getContext().getContentResolver().query(uri,getIdAndNameFold(),null,null,null);
        cursor.moveToFirst();
        final int col_id_fold = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_ID);
        final int col_name_fold = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
        final int col_mod_img = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_MODIFIED);
        final String id_fold = ""+cursor.getLong(col_id_fold);
        final String name_fold = cursor.getString(col_name_fold);
        final long mod_img = cursor.getLong(col_mod_img);
        ActionsContentPerms.create(getContext()).queryItemDB(
                id_fold,
                ActionsContentPerms.GRAND,
                ActionsContentPerms.ZHOPA,
                ActionsContentPerms.SYS_FILE,
                0,
                View.VISIBLE);

        addImgCollect(id_fold,uri.toString(),name_fold,ActionsContentPerms.GRAND,mod_img);
//        if(getListImagesInFolders().containsKey(id_fold)){
//            getListImagesInFolders().get(id_fold).add(uri.toString());
//            getListMutable().put(id_fold,mod_img);
//        }else {
//                ArrayList<String> imgs = new ArrayList<>();
//                imgs.add(uri.toString());
//                getListImagesInFolders().put(id_fold, imgs);
//                getListFolds().put(id_fold,name_fold);
//                getListPerms().put(id_fold,ActionsContentPerms.GRAND);
//                getListMutable().put(id_fold,mod_img);
//        }
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

    /*перенос инпут в файл*/
    private boolean transfer(File newImg, InputStream is) throws IOException {
            OutputStream os = new FileOutputStream(newImg);
            copyStream(is,os);
            is.close();
            os.close();
            return newImg.exists();
    }

    /*пишем из инпут в оутпут*/
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
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Images.Media.DATE_MODIFIED
        };
    }

    private String selection(){
        return MediaStore.Images.Media.BUCKET_DISPLAY_NAME+" = ? ";
    }

    private String[] args(String name){
        return new String[]{name};
    }

}
