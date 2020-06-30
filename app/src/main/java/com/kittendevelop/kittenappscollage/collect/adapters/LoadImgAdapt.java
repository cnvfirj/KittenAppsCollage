package com.kittendevelop.kittenappscollage.collect.adapters;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;

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
        if(getArrChecks()!=null&&modeSelected){
            holder.getCheck().setVisibility(getArrChecks()[position]?View.VISIBLE:View.INVISIBLE);
        }else holder.getCheck().setVisibility(View.INVISIBLE);
        super.onBindViewHolder(holder, position);
    }

    @Override
    public void createHolder(View holder, int pos) {
        super.createHolder(holder, pos);
        if(params!=null)holder.setLayoutParams(params);
    }

    @Override
    public void setIndexKey(int index) {
        super.setIndexKey(index);

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

    @Override
    protected void resetCheckeds(){
        super.resetCheckeds();
//        if(getIndexAdapter()>-1)checkSelect = new boolean[getAll().get(getItems()[getIndexAdapter()].key).size()];
        if(getIndexAdapter()>-1)checkSelect = new boolean[getImages().length];

    }

    @Override
    protected void click(ImageView img, ImageView check, int pos) {
        super.click(img, check, pos);
        if(modeSelected){
            checkSelect[pos] = !checkSelect[pos];
            check.setVisibility(getArrChecks()[pos]?View.VISIBLE:View.INVISIBLE);
        }
    }

    @Override
    protected void longClick(ImageView img, ImageView check, int pos) {
        if(!modeSelected){
            resetCheckeds();
            modeSelected = true;
            check.setVisibility(View.VISIBLE);
            checkSelect[pos] = true;
        }
        super.longClick(img, check, pos);
    }

    public boolean[]getArrChecks(){
        return checkSelect;
    }



}
