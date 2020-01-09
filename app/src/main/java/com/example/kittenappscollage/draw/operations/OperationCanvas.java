package com.example.kittenappscollage.draw.operations;

import android.graphics.Canvas;
import android.graphics.PointF;
import android.view.MotionEvent;

import com.example.kittenappscollage.draw.operations.canvas.BuildPath;
import com.example.kittenappscollage.draw.operations.canvas.DrawText;
import com.example.mutmatrix.DeformMat;

public class OperationCanvas extends Operation {

    private DrawText oDraw;

    private boolean oIsCanvas;

    private static OperationCanvas singleton;

    public OperationCanvas() {
        oDraw = new DrawText();
        oIsCanvas = false;
    }

    public static OperationCanvas get(){
        if(singleton==null){
                singleton = new OperationCanvas();
        }
        return singleton;
    }

    @Override
    public Operation event(Event event) {
        this.event = event;
        if(event.equals(Event.DRAW_SPOT))oDraw.command(BuildPath.Command.SPOT);
        else if(event.equals(Event.DRAW_TEXT))oDraw.command(BuildPath.Command.TEXT);
        return this;
    }

    public OperationCanvas preview(boolean p){
        oDraw.preview(p);
        return this;
    }
    @Override
    public Operation canvas(Canvas canvas) {
        oDraw.canvas(canvas);
        if(canvas!=null)oIsCanvas = true;
        return super.canvas(canvas);
    }

    @Override
    public Operation index(int index) {
        oDraw.index(index);
        return this;
    }

    @Override
    public Operation lyr(int lyr) {
        oDraw.lyr(lyr);
        return this;
    }

    @Override
    public Operation resultMutable(ResultMutable result) {
        oDraw.listener(result);
        return this;
    }

    @Override
    public Operation point(MotionEvent m) {
        oDraw.point(m);
        return this;
    }

    @Override
    public Operation point(PointF p, int action) {
        return null;
    }

    @Override
    public Operation mat(DeformMat mat) {
        return this;
    }

    @Override
    public void apply() {
         oDraw.draw();
    }

    public boolean isCanvas(){
        return oIsCanvas;
    }
}
