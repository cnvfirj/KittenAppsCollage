package com.example.kittenappscollage.collect.adapters;

import android.content.Context;
import android.view.View;

import com.example.kittenappscollage.collect.TouchViewListener;

import java.util.ArrayList;

import static com.example.kittenappscollage.collect.adapters.FileAdapter.SOURCE_DOWNLOAD;
import static com.example.kittenappscollage.collect.adapters.FileAdapter.SOURCE_PHOTO;
import static com.example.kittenappscollage.collect.adapters.FileAdapter.SOURCE_PROJECT;
import static com.example.kittenappscollage.helpers.Massages.LYTE;

/*в этом классе получаем адаптер в зависимости от полученной константы*/
public class SelectorAdapter{

    private static SelectorAdapter singleton = null;

    private FileAdapter sDownAdapter,sFileAdapter,sPhotoAdapter;

    public SelectorAdapter(Context context) {
        sDownAdapter = new FileAdapter(context,SOURCE_DOWNLOAD);
        sFileAdapter = new FileAdapter(context,SOURCE_PROJECT);
        sPhotoAdapter = new FileAdapter(context,SOURCE_PHOTO);
    }

    public static SelectorAdapter get(Context context){
        if (singleton==null){
                singleton = new SelectorAdapter(context);
        }
        return singleton;
    }

    public SelectorAdapter setListImg( int source){
        switch (source){
            case SOURCE_DOWNLOAD:
//                sDownAdapter.setListImg(imgs);
                break;
            case SOURCE_PROJECT:
//                sFileAdapter.setListImg(imgs);
                break;
            case SOURCE_PHOTO:
//                    sPhotoAdapter.setListImg(imgs);
                break;

            default:
                break;
        }
        return this;
    }

    public SelectorAdapter setListener(View.OnClickListener listener){

        return this;
    }

    public FileAdapter adapter(int source){
        switch (source){
            case SOURCE_DOWNLOAD:
                return sDownAdapter;
            case SOURCE_PROJECT:
                return sFileAdapter;
            case SOURCE_PHOTO:
                return sPhotoAdapter;
                     default:
                return null;
        }
    }



}
