package com.example.kittenappscollage.draw.operations.canvas;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PointF;
import android.view.MotionEvent;

public abstract class DrawCanvas {

    public enum Command{
        ARBIT_LINE_1,
        ARBIT_LINE_2,
        ARBIT_LINE_3,
        ARBIT_SPOT,
        TEXT
    }

    protected Command dCommand;

    protected Canvas dCanvas;

    protected int dPreview, dIndex;

    public void command(Command command){
        dCommand = command;
    }

    public void canvas(Canvas canvas){
        dCanvas = canvas;
    }

    public void preview(int p){
        dPreview = p;
    }

    public void index(int i){
        dIndex = i;
    }

    public abstract DrawCanvas point(MotionEvent event);

    public abstract DrawCanvas start(PointF p);

    public abstract DrawCanvas move(PointF p);

    public abstract DrawCanvas fin(PointF p);

}
