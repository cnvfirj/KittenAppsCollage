package com.example.kittenappscollage.collect.adapters;

import android.content.Context;
import android.net.Uri;
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

    private String fold;

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

    public ListenLoadImgAdapter setAll(HashMap<String, ArrayList<String>> all){
        LYTE("ListenLoadImgAdapter set All");
        this.all = all;
        final String[]names = new String[all.size()];
        all.keySet().toArray(names);
        final long data[] = new long[names.length];
        for (int i=0;i<names.length;i++){
            data[i] = new File(names[i]).lastModified();
        }
        sort(data,names);
//        resetCheckeds();
        notifyDataSetChanged();
        return this;
    }

    private void sort(long[]data,String[]names){
        for(int i = data.length-1 ; i > 0 ; i--){
            for(int j = 0 ; j < i ; j++){

                if( data[j] < data[j+1] ){
                    long tmp = data[j];
                    String name = names[j];

                    data[j] = data[j+1];
                    names[j] = names[j+1];

                    data[j+1] = tmp;
                    names[j+1] = name;
                }
            }
        }
        folds = names;
    }

    public void setIndexKey(int index){
        indexKey = index;
        notifyDataSetChanged();
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
        if(listen!=null)listen.createHolder(0, holder, getPositionInEnd(pos));
    }

    protected void createContentHolder(View[]content, int pos){
        if(listen!=null)listen.createContentHolder(0, content, getPositionInEnd(pos));
    }

    protected void click(ImageView img, ImageView check, int pos){
        if(listen!=null)listen.click(0, img,check, getPositionInEnd(pos));
    }

    protected void longClick(ImageView img, ImageView check, int pos) {
        if(listen!=null)listen.longClick(0, img, check, getPositionInEnd(pos));
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
        Glide.with(getContext())
                .load(getAll().get(getFolds()[indexKey]).get(getPositionInEnd(position)))
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
