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

    public static final int TEXT = 1;
    public static final int PAINT = 0;
    public static final int ERASER = 2;

    private int color;

    private int alpha;

    private int width;

    private int erase;

    private Paint paint;

    private Path clip;

    private int type;

    private boolean circ;

    public PresentPaint(Context context) {
        super(context);
    }

    public PresentPaint(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        clip = new Path();
        type = PAINT;
        circ = true;
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(circ) {
            clip.reset();
            clip.addArc(new RectF(0, 0, canvas.getWidth(), canvas.getHeight()), 0, 360);
            canvas.clipPath(clip);
        }
        if(type==PAINT){
            canvas.drawLine(0,canvas.getHeight()/2,getWidth(),canvas.getHeight()/2,paint);
        } else if(type==ERASER){

        } else if(type==TEXT){

        }
    }

    public void setType(int type){
        this.type = type;
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

    public int getWidthPaint(){
        return width;
    }

    private int implementParams(){
        return Color.argb(alpha,Color.red(color),Color.green(color),Color.blue(color));
    }
}
