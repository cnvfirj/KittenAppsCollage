package com.example.kittenappscollage.collect.fragment;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;
import androidx.documentfile.provider.DocumentFile;

import com.example.kittenappscollage.helpers.Massages;
import com.example.kittenappscollage.helpers.db.aller.ActionsContentPerms;
import com.example.kittenappscollage.helpers.rx.ThreadTransformers;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableTransformer;
import io.reactivex.Observer;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

import static com.example.kittenappscollage.collect.adapters.ListenLoadFoldAdapter.ROOT_ADAPTER;
import static com.example.kittenappscollage.helpers.Massages.LYTE;

public class FragmentGalleryActionStorage extends FragmentGalleryActionFile {

    private final String TYPE_PNG = "image/png";

    private final String TYPE_JPEG = "image/jpeg";

    private final String TYPE_JPG = "image/jpg";


    @Override
    protected void renameFoldStorage(String name,String key) {
        super.renameFoldStorage(name,key);


    }

    @Override
    protected void applyDeleteSelectedStorage() {
        super.applyDeleteSelectedStorage();
         threadDelImages(getKey());

    }

    @Override
    protected void deletedFoldStorage(String fold) {
        super.deletedFoldStorage(fold);
        threadDelFold(fold);
    }

    @SuppressLint("CheckResult")
    private void threadDelFold(String fold){
     Observable.create((ObservableOnSubscribe<HashMap<String, ArrayList<String>>>) emitter ->
             deleteImagesAndFold(fold,emitter))
             .compose(new ThreadTransformers.InputOutput<>())
             .doOnComplete(() -> {
                 getBlockItems().remove(getSelectItemRootAdapter());
                 Massages.SHOW_MASSAGE(getContext(),"Папка удалена");
             }).subscribe(stringArrayListHashMap -> setListImagesInFolders(stringArrayListHashMap));
    }

    @SuppressLint("CheckResult")
    private void threadDelImages(String key){
        final int select = getSelectFiles().size();
        final int all = getListImagesInFolders().get(key).size();
        if(select==all){
            getImgAdapt().setModeSelected(false);
            setIndexAdapter(ROOT_ADAPTER);
            getGridLayoutManager().setSpanCount(2);
            getImgAdapt().activate(false);
            getRecycler().setAdapter(getFoldAdapt().activate(true));

            threadDelFold(key);
        }else {
            Observable.create((ObservableOnSubscribe<HashMap<String, ArrayList<String>>>) emitter ->
                    deleteImages(key, emitter))
                    .compose(new ThreadTransformers.InputOutput<>())
                    .doOnComplete(() -> {
                        getBlockItems().remove(getSelectItemRootAdapter());
                        Massages.SHOW_MASSAGE(getContext(), "Выбранные изображения удалены");
                    }).subscribe(stringArrayListHashMap -> setListImagesInFolders(stringArrayListHashMap));
        }
    }

    private void deleteImages(String key,ObservableEmitter<HashMap<String, ArrayList<String>>> emitter){
        Uri treeUri = Uri.parse(getListPerms().get(key));
        int takeFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION;
        getContext().getContentResolver().takePersistableUriPermission(treeUri, takeFlags);
        for (String ur:getSelectFiles()){
            if(delFile(Uri.parse(ur))){
                getListImagesInFolders().get(key).remove(ur);
                emitter.onNext(getListImagesInFolders());
            }
        }
//        getContext().getContentResolver().releasePersistableUriPermission(treeUri, takeFlags);
        emitter.onNext(getListImagesInFolders());
        emitter.onComplete();
    }

    private void deleteImagesAndFold(String key,ObservableEmitter<HashMap<String, ArrayList<String>>> emitter){
        ArrayList<String>images = (ArrayList<String>)getListImagesInFolders().get(key).clone();
        Uri treeUri = Uri.parse(getListPerms().get(key));
        int takeFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION;
        getContext().getContentResolver().takePersistableUriPermission(treeUri, takeFlags);
        for (String ur:images){
            DocumentFile img = DocumentFile.fromSingleUri(getContext(),Uri.parse(ur));
            if(img.isDirectory())break;
            if(img.getType().equals(TYPE_PNG)||img.getType().equals(TYPE_JPG)||img.getType().equals(TYPE_JPEG)) {
                if(delFile(Uri.parse(ur))) {
                   getListImagesInFolders().get(key).remove(ur);
                   emitter.onNext(getListImagesInFolders());

               }
            }
        }
        if(getListImagesInFolders().get(key).size()==0){
            clearLists(key);
            DocumentFile fold = DocumentFile.fromTreeUri(getContext(),treeUri);
            if(fold.listFiles().length==0){
                if(fold.delete()){
                    ActionsContentPerms.create(getContext()).deleteItemDB(key);
//                    getContext().getContentResolver().releasePersistableUriPermission(treeUri, takeFlags);
                }
            }
        }
        emitter.onNext(getListImagesInFolders());
        emitter.onComplete();
    }

    private boolean delFile(Uri fold){
        boolean b = false;
        try {
            b = DocumentsContract.deleteDocument(getContext().getContentResolver(),fold);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return b;
    }

    private String[]projection(){
        return new String[]{MediaStore.Images.Media._ID,
                            MediaStore.Images.Media.BUCKET_ID,
                            MediaStore.Images.Media.DISPLAY_NAME};

    }

    private String selection(){
        return MediaStore.Images.Media.DISPLAY_NAME + " = ? ";
    }

    private String[]selArgs(String key){
        return new String[]{key};
    }


}
