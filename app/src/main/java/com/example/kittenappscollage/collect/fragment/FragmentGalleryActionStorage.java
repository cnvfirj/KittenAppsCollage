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
import com.example.kittenappscollage.helpers.dbPerms.WorkDBPerms;
import com.example.kittenappscollage.helpers.rx.ThreadTransformers;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

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
        for (String ur:getSelectFiles()){
            if(delDocFile(Uri.parse(ur))){
                getListImagesInFolders().get(key).remove(ur);
                emitter.onNext(getListImagesInFolders());
            }
        }
        emitter.onNext(getListImagesInFolders());
        emitter.onComplete();
    }

    private void deleteImagesAndFold(String key,ObservableEmitter<HashMap<String, ArrayList<String>>> emitter){
        ArrayList<String>images = (ArrayList<String>)getListImagesInFolders().get(key).clone();
        Uri treeUri = Uri.parse(key);
        for (String ur:images){
            DocumentFile img = DocumentFile.fromSingleUri(getContext(),Uri.parse(ur));

            if(img.isDirectory())continue;

            if(img.getType().equals(TYPE_PNG)||img.getType().equals(TYPE_JPG)||img.getType().equals(TYPE_JPEG)) {

                    if(delDocFile(Uri.parse(ur))) {
                        getListImagesInFolders().get(key).remove(ur);
                        emitter.onNext(getListImagesInFolders());

                }
            }
        }

//        int takeFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION;
//        getContext().getContentResolver().releasePersistableUriPermission(treeUri, takeFlags);

        if(getListImagesInFolders().get(key).size()==0){
            clearLists(key);
            WorkDBPerms.get(getContext()).delItem(key);
            DocumentFile fold = DocumentFile.fromTreeUri(getContext(),treeUri);
            if(fold.listFiles().length==0){
                if(delDocFile(fold.getUri())){

                }
            }
        }
        emitter.onNext(getListImagesInFolders());
        emitter.onComplete();
    }


    private boolean delDocFile(Uri uri){
//        int takeFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION;

        boolean b = false;
        try {
//            getContext().getContentResolver().takePersistableUriPermission(uri, takeFlags);
            b = DocumentsContract.deleteDocument(getContext().getContentResolver(),uri);
//            getContext().getContentResolver().releasePersistableUriPermission(uri, takeFlags);
        }catch(FileNotFoundException e) {
            e.printStackTrace();
        }catch(SecurityException s){
            LYTE("FragmentGalleryActionStorage ex del "+s.toString());
        }
        return b;
    }

//    private String getRealPath(String lastSegment) {
//        String[]split = lastSegment.split("[:]");
//        String[]sub = lastSegment.split(split[0]+":");
//        String[]storage = getContext().getExternalFilesDir(null).getAbsolutePath().split("[/]");
//        return "/"+storage[1]+"/"+split[0]+"/"+sub[1];
//    }

//    private String[]projection(){
//        return new String[]{MediaStore.Images.Media._ID,
//                            MediaStore.Images.Media.BUCKET_ID,
//                            MediaStore.Images.Media.DISPLAY_NAME};
//
//    }
//
//    private String selection(){
//        return MediaStore.Images.Media.DISPLAY_NAME + " = ? ";
//    }
//
//    private String[]selArgs(String key){
//        return new String[]{key};
//    }


}
