package com.example.kittenappscollage.collect.adapters;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import java.io.File;

import static com.example.kittenappscollage.collect.adapters.FileAdapter.SOURCE_DOWNLOAD;
import static com.example.kittenappscollage.collect.adapters.FileAdapter.SOURCE_PHOTO;
import static com.example.kittenappscollage.collect.adapters.FileAdapter.SOURCE_PROJECT;

/*в этом классе получаем адаптер в зависимости от полученной константы*/
public class SelectorAdapter{

    private PresentAdapter sDownAdapter,sFileAdapter,sPhotoAdapter;

    private FrameLayout.LayoutParams params;


    public SelectorAdapter(Context context) {
        sDownAdapter = new PresentAdapter(context,SOURCE_DOWNLOAD);
        sFileAdapter = new PresentAdapter(context,SOURCE_PROJECT);
        sPhotoAdapter = new PresentAdapter(context,SOURCE_PHOTO);

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

    public SelectorAdapter setListener(PresentAdapter.ModeSelected listener){
        sDownAdapter.setListener(listener);
        sFileAdapter.setListener(listener);
        sPhotoAdapter.setListener(listener);
        return this;
    }

    public SelectorAdapter scanAll(){
        sDownAdapter.requestList().resetArrChecks();
        sFileAdapter.requestList().resetArrChecks();
        sPhotoAdapter.requestList().resetArrChecks();
        return this;
    }
    public PresentAdapter update(int source){
        switch (source){
            case SOURCE_DOWNLOAD:
                sDownAdapter.requestList();
                return sDownAdapter;
            case SOURCE_PROJECT:
                sFileAdapter.requestList();
                return sFileAdapter;
            case SOURCE_PHOTO:
                sPhotoAdapter.requestList();
                return sPhotoAdapter;
        }
        sFileAdapter.requestList();
        return sFileAdapter;
    }
    public PresentAdapter reset(int source){
        switch (source){
            case SOURCE_DOWNLOAD:
                return sDownAdapter.resetChecks();
            case SOURCE_PROJECT:
                return sFileAdapter.resetChecks();
            case SOURCE_PHOTO:
                return sPhotoAdapter.resetChecks();
        }
        sFileAdapter.resetChecks();
        return sFileAdapter;
    }

    public PresentAdapter adapter(int source){
        switch (source){
            case SOURCE_DOWNLOAD:
                return sDownAdapter;
            case SOURCE_PROJECT:
                return sFileAdapter;
            case SOURCE_PHOTO:
                return sPhotoAdapter;
        }
        return sFileAdapter;
    }



}
