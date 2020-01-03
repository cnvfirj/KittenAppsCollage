package com.example.kittenappscollage.collect.adapters.up;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kittenappscollage.R;
import com.example.kittenappscollage.collect.adapters.SuperAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class StartLoadImgAdapter extends RecyclerView.Adapter<StartLoadImgAdapter.ImgHolder> implements Serializable {

    private HashMap<String, ArrayList<String>> all;

    private String[] folds;

    private Context context;

    private String fold;

    public StartLoadImgAdapter(Context context) {
        this.context = context;
    }

    public StartLoadImgAdapter(Context context, HashMap<String, ArrayList<String>> all) {
        this.all = all;
        this.context = context;
        folds = new String[all.size()];
        all.keySet().toArray(folds);
    }

    public StartLoadImgAdapter setAll(HashMap<String, ArrayList<String>> all){
        this.all = all;
        folds = new String[all.size()];
        all.keySet().toArray(folds);
        notifyDataSetChanged();
        return this;
    }

    public StartLoadImgAdapter stepInFold(String fold){
        this.fold = fold;
        return this;
    }

    protected void click(ImageView image, ImageView check, int pos){

    }

    protected void longTouch(ImageView image, ImageView check, int pos){

    }

    protected void createdContentHolder(ImageView image, ImageView check, int pos){

    }

    protected void createHolder(View holder){

    }

    @NonNull
    @Override
    public ImgHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ImgHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.collect_img_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ImgHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }


    protected class ImgHolder extends CollectHolder{

        public ImgHolder(@NonNull View itemView) {
            super(itemView);
            createHolder(itemView);
            createdContentHolder(getImage(),getCheck(),getAdapterPosition());
        }

        @Override
        public void onClick(View view) {
            super.onClick(view);
            click(getImage(),getCheck(),getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View view) {
            longTouch(getImage(),getCheck(),getAdapterPosition());
            return true;
        }
    }
}
