package com.example.kittenappscollage.collect.adapters;

import androidx.recyclerview.widget.DiffUtil;

public class DiffCallbackImg extends DiffUtil.Callback  {

    private String[]oldImgs;

    private String[]newImgs;

    public DiffCallbackImg(String[] oldImgs, String[] newImgs) {
        this.oldImgs = oldImgs;
        this.newImgs = newImgs;
    }

    @Override
    public int getOldListSize() {
        if(oldImgs!=null)return oldImgs.length;
        return 0;
    }

    @Override
    public int getNewListSize() {
        if(newImgs!=null)return newImgs.length;
        return 0;
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldImgs[getPosOld(oldItemPosition)].equals(newImgs[getPosNew(newItemPosition)]);
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return true;
    }

    protected int getPosOld(int position){
        return getOldListSize()-(position+1);
    }

    protected int getPosNew(int position){
        return getNewListSize()-(position+1);
    }


}
