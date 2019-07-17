package com.example.kittenappscollage.draw.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.kittenappscollage.draw.view.operations.ApplyOperation;
import com.example.kittenappscollage.draw.view.operations.Operation;

public class ViewDraw extends View {

    private ApplyOperation vApplyOperation;

    private boolean vNonBlock;

    public ViewDraw(Context context) {
        super(context);
        vApplyOperation = new ApplyOperation();
        vNonBlock = true;
    }

    public ViewDraw(Context context, AttributeSet attrs) {
        super(context, attrs);
        vApplyOperation = new ApplyOperation();
        vNonBlock = true;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(vNonBlock) {
            vApplyOperation.event(event);
            invalidate();
        }
        return super.onTouchEvent(event);
    }

    public void applyMutable(){
        invalidate();
    }

    public void command(Operation.Command command){
        vApplyOperation.command(command);
    }

    public void nonBlockTouch(boolean nonBlock){
        vNonBlock = nonBlock;
    }
}
