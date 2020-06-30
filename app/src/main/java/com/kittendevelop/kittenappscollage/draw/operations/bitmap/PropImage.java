package com.kittendevelop.kittenappscollage.draw.operations.bitmap;

import android.graphics.Bitmap;
import android.graphics.Point;

public class PropImage {

    public static int colorPix(Bitmap b, Point p){
        return b.getPixel(p.x,p.y);
    }
}
