package com.example.targetviewnote.veil;

import android.content.Context;
import android.graphics.RectF;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

public class DrawMidiVeil extends DrawVeil {

    public DrawMidiVeil(Context context) {
        super(context);
    }

    public DrawMidiVeil(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected RectF midiVeil() {
        if(getTarget().width()*4<getTarget().height()){
            if(getTarget().width()<getWidth()/4){
                if(getTarget().right<getWidth()/3||getTarget().left>getWidth()-getWidth()/3)
                    return midiVeilInVerticalTarget();
            }
        }
        return midiVeilInHorisontalTarget();
    }

    private RectF midiVeilInVerticalTarget(){
        RectF r = new RectF();
        RectF target = addFrameTarget();
        if (target.top > (getHeight() - target.bottom)) {
            /*распологаем вверху*/
            float left = 0;
            float top = (target.bottom + getIndent()) - getWidth();
            if(target.height()>getWidth())top = target.top-getIndent();
            float right = getWidth();
            float bottom = target.bottom+getIndent();
            r.set(left, top, right, bottom);
        } else {
            /*распологаем внизу*/
            float left = 0;
            float top = target.top-getIndent();
            float right = getWidth();
            float bottom = (target.top-getIndent())+getWidth();
            if(target.height()>getWidth())bottom = target.bottom+getIndent();
            r.set(left, top, right, bottom);

        }
        if (r.top < 0) r.set(r.left, 0, r.right, r.bottom);
        if (r.bottom > getHeight()) r.set(r.left, r.top, r.right, getHeight());
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
        if (r.top < 0) r.set(r.left, 0, r.right, r.bottom);
        if (r.bottom > getHeight()) r.set(r.left, r.top, r.right, getHeight());
        /**/
        return r;
    }
}
