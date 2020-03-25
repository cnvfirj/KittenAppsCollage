package com.example.kittenappscollage.collect.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kittenappscollage.R;

public class CollectHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener,View.OnLongClickListener {

    private ImageView image;
    private ImageView check;

    public CollectHolder(@NonNull View itemView) {
        super(itemView);
        image = itemView.findViewById(R.id.collect_item_gallery_image);
        image.setOnClickListener(this);
        image.setOnLongClickListener(this);
        image.setScaleType(ImageView.ScaleType.CENTER_CROP);
        check = itemView.findViewById(R.id.collect_item_select);

    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public boolean onLongClick(View view) {
        return true;
    }

    public ImageView getImage(){
        return image;
    }

    public ImageView getCheck(){
        return check;
    }

}
