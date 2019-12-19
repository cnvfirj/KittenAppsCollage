package com.example.kittenappscollage.draw.operations.canvas;

import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.PointF;

import com.example.kittenappscollage.draw.repozitoryDraw.RepDraw;

import static com.example.kittenappscollage.helpers.Massages.LYTE;

public class DrawText extends DrawSpot {

    private float hLengthPath;

    private PointF hStart, hMove;

    private Paint hPaintText;

    private float hSizeText;

    private String hText;

    public DrawText() {
        hPaintText = new Paint(Paint.ANTI_ALIAS_FLAG);
        hText = "";
    }

    @Override
    protected void start(PointF s) {
        super.start(s);
        hLengthPath = 0;
        hStart = s;
        initParams();
    }

    @Override
    protected void move(PointF m) {
        super.move(m);
        hMove = m;
        hLengthPath+=lengthVector(vector());
    }


    @Override
    public void draw() {
        if (getCommand().equals(Command.TEXT)) {
            drawText();
        }else super.draw();
    }

    private void drawText(){
        if(!getPath().isEmpty()) {
            getCanvas().drawTextOnPath(gethText(), getPath(), 0, getTextSize() / 2, getPaintText());
            applyMutable();
        }
    }

    private float lengthVector(PointF vector){
        return (float) Math.sqrt(vector.x*vector.x+vector.y*vector.y);
    }

    private PointF vector(){
        return new PointF(getMove().x-getStart().x,getMove().y-getStart().y);
    }

    private float scaleX(){
        float i = getLengthPath()/(RepDraw.get().getText().length()*(getTextSize()/1.66f));
        i = i>2?2:i;
        return i;
    }

    private void initParams(){
        hSizeText = RepDraw.get().getWidth()*1.3f;
        hText = RepDraw.get().getText();
        getPaintText().setColor(RepDraw.get().getColor());
        getPaintText().setTextSize(getTextSize());
        getPaintText().setStrokeWidth(2);
        getPaintText().setPathEffect(new CornerPathEffect(RepDraw.get().getWidth()*3));
        getPaintText().setStyle(RepDraw.get().isTextFill()? Paint.Style.FILL_AND_STROKE: Paint.Style.STROKE);
        getPaintText().setTextSkewX(RepDraw.get().isTextFill()?-0.6f:0);
//        getPaintText().setTextScaleX(0.5f);
    }

    protected float getLengthPath(){
        return hLengthPath;
    }

    private PointF getStart(){
        return hStart;
    }

    private PointF getMove(){
        return hMove;
    }

    private Paint getPaintText(){
        return hPaintText;
    }

    private float getTextSize(){
        return hSizeText;
    }

    private String gethText(){
        return hText;
    }
}
