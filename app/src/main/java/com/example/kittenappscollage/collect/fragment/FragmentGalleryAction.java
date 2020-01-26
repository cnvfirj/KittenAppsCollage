package com.example.kittenappscollage.collect.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.storage.StorageManager;
import android.provider.DocumentsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.documentfile.provider.DocumentFile;

import com.example.kittenappscollage.collect.dialogActions.DialogAction;
import com.example.kittenappscollage.collect.dialogActions.ListenActions;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import static com.example.kittenappscollage.collect.adapters.ListenLoadFoldAdapter.ROOT_ADAPTER;
import static com.example.kittenappscollage.collect.dialogActions.DialogAction.ACTION_DELETE;
import static com.example.kittenappscollage.helpers.Massages.LYTE;

public class FragmentGalleryAction extends FragmentSelectedGallery implements ListenActions {

    private final int REQUEST_STORAGE = 121;

    private final String TAG_DIALOG = "dialog act";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        permission();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void clickSel_1(ImageView v) {
        super.clickSel_1(v);
        /*скріть папку*/
        /*візов диалога*/
    }

    @Override
    protected void clickSel_2(ImageView v) {
        super.clickSel_2(v);
        /*переименовать*/
        /*візов диалога*/
        DialogAction.inst(DialogAction.ACTION_RENAME, getIndexAdapter(),this)
                .show(getFragmentManager().beginTransaction(),TAG_DIALOG);
    }

    @Override
    protected void clickSel_3(ImageView v) {
        super.clickSel_3(v);
        if(getIndexAdapter()== ROOT_ADAPTER){
            /*переместить на карту*/
            /*візов диалога*/
        }else {
            /*поделиться вібранное*/
            /*візов диалога*/
        }
    }

    @Override
    protected void clickSel_4(ImageView v) {
        super.clickSel_4(v);
        if(getIndexAdapter()== ROOT_ADAPTER){
            /*удалить папку*/
            /*візов диалога*/

        }else {
            /*удалить вібраное*/
            /*візов диалога*/

        }
        DialogAction.inst(ACTION_DELETE, getIndexAdapter(),this)
                .show(getFragmentManager().beginTransaction(),TAG_DIALOG);
    }

    @Override
    public void result(boolean done, int action, int indexAdapter) {
        invisibleMenu();
        if(!done)return;
        if(action==ACTION_DELETE){
            if(indexAdapter==ROOT_ADAPTER)deleteFolder();
            else deleteSelectedImg();
        }

    }

    /*это применимо только в корневом адаптере*/
    /*применить другое сканирование почему то старую папку не удаляет*/
    @Override
    public void result(boolean done, String name) {
        invisibleMenu();
//        if(done&&!name.isEmpty()) {
////
//            final String nameFold = getListFolds().get(getKey());//имя папки
//            final String excludeNameFold = getKey().split(nameFold)[0];
//            final String newFold = excludeNameFold+name;//новое имя папки
//            File oldfile = new File(getKey());
//            File newfile = new File(newFold);
////            oldfile.canWrite();
//            if(oldfile.renameTo(newfile)){
////            if(rename(getKey(),nameFold)){
//
//                ArrayList<String>imgs = getListImagesInFolders().get(getKey());//все коллажи
//                ArrayList<String >newImgs = new ArrayList<>();
//                for (String path:imgs){
//                    String[]split = path.split(nameFold);
//                    String p = split[0]+name+split[1];
//                    newImgs.add(p);
//                    getContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(p))));
//                    getContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(path))));
//
//                }
//
//                getListImagesInFolders().put(newFold,newImgs);
//                getListImagesInFolders().remove(getKey());
//
//                getListFolds().put(newFold, nameFold);
//                getListFolds().remove(getKey());
//
//                getIndexesStorage().put(newFold,getIndexesStorage().get(getKey()));
//                getIndexesStorage().remove(getKey());
//
//                getListPartition().put(nameFold,getListPartition().get(getKey()));
//                getListPartition().remove(getKey());
//
//            }
//            getFoldAdapt().setAll(getListImagesInFolders());
//            getImgAdapt().setAll(getListImagesInFolders());
//        }
    }

    /*вывести это все в паралельный поток*/
    private void deleteFolder(){
//        ArrayList<String>imgs = getListImagesInFolders().get(getKey());
//        for (String img:imgs){
//            File file = new File(img);
//            if(file.exists())file.delete();
//            getContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
//        }
//        File file = new File(getKey());
//        if(file.exists())file.delete();
//
//        getListImagesInFolders().remove(getKey());
//        getListFolds().remove(getKey());
//        getIndexesStorage().remove(getKey());
//        getListPartition().remove(getKey());
//
//        getFoldAdapt().setAll(getListImagesInFolders());
//        getImgAdapt().setAll(getListImagesInFolders());
    }

    private void deleteSelectedImg(){
//        for (String img:getSelectFiles()){
//            File file = new File(img);
//            if(file.exists())file.delete();
//            getContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
//            getListImagesInFolders().get(getKey()).remove(img);
//        }
//
//        getFoldAdapt().setAll(getListImagesInFolders());
//        getImgAdapt().setAll(getListImagesInFolders());


    }

    private boolean rename(String oldName, String newName){
        boolean b = false;
        LYTE(oldName);
        Uri treeUri = Uri.parse("/tree/2E26-1C24/Download");

//            DocumentFile folder = DocumentFile.fromTreeUri(getContext(), treeUri);
//            DocumentFile fileToRename = folder.listFiles()[0];
            try{
                DocumentsContract.renameDocument(getContext().getContentResolver(), treeUri, newName);
                b = true;
            }
            catch(FileNotFoundException exception){

            }
            return b;
    }


    private void permission(){
//        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
//        startActivityForResult(intent, REQUEST_STORAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        if(requestCode == REQUEST_STORAGE && resultCode == Activity.RESULT_OK && data!= null && data.getData() != null){
//            Uri treeUri = data.getData();
//            int takeFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION;
//            getContext().getContentResolver().takePersistableUriPermission(treeUri, takeFlags);
//        }
//        else {
//            super.onActivityResult(requestCode, resultCode, data);
//        }
    }

    private void publicUri(Uri uri){

    }
}
