package com.example.kittenappscollage.collect.adapters.up;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.HashMap;

public class LoadFoldAdapt extends StartLoadFoldAdapter {

    private FrameLayout.LayoutParams params;

    public LoadFoldAdapt(Context context) {
        super(context);
    }

    public LoadFoldAdapt(Context context, HashMap<String, ArrayList<String>> all) {
        super(context, all);
    }

    @Override
    public StartLoadFoldAdapter setAll(HashMap<String, ArrayList<String>> all) {
        return super.setAll(all);
    }

    public void setParams(int width){
        int w = width/2;
        params = new FrameLayout.LayoutParams(w,w);
    }

    @Override
    protected void createHolder(View holder) {
        super.createHolder(holder);
        if(params!=null)holder.setLayoutParams(params);
    }
}
