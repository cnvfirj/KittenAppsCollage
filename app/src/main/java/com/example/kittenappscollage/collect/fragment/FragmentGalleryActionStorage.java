package com.example.kittenappscollage.collect.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.DocumentsContract;

import androidx.annotation.Nullable;
import androidx.documentfile.provider.DocumentFile;

import com.bumptech.glide.load.model.ResourceLoader;
import com.example.kittenappscollage.helpers.Massages;
import com.example.kittenappscollage.helpers.dbPerms.WorkDBPerms;
import com.example.kittenappscollage.helpers.rx.ThreadTransformers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

import static com.example.kittenappscollage.collect.adapters.ListenLoadFoldAdapter.ROOT_ADAPTER;
import static com.example.kittenappscollage.helpers.Massages.LYTE;

public class FragmentGalleryActionStorage extends FragmentGalleryShareImages {

    private final int REQUEST_FOLDER = 903;

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
                        Massages.SHOW_MASSAGE(getContext(), "Выбранные изображения удалены");
                    }).subscribe(stringArrayListHashMap -> setListImagesInFolders(stringArrayListHashMap));
        }
    }

    @SuppressLint("CheckResult")
    private void threadCopy(Uri uri){
        Observable.create((ObservableOnSubscribe<HashMap<String, ArrayList<String>>>) emitter ->
                copyImages(uri,getListImagesInFolders().get(getKey()),emitter))
                .compose(new ThreadTransformers.InputOutput<>())
                .doOnComplete(() -> Massages.SHOW_MASSAGE(getContext(), "Изображения скопированы"))
                .subscribe(stringArrayListHashMap -> setListImagesInFolders(stringArrayListHashMap));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==REQUEST_FOLDER){
            if(resultCode== Activity.RESULT_OK){
                threadCopy(data.getData());
            }
        }else super.onActivityResult(requestCode, resultCode, data);
    }

    private void requestFoldToCopy(){
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        startActivityForResult(intent, REQUEST_FOLDER);
    }

    private void copyImages(Uri uri, ArrayList<String>imgs, ObservableEmitter<HashMap<String, ArrayList<String>>> emitter){
        int takeFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION ;
        getContext().getContentResolver().takePersistableUriPermission(uri, takeFlags);
        DocumentFile fold = DocumentFile.fromTreeUri(getContext(), uri);
        WorkDBPerms.get(getContext()).setAction(WorkDBPerms.INSERT,fold.getUri().toString(), fold.getName());
        try {
            for (String img:imgs) {
                Uri u = Uri.parse(img);
                    saveImg(u,fold);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


    }

    private void saveImg(Uri uri, DocumentFile fold) throws FileNotFoundException {
        InputStream is = getContext().getContentResolver().openInputStream(uri);
        DocumentFile old = DocumentFile.fromSingleUri(getContext(),uri);
        DocumentFile img = fold.createFile(old.getType(), old.getName());
        OutputStream out = getContext().getContentResolver().openOutputStream(img.getUri());
        byte[] buffer = new byte[8 * 1024];
        int bytesRead;
            try {
                while ((bytesRead = is.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
                is.close();
                out.close();
            }catch (IOException e){

            }

    }

    private void deleteImages(String key,ObservableEmitter<HashMap<String, ArrayList<String>>> emitter){
        for (String img:getSelectFiles()){
            if(delFile(Uri.parse(img))>0) {
                getListImagesInFolders().get(key).remove(img);
                emitter.onNext(getListImagesInFolders());
            }
        }
        emitter.onComplete();
    }

    private void deleteImagesAndFold(String key,ObservableEmitter<HashMap<String, ArrayList<String>>> emitter){
        ArrayList<String>images = (ArrayList<String>)getListImagesInFolders().get(key).clone();
        for (String img:images){
            if(delFile(Uri.parse(img))>0) {
                getListImagesInFolders().get(key).remove(img);
                emitter.onNext(getListImagesInFolders());
            }
        }
        if(getListImagesInFolders().get(key).size()==0){
            clearLists(key);
            WorkDBPerms.get(getContext()).delItem(key);
            DocumentFile fold = DocumentFile.fromTreeUri(getContext(),Uri.parse(key));
            if(fold.listFiles().length==0){
                delDocFile(fold.getUri());
            }
        }
        emitter.onNext(getListImagesInFolders());
        emitter.onComplete();
    }

    private int delFile(Uri uri){
        return getContext().getContentResolver().delete(uri,null,null);
    }

    private boolean delDocFile(Uri uri){

        boolean b = false;
        try {
            b = DocumentsContract.deleteDocument(getContext().getContentResolver(),uri);
        }catch(FileNotFoundException e) {
            LYTE("FragmentGalleryActionStorage FileNotFoundException del "+e.toString());
        }catch(SecurityException s){
            LYTE("FragmentGalleryActionStorage SecurityException del "+s.toString());
        }catch (IllegalArgumentException i){
            LYTE("FragmentGalleryActionStorage IllegalArgumentException del "+i.toString());
            b = true;
        }
        return b;
    }

    protected void clearLists(String key){
        if(getListImagesInFolders().containsKey(key)) {
            getListImagesInFolders().remove(key);
            getListFolds().remove(key);
            getListMutable().remove(key);
        }
    }

    private String getRealPath(String lastSegment) {
        String[]split = lastSegment.split("[:]");
        String[]sub = lastSegment.split(split[0]+":");
        String[]storage = getContext().getExternalFilesDir(null).getAbsolutePath().split("[/]");
        String p = "/"+storage[1]+"/"+split[0]+"/"+sub[1];
        if(p.startsWith("/storage/primary/")){
            String[]correct = p.split("/storage/primary/");
            return "/storage/emulated/0/"+correct[1];
        } else return p;
    }

}
