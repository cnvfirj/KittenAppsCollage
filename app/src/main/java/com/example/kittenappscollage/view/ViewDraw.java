package com.example.kittenappscollage.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.kittenappscollage.draw.operations.ApplyOperation;
import com.example.kittenappscollage.draw.operations.Operation;
import com.example.kittenappscollage.packProj.RepDraw;

import static com.example.kittenappscollage.helpers.Massages.MASSAGE;

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
        if(RepDraw.get().isImg()){
            canvas.drawBitmap(getImg(),getMatrImg(),null);
        }
        if(RepDraw.get().isLyr()){
            canvas.drawBitmap(getLyr(),getMatrLyr(),null);
        }
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

    private Bitmap getImg(){
        return RepDraw.get().getImg();
    }

    private Bitmap getLyr(){
        return RepDraw.get().getLyr();
    }

    private Matrix getMatrImg(){
        return RepDraw.get().getIMat().matrix();
    }

    private Matrix getMatrLyr(){
        return RepDraw.get().getLMat().matrix();
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
