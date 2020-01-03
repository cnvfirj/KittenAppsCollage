package com.example.kittenappscollage.collect.adapters;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import static com.example.kittenappscollage.helpers.Massages.LYTE;

public class PresentAdapter extends FileAdapter {

    private FrameLayout.LayoutParams params;

    private boolean modeSelected;

    private HashMap<Integer, File>map;

    private ModeSelected listenModeSelected;


    public PresentAdapter(Context c, int source) {
        super(c, source);
    }

    public PresentAdapter resetChecks(){
        resetArrChecks();
        modeSelected = false;
        notifyDataSetChanged();
        return this;
    }

    public PresentAdapter setMap(HashMap<Integer, File>map){
        this.map = map;
        return this;
    }

    public PresentAdapter setListener(ModeSelected listen){
        listenModeSelected = listen;
        return this;
    }

    public PresentAdapter setModeSelected(boolean mode){
        modeSelected = mode;
        return this;
    }

    public void deleteItems(Collection<Integer>c){

      File[]files = new File[getArrFiles().size()];
        getArrFiles().toArray(files);
        getArrFiles().clear();
        for (int i=0;i<files.length;i++){
            if(!c.contains(i))getArrFiles().add(files[i]);
        }
      resetChecks();
    }

    public void deleteItem(int position){
        getArrFiles().remove(position);
    }

    @Override
    protected void click(ImageView image, ImageView check, int position) {
        super.click(image, check, position);
             if(modeSelected){
                getArrChecks()[position] = !getArrChecks()[position];
                check.setVisibility(getArrChecks()[position]?View.VISIBLE:View.INVISIBLE);
                 if(map!=null) {
                if (getArrChecks()[position]) {
                    map.put(position, getArrFiles().get(position));
                } else {
                    map.remove(position);
                }
                if(map.isEmpty()){
                    modeSelected = false;
                    if(listenModeSelected!=null)listenModeSelected.selected(false);
                }
            }
        }else {
            if(listenModeSelected!=null)listenModeSelected.click(getArrFiles().get(position));
        }
    }

    @Override
    protected void longTouch(ImageView image, ImageView check, int position) {
        super.longTouch(image, check, position);
        if(listenModeSelected!=null)listenModeSelected.selected(true);
        getArrChecks()[position] = true;
        check.setVisibility(View.VISIBLE);
        modeSelected = true;
        if(map!=null)map.put(position,getArrFiles().get(position));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull List<Object> payloads) {
        holder.getCheck().setVisibility(getArrChecks()[position]?View.VISIBLE:View.INVISIBLE);
        super.onBindViewHolder(holder, position, payloads);
    }

    @Override
    protected void createHolder(View holder) {
        super.createHolder(holder);
        if(params!=null)holder.setLayoutParams(params);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        /*сюда ставим изображение по дефолту*/
          super.onBindViewHolder(holder,position);
    }

    public void setParams(FrameLayout.LayoutParams params){
      this.params = params;
    }

    public interface ModeSelected{
        void selected(boolean mode);
        void click(File file);
    }
}
