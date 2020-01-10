package com.example.kittenappscollage.draw.textProp;

import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kittenappscollage.R;
import com.example.kittenappscollage.collect.adapters.ListenLoadFoldAdapter;
import com.example.kittenappscollage.view.PresentPaint;

import java.io.Serializable;

public class AdapterShrift  extends RecyclerView.Adapter<AdapterShrift.ShriftHolder> implements Serializable {

    @NonNull
    @Override
    public ShriftHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AdapterShrift.ShriftHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_present_draw,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ShriftHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ShriftHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{

        private PresentPaint present;


        public ShriftHolder(@NonNull View itemView) {
            super(itemView);
            present = itemView.findViewById(R.id.item_present_draw);
            present.setType(PresentPaint.TEXT);
        }

        @Override
        public void onClick(View view) {

        }

        public PresentPaint getPresent(){
            return present;
        }
    }


}
