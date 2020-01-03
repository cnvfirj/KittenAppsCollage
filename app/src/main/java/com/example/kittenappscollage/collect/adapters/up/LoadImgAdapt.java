package com.example.kittenappscollage.collect.adapters.up;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.HashMap;

public class LoadImgAdapt extends StartLoadImgAdapter {

    private FrameLayout.LayoutParams params;

    public LoadImgAdapt(Context context) {
        super(context);
    }

    public LoadImgAdapt(Context context, HashMap<String, ArrayList<String>> all) {
        super(context, all);
    }


    public void setParams(int width){
        int w = width/3;
        params = new FrameLayout.LayoutParams(w,w);
    }

    @Override
    protected void createHolder(View holder) {
        super.createHolder(holder);
        if(params!=null)holder.setLayoutParams(params);
    }
}
