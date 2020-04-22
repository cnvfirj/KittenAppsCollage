package com.example.targetviewnote.veil;

import android.content.Context;
import android.graphics.RectF;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

public class DrawMiniVeil extends DrawMidiVeil {

    public DrawMiniVeil(Context context) {
        super(context);
    }

    public DrawMiniVeil(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected RectF miniVeil() {
        if(getTarget().width()*2<getTarget().height()){
//            if(getTarget().width()<getWidth()/3){
                if(getTarget().right+getIndent()<getWidth()/2||getTarget().left-getIndent()>getWidth()/2)
                    return miniVeilInVerticalTarget();
//            }
        }
        return miniVeilInHorisontalTarget();
    }

    private RectF miniVeilInVerticalTarget(){
        RectF r = new RectF();
        RectF target = addFrameTarget();
        if (target.top > (getHeight() - target.bottom)) {
            /*распологаем вверху*/
            float left = target.left-getIndent();
            if(target.left>getWidth()/2)left = target.left-getIndent()-getWidth()/2;
            float top = (target.top - getIndent()) - getWidth()/2;
            if(target.height()>getWidth()/2)top = target.top-getIndent();
            float right = target.right+getIndent()+getWidth()/2;
            if(target.left>getWidth()/2)right = target.right+getIndent();
            float bottom = target.bottom+getIndent();
            r.set(left, top, right, bottom);
        } else {
            /*распологаем внизу*/
            float left = target.left-getIndent();
            if(target.left>getWidth()/2)left = target.left-getIndent()-getWidth()/2;
            float top = target.top-getIndent();
            float right = target.right+getIndent()+getWidth()/2;
            if(target.left>getWidth()/2)right = target.right+getIndent();
            float bottom = (target.top-getIndent())+getWidth();
            if(target.height()>getWidth()/2)bottom = target.bottom+getIndent();
            r.set(left, top, right, bottom);
        }
        correct(r);
        createVerticalTargetContent(target,r);
        return r;
    }

    private RectF miniVeilInHorisontalTarget(){
        RectF r = new RectF();
        RectF target = addFrameTarget();
        if (target.top > (getHeight() - target.bottom)) {
            r.set(0, (target.bottom + getIndent()) - getWidth()/2, getWidth(), target.bottom + getIndent());
            /*распологаем вверху*/
        } else {
            /*распологаем внизу*/
            r.set(0, target.top - getIndent(), getWidth(), target.bottom + getWidth()/2);
        }
        correct(r);
        createHorizontalTargetContent(target,r);
        return r;
    }
}
