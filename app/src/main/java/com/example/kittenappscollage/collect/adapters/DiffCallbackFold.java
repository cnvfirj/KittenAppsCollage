package com.example.kittenappscollage.collect.adapters;

import androidx.recyclerview.widget.DiffUtil;

public class DiffCallbackFold extends DiffUtil.Callback {

    private ListenLoadFoldAdapter.Item[]oldList;

    private ListenLoadFoldAdapter.Item[]newList;

    public DiffCallbackFold(ListenLoadFoldAdapter.Item[] oldList, ListenLoadFoldAdapter.Item[] newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {

        if(oldList!=null)return oldList.length;
        else return 0;
    }

    @Override
    public int getNewListSize() {
        if(newList!=null)return newList.length;
        else return 0;
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList[oldItemPosition].sizeItemsFold==newList[newItemPosition].sizeItemsFold;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList[oldItemPosition].equals(newList[newItemPosition]);
    }
}
