package com.kittendevelop.kittenappscollage.draw.operations;

import android.graphics.Bitmap;
import android.graphics.PointF;
import android.view.MotionEvent;

import com.kittendevelop.kittenappscollage.draw.operations.bitmap.MutableBit;
import com.kittendevelop.kittenappscollage.draw.operations.bitmap.MutableElastLine;
import com.example.mutmatrix.DeformMat;

public class OperationBitmap extends Operation{

    private static OperationBitmap singleton;

    private MutableElastLine oMutableBit;


    public OperationBitmap() {
        oMutableBit = new MutableElastLine();

    }

    public static OperationBitmap get(){
        if(singleton==null){
//            synchronized (OperationBitmap.class){
                singleton = new OperationBitmap();
//            }
        }
        return singleton;
    }

    @Override
    public Operation event(Event event) {
        this.event = event;
        if(event.equals(Operation.Event.LAYERS_CUT))oMutableBit.command(MutableBit.Command.CUT);
        else if(event.equals(Event.LAYERS_ELASTIC_1))oMutableBit.command(MutableBit.Command.ELAST_1);
        else if(event.equals(Event.LAYERS_ELASTIC_2))oMutableBit.command(MutableBit.Command.ELAST_2);
        else if(event.equals(Event.LAYERS_ELASTIC_3))oMutableBit.command(MutableBit.Command.ELAST_3);
        else if(event.equals(Event.LAYERS_ELASTIC_4))oMutableBit.command(MutableBit.Command.ELAST_4);
        else if(event.equals(Event.LAYERS_FILL_TO_BORDER))oMutableBit.command(MutableBit.Command.FILL_B);
        else if(event.equals(Event.LAYERS_FILL_TO_COLOR))oMutableBit.command(MutableBit.Command.FILL_C);
        else if(event.equals(Event.LAYERS_LINE_1))oMutableBit.command(MutableBit.Command.LINE_1);
        else if(event.equals(Event.LAYERS_LINE_2))oMutableBit.command(MutableBit.Command.LINE_2);
        else if(event.equals(Event.LAYERS_LINE_3))oMutableBit.command(MutableBit.Command.LINE_3);
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
    public Operation lyr(int lyr) {
        oMutableBit.lyr(lyr);
        return this;
    }

    @Override
    public Operation point(MotionEvent m) {
        oMutableBit.point(m);
        return this;
    }

    @Override
    public Operation resultMutable(ResultMutable result) {
        oMutableBit.listener(result);
        return this;
    }

    @Override
    public Operation point(PointF p, int action) {
        return this;
    }

    @Override
    public Operation ready(boolean ready) {
        oMutableBit.ready(ready);
        return super.ready(ready);
    }

    @Override
    public void apply() {
        oMutableBit.apply();
    }

    @Override
    public int getLyr() {
         return oMutableBit.getIndexLyr();
    }
}
