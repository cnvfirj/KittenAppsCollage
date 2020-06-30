package com.kittendevelop.kittenappscollage.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.SizeF;
import android.view.View;

public class PresentLyr extends View {


    private Bitmap pPresent;


    public PresentLyr(Context context) {
        super(context);
    }

    public PresentLyr(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
       if(pPresent!=null&&!pPresent.isRecycled()){
           canvas.drawBitmap(pPresent,rectImage(pPresent),
                   location(createSize(canvas.getWidth(),canvas.getHeight()), createSize(rectImage(pPresent).width(),rectImage(pPresent).height())),null);
       }
    }

    private RectF location(SizeF c, SizeF r){
        float scale = c.getWidth()/r.getWidth()<c.getHeight()/r.getHeight()
                ? c.getWidth()/r.getWidth()
                :c.getHeight()/r.getHeight();
        SizeF n = new SizeF(r.getWidth()*scale,r.getHeight()*scale);
        RectF rect = new RectF(c.getWidth()/2-n.getWidth()/2, c.getHeight()/2-n.getHeight()/2,
                c.getWidth()/2+n.getWidth()/2, c.getHeight()/2+n.getHeight()/2);
        return rect;
    }

    private Rect rectImage(Bitmap image){
        return new Rect(0,0,image.getWidth(),image.getHeight());
    }

    private SizeF createSize(int w, int h){
        return new SizeF(w,h);
    }


    public void presentBitmap(Bitmap bitmap){
       pPresent = bitmap;
       invalidate();
    }

    public Bitmap getPresent(){
        return pPresent;
    }

    public void clear(){
        if(pPresent!=null&&!pPresent.isRecycled()){
            pPresent.recycle();
        }
    }
}
