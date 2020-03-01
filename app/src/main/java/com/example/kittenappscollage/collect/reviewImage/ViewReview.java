package com.example.kittenappscollage.collect.reviewImage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.kittenappscollage.helpers.rx.ThreadTransformers;
import com.example.mutmatrix.DeformMat;
import com.example.mutmatrix.actions.Deform;

import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;

import static com.example.kittenappscollage.helpers.Massages.LYTE;

public class ViewReview extends View {

    private DeformMat mat;

    private Bitmap bitmap;

    private Matrix matrix;

    private boolean block;

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
        if(test()){
            canvas.drawBitmap(bitmap,mat.matrix(matrix),null);
        }
        else canvas.drawColor(Color.RED);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(!block){
            mat.event(event);
            invalidate();
            return true;
        }else return super.onTouchEvent(event);
    }

    public void setUriBitmap(Uri img){
        zeroing(bitmap);
        loadImg(img);

    }

    @SuppressLint("CheckResult")
    private void loadImg(Uri uri){
        Observable.create((ObservableOnSubscribe<Bitmap>) emitter -> {
            emitter.onNext(bitmap(uri));
            emitter.onComplete();
        }).compose(new ThreadTransformers.InputOutput<>())
                .subscribe(b -> {
                    bitmap = b;
                    if(test()) {
                        mat.reset().bitmap(new PointF(bitmap.getWidth(), bitmap.getHeight())).view(new PointF(getWidth(), getHeight()));
                        locBitmap(DeformMat.Command.SCALE_ADAPT);
                        block = false;
                        invalidate();
                    }
                });
    }

    private void locBitmap(DeformMat.Command command){
        mat.command(command);
        mat.event(MotionEvent.obtain(1l,1l,MotionEvent.ACTION_DOWN,0,0,0));
        mat.command(DeformMat.Command.TRANSLATE);
        mat.event(MotionEvent.obtain(1l,1l,MotionEvent.ACTION_DOWN,0,0,0));
        float scaleH = mat.getRepository().getScale()*bitmap.getHeight();
        float scaleW = mat.getRepository().getScale()*bitmap.getWidth();
            float stepH = (getHeight()-scaleH)/2;
            float stepW = (getWidth()-scaleW)/2;
            mat.event(MotionEvent.obtain(1l,1l,MotionEvent.ACTION_MOVE,stepW,stepH,0));

    }

    private Bitmap bitmap(Uri uri) throws IOException {
        return MediaStore.Images.Media.getBitmap (getContext().getContentResolver (), uri);
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
        block = true;
    }

    public void resetImg(){
        if(bitmap!=null&&!bitmap.isRecycled())bitmap.recycle();
    }
}
