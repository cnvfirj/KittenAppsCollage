package com.example.kittenappscollage.draw.operations;

import android.graphics.Canvas;
import android.graphics.PointF;
import android.view.MotionEvent;

import com.example.kittenappscollage.draw.operations.canvas.DrawCanvas;
import com.example.kittenappscollage.draw.operations.canvas.DrawText;

import static com.example.kittenappscollage.helpers.Massages.LYTE;

public class OperationCanvas extends Operation {

    public final static int DRAW_PREVIEW = 145;
    public final static int DRAW_BITMAP = 129;

    private DrawText oDraw;

    private static OperationCanvas singleton;

    public OperationCanvas() {
        oDraw = new DrawText();
    }

    public static OperationCanvas get(){
        if(singleton==null){
//            synchronized (OperationCanvas.class){
                singleton = new OperationCanvas();
//            }
        }
        return singleton;
    }

    @Override
    public Operation event(Event event) {
        this.event = event;
        if(event.equals(Event.DRAW_A_LINE_1))oDraw.command(DrawCanvas.Command.ARBIT_LINE_1);
        else if(event.equals(Event.DRAW_A_LINE_2))oDraw.command(DrawCanvas.Command.ARBIT_LINE_2);
        else if(event.equals(Event.DRAW_A_LINE_3))oDraw.command(DrawCanvas.Command.ARBIT_LINE_3);
        else if(event.equals(Event.DRAW_SPOT))oDraw.command(DrawCanvas.Command.ARBIT_SPOT);
        else if(event.equals(Event.DRAW_TEXT))oDraw.command(DrawCanvas.Command.TEXT);
        return this;
    }

    public OperationCanvas preview(int p){
        oDraw.preview(p);
        return this;
    }
    @Override
    public Operation canvas(Canvas canvas) {
        oDraw.canvas(canvas);
        return super.canvas(canvas);
    }

    @Override
    public Operation index(int index) {
        oDraw.index(index);
        return super.index(index);
    }

    @Override
    public Operation lyr(int lyr) {

        return super.lyr(lyr);
    }

    @Override
    public Operation point(MotionEvent m) {
        oDraw.point(m);
        return null;
    }

    @Override
    public Operation point(PointF p, int action) {
        return null;
    }

    @Override
    public void apply() {
        oDraw.draw();
    }
}
