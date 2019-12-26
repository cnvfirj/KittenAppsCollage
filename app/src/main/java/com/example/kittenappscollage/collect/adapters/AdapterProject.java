package com.example.kittenappscollage.collect.adapters;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.example.kittenappscollage.collect.RequestImagesInStorage;
import com.example.kittenappscollage.helpers.RequestFolder;

/**
 * Created by Admin on 22.03.2018.
 */

public class AdapterProject extends SuperAdapter {

    public AdapterProject(Context aContext, int source) {
        super(aContext, source);
    }

    @Override
    public void requestList() {
//       RequestImagesInStorage.get(aContext,sListImageListener).loadImages(RequestFolder.getFolderImages());
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
//        holder.getImage().setImageDrawable(aContext.getDrawable(R.drawable.all_default_img_item_collect));
//        final String key = aListImg.get(position);

//        Glide.with(aContext)
//                .load(key)
////                .apply(new RequestOptions().placeholder(R.drawable.all_default_img_item_collect))
//                .into(holder.getImage());


    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }
}
