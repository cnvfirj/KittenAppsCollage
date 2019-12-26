package com.example.kittenappscollage.collect.adapters;

import android.content.Context;

import com.example.kittenappscollage.collect.TouchViewListener;

import java.util.ArrayList;

/*в этом классе получаем адаптер в зависимости от полученной константы*/
public class SelectorAdapter{

    private static SelectorAdapter singleton = null;

    private SuperAdapter sDownAdapter,sFileAdapter,sPhotoAdapter;

    public SelectorAdapter(Context context) {
        sDownAdapter = new AdapterDownloads(context,SuperAdapter.SOURCE_DOWNLOAD);
        sFileAdapter = new AdapterProject(context,SuperAdapter.SOURCE_PROJECT);
        sPhotoAdapter = new AdapterPhoto(context,SuperAdapter.SOURCE_PHOTO);
    }

    public static SelectorAdapter get(Context context){
        if (singleton==null){
                singleton = new SelectorAdapter(context);
        }
        return singleton;
    }

    public SelectorAdapter setListImg(ArrayList<String> imgs, int source){
        switch (source){
            case SuperAdapter.SOURCE_DOWNLOAD:
//                sDownAdapter.setListImg(imgs);
                break;
            case SuperAdapter.SOURCE_PROJECT:
//                sFileAdapter.setListImg(imgs);
                break;
            case SuperAdapter.SOURCE_PHOTO:
//                    sPhotoAdapter.setListImg(imgs);
                break;

            default:
                break;
        }
        return this;
    }

    public SelectorAdapter setListener(TouchViewListener listener){
        /*слушатель добавим в основной активити*/
//        sDownAdapter.setListener(listener);
//        sFileAdapter.setListener(listener);
//        sPhotoAdapter.setListener(listener);
        return this;
    }

    public SuperAdapter adapter(int source){
        switch (source){
            case SuperAdapter.SOURCE_DOWNLOAD:
                return sDownAdapter;
            case SuperAdapter.SOURCE_PROJECT:
                return sFileAdapter;
            case SuperAdapter.SOURCE_PHOTO:
                return sPhotoAdapter;
                     default:
                return null;
        }
    }



}
