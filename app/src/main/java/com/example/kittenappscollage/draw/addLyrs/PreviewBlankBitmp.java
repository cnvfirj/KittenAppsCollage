package com.example.kittenappscollage.draw.addLyrs;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Size;
import android.util.SizeF;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.kittenappscollage.view.CustomFon;
import com.madrapps.pikolo.HSLColorPicker;

import static com.example.kittenappscollage.helpers.Massages.LYTE;
import static com.example.kittenappscollage.helpers.Massages.MASSAGE;


public class PreviewBlankBitmp extends CustomFon {


    private boolean cCorrect;

    private int cColorFon;

    private Size cSize;

    private Paint cPaintBitmap;



    public PreviewBlankBitmp(Context context) {
        super(context);
        initVars();
    }

    public PreviewBlankBitmp(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initVars();
    }

    private void initVars(){
        cCorrect = false;
        cColorFon = Color.WHITE;
        cPaintBitmap = new Paint();
        cPaintBitmap.setStyle(Paint.Style.FILL);
        cPaintBitmap.setColor(cColorFon);
        cPaintBitmap.setStrokeWidth(1);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(rect(),cPaintBitmap);
    }

    private RectF rect(){
        if(cSize==null||cSize.getHeight()==0||cSize.getWidth()==0){
            createSize();
        }
           return new RectF(location(new SizeF(getWidth(),getHeight()),new SizeF(cSize.getWidth(),cSize.getHeight())));

    }

    private void createSize(){
        cSize = new Size(getHeight(),getWidth());
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

    public void size(Size size){
        if(size!=null) {
            cSize = size;
            cCorrect = true;
        }else cCorrect = false;
        invalidate();
    }

    public void fon(int color){
        cColorFon = color;
        cPaintBitmap.setColor(color);
        invalidate();
    }

    public int getColorFon() {
        return cColorFon;
    }

    public Size getSize() {
        if(cSize==null||cSize.getHeight()==0||cSize.getWidth()==0){
            createSize();

        }
        return cSize;
    }

}
