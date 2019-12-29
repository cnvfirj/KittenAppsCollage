package com.example.kittenappscollage.collect.adapters;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import static com.example.kittenappscollage.collect.adapters.FileAdapter.SOURCE_DOWNLOAD;
import static com.example.kittenappscollage.collect.adapters.FileAdapter.SOURCE_PHOTO;
import static com.example.kittenappscollage.collect.adapters.FileAdapter.SOURCE_PROJECT;

/*в этом классе получаем адаптер в зависимости от полученной константы*/
public class SelectorAdapter{

    private static SelectorAdapter singleton = null;

    private PresentAdapter sDownAdapter,sFileAdapter,sPhotoAdapter;

    private FrameLayout.LayoutParams params;

    public SelectorAdapter(Context context) {
        sDownAdapter = new PresentAdapter(context,SOURCE_DOWNLOAD);
        sFileAdapter = new PresentAdapter(context,SOURCE_PROJECT);
        sPhotoAdapter = new PresentAdapter(context,SOURCE_PHOTO);
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

    public void setParams(int width){
        int w = width/3;
       params = new FrameLayout.LayoutParams(w,w);
       sDownAdapter.setParams(params);
       sPhotoAdapter.setParams(params);
       sFileAdapter.setParams(params);
    }

    public SelectorAdapter setListener(View.OnClickListener listener){

        return this;
    }

    public FileAdapter adapter(int source){
        switch (source){
            case SOURCE_DOWNLOAD:
                return sDownAdapter.resetChecks();
            case SOURCE_PROJECT:
                return sFileAdapter.resetChecks();
            case SOURCE_PHOTO:
                return sPhotoAdapter.resetChecks();
                     default:
                return null;
        }
    }



}
