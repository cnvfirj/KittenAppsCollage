package com.example.kittenappscollage.draw.operations.bitmap;

import android.graphics.Bitmap;
import android.graphics.Path;
import android.graphics.PointF;

import com.example.kittenappscollage.draw.operations.Operation;
import com.example.mutablebitmap.DeformMat;


public abstract class MutableBit {

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

    protected boolean mVisibleMark;

    protected Operation.ResultMutable mResultMutable;

    private String mName = "";

    public MutableBit bitmap(Bitmap bitmap){
        return this;
    }

    public MutableBit mat(DeformMat mat){
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

    public MutableBit size(float size){
        return this;
    }

    public MutableBit color(int color){
        return this;
    }

    public MutableBit alpha(int alpha){
        return this;
    }

    public MutableBit marker(boolean visible){
        mVisibleMark = visible;
        return this;
    }
    public MutableBit listResult(Operation.ResultMutable listResult){
        mResultMutable = listResult;
        return this;
    }

    public MutableBit view(PointF view){
        return this;
    }

    public abstract void apply();

    public Path getPath(){
        return new Path();
    }

    public Path createPath(){

        return null;
    }

    protected void name(String name){
        mName = name;
    }

    public String getName() {
        return mName;
    }

}
