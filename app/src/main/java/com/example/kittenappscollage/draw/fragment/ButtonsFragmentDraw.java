package com.example.kittenappscollage.draw.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ButtonsFragmentDraw extends SuperFragmentDraw {

    private ImageView dUndo, dRedo,dInfo, dAllLyrs, dChangeLyrs, dDelLyr, dDelAll;

    private ImageView dPaint, dFill, dEraser, dCut, dText, dDeform, dScale, dTrans;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initButtons(view);
    }

    private void initButtons(View view){

    }
}
