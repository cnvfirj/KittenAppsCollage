package com.example.kittenappscollage.draw.operations;

import android.graphics.Bitmap;
import android.graphics.Path;
import android.graphics.PointF;
import android.view.MotionEvent;

import com.example.kittenappscollage.draw.operations.bitmap.MutableBit;
import com.example.kittenappscollage.draw.operations.bitmap.MutableCut;
import com.example.mutablebitmap.DeformMat;

import static com.example.kittenappscollage.helpers.Massages.LYTE;

public class OperationBitmap extends Operation{

    private static OperationBitmap singleton;

    private MutableBit oMutableBit;

    public OperationBitmap() {
        oMutableBit = new MutableCut();
    }

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
        return null;
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
