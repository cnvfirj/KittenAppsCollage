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

    private int colorDimming;

    public DrawVeil(Context context) {
        super(context);
    }

    public DrawVeil(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(getTarget()!=null) {
            drawTarget(canvas);
            drawVeil(canvas);
            drawDimming(canvas);
        }
    }

    @Override
    public void setColorVeil(int colorBackground) {
        super.setColorVeil(colorBackground);
        paintVeil.setColor(colorBackground);
    }

    public int getColorDimming() {
        return colorDimming;
    }

    public void setColorDimming(int colorDimming) {
        this.colorDimming = colorDimming;
    }

    public int getContentVeil() {
        return contentVeil;
    }

    public void setContentVeil(int contentVeil) {
        this.contentVeil = contentVeil;
    }

    public void setFrame(int frame) {
        this.frame = frame;
    }

    private void drawTarget(Canvas canvas){
        if(getTarget()!=null){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                canvas.clipOutRect(addFrameTarget());
//                canvas.clipPath(transform(addFrameTarget()));
            }else {
                canvas.clipPath(transform(addFrameTarget()));
            }
        }
    }

    private void drawVeil(Canvas canvas){
        defineVeil();
        clipContent(canvas);
        canvas.drawRect(getVeil(),paintVeil);
    }

    private void drawDimming(Canvas canvas){
        if(colorDimming!=0){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                canvas.clipOutRect(getVeil());
            }else {
                canvas.clipPath(transform(getVeil()));
            }
            canvas.drawColor(colorDimming);
        }
    }

    protected Path transform(RectF r){
        Path p = new Path();
        p.moveTo(0,0);
        p.lineTo(getWidth(),0);
        p.lineTo(getWidth(),getHeight());
        p.lineTo(0,getHeight());
        p.lineTo(0,r.bottom);
        p.lineTo(r.right,r.bottom);
        p.lineTo(r.right,r.top);
        p.lineTo(r.left,r.top);
        p.lineTo(r.left,r.bottom);
        p.lineTo(0,r.bottom);
        p.close();
        return p;
    }

    private void defineVeil(){
        if(contentVeil==TargetView.MIDI_VEIL)setVeil(midiVeil());
        else if(contentVeil==TargetView.MINI_VEIL)setVeil(miniVeil());
    }

    protected void clipContent(Canvas canvas){

    }

    protected int getIndent(){
        return indent;
    }

    protected RectF midiVeil(){
        return null;
    }

    protected RectF miniVeil(){
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
