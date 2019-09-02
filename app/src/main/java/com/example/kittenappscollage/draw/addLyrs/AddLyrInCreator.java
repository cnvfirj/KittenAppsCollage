package com.example.kittenappscollage.draw.addLyrs;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.kittenappscollage.R;
import com.madrapps.pikolo.HSLColorPicker;
import com.madrapps.pikolo.listeners.OnColorSelectionListener;
import com.madrapps.pikolo.listeners.SimpleColorSelectionListener;

import static com.example.kittenappscollage.helpers.Massages.MASSAGE;

public class AddLyrInCreator extends SelectedFragment {


    private PreviewBlankBitmp aPreview;

    private HSLColorPicker aSelectColor;

    private ImageView aColorPickCall;

    private int aColor;

    private int aNumb;


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
        aNumb = 0;
        paramView(aPreview);
        paramView(aSelectColor);
        paramView(aColorPickCall);


        addListen();

    }

    private void addListen(){
        aSelectColor.setColor(aColor);
        aSelectColor.setColorSelectionListener(new SimpleColorSelectionListener(){
            @Override
            public void onColorSelected(int color) {
                super.onColorSelected(color);
                aColor = color;
            }
        });

        aColorPickCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!aColorPickCall.isSelected()){
                    aSelectColor.animate().scaleX(1).scaleY(1).translationX(0).translationY(0).setDuration(500).start();
                }else {
                    aSelectColor.animate().scaleX(0.1f).scaleY(0.1f).translationX(0).translationY(0).setDuration(500).start();
                }
                aColorPickCall.setSelected(!aColorPickCall.isSelected());
            }
        });
    }

    private void paramView(final View view){
        /*здесь дожидаемся создания вью для расчета расстояний между кнопками*/
        ViewTreeObserver viewTreeObserver = view.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    aNumb++;
                    if(aNumb==3)applyTransform(true);
                }
            });
        }
    }
    private void applyTransform(boolean hide){
        float transX = 0;
        float transY = 0;
        float scaleX = 1;
        float scaleY = 1;
        if(hide) {
            scaleX = aColorPickCall.getWidth() / aSelectColor.getWidth();
            scaleY = aColorPickCall.getWidth() / aSelectColor.getWidth();
        }


        aSelectColor.animate().scaleX(scaleX).scaleY(scaleY).translationX(transX).translationY(transY).setDuration(0).start();
    }


}
