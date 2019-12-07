package com.example.kittenappscollage.draw.operations.canvas;

import android.graphics.PointF;
import android.view.MotionEvent;

public class DrawArbitLine extends DrawCanvas {



    @Override
    public DrawCanvas point(MotionEvent event) {
        int action = event.getActionMasked();
        int index = event.getActionIndex();
        int id = event.getPointerId(index);
        if(action== MotionEvent.ACTION_DOWN)start(new PointF(event.getX(),event.getY()));
        else if(action==MotionEvent.ACTION_MOVE&&id==0)move(new PointF(event.getX(),event.getY()));
        else if(action==MotionEvent.ACTION_UP)fin(new PointF(event.getX(),event.getY()));
        return null;
    }

    @Override
    public DrawCanvas start(PointF p) {
        return null;
    }

    @Override
    public DrawCanvas move(PointF p) {
        return null;
    }

    @Override
    public DrawCanvas fin(PointF p) {
        return null;
    }



}
