package com.example.kittenappscollage.collect.fragment;

import android.widget.ImageView;

import com.example.kittenappscollage.helpers.App;

import static com.example.kittenappscollage.collect.adapters.ListenLoadFoldAdapter.ROOT_ADAPTER;
import static com.example.kittenappscollage.helpers.Massages.LYTE;
import static com.example.kittenappscollage.helpers.Massages.MASSAGE;

public class FragmentGalleryAction extends FragmentSelectedGallery{

    private final String TAG_DIALOG = "FragmentGalleryAction dialog act";

    @Override
    protected void clickSel_1(ImageView v) {
        super.clickSel_1(v);
        invisFolder();

    }

    @Override
    protected void clickSel_3(ImageView v) {
        super.clickSel_3(v);
        if(getIndexAdapter()== ROOT_ADAPTER){
            copyFolder();
        }else {
            shareSelectedImg(getIndexAdapter());
        }
    }

    @Override
    protected void clickSel_4(ImageView v) {
        super.clickSel_4(v);
        if(getIndexAdapter()==ROOT_ADAPTER)deleteFolder();
        else deleteSelectedImg(getIndexAdapter());
    }

    private void deleteFolder(){
           deletedFoldStorage(getKey());
    }

    private void copyFolder(){
        LYTE("FragmentGalleryAction copy folder - "+getKey());

    }

    private void shareSelectedImg(int adapter){

    }

    private void deleteSelectedImg(int adapter){
            applyDeleteSelectedStorage();
    }

    protected boolean version(){
        return App.checkVersion();
    }

    protected void invisFolder(){
        /*делает невидимой папку в этом приложении
        * файловая система не имеет значение.
        * Так как данные вносятся в базу данных*/
    }

    protected void applyDeleteSelectedStorage(){
       /*удалить выбранные изображения как файлы*/

    }

    /*android > 9 file system*/
    protected void deletedFoldStorage(String fold){
        /*удалить папку как Storage Assets Framework*/

    }



}
