package com.example.kittenappscollage.draw.addLyrs.loadImage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;


public class LoadForNet extends LoadImage implements StrategyLoadImage {

    public LoadForNet(Context lContext) {
        super(lContext);
    }

    @SuppressLint("CheckResult")
    @Override
    public void way(Object way) {
        CreateBitmap
                .createBitmapRequestInNet(CreateBitmap.createURL((String) way))
                .subscribe(b-> lListener.loadImage((Bitmap)b));
    }
}
