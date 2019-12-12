package com.example.kittenappscollage.draw.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.example.kittenappscollage.draw.DialogSelectParams;
import com.example.kittenappscollage.draw.repozitoryDraw.RepDraw;
import com.example.kittenappscollage.draw.operations.Operation;

import static com.example.kittenappscollage.draw.repozitoryDraw.RepParams.KEY_SAVE_ALPHA;
import static com.example.kittenappscollage.draw.repozitoryDraw.RepParams.KEY_SAVE_COLOR;
import static com.example.kittenappscollage.draw.repozitoryDraw.RepParams.KEY_SAVE_TEXT;
import static com.example.kittenappscollage.draw.repozitoryDraw.RepParams.KEY_SAVE_WIDTH;
import static com.example.kittenappscollage.helpers.Massages.LYTE;

/*применяем инструменты обработки изображения*/
public class ApplyDrawToolsFragmentDraw extends ApplyCommonToolsFragmentDraw {

    @Override
    protected void toolPaint(ImageView v) {
        super.toolPaint(v);
        if(dIndexPaint%10==1)dViewDraw.setEvent(Operation.Event.LAYERS_LINE_1);
        else if(dIndexPaint%10==2)dViewDraw.setEvent(Operation.Event.LAYERS_LINE_2);
        else if(dIndexPaint%10==3)dViewDraw.setEvent(Operation.Event.LAYERS_LINE_3);
        else if(dIndexPaint%10==4)dViewDraw.setEvent(Operation.Event.DRAW_SPOT);
//        RepDraw.get().createOverlay();
//        enabledGrouping(true);
    }

    @Override
    protected void toolErase(ImageView v) {
        super.toolErase(v);
        if(dIndexErase%10==1)dViewDraw.setEvent(Operation.Event.LAYERS_ELASTIC_1);
        else if(dIndexErase%10==2)dViewDraw.setEvent(Operation.Event.LAYERS_ELASTIC_2);
        else if(dIndexErase%10==3)dViewDraw.setEvent(Operation.Event.LAYERS_ELASTIC_3);
        else if(dIndexErase%10==4)dViewDraw.setEvent(Operation.Event.LAYERS_ELASTIC_4);
//        RepDraw.get().recycleOverlay();
//        enabledGrouping(false);

    }

    @Override
    protected void toolFill(ImageView v) {
        super.toolFill(v);
        if(dIndexFill%10==1)dViewDraw.setEvent(Operation.Event.LAYERS_FILL_TO_COLOR);
        else if(dIndexFill%10==2)dViewDraw.setEvent(Operation.Event.LAYERS_FILL_TO_BORDER);
//        RepDraw.get().recycleOverlay();
//        enabledGrouping(false);
    }

    @Override
    protected void toolText(ImageView v) {
        super.toolText(v);
        dViewDraw.setEvent(Operation.Event.DRAW_TEXT);
//        RepDraw.get().createOverlay();
//        enabledGrouping(true);
    }

    @Override
    protected void toolCut(ImageView v) {
        super.toolCut(v);
        dViewDraw.setEvent(Operation.Event.LAYERS_CUT);
//        RepDraw.get().recycleOverlay();
//        enabledGrouping(false);
    }

    @Override
    protected void toolDeformRotate(ImageView v) {
        super.toolDeformRotate(v);
        if(dIndexDefRot%10==1)dViewDraw.setEvent(Operation.Event.MATRIX_R);
        else if(dIndexDefRot%10==2)dViewDraw.setEvent(Operation.Event.MATRIX_D);
        else if(dIndexDefRot%10==3)dViewDraw.setEvent(Operation.Event.MATRIX_RESET_DR);
//        RepDraw.get().recycleOverlay();
//        enabledGrouping(false);

    }

    @Override
    protected void toolTranslate(ImageView v) {
        super.toolTranslate(v);
        dViewDraw.setEvent(Operation.Event.MATRIX_T);
//        RepDraw.get().recycleOverlay();
//        enabledGrouping(true);
    }

    @Override
    protected void toolScale(ImageView v) {
        super.toolScale(v);
        if(dIndexScale%10==1)dViewDraw.setEvent(Operation.Event.MATRIX_S_P);
        else if(dIndexScale%10==2)dViewDraw.setEvent(Operation.Event.MATRIX_S_M);
//        RepDraw.get().recycleOverlay();

    }

    @Override
    protected void toolProperties(ImageView v) {
        super.toolProperties(v);
        DialogSelectParams d = new DialogSelectParams();
        d.show(getChildFragmentManager(),d.getClass().getName());
    }

    @Override
    protected void doneCut() {
        super.doneCut();
        dViewDraw.doneCut();
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

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences p = getActivity().getPreferences(Context.MODE_PRIVATE);
        RepDraw.get().setAlpha(p.getInt(KEY_SAVE_ALPHA,0));
        RepDraw.get().setColor(p.getInt(KEY_SAVE_COLOR, Color.BLACK));
        RepDraw.get().setWidth(p.getFloat(KEY_SAVE_WIDTH,50));
        RepDraw.get().setText(p.getString(KEY_SAVE_TEXT,"Your Text"));
    }
}
