package com.example.kittenappscollage.draw;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;

import com.example.kittenappscollage.helpers.rx.ThreadTransformers;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

public class MutableBitmap {

    private static Matrix matrix = new Matrix();

    public static Observable<Object> requestMutable(final Bitmap bitmap, final int scale, final int alpha){
        return Observable.create(new ObservableOnSubscribe<Bitmap>() {
            @Override
            public void subscribe(ObservableEmitter<Bitmap> emitter) throws Exception {
                emitter.onNext(mutable(bitmap,scale,alpha));
                emitter.onComplete();
            }
        }).compose(new ThreadTransformers.Processor<>());
    }

    public static Bitmap mirror(Bitmap bitmap){
        matrix.reset();
        matrix.setScale(-1,1);
        matrix.postTranslate(bitmap.getWidth(),0);
        bitmap = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
        return bitmap;
    }

    private static Bitmap mutable(Bitmap bitmap, int scale,int alpha){
        return alpha(scale(copy(bitmap),scale),alpha);
    }


    private static Bitmap scale(Bitmap bitmap, int val){
        matrix.reset();
        float scale = val/100.0f;
        if(scale<0.01)scale = 0.01f;
        matrix.postScale(scale,scale);
        return Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,false);
    }


    private static Bitmap alpha(Bitmap bitmap, int val){
        if(val==255)return bitmap;
        bitmap.setHasAlpha(true);
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        int[]src = new int[w*h];
        bitmap.getPixels(src,0,w,0,0,w,h);
        swap(src,valAlpha(val));
        bitmap.setPixels(src,0,w,0,0,w,h);
        return bitmap;
    }



    private static Bitmap copy(Bitmap bitmap){
        return bitmap.copy(Bitmap.Config.ARGB_8888,true);
    }


    private static int valAlpha(int val){
        float alpha = 255.0f/100.0f*val;

        return (int) alpha;
    }

    private static void swap(int[]src,int alpha){
        for (int i = 0;i<src.length;i++){
            int col = src[i];
            if(alpha< Color.alpha(col)) {
                src[i] = Color.argb(alpha,
                        Color.red(col),
                        Color.green(col),
                        Color.blue(col));
            }
        }
    }
}
