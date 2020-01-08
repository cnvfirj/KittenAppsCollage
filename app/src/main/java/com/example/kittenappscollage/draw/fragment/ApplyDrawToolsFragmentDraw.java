package com.example.kittenappscollage.draw.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.example.kittenappscollage.draw.DialogSelecledTextFragment;
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

    private final String KEY_EVENT = "key event";

    @Override
    protected void toolPaint(ImageView v) {
        super.toolPaint(v);
        int e = Operation.Event.LAYERS_LINE_1.ordinal();
        if(dIndexPaint%10==1)e = dViewDraw.setEvent(Operation.Event.LAYERS_LINE_1);
        else if(dIndexPaint%10==2)e = dViewDraw.setEvent(Operation.Event.LAYERS_LINE_2);
        else if(dIndexPaint%10==3)e = dViewDraw.setEvent(Operation.Event.LAYERS_LINE_3);
        else if(dIndexPaint%10==4)e = dViewDraw.setEvent(Operation.Event.DRAW_SPOT);
        getEditor().putInt(KEY_EVENT,e).apply();
    }

    @Override
    protected void toolErase(ImageView v) {
        super.toolErase(v);
        int e = Operation.Event.LAYERS_ELASTIC_1.ordinal();
        if(dIndexErase%10==1)e = dViewDraw.setEvent(Operation.Event.LAYERS_ELASTIC_1);
        else if(dIndexErase%10==2)e = dViewDraw.setEvent(Operation.Event.LAYERS_ELASTIC_2);
        else if(dIndexErase%10==3)e = dViewDraw.setEvent(Operation.Event.LAYERS_ELASTIC_3);
        else if(dIndexErase%10==4)e = dViewDraw.setEvent(Operation.Event.LAYERS_ELASTIC_4);
        getEditor().putInt(KEY_EVENT,e).apply();
    }

    @Override
    protected void toolFill(ImageView v) {
        super.toolFill(v);
        int e = Operation.Event.LAYERS_FILL_TO_COLOR.ordinal();
        if(dIndexFill%10==1) e = dViewDraw.setEvent(Operation.Event.LAYERS_FILL_TO_COLOR);
        else if(dIndexFill%10==2)e = dViewDraw.setEvent(Operation.Event.LAYERS_FILL_TO_BORDER);
        getEditor().putInt(KEY_EVENT,e).apply();
    }

    @Override
    protected void toolText(ImageView v) {
        super.toolText(v);
        int e = dViewDraw.setEvent(Operation.Event.DRAW_TEXT);
        getEditor().putInt(KEY_EVENT,e).apply();
    }

    @Override
    protected void toolCut(ImageView v) {
        super.toolCut(v);
        int e = dViewDraw.setEvent(Operation.Event.LAYERS_CUT);
        getEditor().putInt(KEY_EVENT,e).apply();
    }

    @Override
    protected void toolDeformRotate(ImageView v) {
        super.toolDeformRotate(v);
        int e = Operation.Event.MATRIX_R.ordinal();
        if(dIndexDefRot%10==1)e = dViewDraw.setEvent(Operation.Event.MATRIX_R);
        else if(dIndexDefRot%10==2)e = dViewDraw.setEvent(Operation.Event.MATRIX_D);
        else if(dIndexDefRot%10==3)e = dViewDraw.setEvent(Operation.Event.MATRIX_RESET_DR);
        getEditor().putInt(KEY_EVENT,e).apply();
    }

    @Override
    protected void toolTranslate(ImageView v) {
        super.toolTranslate(v);
        int e = dViewDraw.setEvent(Operation.Event.MATRIX_T);
        getEditor().putInt(KEY_EVENT,e).apply();
    }

    @Override
    protected void toolScale(ImageView v) {
        super.toolScale(v);
        int e = Operation.Event.MATRIX_S_P.ordinal();
        if(dIndexScale%10==1)e = dViewDraw.setEvent(Operation.Event.MATRIX_S_P);
        else if(dIndexScale%10==2)e = dViewDraw.setEvent(Operation.Event.MATRIX_S);
        else if(dIndexScale%10==3)e = dViewDraw.setEvent(Operation.Event.MATRIX_S_M);
        getEditor().putInt(KEY_EVENT,e).apply();
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
        DialogSelecledTextFragment.get().show(getChildFragmentManager(),DialogSelecledTextFragment.DIALOG_SEL_TXT);
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
        Operation.Event e = Operation.Event.values()[getPreferences().getInt(KEY_EVENT, Operation.Event.MATRIX_T.ordinal())];
        dViewDraw.setEvent(e);
        RepDraw.get().setAlpha(getPreferences().getInt(KEY_SAVE_ALPHA,0));
        RepDraw.get().setColor(getPreferences().getInt(KEY_SAVE_COLOR, Color.BLACK));
        RepDraw.get().setWidth(getPreferences().getFloat(KEY_SAVE_WIDTH,50));
        RepDraw.get().setText(getPreferences().getString(KEY_SAVE_TEXT,"Your Text"));
    }
}
