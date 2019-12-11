package com.example.kittenappscollage.draw.operations;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.PointF;
import android.view.MotionEvent;

import com.example.mutmatrix.DeformMat;

public abstract class Operation {

    public enum Event{
        DRAW,
        DRAW_A_LINE_1,
        DRAW_A_LINE_2,
        DRAW_A_LINE_3,

        DRAW_SPOT,
        DRAW_TEXT,

        MATR,
        MATRIX_T,
        MATRIX_S_P,
        MATRIX_S_M,
        MATRIX_S,
        MATRIX_S_1,
        MATRIX_S_2,
        MATRIX_S_3,
        MATRIX_S_4,
        MATRIX_R,
        MATRIX_D,
        MATRIX_RESET_DR,
        MATRIX_RESET_T,

        LAYE,
        LAYERS_FILL_TO_COLOR,
        LAYERS_FILL_TO_BORDER,
        LAYERS_CUT,
        LAYERS_ELASTIC_1,
        LAYERS_ELASTIC_2,
        LAYERS_ELASTIC_3,
        LAYERS_ELASTIC_4,
        LAYERS_LINE_1,
        LAYERS_LINE_2,
        LAYERS_LINE_3,

        NULLABLE
    }


    protected Event event;

    private boolean ready;


    public Operation ready(boolean ready){
        this.ready = ready;
        return this;
    }
    public boolean isReady(){
        return ready;
    }

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

    public Operation lyr(int lyr){
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

        public void result(Bitmap img, DeformMat mat,int index, int lyr, int mutable);

        public void repers(PointF[]points, boolean ready);

    }


}
