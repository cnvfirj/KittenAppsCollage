package com.kittendevelop.kittenappscollage.draw.textProp;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kittendevelop.kittenappscollage.R;
import com.kittendevelop.kittenappscollage.view.CustomFon;
import com.kittendevelop.kittenappscollage.view.PresentPaint;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class AdapterShrift  extends RecyclerView.Adapter<AdapterShrift.ShriftHolder> implements Serializable {

    private ArrayList<String>fonts;

    private Context context;

    private int sizeBase;

    public AdapterShrift(Context context,@NonNull String[]base){
        fonts = new ArrayList<>();

        if(base!=null){
            fonts.addAll(Arrays.asList(base));
            sizeBase = base.length;
        }
        this.context = context;
    }

    public AdapterShrift(Context context,@NonNull ArrayList<String>base) {
        this.fonts = base;
        this.context = context;
    }

    public void setFonts(@NonNull ArrayList<String>fonts){
        this.fonts.addAll(fonts);
        notifyDataSetChanged();
    }

    protected void clickItem(PresentPaint view, int pos){
    }

    protected void createHolder(View holder, PresentPaint view, int pos){

    }

    public ArrayList<String>getFonts(){
        return fonts;
    }

    @NonNull
    @Override
    public ShriftHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AdapterShrift.ShriftHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_present_draw,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ShriftHolder holder, int position) {
        if(fonts.get(position)!=null) {
            if (position < sizeBase) {//количество шрифтов в активе
                holder.getPresent().setShrift(Typeface.createFromAsset(
                        context.getAssets(), "fonts/"+fonts.get(position)
                ));
            } else {
                holder.getPresent().setShrift(Typeface.createFromFile(fonts.get(position)
                ));
            }
        }
    }

    @Override
    public int getItemCount() {
        return fonts.size();
    }

    public int getSizeBase() {
        return sizeBase;
    }

    public class ShriftHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{

        private PresentPaint present;

        private CustomFon fon;


        public ShriftHolder(@NonNull View itemView) {
            super(itemView);
            present = itemView.findViewById(R.id.item_present_draw);
            present.setType(PresentPaint.TEXT);
            present.setOnClickListener(this);
            fon = itemView.findViewById(R.id.iten_present_draw_fon);
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) fon.getLayoutParams();
            params.topMargin = 2;
            params.bottomMargin = 2;
            fon.setLayoutParams(params);
            params = (FrameLayout.LayoutParams) present.getLayoutParams();
            params.topMargin = 2;
            params.bottomMargin = 2;
            present.setLayoutParams(params);
            createHolder(itemView,present,getAdapterPosition());
        }

        @Override
        public void onClick(View view) {
           clickItem((PresentPaint)view,getAdapterPosition());
        }

        public PresentPaint getPresent(){
            return present;
        }
    }


}
