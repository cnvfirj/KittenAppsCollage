package com.example.kittenappscollage.draw;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.kittenappscollage.draw.operations.ApplyOperation;
import com.example.kittenappscollage.draw.operations.Operation;
import com.example.kittenappscollage.draw.RepDraw;
import com.example.mutablebitmap.DeformMat;

import static com.example.kittenappscollage.helpers.Massages.LYTE;

public class ViewDraw extends View implements RepDraw.Mutable {

    private ApplyOperation vAppOp;

    private boolean vNonBlock;

    private Matrix vMatrL, vMatrI;

    private Paint paint;

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
        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Path path = new Path();
        path.reset();
        if(RepDraw.get().isImg()){
            canvas.drawBitmap(getImg(),getMatrImg(),null);
            paint.setColor(Color.RED);
            PointF[]p = RepDraw.get().getIMat().muteDeformLoc(DeformMat.Coordinates.DISPLAY_ROTATE_DEFORM);
            path.moveTo(p[0].x,p[0].y);
            path.lineTo(p[1].x,p[1].y);
            path.lineTo(p[2].x,p[2].y);
            path.lineTo(p[3].x,p[3].y);
            path.close();
            canvas.drawPath(path,paint);

        }
        if(RepDraw.get().isLyr()){
            canvas.drawBitmap(getLyr(),getMatrLyr(),null);
            paint.setColor(Color.GREEN);
            PointF[]p = RepDraw.get().getLMat().muteDeformLoc(DeformMat.Coordinates.DISPLAY_ROTATE_DEFORM);
            path.moveTo(p[0].x,p[0].y);
            path.lineTo(p[1].x,p[1].y);
            path.lineTo(p[2].x,p[2].y);
            path.lineTo(p[3].x,p[3].y);
            path.close();
            canvas.drawPath(path,paint);
        }
        if(RepDraw.get().getRepers()!=null){
            if(RepDraw.get().isCorrectRepers()){
              for(int i=0;i<RepDraw.get().getRepers().length;i++) {
                  if (RepDraw.get().getRepers()[i] != null) {
                      canvas.drawCircle(RepDraw.get().getRepers()[i].x, RepDraw.get().getRepers()[i].y,
                        RepDraw.get().getView().x / 40, paint); }
              }
        }else {
                path.reset();
                path.moveTo(RepDraw.get().getRepers()[0].x,RepDraw.get().getRepers()[0].y);
                path.lineTo(RepDraw.get().getRepers()[1].x,RepDraw.get().getRepers()[1].y);
                path.lineTo(RepDraw.get().getRepers()[2].x,RepDraw.get().getRepers()[2].y);
                path.lineTo(RepDraw.get().getRepers()[3].x,RepDraw.get().getRepers()[3].y);
                path.close();
                canvas.drawPath(path,paint);

            }
        }
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

    @Override
    public void mutLyr(boolean is) {

    }

    @Override
    public void mutAll(boolean is) {

    }

    @Override
    public void mutImg(boolean is) {

        invalidate();
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
