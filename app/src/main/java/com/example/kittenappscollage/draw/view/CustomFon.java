package com.example.kittenappscollage.draw.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

public class CustomFon extends AppCompatImageView {

    public final static int RECT = 1;
    public final static int CELL = 2;

    public final static int BACKGROUND = -555;

    public final static int ELAST = -25;
    public final static int FILL = -15;
    public final static int PAINT = -5;
    public final static int NON = 0;


    private boolean pIsCircle;

    private int pTypeFill;

    private int pColorFill;

    private int pAlphaPrint;

    private int pWidthPrint;

    private int pPresentation;

    private float pPowerDegree;

    private float pStep;

    private Rect pPrintPress;

    private Path pPath;

    private Path pWindow;

    private Paint pPaint;

    private Paint pPresPaint;





    public CustomFon(Context context) {
        super(context);
        var();
    }

    public CustomFon(Context context, AttributeSet attrs) {
        super(context, attrs);
        var();
    }

    public CustomFon(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs,defStyleAttr);
        var();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(pIsCircle){
            canvas.clipPath(pWindow);
        }
        canvas.drawColor(Color.WHITE);
        canvas.drawPath(pPath,pPaint);
        if(pPresentation==FILL){
            canvas.drawCircle(getWidth()/2,getWidth()/2,getWidth()/2,pPresPaint);
        }else if(pPresentation==ELAST){

            canvas.drawRect(0,0,pPrintPress.right,pPrintPress.top,new Paint());
            canvas.drawRect(0,pPrintPress.bottom,getWidth(),getHeight(),new Paint());
            canvas.drawRect(pPrintPress,pPresPaint);
        }else if(pPresentation==PAINT){
            canvas.drawRect(pPrintPress,pPresPaint);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        pStep = (float)getWidth()/pPowerDegree;
        pPrintPress.set(0,getHeight()/2-pWidthPrint/2,getWidth(),getHeight()/2+pWidthPrint/2);

        pPaint.setStrokeWidth(pStep);
        buildPath();
        buildWindow();
    }

    public void setPressColor(int pPressColor) {
        pPresPaint.setColor(pPressColor);
        invalidate();
    }

    public void setPresentation(int pPresentation) {
        this.pPresentation = pPresentation;
            invalidate();
    }

    public void setCircle(boolean circle) {
        pIsCircle = circle;
        invalidate();
    }


    public void setAlphaPrint(int alpha) {
        pAlphaPrint = alpha;
        pPresPaint.setColor(Color.argb(alpha, Color.red(Color.BLACK), Color.green(Color.BLACK), Color.blue(Color.BLACK)));
        invalidate();
    }

    public void setWidthPrint(int width){
        pWidthPrint = width;
        pPrintPress.set(0,getHeight()/2-width/2,getWidth(),getHeight()/2+width/2);
        invalidate();
    }

//    private void euclideanAlgorithm(){
//       pNumberStepX = getWidth();
//       pNumberStepY = getHeight();
//       while (pNumberStepY>0.1&&pNumberStepX>0.1){
//
//           if(pNumberStepX>pNumberStepY){
//               pNumberStepX = pNumberStepX%pNumberStepY;
//           }else {
//               pNumberStepY = pNumberStepY%pNumberStepX;
//           }
//       }
//       pStep = (pNumberStepX+pNumberStepY);
//       if(pStep==1)pStep=12;
//    }


    private void var(){
        pTypeFill = RECT;
        pIsCircle = false;
        pColorFill = Color.LTGRAY;
        pPath = new Path();
        pWindow = new Path();
        pPowerDegree = 15;
        pPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        pPaint.setStyle(Paint.Style.FILL);
        pPaint.setColor(pColorFill);
        pPresentation = BACKGROUND;
        pPresPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        pPresPaint.setStyle(Paint.Style.FILL);
        pPresPaint.setColor(Color.BLACK);
        pPrintPress = new Rect();
    }

    private void buildWindow(){
        pWindow.reset();
        pWindow.addCircle(getWidth()/2,getWidth()/2,getWidth()/2, Path.Direction.CCW);
    }

    private void buildPath(){
        pPath.reset();
        if(pTypeFill==RECT){
            RectF rect = new RectF(0,0,pStep,pStep);

            for (int y=1;y<=getHeight()/pStep+1;y++){
              for (int x=0;x<=getWidth()/pStep;x++){
                  if(y%2==0&&x%2==1){
                      pPath.addRect(rect, Path.Direction.CCW);
                      rect.left = rect.left+pStep*2;
                      rect.right = rect.right+pStep*2;
                  }
                  if(y%2==1&&x%2==0){
                      pPath.addRect(rect, Path.Direction.CCW);
                      rect.left = rect.left+pStep*2;
                      rect.right = rect.right+pStep*2;
                  }
              }
                if(y%2==0){
                    rect.left = 0;
                    rect.right = pStep;
                }else {
                    rect.left = pStep;
                    rect.right = pStep*2;
                }
                rect.top = rect.top+pStep;
                rect.bottom = rect.bottom+pStep;
            }
        }else if(pTypeFill==CELL){

        }
    }
}
