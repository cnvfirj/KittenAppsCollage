package com.example.kittenappscollage.collect.adapters.up;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.HashMap;

public class LoadFoldAdapt extends ListenLoadFoldAdapter {

    private FrameLayout.LayoutParams params;

    public LoadFoldAdapt(Context context) {
        super(context);
    }


    @Override
    public ListenLoadFoldAdapter setAll(HashMap<String, ArrayList<String>> all) {
        return super.setAll(all);
    }

    public void setParams(int width){
        int w = width/2;
        params = new FrameLayout.LayoutParams(w,w);
    }

    @Override
    public void createHolder(View holder, int pos) {
        super.createHolder(holder, pos);
        if(params!=null)holder.setLayoutParams(params);

    }
}
