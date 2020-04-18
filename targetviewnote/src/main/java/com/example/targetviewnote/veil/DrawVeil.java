package com.example.targetviewnote.veil;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import com.example.targetviewnote.TargetView;

public class DrawVeil extends ListenVeil {

    private Paint paintTarget;

    private Paint paintVeil;

    private int formVeil;

    private int formTarget;

    public DrawVeil(Context context) {
        super(context);
    }

    public DrawVeil(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setFormTarget(int form){
        formTarget = form;
    }

    public void setFormVeil(int form){
        formVeil = form;
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawTarget(canvas);
        drawVeil(canvas);
    }

    private void drawTarget(Canvas canvas){
        if(getTarget()!=null)drawRect(canvas,formTarget,true);
    }

    private void drawVeil(Canvas canvas){
        if(getVeil()==null)canvas.drawColor(getColorBackground());
        else drawRect(canvas,formVeil,false);

    }

    private void drawRect(Canvas canvas, int i, boolean target){
        if(i==TargetView.FORM_RECT){
            canvas.drawRect(getVeil(),target?paintTarget:paintVeil);
        }else if(i==TargetView.FORM_OVAL){
            canvas.drawOval(getVeil(),target?paintTarget:paintVeil);
        }else if(i==TargetView.FORM_CIRC){
            final float w = getVeil().right-getVeil().left;
            final float h = getVeil().bottom-getVeil().top;
            final float radius = w>h?w/2:h/2;
            canvas.drawCircle(getVeil().centerX(),getVeil().centerY(),radius,target?paintTarget:paintVeil);
        }
    }

    @Override
    protected void initVars() {
        super.initVars();
        paintTarget = new Paint();
        paintTarget.setColor(Color.TRANSPARENT);
        paintTarget.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        paintVeil = new Paint();
        paintVeil.setColor(getColorBackground());
        formTarget = TargetView.FORM_RECT;
        formVeil = TargetView.FORM_RECT;
    }
}
