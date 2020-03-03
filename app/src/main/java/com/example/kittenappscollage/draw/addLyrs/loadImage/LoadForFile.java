package com.example.kittenappscollage.draw.addLyrs.loadImage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.net.Uri;
import android.provider.MediaStore;

import com.example.kittenappscollage.helpers.rx.ThreadTransformers;
import com.example.mutmatrix.DeformMat;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;


public class LoadForFile extends LoadImage implements StrategyLoadImage {

    public LoadForFile(Context lContext) {
        super(lContext);
    }

    @SuppressLint("CheckResult")
    @Override
    public void way(Object way) {
        loadImg(Uri.parse((String)way));
    }

    @SuppressLint("CheckResult")
    private void loadImg(Uri uri){
        Observable.create((ObservableOnSubscribe<Bitmap>) emitter -> {
            emitter.onNext(MediaStore.Images.Media.getBitmap (lContext.getContentResolver (), uri));
            emitter.onComplete();
        }).compose(new ThreadTransformers.InputOutput<>())
                .subscribe(b -> {
                    lListener.loadImage(b);
                });
    }

    /*Bitmap thumbnail =
        getApplicationContext().getContentResolver().loadThumbnail(
        content-uri, new Size(640, 480), null);*/
}
