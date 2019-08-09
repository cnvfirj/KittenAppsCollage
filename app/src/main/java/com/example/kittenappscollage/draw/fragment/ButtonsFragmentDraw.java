package com.example.kittenappscollage.draw.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.kittenappscollage.R;

import java.sql.Time;
import java.util.concurrent.TimeUnit;

import static com.example.kittenappscollage.helpers.Massages.MASSAGE;

public class ButtonsFragmentDraw extends SuperFragmentDraw {

    private ImageView dUndo, dRedo,dInfo, dAllLyrs, dChangeLyrs, dDelLyr, dDelAll, dUnion;

    private ImageView dPaint, dFill, dEraser, dCut, dText, dDeform, dScale, dTrans;

    private boolean dVisibleInfo, dOnAllLyrs;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        dVisibleInfo = false;
        dOnAllLyrs = false;
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initButtons(view);
    }

    private void initButtons(View view){
        dUndo = view.findViewById(R.id.tool_undo);
        dUndo.setOnClickListener(this);
        dRedo = view.findViewById(R.id.tool_redo);
        dRedo.setOnClickListener(this);
        dInfo = view.findViewById(R.id.tool_info);
        dInfo.setOnClickListener(this);
        dAllLyrs = view.findViewById(R.id.tool_all_lyrs);
        dAllLyrs.setOnClickListener(this);
        dChangeLyrs = view.findViewById(R.id.tool_change);
        dChangeLyrs.setOnClickListener(this);
        dDelLyr = view.findViewById(R.id.tool_del_lyr);
        dDelLyr.setOnClickListener(this);
        dUnion = view.findViewById(R.id.tool_union);
        dUnion.setOnClickListener(this);
        dDelAll = view.findViewById(R.id.tool_del_all);
        dDelAll.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.tool_undo:
                stepUndo();
                break;
            case R.id.tool_redo:
                stepRedo();
                break;
            case R.id.tool_info:
                onInfo();
                break;
            case R.id.tool_all_lyrs:
                onAllLyrs();
                break;
            case R.id.tool_change:
                changeLyrs();
                break;
            case R.id.tool_del_lyr:
                delLyr();
                break;
            case R.id.tool_union:
                unionLyrs();
                break;
            case R.id.tool_del_all:
                delAll();
                break;
        }
    }



    protected void onAllLyrs(){
        if(dOnAllLyrs){
            dOnAllLyrs = false;
        }else {
            dOnAllLyrs = true;
        }
        dAllLyrs.setSelected(dOnAllLyrs);
    }

    protected void onInfo(){
        if(dVisibleInfo){
            dVisibleInfo = false;
        }else {
            dVisibleInfo = true;
        }
        dInfo.setSelected(dVisibleInfo);
    }

    protected void stepUndo(){
          dUndo.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.blink));
    }

    protected void stepRedo(){
         dRedo.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.blink));
    }

    protected void changeLyrs(){
        dChangeLyrs.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.blink));
    }

    protected void delLyr(){
        dDelLyr.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.blink));
    }

    protected void unionLyrs(){
        dUnion.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.blink));
    }

    protected void delAll(){
        dDelAll.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.blink));

    }

}
