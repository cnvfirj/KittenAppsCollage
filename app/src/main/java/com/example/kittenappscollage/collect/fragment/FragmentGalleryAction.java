package com.example.kittenappscollage.collect.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.UriPermission;
import android.net.Uri;
import android.os.Build;
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
import com.example.kittenappscollage.helpers.Massages;
import com.example.kittenappscollage.helpers.RequestFolder;
import com.example.kittenappscollage.helpers.db.ActionsDataBasePerms;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static com.example.kittenappscollage.collect.adapters.ListenLoadFoldAdapter.ROOT_ADAPTER;
import static com.example.kittenappscollage.collect.dialogActions.DialogAction.ACTION_DELETE;
import static com.example.kittenappscollage.helpers.Massages.LYTE;

public class FragmentGalleryAction extends FragmentSelectedGallery implements ListenActions {

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
        hideMenuInAction();
        if(!done)return;
        if(action==ACTION_DELETE){
            if(indexAdapter==ROOT_ADAPTER)deleteFolder();
            else deleteSelectedImg();
        }

    }

    private boolean version(){
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.Q;
    }

    /*это применимо только в корневом адаптере*/
    /*применить другое сканирование почему то старую папку не удаляет*/
    @Override
    public void result(boolean done, String name) {
        hideMenuInAction();
        if(done&&!name.isEmpty()) {
            if (version()) renameFoldAPI21(name);
            else renameFoldAPI29(name);
        }
    }


    private void renameFoldAPI21(String name){

       if(getIndexesStorage().get(getKey())==0) {
           if(getKey().equals(RequestFolder.getFolderImages())){
               renameInDevise(name);
           }else if(getListPerms().get(getKey())==null||getListPerms().get(getKey()).equals(ActionsDataBasePerms.NON_PERM)){
               Massages.SHOW_MASSAGE(getContext(),"Нет прав для переименования");
               invisibleMenu();

           }else renameInDevise(name);

       }else renameFoldAPI29(name);

    }



    private void renameFoldAPI29(String name){

    }

    private void renameInSD(String name){

    }

    private void renameInDevise(String name){
//        final String nameFold = getListFolds().get(getKey());//имя папки
//        final String excludeNameFold = getKey().split(nameFold)[0];
//        final String newFold = excludeNameFold + name;//новое имя папки
//        File oldfile = new File(getKey());
//        File newfile = new File(newFold);
//        if (getListFolds().keySet().contains(newFold)) {
//            Massages.SHOW_MASSAGE(getContext(), "Выбери другое имя папке");
//        } else {
//            File[] old = oldfile.listFiles();
//            if (oldfile.renameTo(newfile)) {
//
//                ActionsDataBasePerms.create(getContext()).initInThread(newFold,ActionsDataBasePerms.GRAND);
//                getListPerms().put(newFold,ActionsDataBasePerms.GRAND);
//                File[] young = newfile.listFiles();
//                for (int i = 0; i < young.length; i++) {
//                    getContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(old[i])));
//                    getContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(young[i])));
//                }
//                if(!getKey().equals(RequestFolder.getFolderImages())){
//                    getListPerms().remove(getKey());
//                    ActionsDataBasePerms.create(getContext()).deleteInThread(getKey());
//                }
//                getListImagesInFolders().remove(getKey());
//                getListFolds().remove(getKey());
//                getIndexesStorage().remove(getKey());
//            } else {
//                Massages.SHOW_MASSAGE(getContext(), "Не удалось переименовать папку");
//            }
//
//        }
    }

    /*вывести это все в паралельный поток*/
    private void deleteFolder(){
        if(version()) {
            if (getIndexesStorage().get(getKey()) == 0) {
                if (getKey().equals(RequestFolder.getFolderImages())) {
                    File file = new File(getKey());

                }
            }
        }else {

        }

    }

    private void deleteSelectedImg(){

    }




    private void permission(){
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

    }

}
