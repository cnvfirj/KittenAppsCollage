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
            Path p = new Path();
            p.reset();
                p.addRect(addFrameTarget(), Path.Direction.CCW);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                canvas.clipOutPath(p);
            }
        }
    }

    private void drawVeil(Canvas canvas){
        defineVeil();
        canvas.drawRect(getVeil(),paintVeil);
    }

    private void defineVeil(){
        if(contentVeil==TargetView.MIDI_VEIL)setVeil(midiVeil());
    }

    protected int getIndent(){
        return indent;
    }

    protected RectF midiVeil(){
//        if(getTarget().width()*4<getTarget().height()){
//            if(getTarget().width()<getWidth()/4){
//                if(getTarget().right<getWidth()/3||getTarget().left>getWidth()-getWidth()/3)
//                    return midiVeilInVerticalTarget();
//            }
//        }
//        return midiVeilInHorisontalTarget();
        return null;
    }

//    protected RectF midiVeilInVerticalTarget(){
//        RectF r = new RectF();
//        RectF target = addFrameTarget();
//        if (target.top > (getHeight() - target.bottom)) {
//            /*распологаем вверху*/
//            float left = 0;
//            float top = (target.bottom + indent) - getWidth();
//            if(target.height()>getWidth())top = target.top-indent;
//            float right = getWidth();
//            float bottom = target.bottom+indent;
//            r.set(left, top, right, bottom);
//        } else {
//            /*распологаем внизу*/
//            float left = 0;
//            float top = target.top-indent;
//            float right = getWidth();
//            float bottom = (target.top-indent)+getWidth();
//            if(target.height()>getWidth())bottom = target.bottom+indent;
//            r.set(left, top, right, bottom);
//
//        }
//        if (r.top < 0) r.set(r.left, 0, r.right, r.bottom);
//        if (r.bottom > getHeight()) r.set(r.left, r.top, r.right, getHeight());
//        return r;
//    }
//    private RectF midiVeilInHorisontalTarget(){
//        RectF r = new RectF();
//        RectF target = addFrameTarget();
//            if (target.top > (getHeight() - target.bottom)) {
//                r.set(0, (target.bottom + indent) - getWidth(), getWidth(), target.bottom + indent);
//                /*распологаем вверху*/
//            } else {
//                /*распологаем внизу*/
//                r.set(0, target.top - indent, getWidth(), target.bottom + getWidth());
//            }
//            if (r.top < 0) r.set(r.left, 0, r.right, r.bottom);
//            if (r.bottom > getHeight()) r.set(r.left, r.top, r.right, getHeight());
//            /**/
//        return r;
//    }

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
