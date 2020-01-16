package com.example.kittenappscollage.collect.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.kittenappscollage.collect.dialogActions.DialogAction;
import com.example.kittenappscollage.collect.dialogActions.ListenActions;

import java.io.File;
import java.util.ArrayList;

import static com.example.kittenappscollage.collect.adapters.ListenLoadFoldAdapter.ROOT_ADAPTER;
import static com.example.kittenappscollage.collect.dialogActions.DialogAction.ACTION_DELETE;
import static com.example.kittenappscollage.helpers.Massages.LYTE;

public class FragmentGalleryAction extends FragmentSelectedGallery implements ListenActions {

    private final String TAG_DIALOG = "dialog act";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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
        if(done&&!name.isEmpty()) {

            final String oldFold = getKey();//адрес папки
            final String nameFold = getListFolds().get(getKey());//имя папки
            final String excludeNameFold = oldFold.split(nameFold)[0];
            final String newFold = excludeNameFold+name;//новое имя папки

            File oldfile = new File(oldFold);
            File newfile = new File(newFold);
//            oldfile.canWrite();
            if(oldfile.renameTo(newfile)){
                ArrayList<String>imgs = getListImagesInFolders().get(getKey());//все коллажи
                ArrayList<String >newImgs = new ArrayList<>();
                for (String path:imgs){
                    String[]split = path.split(nameFold);
                    String p = split[0]+name+split[1];
                    newImgs.add(p);
                    getContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(p))));
                    getContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(path))));

                }
                getListImagesInFolders().remove(getKey());
                getListImagesInFolders().put(name,newImgs);
                getListFolds().remove(getKey());
                getListFolds().put(name, newFold);

            }
                 getFoldAdapt().setAll(getListImagesInFolders());
                 getImgAdapt().setAll(getListImagesInFolders());
        }
    }

    /*вывести это все в паралельный поток*/
    private void deleteFolder(){
        ArrayList<String>imgs = getListImagesInFolders().get(getKey());
        for (String img:imgs){
            File file = new File(img);
            if(file.exists())file.delete();
            getContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
        }

        File file = new File(getKey());
        if(file.exists())file.delete();

        getListImagesInFolders().remove(getKey());
        getListFolds().remove(getKey());
        getIndexesStorage().remove(getKey());
        getFoldAdapt().setAll(getListImagesInFolders());
        getImgAdapt().setAll(getListImagesInFolders());
    }

    private void deleteSelectedImg(){
        for (String img:getSelectFiles()){
            File file = new File(img);
            if(file.exists())file.delete();
            getContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
            getListImagesInFolders().get(getKey()).remove(img);
        }

        getFoldAdapt().setAll(getListImagesInFolders());
        getImgAdapt().setAll(getListImagesInFolders());


    }
}
