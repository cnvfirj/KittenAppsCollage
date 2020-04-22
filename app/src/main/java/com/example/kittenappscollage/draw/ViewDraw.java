package com.example.kittenappscollage.draw;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.kittenappscollage.R;
import com.example.kittenappscollage.draw.fragment.ApplyDrawToolsFragmentDraw;
import com.example.kittenappscollage.draw.operations.ApplyOperation;
import com.example.kittenappscollage.draw.operations.Operation;
import com.example.kittenappscollage.draw.operations.OperationCanvas;
import com.example.kittenappscollage.draw.repozitoryDraw.RepDraw;

import static com.example.kittenappscollage.helpers.Massages.LYTE;
import static com.example.kittenappscollage.helpers.Massages.SHOW_MASSAGE;

public class ViewDraw extends View {

    private ApplyOperation vAppOp;

    private ViewDrawHelper vHelper;

    private boolean vNonBlock;

    private boolean vPipette, vSelectColor;

    private Matrix vMatrL, vMatrI, vMatrO;

    private boolean vInfo;

    private OperationCanvas vPreview;

    private ApplyDrawToolsFragmentDraw.Pipette vListenPipette;


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
        vPipette = false;
        vSelectColor = false;
    }

    @Override
    protected void onDraw(Canvas canvas) {

          if (RepDraw.get().isImg()) {
              canvas.drawBitmap(getImg(), getMatrImg(), null);
          }
          if (RepDraw.get().isLyr()) {
              canvas.drawBitmap(getLyr(), getMatrLyr(), null);
          }
          if (RepDraw.get().isOver()) {
              canvas.drawBitmap(getOver(), getMatrOver(), null);
          }
          if (isDraw()) {
              vPreview.canvas(canvas).apply();
          }

          if (vInfo) {
              if (RepDraw.get().isImg())
                  vHelper.drawInfo(canvas, RepDraw.get().getIMat(), "main", Color.RED);
              if (RepDraw.get().isLyr())
                  vHelper.drawInfo(canvas, RepDraw.get().getLMat(), "over", Color.BLUE);
          }

          if (vAppOp.getEvent() != null && vAppOp.getEvent().equals(Operation.Event.LAYERS_CUT) && RepDraw.get().isImg())
              vHelper.drawShadow(canvas, RepDraw.get().getRepers(), getContext().getResources().getColor(R.color.colorShadow));

        super.onDraw(canvas);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(!vPipette) {
            if (vNonBlock) {
                if (RepDraw.get().isImg()) {
                    if(!vPipette) {
                        vAppOp.point(event);
                        if (isDraw()) vPreview.point(event);
                        invalidate();
                    }

                }
            }
        }else {

            if(event.getAction()==MotionEvent.ACTION_DOWN) {
                if (vAppOp.getColorBitmap(event) != 0) {
                    vSelectColor = true;
                } else
                    SHOW_MASSAGE(getContext(), getContext().getResources().getString(R.string.SELECT_POINT_PIPETTE));
            }else if(event.getAction()==MotionEvent.ACTION_UP){
                if (vSelectColor&&vListenPipette != null) {
                    vListenPipette.listen(false);
                }
            }
        }
        return true;
    }

    public void setListPipette(ApplyDrawToolsFragmentDraw.Pipette p){
        vListenPipette = p;
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

    public void applyPipette(boolean p){
        vSelectColor = false;
        vPipette = p;
    }

    public int setEvent(Operation.Event event){
        vAppOp.event(event);
        vPreview.event(event);
        return event.ordinal();
    }

}
