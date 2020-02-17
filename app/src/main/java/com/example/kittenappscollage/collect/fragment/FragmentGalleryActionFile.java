package com.example.kittenappscollage.collect.fragment;


import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.kittenappscollage.helpers.Massages;
import com.example.kittenappscollage.helpers.db.aller.ActionsContentPerms;
import com.example.kittenappscollage.helpers.rx.ThreadTransformers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

import static android.provider.MediaStore.VOLUME_EXTERNAL;
import static com.example.kittenappscollage.collect.adapters.ListenLoadFoldAdapter.ROOT_ADAPTER;
import static com.example.kittenappscollage.helpers.Massages.LYTE;
import static com.example.kittenappscollage.helpers.db.aller.ActionsContentPerms.ZHOPA;

public class FragmentGalleryActionFile extends FragmentGalleryAction {

    private Set<Integer> blockItems;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        blockItems = new HashSet<>();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void click(int adapter, ImageView img, ImageView check, int pos) {
        if(blockItems.contains(pos)){
            Massages.SHOW_MASSAGE(getContext(),"Дождись выполнения операции");
        }else super.click(adapter, img, check, pos);

    }

    @Override
    public void longClick(int adapter, ImageView img, ImageView check, int pos) {
        if(blockItems.contains(pos)){
            Massages.SHOW_MASSAGE(getContext(),"Дождись выполнения операции");
        }else super.longClick(adapter, img, check, pos);

    }

    @Override
    protected void renameFoldFile(String name,String key){
        super.renameFoldFile(name, key);
        threadRename(name,key);

    }

    @Override
    protected void applyDeleteSelectedFile(){
        super.applyDeleteSelectedFile();
        threadDelSelected(getKey());
    }

    @Override
    protected void deletedFoldFile(String fold){
        super.deletedFoldFile(fold);
        threadDelete(fold);
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
//        getRecycler().getRecycledViewPool().clear();
        Observable.create((ObservableOnSubscribe<HashMap<String, ArrayList<String>>>) emitter ->
                renameFold(name,key, emitter)).compose(new ThreadTransformers.InputOutput<>())
                .doOnComplete(() -> {
                    blockItems.remove(getSelectItemRootAdapter());
                    Massages.SHOW_MASSAGE(getContext(),"Папка переименована");
                })
                .subscribe(stringArrayListHashMap -> {
                    setListImagesInFolders(stringArrayListHashMap);
                });
    }

    @SuppressLint("CheckResult")
    private void threadDelete(String key){
//        getRecycler().getRecycledViewPool().clear();
        Observable.create((ObservableOnSubscribe<HashMap<String, ArrayList<String>>>) emitter -> deleteFold(key,emitter)).compose(new ThreadTransformers.InputOutput<>())
                .doOnComplete(() -> {
                    blockItems.remove(getSelectItemRootAdapter());
                    Massages.SHOW_MASSAGE(getContext(),"Папка удалена");
                })
                .subscribe(stringArrayListHashMap -> {
                    setListImagesInFolders(stringArrayListHashMap);
                });
    }

    @SuppressLint("CheckResult")
    private void threadDelSelected(String key){
        final int sel = getSelectFiles().size();
        final int all = getListImagesInFolders().get(key).size();
        if(sel==all){
            getImgAdapt().setModeSelected(false);
            setIndexAdapter(ROOT_ADAPTER);
            getGridLayoutManager().setSpanCount(2);
            getRecycler().setAdapter(getFoldAdapt());

            threadDelete(key);
        } else {
//            getRecycler().getRecycledViewPool().clear();
            Observable.create((ObservableOnSubscribe<HashMap<String, ArrayList<String>>>)
                    emitter -> deleteSelected(key, emitter)).compose(new ThreadTransformers.InputOutput<>())
                    .doOnComplete(() -> {
                        Massages.SHOW_MASSAGE(getContext(), "Выбранные изображения удалены");
                    })
                    .subscribe(stringArrayListHashMap -> {
                        setListImagesInFolders(stringArrayListHashMap);
                    });
        }
    }

    private void deleteSelected(String key,ObservableEmitter<HashMap<String, ArrayList<String>>> emitter){
        for (String img:getSelectFiles()){
            delImage(Uri.parse(img),1);
            getListImagesInFolders().get(key).remove(img);
            emitter.onNext(getListImagesInFolders());
        }
        emitter.onNext(getListImagesInFolders());
        emitter.onComplete();
    }

    private void deleteFold(String key,ObservableEmitter<HashMap<String, ArrayList<String>>> emitter){
        blockItems.add(getSelectItemRootAdapter());
        ArrayList<String>images = (ArrayList<String>)getListImagesInFolders().get(key).clone();
        for (int i=0;i<images.size();i++){
            if(i>0) {
                if (i == images.size() - 1) delImage(Uri.parse(images.get(i)), -5);
                else delImage(Uri.parse(images.get(i)), i);
            }else delImage(Uri.parse(images.get(i)), 1);

            getListImagesInFolders().get(key).remove(images.get(i));
            if(emitter!=null)emitter.onNext(getListImagesInFolders());
        }
        clearLists(key);
        if(emitter!=null)emitter.onNext(getListImagesInFolders());
        if(emitter!=null)emitter.onComplete();
    }

    private void renameFold(String name, String key, ObservableEmitter<HashMap<String, ArrayList<String>>> emitter){
        blockItems.add(getSelectItemRootAdapter());
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
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        clearLists(key);
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
        if(i<0){
            cursor = getContext().getContentResolver().query(uri,getPathAndNameImg(),null,null,null);
            cursor.moveToFirst();
            final int col_data = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            File data = new File(cursor.getString(col_data));
            data.getParentFile().delete();
            ActionsContentPerms.create(getContext()).deleteItemDB(id_fold);
        }
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
                ZHOPA,
                ActionsContentPerms.SYS_FILE,
                0,
                View.VISIBLE);

        addImgCollect(id_fold,uri.toString(),name_fold,ActionsContentPerms.GRAND,mod_img);

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
