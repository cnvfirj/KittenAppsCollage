package com.example.kittenappscollage.collect.adapters;

import android.content.Context;
import android.widget.ImageView;

import androidx.annotation.NonNull;

public class LoaderImgAdapter extends FileAdapter {

    public LoaderImgAdapter(Context c, int source) {
        super(c, source);
    }

    @Override
    protected void click(ImageView image, ImageView check, int position) {
        super.click(image, check, position);
    }

    @Override
    protected void clickLong(ImageView image, ImageView check, int position) {
        super.clickLong(image, check, position);
    }

    @Override
    protected void createdItems(ImageView image, ImageView check) {
        super.createdItems(image, check);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        /*сюда ставим изображение по дефолту*/
          super.onBindViewHolder(holder,position);
    }
}
