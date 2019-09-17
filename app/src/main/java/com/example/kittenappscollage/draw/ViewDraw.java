package com.example.kittenappscollage.draw;

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
import com.example.kittenappscollage.draw.RepDraw;

public class ViewDraw extends View {

    private ApplyOperation vAppOp;

    private boolean vNonBlock;

    private Matrix vMatrL, vMatrI;

    public ViewDraw(Context context) {
        super(context);
        vAppOp = new ApplyOperation();
        vNonBlock = true;
        vMatrI = new Matrix();
        vMatrL = new Matrix();
         }

    public ViewDraw(Context context, AttributeSet attrs) {
        super(context, attrs);
        vAppOp = new ApplyOperation();
        vNonBlock = true;
        vMatrI = new Matrix();
        vMatrL = new Matrix();
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
            if(RepDraw.get().isImg()){
                vAppOp.point(event);
            }
            invalidate();
        }
        return true;
    }



    private Bitmap getImg(){
        return RepDraw.get().getImg();
    }

    private Bitmap getLyr(){
        return RepDraw.get().getLyr();
    }

    private Matrix getMatrImg(){
        return RepDraw.get().getIMat().matrix(vMatrI);
    }

    private Matrix getMatrLyr(){
        return RepDraw.get().getLMat().matrix(vMatrL);
    }

    public void groupLyrs(boolean gr){
        vAppOp.grouping(gr);
    }

    public void nonBlockTouch(boolean nonBlock){
        vNonBlock = nonBlock;
    }

    public void setEvent(Operation.Event event){
        vAppOp.event(event);
    }
}
