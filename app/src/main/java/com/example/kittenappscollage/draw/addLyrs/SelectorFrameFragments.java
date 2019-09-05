package com.example.kittenappscollage.draw.addLyrs;

import android.graphics.Bitmap;
import android.view.View;

public interface SelectorFrameFragments {
    public void backInAddLyr(View v, Object way);
    public void backInSelectedLyr();
    public void exitAll();
    public void doneLyr(Bitmap b);
}
