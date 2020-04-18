package com.example.targetviewnote.veil;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.targetviewnote.TargetView;

public class BodyVeil extends View {

    /*цвет завесы*/
    private int colorBackground;
    /*цель которую видно сквозь завесу*/
    private RectF target;
    /*завеса на весь эеран. Может быть и не на весь*/
    private RectF veil;
    /*форма цели*/
    private int formTarget;
    /*форма завесы*/
    private int formVeil;

    public BodyVeil(Context context) {
        super(context);
        initVars();
    }

    public BodyVeil(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initVars();
    }

    public int getColorBackground() {
        return colorBackground;
    }

    public void setColorBackground(int colorBackground) {
        this.colorBackground = colorBackground;
    }

    public RectF getTarget() {
        return target;
    }

    public void setTarget(RectF target) {
        this.target = target;
//        invalidate();
    }

    public RectF getVeil() {
        return veil;
    }

    public void setVeil(RectF veil) {
        this.veil = veil;
//        invalidate();
    }

    public int getFormTarget() {
        return formTarget;
    }

    public void setFormTarget(int formTarget) {
        this.formTarget = formTarget;

    }

    public int getFormVeil() {
        return formVeil;
    }

    public void setFormVeil(int formVeil) {
        this.formVeil = formVeil;
    }

    protected void initVars(){
        formVeil = TargetView.FORM_RECT;
        formTarget = TargetView.FORM_RECT;
        colorBackground = Color.GRAY;
    }
}
