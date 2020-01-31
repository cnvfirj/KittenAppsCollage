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
import com.example.kittenappscollage.helpers.App;
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
        DialogAction.inst(ACTION_DELETE, getIndexAdapter(),this)
                .show(getFragmentManager().beginTransaction(),TAG_DIALOG);
    }

    @Override
    public void result(boolean done, int action, int indexAdapter) {
        if(!done)return;
        if(action==ACTION_DELETE){
            if(indexAdapter==ROOT_ADAPTER)deleteFolder();
            else deleteSelectedImg(indexAdapter);
        }
    }

    private boolean version(){
        return App.checkVersion();
    }

    /*это применимо только в корневом адаптере*/
    /*применить другое сканирование почему то старую папку не удаляет*/
    @Override
    public void result(boolean done, String name) {
//        invisibleMenu();
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
               Massages.SHOW_MASSAGE(getContext(),"Нет прав для переименования этой папки");
               invisibleMenu();
           }else renameInDevise(name);
       }else renameFoldAPI29(name);
    }

    private void deleteFolder(){
        if(version()) {
            if (getIndexesStorage().get(getKey()) == 0) {
                deleteInDevVer21(getKey());
            }else {
                /*delete in sd card*/
            }
        }else {
            /*при андроид 11*/
            invisibleMenu();
        }
    }

    private void deleteSelectedImg(int adapter){
        if(version()) {
            if (getIndexesStorage().get(getKey()) == 0) {
                applyDeleteSelectedFiles();
            }else {
                /*delete in sd card*/
            }
        }else {
            /*при андроид 11*/
            invisibleMenu();
        }
    }



    private void renameFoldAPI29(String name){

    }

    private void renameInDevise(String name){
        final String nameFold = getListFolds().get(getKey());//имя папки
        final String excludeNameFold = getKey().split(nameFold)[0];
        final String newFold = excludeNameFold + name;//новое имя папки
        File oldfile = new File(getKey());
        File newfile = new File(newFold);
        if (getListFolds().keySet().contains(newFold)) {
            Massages.SHOW_MASSAGE(getContext(), "Выбери другое имя папке");
        } else {
            File[] old = oldfile.listFiles();
            if (oldfile.renameTo(newfile)) {

                ActionsDataBasePerms.create(getContext()).initInThread(newFold,ActionsDataBasePerms.GRAND);
                getListPerms().put(newFold,ActionsDataBasePerms.GRAND);

                File[] young = newfile.listFiles();
                for (int i = 0; i < young.length; i++) {
                    getContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(old[i])));
                    getContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(young[i])));
                }

                cleaкLists(getKey());

            } else {
                Massages.SHOW_MASSAGE(getContext(), "Не удалось переименовать папку");
            }

        }
    }

    private void cleaкLists(String key){
        if(!key.equals(RequestFolder.getFolderImages())){
            getListPerms().remove(key);
            ActionsDataBasePerms.create(getContext()).deleteInThread(key);
        }
        getListImagesInFolders().remove(key);
        getListFolds().remove(key);
        getIndexesStorage().remove(key);
    }

    private void deleteInDevVer21(String fold){
        LYTE("FragmentGalleryAction delete "+fold);
        if(fold.equals(RequestFolder.getFolderImages())){
            deletedFoldFile(fold);
        } else if(!fold.equals(RequestFolder.getFolderImages())&&getListPerms().get(fold).equals(ActionsDataBasePerms.GRAND)){
           deletedFoldFile(fold);
        }else {
            Massages.SHOW_MASSAGE(getContext(),"Нет прав для удаления папки");
            invisibleMenu();
        }
    }

    private void applyDeleteSelectedFiles(){
        if(getListPerms().get(getKey())==null||getListPerms().get(getKey()).equals(ActionsDataBasePerms.NON_PERM)){
            Massages.SHOW_MASSAGE(getContext(),"Нет прав для удаления из этой папки");
            invisibleMenu();
            return;
        }
       boolean[]checks = getImgAdapt().getArrChecks();
       ArrayList<String>imgs = getListImagesInFolders().get(getKey());
       int sum = 0;
       int all = imgs.size();
       for (int i=0;i<checks.length;i++){
           if(checks[i]){
               sum++;
               /*перебираем файлы в обратном порядке*/
               File f = new File(imgs.get(all-(i+1)));
               if(f.exists())f.delete();
               getListImagesInFolders().get(getKey()).remove(f.getAbsolutePath());
               setListImagesInFolders(getListImagesInFolders());
               getContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(f)));
           }
       }

       if(sum>0&&sum<all){
           /*так как изменение произошло в текущей директрии
           * то в адаптере эта папка получит индекс 0.
           * Это связано с сортировкой по времени изменения*/
           getImgAdapt().setIndexKey(0);
       }

       if(sum==all){
           File f = new File(getKey());
           if(f.exists()){
               if(f.delete()){
                   if(!getKey().equals(RequestFolder.getFolderImages()))
                   ActionsDataBasePerms.create(getContext()).deleteInThread(getKey());
               }
           }
           cleaкLists(getKey());
           if (getImgAdapt().isModeSelected()) {
               invisibleMenu();
               getImgAdapt().setModeSelected(false);
           }

           setIndexAdapter(ROOT_ADAPTER);
           getGridLayoutManager().setSpanCount(2);
           getRecycler().setAdapter(getFoldAdapt());

           setListImagesInFolders(getListImagesInFolders());
       }
    }


    private void deletedFoldFile(String fold){
        File file = new File(fold);
        File[]files = file.listFiles();
        for (File f:files){
            if(f.exists()) {
                if(f.delete()){
                    if(!fold.equals(RequestFolder.getFolderImages()))
                        ActionsDataBasePerms.create(getContext()).deleteInThread(fold);
                }
            }
            getListImagesInFolders().get(fold).remove(f.getAbsolutePath());
            setListImagesInFolders(getListImagesInFolders());
            getContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(f)));
        }
        if(file.exists()){
            if(file.delete())ActionsDataBasePerms.create(getContext()).deleteInThread(fold);

        }

        cleaкLists(fold);
        setListImagesInFolders(getListImagesInFolders());
    }






    private void permission(){
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

    }

}
