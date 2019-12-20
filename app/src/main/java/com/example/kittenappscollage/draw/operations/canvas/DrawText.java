package com.example.kittenappscollage.draw.operations.canvas;

import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.PointF;

import com.example.kittenappscollage.draw.repozitoryDraw.RepDraw;

import static com.example.kittenappscollage.helpers.Massages.LYTE;

public class DrawText extends DrawSpot {

    private PointF hStart, hMove;

    private Paint hPaintText;

    private float hSizeText;

    private String hText;

    private float hLengthPath;

    public DrawText() {
        hPaintText = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    protected void start(PointF s) {
        super.start(s);
        initParams();
        hStart = new PointF(s.x,s.y);
        hLengthPath = 0;
    }

    @Override
    protected void move(PointF m) {
        super.move(m);
        if(hMove!=null){
            hStart = new PointF(hMove.x,hMove.y);
        }
        hMove = new PointF(m.x,m.y);
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
            getCanvas().drawTextOnPath(hText, getPath(), 0, -5, getPaintText());
            if(isPreview())getCanvas().drawPath(getPath(),getPaintSpot());
            applyMutable();
            if(!isPreview())hText = "";
        }
    }

    private void initParams(){
        hText = RepDraw.get().getText();
        hSizeText = RepDraw.get().getWidth()*1.3f;
        getPaintText().setColor(RepDraw.get().getColor());
        getPaintText().setTextSize(getTextSize());
        getPaintText().setStrokeWidth(2);
        getPaintText().setPathEffect(new CornerPathEffect(RepDraw.get().getWidth()*3));
        getPaintText().setStyle(RepDraw.get().isTextFill()? Paint.Style.FILL_AND_STROKE: Paint.Style.STROKE);
        getPaintText().setTextSkewX(RepDraw.get().isTextFill()?-0.6f:0);

        getPaintSpot().setColor(inversion(RepDraw.get().getColor()));
        getPaintSpot().setStyle(Paint.Style.STROKE);
        getPaintSpot().setStrokeWidth(2);
    }

    private Paint getPaintText(){
        return hPaintText;
    }

    private float getTextSize(){
        return hSizeText;
    }

    private PointF vector(){
        return new PointF(hMove.x-hStart.x,hMove.y-hStart.y);
    }

    private float lengthVector(PointF vector){
        return (float) Math.sqrt(vector.x*vector.x+vector.y*vector.y);
    }

    private int inversion(int color){
        return Color.rgb(255-Color.red(color),255-Color.green(color),255-Color.blue(color));
    }

}
