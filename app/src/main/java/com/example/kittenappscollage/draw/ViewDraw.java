package com.example.kittenappscollage.draw;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.mutablebitmap.DeformMat;

public class ViewDraw extends View {

    public ViewDraw(Context context) {
        super(context);
    }

    public ViewDraw(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    public void applyMutable(){
        invalidate();
    }
}
