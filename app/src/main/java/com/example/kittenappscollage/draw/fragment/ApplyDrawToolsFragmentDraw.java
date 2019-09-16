package com.example.kittenappscollage.draw.fragment;

import android.widget.ImageView;

import com.example.kittenappscollage.draw.operations.Operation;

/*применяем инструменты обработки изображения*/
public class ApplyDrawToolsFragmentDraw extends ApplyCommonToolsFragmentDraw {

    @Override
    protected void toolPaint(ImageView v) {
        super.toolPaint(v);
        if(dIndexPaint%10==1)dViewDraw.setEvent(Operation.Event.DRAW_A_LINE_1);
        else if(dIndexPaint%10==2)dViewDraw.setEvent(Operation.Event.DRAW_A_LINE_2);
        else if(dIndexPaint%10==3)dViewDraw.setEvent(Operation.Event.DRAW_A_LINE_3);
    }

    @Override
    protected void toolErase(ImageView v) {
        super.toolErase(v);
        if(dIndexErase%10==1)dViewDraw.setEvent(Operation.Event.LAYERS_ELASTIC_C);
        else if(dIndexErase%10==2)dViewDraw.setEvent(Operation.Event.LAYERS_ELASTIC_H);
        else if(dIndexErase%10==3)dViewDraw.setEvent(Operation.Event.LAYERS_ELASTIC_S);

    }

    @Override
    protected void toolFill(ImageView v) {
        super.toolFill(v);
        if(dIndexFill%10==1)dViewDraw.setEvent(Operation.Event.LAYERS_FILL_TO_COLOR);
        else if(dIndexFill%10==2)dViewDraw.setEvent(Operation.Event.LAYERS_FILL_TO_BORDER);

    }

    @Override
    protected void toolText(ImageView v) {
        super.toolText(v);
        dViewDraw.setEvent(Operation.Event.DRAW_TEXT);
    }

    @Override
    protected void toolCut(ImageView v) {
        super.toolCut(v);
        dViewDraw.setEvent(Operation.Event.LAYERS_CUT);
    }

    @Override
    protected void toolDeformRotate(ImageView v) {
        super.toolDeformRotate(v);
        if(dIndexDefRot%10==1)dViewDraw.setEvent(Operation.Event.MATRIX_R);
        else if(dIndexDefRot%10==2)dViewDraw.setEvent(Operation.Event.MATRIX_D);
        else if(dIndexDefRot%10==3)dViewDraw.setEvent(Operation.Event.MATRIX_RESET_DR);

    }

    @Override
    protected void toolTranslate(ImageView v) {
        super.toolTranslate(v);
        dViewDraw.setEvent(Operation.Event.MATRIX_T);
    }

    @Override
    protected void toolScale(ImageView v) {
        super.toolScale(v);
        if(dIndexScale%10==1)dViewDraw.setEvent(Operation.Event.MATRIX_S_P);
        else if(dIndexScale%10==2)dViewDraw.setEvent(Operation.Event.MATRIX_S_M);

    }

    @Override
    protected void toolProperties(ImageView v) {
        super.toolProperties(v);
    }

    @Override
    protected void doneCut() {
        super.doneCut();
    }

    @Override
    protected void enterText() {
        super.enterText();
    }
}
