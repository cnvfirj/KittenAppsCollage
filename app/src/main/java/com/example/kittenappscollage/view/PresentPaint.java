package com.example.kittenappscollage.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class PresentPaint extends View {

    private int color;

    private int alpha;

    private int width;

    private Paint paint;

    private Path clip;

    public PresentPaint(Context context) {
        super(context);
    }

    public PresentPaint(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        clip = new Path();
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        clip.reset();
        clip.addArc(new RectF(0,0,canvas.getWidth(),canvas.getHeight()),0,360);
        canvas.clipPath(clip);
        canvas.drawLine(0,canvas.getHeight()/2,getWidth(),canvas.getHeight()/2,paint);
    }

    public void setColor(int color) {
        this.color = color;
        paint.setColor(implementParams());
        invalidate();
    }

    public void setAlpha(int alpha){
        this.alpha = alpha;
        paint.setColor(implementParams());
        invalidate();
    }


    public int getColor() {
        return implementParams();
    }

    public void setWidthPaint(int width) {
        paint.setStrokeWidth(width);
        this.width = width;
        invalidate();
    }

    private int implementParams(){
        return Color.argb(alpha,Color.red(color),Color.green(color),Color.blue(color));
    }
}
