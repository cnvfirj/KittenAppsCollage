package com.example.kittenappscollage.draw.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.kittenappscollage.R;

import java.sql.Time;
import java.util.concurrent.TimeUnit;

import static com.example.kittenappscollage.helpers.Massages.MASSAGE;

public class ButtonsFragmentDraw extends SuperFragmentDraw {

    private ImageView dUndo, dRedo,dInfo, dAllLyrs, dChangeLyrs, dDelLyr, dDelAll;

    private ImageView dPaint, dFill, dEraser, dCut, dText, dDeform, dScale, dTrans;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initButtons(view);
    }

    private void initButtons(View view){
        dUndo = view.findViewById(R.id.tool_undo);
        dUndo.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.tool_undo:
                stepUndo();
                break;
        }
    }

    protected void stepUndo(){
//        dUndo.setActivated(true);

//
//        try {
//            TimeUnit.MILLISECONDS.sleep(500);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        dUndo.setActivated(false);
    }
}
