package com.example.kittenappscollage.draw.operations;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.PointF;
import android.view.MotionEvent;

import androidx.annotation.Nullable;

import com.example.mutablebitmap.DeformMat;

import static com.example.kittenappscollage.helpers.Massages.LYTE;

public abstract class Operation {



    public enum Event{
        DRAW,
        DRAW_FON,
        DRAW_A_LINE_1,
        DRAW_A_LINE_2,
        DRAW_A_LINE_3,

        DRAW_SPOT,
        DRAW_TEXT,
        DRAW_LINE,
        DRAW_RECT,
        DRAW_OVAL,
        DRAW_NOTE,
        DRAW_RAD,
        DRAW_LENGTH,
        DRAW_ANGLE,

        MATR,
        MATRIX_T,
        MATRIX_S_P,
        MATRIX_S_M,
        MATRIX_R,
        MATRIX_D,
        MATRIX_RESET_DR,
        MATRIX_MIRR,

        LAYE,
        LAYERS_FILL_TO_COLOR,
        LAYERS_FILL_TO_BORDER,
        LAYERS_CUT,
        LAYERS_CUT_BORD,
        LAYERS_ELASTIC_1,
        LAYERS_ELASTIC_2,
        LAYERS_ELASTIC_3,
        LAYERS_ELASTIC_4,
        LAYERS_SCALE,
        LAYERS_MIRR_V,
        LAYERS_MIRR_H,
        LAYERS_INSCRIBE,


        FON,
        NULLABLE
    }

    public enum LayerFlag{
        IMG,
        LYR,
        NON
    }

    public enum FixedPoints{
        P_SHIFT_1,
        P_SHIFT_2,
        P_SHIFT_3,
        P_SHIFT_4,
        P_ACTION,
        SEARCH,
        NON
    }

    protected Event event;



    public Operation mat(DeformMat mat){
        return this;
    }

    public Operation canvas(Canvas canvas){
        return this;
    }

    public Operation view(PointF view){
        return this;
    }

    public Operation bitmap(Bitmap bitmap){
        return this;
    }

    public Operation index(int index){
        return this;
    }

    public Operation mark(boolean vis){
        return this;
    }

    public Operation size(float size){
        return this;
    }

    public Operation color(int color){
        return this;
    }

    public Operation alpha(int alpha){
        return this;
    }

    public Path createPath(){
        return null;
    }

    public Operation resultMutable(ResultMutable result){
        return this;
    }

    public Event getEvent(){
        return event;
    }

    public abstract Operation event(Event event);

    public abstract Operation point(MotionEvent m);

    public abstract Operation point(PointF p, int action);

    public abstract void apply();

    public void applyAll(){

    }



    public interface ResultMutable{

        public void result(Bitmap img, DeformMat mat,int index);

        public void repers(PointF[]points, boolean ready);

    }


}
