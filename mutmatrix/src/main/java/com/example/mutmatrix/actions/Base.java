package com.example.mutmatrix.actions;

import android.content.Context;
import android.graphics.PointF;
import android.view.MotionEvent;

import com.example.mutmatrix.CompRep;
import com.example.mutmatrix.DeformMat;

public abstract class Base {

    protected CompRep rep;

    protected Context context;

    public Base(Context context) {
        this.context = context;
        reset();
    }

    public Base rep(CompRep rep){
        reset();
        this.rep = rep;
        return this;
    }

    public Base touch(MotionEvent event){
        return this;
    }

    public Base view(PointF v){
        return this;
    }

    public PointF getPointBitmap(PointF p){
        return null;
    }

    public PointF[]muteDeformLoc(Deform.Coordinates c){
        return null;
    }

    public void scalar(DeformMat.Command s){

    }

    public void specialCommand(DeformMat.SpecialCommand c, PointF p){

    }

    public abstract void reset();

    public abstract void start(PointF p);

    public abstract void move(PointF p);

    public abstract void fin(PointF p);


}
