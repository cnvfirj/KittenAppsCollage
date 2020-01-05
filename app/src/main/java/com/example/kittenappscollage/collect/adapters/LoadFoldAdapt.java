package com.example.kittenappscollage.collect.adapters;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.HashMap;

public class LoadFoldAdapt extends ListenLoadFoldAdapter {

    private FrameLayout.LayoutParams params;

    private boolean modeSelected;

    private boolean[]checkSelect;

    public LoadFoldAdapt(Context context) {
        super(context);
        modeSelected = false;
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

    private void resetCheckeds(){
        checkSelect = new boolean[getAll().size()];
    }

    @Override
    protected void longClick(ImageView img, ImageView check, int pos) {
        if(!isModeSelected()){
            modeSelected = true;
            check.setVisibility(View.VISIBLE);
            resetCheckeds();
            getArrChecks()[pos]=true;
        }
        super.longClick(img, check, pos);

    }

    public boolean isModeSelected() {
        return modeSelected;
    }

    public void setModeSelected(boolean mode){
        if(mode!=modeSelected) {
            modeSelected = mode;
            resetCheckeds();
            notifyDataSetChanged();
        }
    }

    protected boolean[]getArrChecks(){
        return checkSelect;
    }
}
