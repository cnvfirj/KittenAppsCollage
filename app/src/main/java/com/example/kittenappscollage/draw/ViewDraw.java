package com.example.kittenappscollage.draw;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.kittenappscollage.R;
import com.example.kittenappscollage.draw.operations.ApplyOperation;
import com.example.kittenappscollage.draw.operations.Operation;
import com.example.kittenappscollage.draw.operations.OperationCanvas;
import com.example.kittenappscollage.draw.repozitoryDraw.RepDraw;

public class ViewDraw extends View {

    private ApplyOperation vAppOp;

    private ViewDrawHelper vHelper;

    private boolean vNonBlock;

    private Matrix vMatrL, vMatrI, vMatrO;

    private boolean vInfo;

    private OperationCanvas vPreview;


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
        vMatrO = new Matrix();
        vPreview = new OperationCanvas().preview(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        if(RepDraw.get().isImg()){
            canvas.drawBitmap(getImg(),getMatrImg(),null);
        }
        if(RepDraw.get().isLyr()){
            canvas.drawBitmap(getLyr(),getMatrLyr(),null);
        }
        if(RepDraw.get().isOver()){
            canvas.drawBitmap(getOver(), getMatrOver(),null);
        }
        if(isDraw()){
            vPreview.canvas(canvas).apply();
        }

        if(vInfo){
            if(RepDraw.get().isImg())vHelper.drawInfo(canvas,RepDraw.get().getIMat(),"img",Color.RED);
            if(RepDraw.get().isLyr())vHelper.drawInfo(canvas,RepDraw.get().getLMat(),"over",Color.BLUE);
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
                if(isDraw())vPreview.point(event);
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

    private Bitmap getOver(){
        return RepDraw.get().getOverlay();
    }

    private Matrix getMatrImg(){

        return RepDraw.get().getIMat().matrix(vMatrI);
    }

    private Matrix getMatrLyr(){
        return RepDraw.get().getLMat().matrix(vMatrL);
    }

    private Matrix getMatrOver(){
        return RepDraw.get().getOMat().matrix(vMatrO);
    }

    private boolean isDraw(){
        return vAppOp.getEvent().equals(Operation.Event.DRAW_SPOT)||
                vAppOp.getEvent().equals(Operation.Event.DRAW_TEXT);
    }

    public void groupLyrs(boolean gr){
        vAppOp.grouping(gr);
    }

    public void nonBlockTouch(boolean nonBlock){
        vNonBlock = nonBlock;
    }

    public int setEvent(Operation.Event event){
        vAppOp.event(event);
        vPreview.event(event);
        return event.ordinal();
    }

}
