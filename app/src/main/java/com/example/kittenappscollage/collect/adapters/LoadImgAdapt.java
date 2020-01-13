package com.example.kittenappscollage.collect.adapters;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;

public class LoadImgAdapt extends ListenLoadImgAdapter {

    private FrameLayout.LayoutParams params;

    private boolean modeSelected;

    private boolean[]checkSelect;

    public LoadImgAdapt(Context context) {
        super(context);
        modeSelected = false;
    }

    public void setParams(int width){
        int w = width/3;
        params = new FrameLayout.LayoutParams(w,w);
    }

    @Override
    public void onBindViewHolder(@NonNull ImgHolder holder, int position) {
        holder.getCheck().setVisibility(getArrChecks()[position]?View.VISIBLE:View.INVISIBLE);
        super.onBindViewHolder(holder, position);
    }

    @Override
    public ListenLoadImgAdapter setAll(HashMap<String, ArrayList<String>> all) {
        resetChecks(all.size());
        return super.setAll(all);
    }

    @Override
    public void createHolder(View holder, int pos) {
        super.createHolder(holder, pos);
        if(params!=null)holder.setLayoutParams(params);
    }

    @Override
    public void setIndexKey(int index) {
        super.setIndexKey(index);
        resetCheckeds();
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

    private void resetChecks(int size){
        checkSelect = new boolean[size];
    }

    private void resetCheckeds(){
        checkSelect = new boolean[getAll().get(getFolds()[getIndexAdapter()])
                                          .size()];
    }

    @Override
    protected void click(ImageView img, ImageView check, int pos) {
        super.click(img, check, pos);
        if(modeSelected){
            checkSelect[pos] = !checkSelect[pos];
            check.setVisibility(getArrChecks()[pos]?View.VISIBLE:View.INVISIBLE);
        }
        int position = getPositionInEnd(pos);
    }

    @Override
    protected void longClick(ImageView img, ImageView check, int pos) {
        if(!modeSelected){
            modeSelected = true;
            check.setVisibility(View.VISIBLE);
            checkSelect[pos] = true;
        }
        super.longClick(img, check, pos);
    }

    protected boolean[]getArrChecks(){
        return checkSelect;
    }



}
