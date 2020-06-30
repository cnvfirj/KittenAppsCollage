package com.kittendevelop.kittenappscollage.collect.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.kittendevelop.kittenappscollage.R;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;

public class ListenLoadFoldAdapter extends RecyclerView.Adapter<ListenLoadFoldAdapter.FoldHolder> implements Serializable{

    public static final int ROOT_ADAPTER = -5;

    private boolean active;

    private Item[] items;

    private Context context;

    private ListenAdapter listen;

//    private HashMap<String,String>perms;

    public ListenLoadFoldAdapter(Context context) {
        this.context = context;
    }

    public ListenLoadFoldAdapter setListen(ListenAdapter listen){
        this.listen = listen;
        return this;
    }

    public ListenLoadFoldAdapter activate(boolean a){
        active = a;
        return this;
    }

    public ListenLoadFoldAdapter setAll(HashMap<String, ArrayList<String>> all, HashMap<String,String>namesFolds) {

        Item[] items = new Item[all.size()];
        String[] folds = all.keySet().toArray(new String[all.keySet().size()]);
        for(int i=0;i<items.length;i++){
            items[i] = new Item();
                if(all.get(folds[i])==null||all.get(folds[i]).size()==0)continue;
                items[i].key = folds[i];
                items[i].sizeItemsFold = all.get(folds[i]).size();
                items[i].uriIconFold = all.get(folds[i]).get(items[i].sizeItemsFold - 1);
                items[i].nameFold = namesFolds.get(folds[i]);
        }

//        /*сортируем ключи по их значению в мапе*/
        Arrays.sort(items, new Comparator<Item>() {
            @Override
            public int compare(Item o1, Item o2) {
                final Long m1 = o1.mutableLastImg;
                final Long m2 = o2.mutableLastImg;

                return m1.compareTo(m2);
            }
        });


        if(active){
            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffCallbackFold(this.items,items));
            diffResult.dispatchUpdatesTo(this);
        }
        this.items = items;
        return this;
    }

    protected void createHolder(View holder, int pos){
        if(listen!=null)listen.createHolder(ROOT_ADAPTER, holder, pos);
    }

    protected void createContentHolder(View[]content, int pos){
        if(listen!=null)listen.createContentHolder(ROOT_ADAPTER, content, pos);
    }

    protected void click(ImageView img, ImageView check, int pos){
        if(listen!=null)listen.click(ROOT_ADAPTER, img,check, pos);
    }

    protected void longClick(ImageView img, ImageView check, int pos) {
        if(listen!=null)listen.longClick(ROOT_ADAPTER, img, check, pos);
    }

    protected Context getContext(){
        return context;
    }

    protected boolean isActive(){
        return active;
    }

    @NonNull
    @Override
    public FoldHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FoldHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.collect_fold_item,parent,false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull FoldHolder holder, int position) {
        if(position<getItems().length) {
            final Item item = getItems()[position];
            final int index = item.sizeItemsFold;
            holder.getName().setText(item.nameFold);
            holder.getCol().setText(Integer.toString(index));

                if(items[position].uriIconFold!=null) {
                    final String i = items[position].uriIconFold;
                    if(i!=null){
                    final Uri uri = Uri.parse(i);
                    if (items[position].sizeItemsFold > 0) {
                        Glide.with(getContext())
                                .load(uri)
                                .placeholder(R.drawable.ic_update)
                                .error(R.drawable.ic_error)
                                .into(holder.getImage());
                    }else {
                        holder.getImage().setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_update,null));
                    }
                    }
                }
        }
        
    }

    @Override
    public int getItemCount() {
        if(getItems()==null){
            return 0;
        } else {
            return getItems().length;
        }
    }

    public Item[]getItems(){
        return items;
    }

    protected class FoldHolder extends CollectHolder{


        private TextView name;

        private TextView col;


        public FoldHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.collect_item_name_dir);
            col = itemView.findViewById(R.id.collect_item_col_img_in_dir);
            createHolder(itemView, getAdapterPosition());
            createContentHolder(new View[]{getImage(),getCheck(),getName(),getCol()},getAdapterPosition());
        }


        @Override
        public void onClick(View view) {
            click(getImage(),getCheck(),getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View view) {
            longClick(getImage(),getCheck(),getAdapterPosition());
            return true;
        }

        public TextView getName(){
            return name;
        }

        public TextView getCol(){
            return col;
        }

    }

    public class Item{

        protected String key;

        protected String uriIconFold;

        protected String nameFold;

        protected String permission;

        protected int sizeItemsFold;

        protected long mutableLastImg;

        public String getKey() {
            return key;
        }

        public String getUriIconFold() {
            return uriIconFold;
        }

        public String getNameFold() {
            return nameFold;
        }

        public String getPermission() {
            return permission;
        }

        public int getSizeItemsFold() {
            return sizeItemsFold;
        }

        public long getMutableLastImg() {
            return mutableLastImg;
        }
    }

}
