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

    private String text;

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
        circ = false;
        text = "Your Text";
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
            canvas.drawLine(0,(float) canvas.getHeight()/2.0f,getWidth(),(float) canvas.getHeight()/2.0f,paint);

        } else if(type==ERASER){
            clip.reset();
            clip.addRect(0,0,(float) canvas.getWidth(),(float) canvas.getHeight()/2.0f-(float) width/2.0f, Path.Direction.CCW);
            clip.addRect(0,(float) canvas.getHeight()/2.0f+width/2.0f,(float)canvas.getWidth(),(float) canvas.getHeight(), Path.Direction.CCW);
            canvas.save();
            canvas.clipPath(clip);
            canvas.drawColor(Color.argb(255,Color.red(color),Color.green(color),Color.blue(color)));
            canvas.restore();
            canvas.drawLine(0,(float) canvas.getHeight()/2,getWidth(),(float) canvas.getHeight()/2,paint);
        } else if(type==TEXT){
            clip.reset();
            clip.moveTo(0,(float) canvas.getHeight()/2);
            clip.lineTo(getWidth(),(float) canvas.getHeight()/2);
            paint.setTextSize(width*1.2f);
            paint.setStrokeWidth((float) width/9);
            paint.setTextAlign(Paint.Align.CENTER);
            if(getWidth()<(text.length()*(width/2f)))paint.setTextAlign(Paint.Align.LEFT);
            canvas.drawTextOnPath(text, clip, 0, (float) width/3, paint);

        }
    }

    public void setType(int type){
        this.type = type;
        if(type==ERASER)setAlpha(0);
        else invalidate();
    }

    public void setText(String text){
        this.text = text;
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
