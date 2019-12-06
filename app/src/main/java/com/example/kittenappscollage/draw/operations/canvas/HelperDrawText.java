package com.example.kittenappscollage.draw.operations.canvas;

import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.PointF;

import com.example.kittenappscollage.draw.repozitoryDraw.RepDraw;

public class HelperDrawText extends HelperDrawLine{

    protected float hSizeText;

    protected float hLengthPath;

    protected PointF hStart;

    protected PointF hMove;

    @Override
    public HelperDrawLine start(PointF start) {
        hStart = new PointF(start.x,start.y);
        hLengthPath = 0;
        return super.start(start);
    }

    @Override
    public HelperDrawLine move(PointF move) {
        if(hMove!=null){
            hStart = new PointF(hMove.x,hMove.y);
        }
        hMove = new PointF(move.x,move.y);
        hLengthPath+=lengthVector(vector());
        return super.move(move);

    }

    @Override
    public HelperDrawLine size(float size) {
        hSize = size;
        hSizeText = size*1.3f;
        hPaint.setTextSize(hSizeText);
        hPaint.setStrokeWidth(2);
        hPaint.setPathEffect(new CornerPathEffect(size*3));
        return this;
    }

    @Override
    public void draw() {
//        hPaint.setStyle(dFill? Paint.Style.FILL: Paint.Style.STROKE);
//        hPaint.setTextSkewX(dItalic?-0.6f:0);//наклон
        hPaint.setTextAlign(Paint.Align.LEFT);
        hPaint.setTextScaleX(scaleX());
//        hCanvas.drawTextOnPath(dText, hPath, 0, hSize/2, hPaint);
        hCanvas.drawTextOnPath(RepDraw.get().getText(), hPath, 0, hSize/2, hPaint);

    }

    public void resetPoints(){
        hStart = null;
        hMove = null;
    }

    private PointF vector(){
        return new PointF(hMove.x-hStart.x,hMove.y-hStart.y);
    }

    private float lengthVector(PointF vector){
        return (float) Math.sqrt(vector.x*vector.x+vector.y*vector.y);
    }

    private float scaleX(){
//        float i = hLengthPath/(dText.length()*(hSizeText/1.66f));
        float i = hLengthPath/(RepDraw.get().getText().length()*(hSizeText/1.66f));

        i = i>2?2:i;
        return i;
    }

}
