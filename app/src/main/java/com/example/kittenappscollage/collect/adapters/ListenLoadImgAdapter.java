package com.example.kittenappscollage.collect.adapters;

import android.content.ContentUris;
import android.content.Context;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.kittenappscollage.R;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import static com.example.kittenappscollage.helpers.Massages.LYTE;

public class ListenLoadImgAdapter extends RecyclerView.Adapter<ListenLoadImgAdapter.ImgHolder> implements Serializable{

    private HashMap<String, ArrayList<String>> all;

    private String[] folds;

    private Context context;

    private int indexKey;

    private ListenAdapter listen;

    public ListenLoadImgAdapter(Context context) {
        indexKey = -1;
        this.context = context;
    }

    public ListenLoadImgAdapter setListen(ListenAdapter listen){
        this.listen = listen;
        return this;
    }

    public ListenLoadImgAdapter setAll(HashMap<String, ArrayList<String>> all,String[] folds){
        LYTE("ListenLoadImgAdapter set All");
        this.all = all;
       if(folds==null) {
           this.folds = new String[all.size()];
           all.keySet().toArray(this.folds);
       }else this.folds = folds;

        notifyDataSetChanged();
        return this;
    }

    public void setIndexKey(int index){
        indexKey = index;
        notifyDataSetChanged();
    }

    public ArrayList<String>getOperationList(){
        return getAll().get(getFolds()[indexKey]);
    }

    protected void resetCheckeds(){

    }

    protected Context getContext(){
        return context;
    }

    protected HashMap<String, ArrayList<String>> getAll(){
        return all;
    }

    protected String[] getFolds(){
        return folds;
    }

    protected int getIndexAdapter(){
        return indexKey;
    }

    protected void createHolder(View holder, int pos){
        if(listen!=null)listen.createHolder(indexKey, holder, getPositionInEnd(pos));
    }

    protected void createContentHolder(View[]content, int pos){
        if(listen!=null)listen.createContentHolder(indexKey, content, getPositionInEnd(pos));
    }

    protected void click(ImageView img, ImageView check, int pos){
        if(listen!=null)listen.click(indexKey, img,check, getPositionInEnd(pos));
    }

    protected void longClick(ImageView img, ImageView check, int pos) {
        if(listen!=null)listen.longClick(indexKey, img, check, getPositionInEnd(pos));
    }

    /*получаем вариант из конца списка*/
    protected int getPositionInEnd(int position){
        return getItemCount()-(position+1);
    }

    @NonNull
    @Override
    public ImgHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ImgHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.collect_img_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ImgHolder holder, int position) {
//        String s = getAll().get(getFolds()[indexKey]).get(getPositionInEnd(position));
//        LYTE("add img "+s);
        final Uri uri = Uri.parse(getAll().get(getFolds()[indexKey]).get(getPositionInEnd(position)));
        Glide.with(getContext())
                .load(uri)
                .placeholder(R.drawable.ic_image)
                .error(R.drawable.ic_error)
                .into(holder.getImage());
    }

    @Override
    public int getItemCount() {
        return getAll().get(getFolds()[indexKey]).size();
    }

    protected class ImgHolder extends CollectHolder{

        public ImgHolder(@NonNull View itemView) {
            super(itemView);
            createHolder(itemView, getAdapterPosition());
            createContentHolder(new View[]{getImage(),getCheck()},getAdapterPosition());
        }

        @Override
        public void onClick(View view) {
            super.onClick(view);
            click(getImage(),getCheck(),getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View view) {
            longClick(getImage(),getCheck(),getAdapterPosition());
            return true;
        }
    }
}
