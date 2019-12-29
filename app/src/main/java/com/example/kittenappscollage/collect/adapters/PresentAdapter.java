package com.example.kittenappscollage.collect.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;

import static com.example.kittenappscollage.helpers.Massages.LYTE;

public class PresentAdapter extends FileAdapter {

    private FrameLayout.LayoutParams params;

    private boolean modeSelected;

    public PresentAdapter(Context c, int source) {
        super(c, source);
    }

    public PresentAdapter resetChecks(){
        resetArrChecks();
        modeSelected = false;
        return this;
    }

    @Override
    protected void click(ImageView image, ImageView check, int position) {
        super.click(image, check, position);
        if(modeSelected){
            getArrChecks()[position] = !getArrChecks()[position];
            check.setVisibility(getArrChecks()[position]?View.VISIBLE:View.INVISIBLE);
        }
    }

    @Override
    protected void longTouch(ImageView image, ImageView check, int position) {
        super.longTouch(image, check, position);
        getArrChecks()[position] = true;
        check.setVisibility(View.VISIBLE);
        modeSelected = true;
    }

    @Override
    protected void createdItems(ImageView image, ImageView check, int position) {
        super.createdItems(image, check, position);
//        check.setVisibility(getArrChecks()[position]?View.VISIBLE:View.INVISIBLE);
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
}
