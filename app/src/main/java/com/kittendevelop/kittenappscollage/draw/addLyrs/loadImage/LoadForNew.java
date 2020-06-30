package com.kittendevelop.kittenappscollage.draw.addLyrs.loadImage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;


public class LoadForNew extends LoadImage implements StrategyLoadImage {

    public LoadForNew(Context lContext) {
        super(lContext);
    }

    @Override
    public void way(Object way) {
        String[] par = ((String)way).split("_");
        Point size = new Point(Integer.valueOf(par[0]), Integer.valueOf(par[1]));
        int color = Integer.valueOf(par[2]);
        Bitmap image = CreateBitmap.createNew(size);
        Canvas c = new Canvas(image);
        c.drawColor(color);
        lListener.loadImage(image);
    }
}
