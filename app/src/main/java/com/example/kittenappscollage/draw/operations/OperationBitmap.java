package com.example.kittenappscollage.draw.operations;

import android.graphics.PointF;
import android.view.MotionEvent;

import static com.example.kittenappscollage.helpers.Massages.LYTE;

public class OperationBitmap extends Operation{

    private static OperationBitmap singleton;

    public static OperationBitmap get(){
        if(singleton==null){
            synchronized (OperationBitmap.class){
                singleton = new OperationBitmap();
            }
        }
        return singleton;
    }

    @Override
    public Operation event(Event event) {
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
