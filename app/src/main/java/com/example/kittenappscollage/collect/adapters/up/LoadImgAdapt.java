package com.example.kittenappscollage.collect.adapters.up;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.HashMap;

public class LoadImgAdapt extends ListenLoadImgAdapter {

    private FrameLayout.LayoutParams params;



    public LoadImgAdapt(Context context) {
        super(context);
    }

    public void setParams(int width){
        int w = width/3;
        params = new FrameLayout.LayoutParams(w,w);
    }

    @Override
    public void createHolder(View holder, int pos) {
        super.createHolder(holder, pos);
        if(params!=null)holder.setLayoutParams(params);
    }

}
