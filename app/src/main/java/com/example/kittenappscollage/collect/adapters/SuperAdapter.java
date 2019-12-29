package com.example.kittenappscollage.collect.adapters;

import android.annotation.SuppressLint;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.kittenappscollage.R;

import java.io.Serializable;

import static com.example.kittenappscollage.helpers.Massages.LYTE;

public abstract class SuperAdapter extends RecyclerView.Adapter<SuperAdapter.MyViewHolder> implements Serializable {



    protected void click(ImageView image, ImageView check, int position){

    }

    protected void longTouch(ImageView image, ImageView check, int position){

    }

    protected void createdItems(ImageView image, ImageView check, int position){

    }

    protected void createHolder(View holder){

    }

    protected   class MyViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener,View.OnLongClickListener {

        private ImageView image;
        private ImageView check;

        @SuppressLint("ClickableViewAccessibility")
        public MyViewHolder(View itemView) {
            super(itemView);
            createHolder(itemView);
            image = itemView.findViewById(R.id.collect_item_gallery_image);
            check = itemView.findViewById(R.id.collect_item_select);
            createdItems(image, check,getAdapterPosition());
            image.setOnClickListener(this);
            image.setOnLongClickListener(this);
            image.setScaleType(ImageView.ScaleType.CENTER_CROP);

        }

        @Override
        public void onClick(View view) {
            click(image,check,getAdapterPosition());
        }


        @Override
        public boolean onLongClick(View view) {
            longTouch(image,check,getAdapterPosition());
            return true;
        }

        public ImageView getImage(){
            return image;
        }
    }




}
