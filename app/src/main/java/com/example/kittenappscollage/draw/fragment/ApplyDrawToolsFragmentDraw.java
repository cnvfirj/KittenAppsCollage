package com.example.kittenappscollage.draw.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.kittenappscollage.draw.RepDraw;
import com.example.kittenappscollage.draw.operations.Operation;

/*применяем инструменты обработки изображения*/
public class ApplyDrawToolsFragmentDraw extends ApplyCommonToolsFragmentDraw {


    @Override
    protected void toolPaint(ImageView v) {
        super.toolPaint(v);
        if(dIndexPaint%10==1)dViewDraw.setEvent(Operation.Event.DRAW_A_LINE_1);
        else if(dIndexPaint%10==2)dViewDraw.setEvent(Operation.Event.DRAW_A_LINE_2);
        else if(dIndexPaint%10==3)dViewDraw.setEvent(Operation.Event.DRAW_A_LINE_3);
        else if(dIndexPaint%10==4)dViewDraw.setEvent(Operation.Event.DRAW_SPOT);
//        enabledGrouping(true);
    }

    @Override
    protected void toolErase(ImageView v) {
        super.toolErase(v);
        if(dIndexErase%10==1)dViewDraw.setEvent(Operation.Event.LAYERS_ELASTIC_1);
        else if(dIndexErase%10==2)dViewDraw.setEvent(Operation.Event.LAYERS_ELASTIC_2);
        else if(dIndexErase%10==3)dViewDraw.setEvent(Operation.Event.LAYERS_ELASTIC_3);
        else if(dIndexErase%10==4)dViewDraw.setEvent(Operation.Event.LAYERS_ELASTIC_4);
//        enabledGrouping(false);

    }

    @Override
    protected void toolFill(ImageView v) {
        super.toolFill(v);
        if(dIndexFill%10==1)dViewDraw.setEvent(Operation.Event.LAYERS_FILL_TO_COLOR);
        else if(dIndexFill%10==2)dViewDraw.setEvent(Operation.Event.LAYERS_FILL_TO_BORDER);
//        enabledGrouping(false);
    }

    @Override
    protected void toolText(ImageView v) {
        super.toolText(v);
        dViewDraw.setEvent(Operation.Event.DRAW_TEXT);
//        enabledGrouping(true);
    }

    @Override
    protected void toolCut(ImageView v) {
        super.toolCut(v);
        dViewDraw.setEvent(Operation.Event.LAYERS_CUT);
//        enabledGrouping(false);
    }

    @Override
    protected void toolDeformRotate(ImageView v) {
        super.toolDeformRotate(v);
        if(dIndexDefRot%10==1)dViewDraw.setEvent(Operation.Event.MATRIX_R);
        else if(dIndexDefRot%10==2)dViewDraw.setEvent(Operation.Event.MATRIX_D);
        else if(dIndexDefRot%10==3)dViewDraw.setEvent(Operation.Event.MATRIX_RESET_DR);
//        enabledGrouping(false);

    }

    @Override
    protected void toolTranslate(ImageView v) {
        super.toolTranslate(v);
        dViewDraw.setEvent(Operation.Event.MATRIX_T);
//        enabledGrouping(true);
    }

    @Override
    protected void toolScale(ImageView v) {
        super.toolScale(v);
        if(dIndexScale%10==1)dViewDraw.setEvent(Operation.Event.MATRIX_S_P);
        else if(dIndexScale%10==2)dViewDraw.setEvent(Operation.Event.MATRIX_S_M);
//        enabledGrouping(true);

    }

    @Override
    protected void toolProperties(ImageView v) {
        super.toolProperties(v);
    }

    @Override
    protected void doneCut() {
        super.doneCut();
        dViewDraw.doneCut();
        RepDraw.get().zeroingRepers();
    }

    @Override
    protected void enterText() {
        super.enterText();
    }

    @Override
    protected void selectorButtons(int index) {
        super.selectorButtons(index);
        if(index!=TOOL_CUT) RepDraw.get().zeroingRepers();
        dViewDraw.invalidate();
    }
}
