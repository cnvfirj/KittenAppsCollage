package com.example.kittenappscollage.draw.operations.bitmap;

import android.graphics.Bitmap;
import android.graphics.PointF;
import android.view.MotionEvent;

import com.example.kittenappscollage.draw.operations.Operation;
import com.example.kittenappscollage.draw.operations.TouchPoints;
import com.example.mutmatrix.DeformMat;


public abstract class MutableBit extends TouchPoints {

    public enum Command{
        CUT,
        FILL_C,
        FILL_B,
        ELAST_1,
        ELAST_2,
        ELAST_3,
        ELAST_4,
        LINE_1,
        LINE_2,
        LINE_3,
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

    public MutableBit lyr(int lyr){
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

    protected boolean testBitmap(Bitmap b){
        if(b!=null||!b.isRecycled())return true;
        return false;
    }

}
