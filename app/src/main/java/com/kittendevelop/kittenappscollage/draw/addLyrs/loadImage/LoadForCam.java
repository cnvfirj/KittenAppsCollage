package com.kittendevelop.kittenappscollage.draw.addLyrs.loadImage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;



public class LoadForCam extends LoadImage implements StrategyLoadImage {

    public LoadForCam(Context lContext) {
        super(lContext);
    }

    @SuppressLint("CheckResult")
    @Override
    public void way(Object way) {

        DecodeCamera.build()
                .decodeBitmap((DecodeCamera.CameraProperties) way, new LoadProjectListener() {
                    @Override
                    public void loadImage(Bitmap image) {
                        lListener.loadImage(image);
                    }

                });

    }


}
