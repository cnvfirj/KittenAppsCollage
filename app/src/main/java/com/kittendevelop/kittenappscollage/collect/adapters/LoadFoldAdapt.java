package com.kittendevelop.kittenappscollage.collect.adapters;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;

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
    public void onBindViewHolder(@NonNull FoldHolder holder, int position) {
        if(position<getArrChecks().length) {
            if (getArrChecks() != null && modeSelected)
                holder.getCheck().setVisibility(getArrChecks()[position] ? View.VISIBLE : View.INVISIBLE);
            else holder.getCheck().setVisibility(View.INVISIBLE);
        }
         super.onBindViewHolder(holder, position);
    }

    @Override
//    public ListenLoadFoldAdapter setAll(HashMap<String, ArrayList<String>> all, HashMap<String,String>namesFolds, HashMap<String,Long>mutableFolds) {
    public ListenLoadFoldAdapter setAll(HashMap<String, ArrayList<String>> all, HashMap<String,String>namesFolds) {

        resetChecks(all.size());
//        return super.setAll(all,namesFolds,mutableFolds);
        return super.setAll(all,namesFolds);

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

    private void resetChecks(int size){
        modeSelected = false;
        checkSelect = new boolean[size];
    }
    private void resetCheckeds(){
        checkSelect = new boolean[getItems().length];
    }

    @Override
    protected void click(ImageView img, ImageView check, int pos) {
        super.click(img, check, pos);
        if(isModeSelected()){
            if(!getArrChecks()[pos]){
                resetCheckeds();
                check.setVisibility(View.VISIBLE);
                getArrChecks()[pos]=true;
                notifyDataSetChanged();
            }
        }
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
//            LYTE("ListenLoadFoldAdapter mode selected - "+mode);
            notifyDataSetChanged();
        }
    }

    protected boolean[]getArrChecks(){
        return checkSelect;
    }
}
