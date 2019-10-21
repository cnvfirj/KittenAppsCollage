package com.example.kittenappscollage.draw.operations;

import android.graphics.PointF;
import android.view.MotionEvent;

import static com.example.kittenappscollage.helpers.Massages.LYTE;

public class OperationCanvas extends Operation {

    private static OperationCanvas singleton;

    public static OperationCanvas get(){
        if(singleton==null){
            synchronized (OperationCanvas.class){
                singleton = new OperationCanvas();
            }
        }
        return singleton;
    }

    @Override
    public Operation event(Event event) {
        this.event = event;
        return this;
    }

    @Override
    public Operation point(MotionEvent m) {
        return null;
    }

    @Override
    public Operation point(PointF p, int action) {
        return null;
    }

    @Override
    public void apply() {

    }
}
