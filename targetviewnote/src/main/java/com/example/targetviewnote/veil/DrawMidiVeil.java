package com.example.targetviewnote.veil;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.Nullable;

import java.io.InputStream;

public class DrawMidiVeil extends DrawVeil {

    private RectF content;

    public DrawMidiVeil(Context context) {
        super(context);
    }

    public DrawMidiVeil(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected RectF midiVeil() {
        if(getTarget().width()*4<getTarget().height()){
//            if(getTarget().width()<getWidth()/4){
                if(getTarget().right+getIndent()<getWidth()/3||getTarget().left-getIndent()>getWidth()/3)
                    return midiVeilInVerticalTarget();
//            }
        }
        return midiVeilInHorisontalTarget();
    }

    @Override
    protected void clipContent(Canvas canvas) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            canvas.clipOutRect(content);
//            canvas.clipPath(transform(content));
        }else {
            canvas.clipPath(transform(content));
        }
    }

    private RectF midiVeilInVerticalTarget(){
        RectF r = new RectF();
        RectF target = addFrameTarget();
        if (target.top > (getHeight() - target.bottom)) {
            /*распологаем вверху*/
            float left = 0;
            if(target.right<getWidth()/2)left = target.left-getIndent();
            float top = (target.bottom + getIndent()) - getWidth();
            if(target.height()>getWidth())top = target.top-getIndent();
            float right = getWidth();
            if(target.right>getWidth()/2)right = target.right+getIndent();
            float bottom = target.bottom+getIndent();
            r.set(left, top, right, bottom);
        } else {
            /*распологаем внизу*/
            float left = 0;
            if(target.right<getWidth()/2)left = target.left-getIndent();
            float top = target.top-getIndent();
            float right = getWidth();
            if(target.right>getWidth()/2)right = target.right+getIndent();
            float bottom = (target.top-getIndent())+getWidth();
            if(target.height()>getWidth())bottom = target.bottom+getIndent();
            r.set(left, top, right, bottom);
        }
        correct(r);
        createVerticalTargetContent(target,r);
        return r;
    }

    private RectF midiVeilInHorisontalTarget(){
        RectF r = new RectF();
        RectF target = addFrameTarget();
        if (target.top > (getHeight() - target.bottom)) {
            r.set(0, (target.bottom + getIndent()) - getWidth(), getWidth(), target.bottom + getIndent());
            /*распологаем вверху*/
        } else {
            /*распологаем внизу*/
            r.set(0, target.top - getIndent(), getWidth(), target.bottom + getWidth());
        }
        correct(r);
        createHorizontalTargetContent(target,r);
        /**/
        return r;
    }

    protected void correct(RectF r){
        if (r.top < 0) r.set(r.left, 0, r.right, r.bottom);
        if (r.bottom > getHeight()) r.set(r.left, r.top, r.right, getHeight());
    }

    public RectF getLocContent(){
        return content;
    }

    protected void createHorizontalTargetContent(RectF target, RectF r){
        RectF content = null;
        if (target.top > (getHeight() - target.bottom)) {
            content = new RectF(r.left+getIndent(),r.top+getIndent(),r.right-getIndent(),target.top-getIndent());
        }else {
            content = new RectF(r.left+getIndent(),target.bottom+getIndent(),r.right-getIndent(),r.bottom-getIndent());
        }
        setLocContent(content);
    }

    protected void createVerticalTargetContent(RectF target, RectF r){
        RectF content = null;
        if(target.right<getWidth()/2){
            content = new RectF(target.right+getIndent(),r.top+getIndent(),r.right-getIndent(),r.bottom-getIndent());
        }else if(target.left>getWidth()/2){
            content = new RectF(r.left+getIndent(),r.top+getIndent(),target.left-getIndent(), r.bottom-getIndent());
        }
        setLocContent(content);
    }

    protected void setLocContent(RectF r){
        content = r;
        if(getListener()!=null&&r!=null)getListener().rect(content);
    }

    public interface InternalListener{
        void rect(RectF r);
        void click(int i);
    }

}
