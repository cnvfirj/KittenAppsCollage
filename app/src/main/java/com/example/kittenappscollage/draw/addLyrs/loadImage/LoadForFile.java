package com.example.kittenappscollage.draw.addLyrs.loadImage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

import com.example.kittenappscollage.helpers.rx.ThreadTransformers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

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
//            emitter.onNext(MediaStore.Images.Media.getBitmap (lContext.getContentResolver (), uri));
            emitter.onNext(bitmap(uri));
            emitter.onComplete();
        }).compose(new ThreadTransformers.InputOutput<>())
                .subscribe(b -> {
                    lListener.loadImage(b);
                });
    }

    private Bitmap bitmap(Uri uri) throws IOException {
        Bitmap bitmap = null;
        try {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            InputStream is = lContext.getContentResolver().openInputStream(uri);
            BitmapFactory.decodeStream(is, null, options);
            if (is != null) {
                is.close();
            }
            is = lContext.getContentResolver().openInputStream(uri);

            options.inSampleSize =
                    calculateInSampleSize(options,2000,2000);

            options.inJustDecodeBounds = false;

            bitmap = BitmapFactory.decodeStream(is, null, options);
            if (is != null) {
                is.close();
            }
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        }
//        return MediaStore.Images.Media.getBitmap (getContext().getContentResolver (), uri);
        return bitmap;
    }

    private int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        float inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final float halfHeight = height / 1.5f;
            final float halfWidth = width / 1.5f;
            while ((halfHeight / inSampleSize) >= reqHeight || (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 1.5f;
            }
        }
        return Math.round(inSampleSize);
    }

    /*Bitmap thumbnail =
        getApplicationContext().getContentResolver().loadThumbnail(
        content-uri, new Size(640, 480), null);*/
}
