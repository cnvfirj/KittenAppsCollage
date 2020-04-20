package com.example.targetviewnote.veil;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import com.example.targetviewnote.TargetView;

public class DrawVeil extends ListenVeil {


    private Paint paintVeil;

    private int frame;

    private int contentVeil;

    private int indent;

    public DrawVeil(Context context) {
        super(context);
    }

    public DrawVeil(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawTarget(canvas);
        drawVeil(canvas);
    }

    @Override
    public void setColorBackground(int colorBackground) {
        super.setColorBackground(colorBackground);
        paintVeil.setColor(colorBackground);
    }


    public void setFrame(int frame) {
        this.frame = frame;
    }

    private void drawTarget(Canvas canvas){
        if(getTarget()!=null){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                canvas.clipOutPath(p);
                canvas.clipOutRect(addFrameTarget());
            }
        }
    }

    private void drawVeil(Canvas canvas){
        defineVeil();
        clipContent(canvas);
        canvas.drawRect(getVeil(),paintVeil);
    }

    private void defineVeil(){
        if(contentVeil==TargetView.MIDI_VEIL)setVeil(midiVeil());
    }

    protected void clipContent(Canvas canvas){

    }

    protected int getIndent(){
        return indent;
    }

    protected RectF midiVeil(){
        return null;
    }

    protected RectF addFrameTarget(){
        if(frame>0){
            return new RectF(getTarget().left-frame,getTarget().top-frame,getTarget().right+frame,getTarget().bottom+frame);
        }else return getTarget();
    }

    @Override
    protected void initVars() {
        super.initVars();
        paintVeil = new Paint();
        paintVeil.setColor(getColorBackground());
        frame = 0;
        contentVeil = TargetView.MIDI_VEIL;
        indent = 20;
    }
}
