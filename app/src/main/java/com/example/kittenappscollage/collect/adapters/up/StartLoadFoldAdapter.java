package com.example.kittenappscollage.collect.adapters.up;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.kittenappscollage.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class StartLoadFoldAdapter extends RecyclerView.Adapter<StartLoadFoldAdapter.FoldHolder> implements Serializable {

    private HashMap<String, ArrayList<String>> all;

    private String[] folds;

    private Context context;

    public StartLoadFoldAdapter(Context context) {
        this.context = context;
    }

    public StartLoadFoldAdapter(Context context, HashMap<String, ArrayList<String>> all) {
        this.all = all;
        this.context = context;
        folds = new String[all.size()];
        all.keySet().toArray(folds);
    }

    public StartLoadFoldAdapter setAll(HashMap<String, ArrayList<String>> all) {
        this.all = all;
        folds = new String[all.size()];
        all.keySet().toArray(folds);
        notifyDataSetChanged();
        return this;
    }



    protected void click(ImageView img, ImageView check, int pos){

    }

    protected void longClick(ImageView img, ImageView check, int pos){

    }

    protected void createHolder(View holder){

    }

    protected void createContentHolder(ImageView img, ImageView check,TextView name, TextView col, int pos){

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

    @NonNull
    @Override
    public FoldHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FoldHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.collect_fold_item,parent,false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull FoldHolder holder, int position) {
        Glide.with(getContext())
                .load(getAll().get(getFolds()[position]).get(0))
                .into(holder.getImage());
        String name = getFolds()[position];
        holder.getName().setText(name);
        holder.getCol().setText(Integer.toString(getAll().get(getFolds()[position]).size()));
    }

    @Override
    public int getItemCount() {
        return getAll().size();
    }

    protected class FoldHolder extends CollectHolder{


        private TextView name;

        private TextView col;

        public FoldHolder(@NonNull View itemView) {
            super(itemView);
            createHolder(itemView);
            name = itemView.findViewById(R.id.collect_item_name_dir);
            col = itemView.findViewById(R.id.collect_item_col_img_in_dir);
            createContentHolder(getImage(),getCheck(),getName(),getCol(),getAdapterPosition());
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
