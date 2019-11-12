package com.example.kittenappscollage.draw.operations.bitmap;

import android.graphics.Bitmap;

public class MutableFill extends MutableCut {

    private HelpFill mFill;

    protected Bitmap mUnderBitmap;

    public MutableFill() {
        mFill = new HelpFill();
    }

    public MutableFill underBitmap(Bitmap bitmap){
        mUnderBitmap = bitmap;
        return this;
    }
}
