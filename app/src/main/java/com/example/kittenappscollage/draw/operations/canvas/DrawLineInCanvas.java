package com.example.kittenappscollage.draw.operations.canvas;

import android.graphics.Canvas;
import android.graphics.PointF;
import android.view.MotionEvent;

import static com.example.kittenappscollage.helpers.Massages.LYTE;

public class DrawLineInCanvas extends DrawCanvas{


    @Override
    public DrawCanvas point(MotionEvent event) {

        return this;
    }

    @Override
    public DrawCanvas start(PointF p) {
        return this;
    }

    @Override
    public DrawCanvas move(PointF p) {
        return this;
    }

    @Override
    public DrawCanvas fin(PointF p) {
        return this;
    }

    @Override
    public void command(Command command) {
        super.command(command);
    }

    @Override
    public void canvas(Canvas canvas) {
        super.canvas(canvas);
    }


}
