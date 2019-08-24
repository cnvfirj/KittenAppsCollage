package com.example.kittenappscollage.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
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
    }

    public void presentBitmap(Bitmap bitmap){
       pPresent = bitmap;
    }

    public Bitmap getPresent(){
        return pPresent;
    }

    public void clear(){

    }
}
