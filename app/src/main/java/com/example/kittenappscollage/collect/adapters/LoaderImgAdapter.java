package com.example.kittenappscollage.collect.adapters;

import android.content.Context;

import androidx.annotation.NonNull;

public class LoaderImgAdapter extends FileAdapter {

    public LoaderImgAdapter(Context c, int source) {
        super(c, source);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
          super.onBindViewHolder(holder,position);
    }
}
