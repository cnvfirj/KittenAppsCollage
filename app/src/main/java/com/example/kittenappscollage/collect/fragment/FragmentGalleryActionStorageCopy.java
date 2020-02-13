package com.example.kittenappscollage.collect.fragment;

import com.example.kittenappscollage.helpers.App;
import com.example.kittenappscollage.helpers.Massages;

import static com.example.kittenappscollage.helpers.Massages.LYTE;

public class FragmentGalleryActionStorageCopy extends FragmentGalleryActionStorage {

    @Override
    protected void copyFolderInStorage(String fold) {
        super.copyFolderInStorage(fold);
        if(getNamesStorage().size()>0){
            /*copy continued*/
            if(App.checkVersion())copyFoldAPI21(fold);
            else copyFoldAPI29(fold);
        }else Massages.SHOW_MASSAGE(getContext(),"Подключи SD card или накопитель и перезапусти приложение");
    }


    /*android <= 9*/
    private void copyFoldAPI21(String fold){

    }

    /*android > 9*/
    private void copyFoldAPI29(String fold){

    }
}
