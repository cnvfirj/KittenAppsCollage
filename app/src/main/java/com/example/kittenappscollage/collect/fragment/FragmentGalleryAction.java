package com.example.kittenappscollage.collect.fragment;

import android.widget.ImageView;

import com.example.kittenappscollage.collect.dialogActions.DialogAction;
import com.example.kittenappscollage.collect.dialogActions.ListenActions;
import com.example.kittenappscollage.helpers.App;
import com.example.kittenappscollage.helpers.Massages;
import com.example.kittenappscollage.helpers.RequestFolder;
import com.example.kittenappscollage.helpers.db.ActionsDataBasePerms;

import static com.example.kittenappscollage.collect.adapters.ListenLoadFoldAdapter.ROOT_ADAPTER;
import static com.example.kittenappscollage.collect.dialogActions.DialogAction.ACTION_DELETE;
import static com.example.kittenappscollage.collect.dialogActions.DialogAction.ACTION_INVIS;
import static com.example.kittenappscollage.collect.dialogActions.DialogAction.ACTION_SHARE;
import static com.example.kittenappscollage.helpers.Massages.LYTE;
import static com.example.kittenappscollage.helpers.Massages.MASSAGE;

public class FragmentGalleryAction extends FragmentSelectedGallery implements ListenActions {

    private final String TAG_DIALOG = "FragmentGalleryAction dialog act";

    @Override
    protected void clickSel_1(ImageView v) {
        super.clickSel_1(v);
        /*скріть папку*/
        /*візов диалога*/
        DialogAction.inst(DialogAction.ACTION_INVIS, getIndexAdapter(),this)
                .show(getFragmentManager().beginTransaction(),TAG_DIALOG);
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
            DialogAction.inst(ACTION_SHARE, getIndexAdapter(),this)
                    .show(getFragmentManager().beginTransaction(),TAG_DIALOG);
        }else {
            /*поделиться вібранное*/
            /*візов диалога*/
            DialogAction.inst(ACTION_SHARE, getIndexAdapter(),this)
                    .show(getFragmentManager().beginTransaction(),TAG_DIALOG);
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
        }else if(action==ACTION_SHARE){
            if(indexAdapter==ROOT_ADAPTER)copyFolder();
            else shareSelectedImg(indexAdapter);
        }else if(action==ACTION_INVIS){
            invisFolder();
        }
    }

    /*это применимо только в корневом адаптере*/
    @Override
    public void result(boolean done, String name) {
        if(done&&!name.isEmpty()) {
            if (version()) renameFoldFile(name);
            else renameFoldStorage(name);
        }
    }

    private void renameFoldFile(String name){
       if(getIndexesStorage().get(getKey())==0) {
           if(getKey().equals(RequestFolder.getFolderImages())){
               renameFoldFileDevise(name);
           }else if(getListPerms().get(getKey())==null||getListPerms().get(getKey()).equals(ActionsDataBasePerms.NON_PERM)){
               Massages.SHOW_MASSAGE(getContext(),"Нет прав для переименования этой папки");
               invisibleMenu();
           }else renameFoldFileDevise(name);
       }else renameFoldStorage(name);
    }

    private void deleteFolder(){
        if(version()) {
            if (getIndexesStorage().get(getKey()) == 0) {
                deleteFoldDeviseFile(getKey());
            }else {
                /*delete in sd card*/
                deleteFoldStorage(getKey());
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
                applyDeleteSelectedStorage();
            }
        }else {
            /*при андроид 11*/
            invisibleMenu();
        }
    }

    private void deleteFoldDeviseFile(String fold){
        if(fold.equals(RequestFolder.getFolderImages())){
            deletedFoldFile(fold);
        } else if(getListPerms().get(fold).equals(ActionsDataBasePerms.GRAND)){
            deletedFoldFile(fold);
        }else {
            Massages.SHOW_MASSAGE(getContext(),"Нет прав для удаления папки");
            invisibleMenu();
        }
    }

    private void copyFolder(){
        LYTE("FragmentGalleryAction copy folder - "+getKey());
        if(getNamesStorage().size()>1){
            copyFolderStorage(getKey());
        }else {
            Massages.SHOW_MASSAGE(getContext(), "Копировать папку с ее содержимым можно только при наличии карты SD");
        }
    }

    private void shareSelectedImg(int adapter){
        if(version()){
            shareSelImagesFile();
        }
    }

    protected boolean version(){
        return App.checkVersion();
    }

    protected void copyFolderStorage(String fold){
        /*копирование папки с карты сд на у-во или обратно
        * рассмотреть возможность это делать со съемным носителем*/
    }
    protected void invisFolder(){
        /*делает невидимой папку в этом приложении
        * файловая система не имеет значение.
        * Так как данные вносятся в базу данных*/
    }
    protected void shareSelImagesFile(){
        /*расшарить как файловой системы*/
    }

    protected void renameFoldStorage(String name){
       /*переименовать как Storage Assets Framework*/
        if(getListPerms().get(getKey())==null||getListPerms().get(getKey()).equals(ActionsDataBasePerms.NON_PERM)) {
            Massages.SHOW_MASSAGE(getContext(), "Нет прав для переименования этой папки");
            invisibleMenu();
            return;
        }
    }


    protected void renameFoldFileDevise(String name){
       /*переименовать как файл*/
    }

    protected void applyDeleteSelectedFiles(){
       /*удалить выбранные изображения как Storage Assets Framework*/
    }

    protected void applyDeleteSelectedStorage(){
       /*удалить выбранные изображения как файлы*/
    }

    protected void deleteFoldStorage(String fold){
        /*удалить папку как Storage Assets Framework*/
        if(getListPerms().get(getKey())==null||getListPerms().get(getKey()).equals(ActionsDataBasePerms.NON_PERM)) {
            Massages.SHOW_MASSAGE(getContext(), "Нет прав для удаления этой папки");
            invisibleMenu();
            return;
        }
    }

    protected void deletedFoldFile(String fold){
        /*удалить выбранную папку как файл*/
    }


}
