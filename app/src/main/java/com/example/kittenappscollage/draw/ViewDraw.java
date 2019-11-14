package com.example.kittenappscollage.draw;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.kittenappscollage.R;
import com.example.kittenappscollage.draw.operations.ApplyOperation;
import com.example.kittenappscollage.draw.operations.Operation;
import com.example.kittenappscollage.draw.repozitoryDraw.RepDraw;

import static com.example.kittenappscollage.helpers.Massages.LYTE;

public class ViewDraw extends View {

    private ApplyOperation vAppOp;

    private ViewDrawHelper vHelper;

    private boolean vNonBlock;

    private Matrix vMatrL, vMatrI;

    private Paint paint;

    private boolean vInfo;

    public ViewDraw(Context context) {
        super(context);
        initVars();

         }

    public ViewDraw(Context context, AttributeSet attrs) {
        super(context, attrs);
        initVars();
    }

    private void initVars(){
        vAppOp = new ApplyOperation();
        vHelper = new ViewDrawHelper();
        vNonBlock = true;
        vMatrI = new Matrix();
        vMatrL = new Matrix();
        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(RepDraw.get().isImg()){
            canvas.drawBitmap(getImg(),getMatrImg(),null);
        }
        if(RepDraw.get().isLyr()){
            canvas.drawBitmap(getLyr(),getMatrLyr(),null);
        }

        if(vInfo){
            if(RepDraw.get().isImg())vHelper.drawInfo(canvas,RepDraw.get().getIMat(),"img",Color.RED);
            if(RepDraw.get().isLyr())vHelper.drawInfo(canvas,RepDraw.get().getLMat(),"lyr",Color.BLUE);
        }
      if(vAppOp.getEvent()!=null&&vAppOp.getEvent().equals(Operation.Event.LAYERS_CUT)&&RepDraw.get().isImg())
        vHelper.drawShadow(canvas,RepDraw.get().getRepers(),getContext().getResources().getColor(R.color.colorShadow));
        super.onDraw(canvas);
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(vNonBlock) {
            if(RepDraw.get().isImg()){
                vAppOp.point(event);
                invalidate();
            }

        }
        return true;
    }


    public void changeInfo(boolean info){
        vInfo = info;
        invalidate();
    }

    public void doneCut(){
          vAppOp.doneCut();
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
