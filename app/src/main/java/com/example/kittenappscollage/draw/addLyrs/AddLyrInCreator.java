package com.example.kittenappscollage.draw.addLyrs;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.example.kittenappscollage.R;
import com.example.kittenappscollage.view.ExtendsSeekBar;
import com.madrapps.pikolo.HSLColorPicker;
import com.madrapps.pikolo.listeners.SimpleColorSelectionListener;

import static com.example.kittenappscollage.helpers.Massages.MASSAGE;
import static com.example.kittenappscollage.helpers.Massages.SHOW_MASSAGE;

public class AddLyrInCreator extends SelectedFragment {


    private PreviewBlankBitmp aPreview;

    private HSLColorPicker aSelectColor;

    private ImageView aColorPickCall, aDoneParams, aExitAll;

    private int aColor;

    private int aNumb;

//    private ExtendsSeekBar aSeekWidth, aSeekHeight;


    public AddLyrInCreator() {
        aColor = Color.WHITE;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_lyr_in_creator,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        aPreview = view.findViewById(R.id.creator_blank_btmp);
        aSelectColor = view.findViewById(R.id.color_pick);
        aColorPickCall = view.findViewById(R.id.color_pick_call);
        aDoneParams = view.findViewById(R.id.creator_lyr_done);
        aExitAll = view.findViewById(R.id.creator_lyr_back);
//        aSeekWidth = view.findViewById(R.id.creator_seek_w);
//        aSeekHeight = view.findViewById(R.id.creator_seek_h);

        aNumb = 0;
        paramView(aPreview);
        paramView(aSelectColor);
        paramView(aColorPickCall);


        addListen();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.creator_lyr_back:
                selector = (SelectorFrameFragments) getParentFragment();
                selector.exitAll();
                break;
            case R.id.creator_lyr_done:
                if(memoryBitmap(aPreview.getSize())<40) {
                    selector = (SelectorFrameFragments) getParentFragment();
                    String way = aPreview.getSize().getWidth() + "_" + aPreview.getSize().getHeight() + "_" + aPreview.getColorFon();
                    selector.backInAddLyr(view, way);
                }else {
                    SHOW_MASSAGE(getActivity(),getActivity().getResources().getString(R.string.big_big_papper)+
                            aPreview.getSize().getWidth()+":"+aPreview.getSize().getHeight());
                }
                break;
            case R.id.color_pick_call:
                callPalette();
                break;
        }
    }

    private void addListen(){
        aSelectColor.setColor(aColor);
        aSelectColor.setColorSelectionListener(new SimpleColorSelectionListener(){
            @Override
            public void onColorSelected(int color) {
                super.onColorSelected(color);
                aColor = color;
                aColorPickCall.setImageTintList(ColorStateList.valueOf(color));

            }
        });

        aColorPickCall.setOnClickListener(this);
        aDoneParams.setOnClickListener(this);
        aExitAll.setOnClickListener(this);
    }

    private void callPalette(){
        if(!aColorPickCall.isActivated()){
            applyTransform(false,500);
        }else {
            applyTransform(true,500);
            aPreview.fon(aColor);
        }
        aColorPickCall.setActivated(!aColorPickCall.isActivated());
    }

    private void paramView(final View view){
        ViewTreeObserver viewTreeObserver = view.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    aNumb++;
                    if(aNumb==3){
                        applyTransform(true,0);
                        applyTransformTools();
                    }

                }
            });
        }
    }

    private void applyTransformTools(){
        int step = aPreview.getWidth()-aColorPickCall.getRight();
        int button = aColorPickCall.getWidth();
//      LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)aSeekWidth.getLayoutParams();
//      params.width = aPreview.getWidth()-(step*3+button*3);
//      aSeekWidth.setLayoutParams(params);
    }

    private void applyTransform(boolean hide, long time){
       float scale = (float) aColorPickCall.getWidth() / (float) aSelectColor.getWidth();
       float transX = (aColorPickCall.getLeft()+(aColorPickCall.getRight()-aColorPickCall.getLeft())/2)-
                (aSelectColor.getLeft()+(aSelectColor.getRight()-aSelectColor.getLeft())/2);
       float transY = (aColorPickCall.getTop()+(aColorPickCall.getBottom()-aColorPickCall.getTop())/2)-
                (aSelectColor.getTop()+(aSelectColor.getBottom()-aSelectColor.getTop())/2);
        if(hide) {
            aSelectColor.animate().scaleX(scale).scaleY(scale).translationX(transX).translationY(transY).setDuration(time).start();
            aColorPickCall.animate().scaleX(1).scaleY(1).translationX(0).translationY(0).setDuration(time).start();
        }else{
            aSelectColor.animate().scaleX(1).scaleY(1).translationX(0).translationY(0).setDuration(time).start();
            aColorPickCall.animate().scaleX(1.5f).scaleY(1.5f).translationX(-transX).translationY(-transY).setDuration(time).start();
        }


    }

    private float memoryBitmap(Size s){
        float memory = (s.getWidth()*4*s.getHeight())/(1024*1000);
        return memory;

    }


}