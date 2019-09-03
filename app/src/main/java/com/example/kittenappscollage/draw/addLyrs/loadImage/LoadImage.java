package com.example.kittenappscollage.draw.addLyrs.loadImage;

import android.content.Context;


/**/
public class LoadImage {

    protected LoadProjectListener lListener;
    protected Context lContext;

    public LoadImage(Context lContext) {
        this.lContext = lContext;
    }

    public LoadImage setListener(LoadProjectListener listener){
        lListener = listener;
        return this;
    }


}
