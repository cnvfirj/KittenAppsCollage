package com.example.kittenappscollage.collect.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kittenappscollage.R;
import com.example.kittenappscollage.collect.TouchViewListener;
import com.example.kittenappscollage.helpers.RequestFolder;
import com.example.kittenappscollage.helpers.rx.ThreadTransformers;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;

public abstract class SuperAdapter extends RecyclerView.Adapter<SuperAdapter.MyViewHolder> implements Serializable {


    protected void click(ImageView image, ImageView check, int position){

    }

    protected void clickLong(ImageView image, ImageView check,int position){

    }

    protected void touch(View image, MotionEvent event){

    }

    protected void longTouch(View view){

    }

    protected void createdItems(ImageView image, ImageView check){

    }

    protected void createHolder(View holder){

    }

    protected   class MyViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener,View.OnTouchListener, View.OnLongClickListener {

        private ImageView image;
        private ImageView check;
        private boolean longTouch = false;

        @SuppressLint("ClickableViewAccessibility")
        public MyViewHolder(View itemView) {
            super(itemView);
            createHolder(itemView);
            image = itemView.findViewById(R.id.collect_item_gallery_image);
            check = itemView.findViewById(R.id.collect_item_select);
            createdItems(image, check);
            image.setOnClickListener(this);
            image.setOnTouchListener(this);
            image.setOnLongClickListener(this);
            image.setScaleType(ImageView.ScaleType.CENTER_CROP);

        }

        @Override
        public void onClick(View view) {
            click(image,check,getAdapterPosition());
        }

        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if(longTouch){
                clickLong(image,check, getAdapterPosition());
                longTouch = false;
            }
            touch(v,event);
            return true;
        }

        @Override
        public boolean onLongClick(View view) {
            longTouch = true;
            longTouch(view);
            return true;
        }
    }




}
