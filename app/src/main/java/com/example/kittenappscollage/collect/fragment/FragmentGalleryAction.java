package com.example.kittenappscollage.collect.fragment;

import android.widget.ImageView;

import com.example.kittenappscollage.collect.dialogActions.DialogAction;
import com.example.kittenappscollage.collect.dialogActions.ListenActions;
import com.example.kittenappscollage.helpers.App;
import com.example.kittenappscollage.helpers.Massages;
import com.example.kittenappscollage.helpers.RequestFolder;
import com.example.kittenappscollage.helpers.db.aller.ActionsContentPerms;

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
            final String perm = getListPerms().get(getKey());
            if(perm==null||perm.equals(ActionsContentPerms.NON_PERM)){
                Massages.SHOW_MASSAGE(getContext(),"Нет прав для переименования этой папки");
                invisibleMenu();
            }else if(perm.equals(ActionsContentPerms.GRAND)){
                renameFoldFile(name, getKey());
            }else {
               renameFoldStorage(name, getKey());
            }
        }
    }

    private void deleteFolder(){
        final String perm = getListPerms().get(getKey());
       if(perm==null||perm.equals(ActionsContentPerms.NON_PERM)){
           Massages.SHOW_MASSAGE(getContext(),"Нет прав для удаления этой папки");
           invisibleMenu();
       }else if(perm.equals(ActionsContentPerms.GRAND)){
           deletedFoldFile(getKey());
       }else {
           deletedFoldStorage(getKey());
       }

    }

    private void copyFolder(){
        LYTE("FragmentGalleryAction copy folder - "+getKey());

    }

    private void shareSelectedImg(int adapter){

    }

    private void deleteSelectedImg(int adapter){

    }

    protected boolean version(){
        return App.checkVersion();
    }

    protected void copyFolderInStorage(String key){
        /*копирование папки с карты сд на у-во или обратно
        * рассмотреть возможность это делать со съемным носителем*/
    }

    protected void copyFolderInDevice(String key){

    }


    protected void invisFolder(){
        /*делает невидимой папку в этом приложении
        * файловая система не имеет значение.
        * Так как данные вносятся в базу данных*/
    }
    protected void shareSelImagesFile(){
        /*расшарить как файловой системы*/
    }

    protected void renameFoldFile(String name, String key){


    }

    protected void renameFoldStorage(String name, String key){

    }

    /*android >= 9 file system*/
    protected void applyDeleteSelectedFile(){
       /*удалить выбранные изображения как Storage Assets Framework*/

    }

    protected void applyDeleteSelectedStorage(){
       /*удалить выбранные изображения как файлы*/

    }

    /*android > 9 file system*/
    protected void deletedFoldStorage(String fold){
        /*удалить папку как Storage Assets Framework*/

    }

    protected void deletedFoldFile(String fold){
        /*удалить выбранную папку как файл*/

    }


}
