package com.example.kittenappscollage.collect.reviewImage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.mutmatrix.DeformMat;

public class ViewReview extends View {

    private DeformMat mat;

    private Bitmap bitmap;

    private Matrix matrix;

    public ViewReview(Context context) {
        super(context);
        initVar();
    }

    public ViewReview(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initVar();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(test())canvas.drawBitmap(bitmap,mat.matrix(matrix),null);
        else canvas.drawColor(Color.RED);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    public void setBitmap(Bitmap b){
        zeroing(bitmap);
        bitmap = b;
        mat.reset().bitmap(new PointF(b.getWidth(),b.getHeight())).view(new PointF(getWidth(),getHeight()));
        mat.command(DeformMat.Command.SCALE_ADAPT);
        invalidate();
    }

    private boolean test(){
        return bitmap!=null&&!bitmap.isRecycled();
    }

    private void zeroing(Bitmap b){
        if(b!=null&&!b.isRecycled()){
            b.recycle();
        }
    }

    private void initVar(){
        mat = new DeformMat(getContext());
        matrix = new Matrix();
    }
}
