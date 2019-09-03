package com.example.kittenappscollage.draw.addLyrs.loadImage;

import android.content.Context;
import android.graphics.Bitmap;


public class LoadForFile extends LoadImage implements StrategyLoadImage {

    public LoadForFile(Context lContext) {
        super(lContext);
    }

    @Override
    public void way(Object way) {
        CreateBitmap.createRequestInFile((String) way).subscribe(b-> lListener.loadImage((Bitmap)b));
    }
}
