package com.example.kittenappscollage.draw.addLyrs.loadImage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;


public class LoadForFile extends LoadImage implements StrategyLoadImage {

    public LoadForFile(Context lContext) {
        super(lContext);
    }

    @SuppressLint("CheckResult")
    @Override
    public void way(Object way) {
        CreateBitmap.createRequestInFile((String) way).subscribe(b-> lListener.loadImage((Bitmap)b));
    }
}
