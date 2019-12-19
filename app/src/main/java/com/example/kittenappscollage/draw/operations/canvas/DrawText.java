package com.example.kittenappscollage.draw.operations.canvas;

import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.PointF;

import com.example.kittenappscollage.draw.repozitoryDraw.RepDraw;

import static com.example.kittenappscollage.helpers.Massages.LYTE;

public class DrawText extends DrawSpot {

//    private PointF hStart, hMove;

    private Paint hPaintText;

    private float hSizeText;

    public DrawText() {
        hPaintText = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    protected void start(PointF s) {
        super.start(s);
        initParams();
    }

    @Override
    public void draw() {
        if (getCommand().equals(Command.TEXT)) {
            drawText();
        }else super.draw();
    }

    private void drawText(){
        if(!getPath().isEmpty()) {
            getCanvas().drawTextOnPath(RepDraw.get().getText(), getPath(), 0, getTextSize() / 2, getPaintText());
            applyMutable();
        }
    }

    private void initParams(){
        hSizeText = RepDraw.get().getWidth()*1.3f;
        getPaintText().setColor(RepDraw.get().getColor());
        getPaintText().setTextSize(getTextSize());
        getPaintText().setStrokeWidth(2);
        getPaintText().setPathEffect(new CornerPathEffect(RepDraw.get().getWidth()*3));
        getPaintText().setStyle(RepDraw.get().isTextFill()? Paint.Style.FILL_AND_STROKE: Paint.Style.STROKE);
        getPaintText().setTextSkewX(RepDraw.get().isTextFill()?-0.6f:0);

    }

    private Paint getPaintText(){
        return hPaintText;
    }

    private float getTextSize(){
        return hSizeText;
    }

}
