package com.example.targetviewnote.veil;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.Rect;
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

    private RectF veil;

    public BodyVeil(Context context) {
        super(context);
        initVars();
    }

    public BodyVeil(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initVars();
    }

    public RectF getVeil() {
        return veil;
    }

    public void setVeil(RectF veil) {
        this.veil = veil;
    }

    public int getColorBackground() {
        return colorBackground;
    }

    public void setColorVeil(int colorBackground) {
        this.colorBackground = colorBackground;
    }

    public RectF getTarget() {
        return target;
    }

    public void setTarget(int[]t) {
        if(t!=null) target = new RectF(t[0],t[1],t[2],t[3]);
    }

    protected void initVars(){
        colorBackground = Color.GRAY;
    }
}
