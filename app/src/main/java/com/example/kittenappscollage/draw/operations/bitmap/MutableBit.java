package com.example.kittenappscollage.draw.operations.bitmap;

import android.graphics.Bitmap;
import android.graphics.Path;
import android.graphics.PointF;
import android.view.MotionEvent;

import com.example.kittenappscollage.draw.operations.Operation;
import com.example.kittenappscollage.draw.operations.TouchPoints;
import com.example.mutablebitmap.DeformMat;

import static com.example.kittenappscollage.helpers.Massages.LYTE;


public abstract class MutableBit extends TouchPoints {

    public enum Command{
        CUT,
        CUT_A,
        FILL_C,
        FILL_B,
        ELAST_1,
        ELAST_2,
        ELAST_3,
        ELAST_4,
        NON
    }

    public MutableBit() {
        super();
    }

    public MutableBit bitmap(Bitmap bitmap){
        return this;
    }

    public MutableBit mat(DeformMat mat){
        return this;
    }

    public MutableBit index(int index){
        return this;
    }

    public MutableBit command(Command command){
        return this;
    }

    public MutableBit start(PointF start){
        return this;
    }

    public MutableBit move(PointF move){
        return this;
    }

    public MutableBit fin(PointF fin){
        return this;
    }

    public MutableBit point(PointF point, int action){
        return this;
    }

    public MutableBit point(MotionEvent event){
        return this;
    }

    public MutableBit size(float size){
        return this;
    }

    public MutableBit color(int color){
        return this;
    }

    public MutableBit alpha(int alpha){
        return this;
    }

    public MutableBit listener(Operation.ResultMutable listener){
        return this;
    }

    public abstract void apply();


}
