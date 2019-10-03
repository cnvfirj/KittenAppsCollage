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
        if(event.equals(Operation.Event.LAYERS_CUT))oMutableBit.command(MutableBit.Command.CUT);
        else if(event.equals(Operation.Event.LAYERS_ELASTIC_1))oMutableBit.command(MutableBit.Command.ELAST_1);
        else if(event.equals(Operation.Event.LAYERS_ELASTIC_2))oMutableBit.command(MutableBit.Command.ELAST_2);
        else if(event.equals(Operation.Event.LAYERS_ELASTIC_3))oMutableBit.command(MutableBit.Command.ELAST_3);
        else if(event.equals(Operation.Event.LAYERS_ELASTIC_4))oMutableBit.command(MutableBit.Command.ELAST_4);
        else if(event.equals(Operation.Event.LAYERS_FILL_TO_BORDER))oMutableBit.command(MutableBit.Command.FILL_B);
        else if(event.equals(Operation.Event.LAYERS_FILL_TO_COLOR))oMutableBit.command(MutableBit.Command.FILL_C);
        return this;
    }


    @Override
    public Operation mat(DeformMat mat) {
        oMutableBit.mat(mat);
        return super.mat(mat);
    }

    @Override
    public Operation bitmap(Bitmap bitmap) {
        oMutableBit.bitmap(bitmap);
        return super.bitmap(bitmap);
    }

    @Override
    public Operation index(int index) {
        oMutableBit.index(index);
        return super.index(index);
    }

    @Override
    public Operation point(MotionEvent m) {
        oMutableBit.point(m);
        return this;
    }

    @Override
    public Operation resultMutable(ResultMutable result) {
        oMutableBit.listener(result);
        return super.resultMutable(result);
    }

    @Override
    public Operation point(PointF p, int action) {
        return this;
    }

    @Override
    public void apply() {
        oMutableBit.apply();
    }


}
