package com.example.kittenappscollage.collect.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.kittenappscollage.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;


public class ListenLoadImgAdapter extends RecyclerView.Adapter<ListenLoadImgAdapter.ImgHolder> implements Serializable{

    private HashMap<String, ArrayList<String>> all;

    private ListenLoadFoldAdapter.Item[]items;

    private String[]images;

    private Context context;

    private boolean active;

    private int indexKey;

    private String retentKey;

    private ListenAdapter listen;

    public ListenLoadImgAdapter(Context context) {
        indexKey = -1;
        this.context = context;
    }

    public ListenLoadImgAdapter setListen(ListenAdapter listen){
        this.listen = listen;
        return this;
    }

    public ListenLoadImgAdapter activate(boolean a){
        active = a;
        return this;
    }

    public ListenLoadImgAdapter setAll(HashMap<String, ArrayList<String>> all, ListenLoadFoldAdapter.Item[]items,String key){
        this.all = all;
        this.items = items;
        String[] imgs = null;
        if(active){
            if(key==null){
                if(all.get(retentKey)==null){
                    listen.exit(indexKey);
                }else {
                    imgs = new String[all.get(getItems()[indexKey].key).size()];
                    all.get(getItems()[indexKey].key).toArray(imgs);
                }
            } else {
                if (all.get(key) == null) {
                    listen.exit(indexKey);
                } else {
                    imgs = new String[all.get(key).size()];
                    all.get(key).toArray(imgs);
                }
            }
            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffCallbackImg(this.images,imgs));
            diffResult.dispatchUpdatesTo(this);
        }

        this.images = imgs;
        return this;
    }

    public void setIndexKey(int index){
        if(index>=0) {
                if (all.get(getItems()[index].key) != null) {
                    indexKey = index;
                    retentKey = getItems()[index].key;
                    images = new String[all.get(getItems()[indexKey].key).size()];
                    all.get(getItems()[indexKey].key).toArray(images);

            }
        }
    }

    public ArrayList<String>getOperationList(){
        return all.get(retentKey);
    }


    protected void resetCheckeds(){

    }

    protected Context getContext(){
        return context;
    }


    protected String[]getImages(){
        return images;
    }

    protected ListenLoadFoldAdapter.Item[]getItems(){
        return items;
    }

    protected int getIndexAdapter(){
        return indexKey;
    }

    protected void createHolder(View holder, int pos){
//        if(listen!=null)listen.createHolder(indexKey, holder, getPositionInEnd(pos));
        if(listen!=null)listen.createHolder(indexKey, holder, pos);

    }

    protected void createContentHolder(View[]content, int pos){
//        if(listen!=null)listen.createContentHolder(indexKey, content, getPositionInEnd(pos));
        if(listen!=null)listen.createContentHolder(indexKey, content, pos);

    }

    protected void click(ImageView img, ImageView check, int pos){
//        if(listen!=null)listen.click(indexKey, img,check, getPositionInEnd(pos));
       if(listen!=null)listen.click(indexKey, img,check, pos);

    }

    protected void longClick(ImageView img, ImageView check, int pos) {
//        if(listen!=null)listen.longClick(indexKey, img, check, getPositionInEnd(pos));
        if(listen!=null)listen.longClick(indexKey, img, check, pos);

    }

    /*получаем вариант из конца списка*/
    protected int getPositionInEnd(int position){
        return getItemCount()-(position+1);
    }

    protected boolean isActive(){
        return active;
    }

    @NonNull
    @Override
    public ImgHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ImgHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.collect_img_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ImgHolder holder, int position) {
        final Uri uri = Uri.parse(images[position]);

        Glide.with(getContext())
                .load(uri)
                .placeholder(R.drawable.ic_image)
                .error(R.drawable.ic_error)
                .into(holder.getImage());
    }

    @Override
    public int getItemCount() {
//        return all.get(getItems()[indexKey].key).size();
        if(images==null)return 0;
        else return images.length;
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
