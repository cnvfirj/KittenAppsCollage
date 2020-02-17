package com.example.kittenappscollage.collect.adapters;

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
import com.example.kittenappscollage.R;
import com.example.kittenappscollage.collect.fragment.FragmentScanAllImages;

import java.io.File;
import java.io.Serializable;
import java.net.URI;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Set;

import static com.example.kittenappscollage.helpers.Massages.LYTE;

public class ListenLoadFoldAdapter extends RecyclerView.Adapter<ListenLoadFoldAdapter.FoldHolder> implements Serializable{

    public static final int ROOT_ADAPTER = -5;

    private HashMap<String, ArrayList<String>> all;

    private HashMap<String,String>namesFolds;

    private HashMap<String,Long>mutableFolds;

    private String[] folds;

    private Context context;

    private ListenAdapter listen;

    public ListenLoadFoldAdapter(Context context) {
        this.context = context;
    }

    public ListenLoadFoldAdapter setListen(ListenAdapter listen){
        this.listen = listen;
        return this;
    }

    public ListenLoadFoldAdapter setAll(HashMap<String, ArrayList<String>> all, HashMap<String,String>namesFolds,HashMap<String,Long>mutable) {

        this.all = all;
        this.namesFolds = namesFolds;
        this.mutableFolds = mutable;
        folds = mutable.keySet().toArray(new String[mutable.keySet().size()]);
//        Long[]val = mutable.values().toArray(new Long[mutable.values().size()]);
//        sort(val,folds);

        Arrays.sort(folds, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return mutableFolds.get(o2).compareTo(mutableFolds.get(o1));
            }
        });

        notifyDataSetChanged();
        return this;
    }

    public String[]sortKeys(){
        return folds;
    }

    private void sort(Long[]data,String[]names){
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

    protected HashMap<String, ArrayList<String>> getAll(){
        return all;
    }

    protected HashMap<String, String>getNamesFolds(){
        return namesFolds;
    }

    protected String[] getFolds(){
        return folds;
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
        if(getAll().get(sortKeys()[position])!=null) {
            final int index = getAll().get(sortKeys()[position]).size();
            final Uri uri = Uri.parse(getAll().get(sortKeys()[position]).get(index - 1));
            if (index > 0) {
                Glide.with(getContext())
                        .load(uri)
                        .into(holder.getImage());
            }

            holder.getName().setText(getNamesFolds().get(getFolds()[position]));
            holder.getCol().setText(Integer.toString(getAll().get(getFolds()[position]).size()));
        }
        
    }

    @Override
    public int getItemCount() {
        if(getAll()==null){
            return 0;
        } else {
            return getAll().size();
        }
    }

    public String[]getKeys(){
        return folds;
    }

    private boolean check(int pos){
        return sortKeys()[pos]!=null&&getAll().get(sortKeys()[pos])!=null;
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

}
