package com.example.kittenappscollage.draw.operations.bitmap;

import android.graphics.Bitmap;
import com.example.kittenappscollage.draw.repozitoryDraw.RepDraw;

public class HelpUnderFill extends HelpFill {

    protected Bitmap hOverlayBitmap;

    protected int[] hOverPix;

    @Override
    protected void useParams() {
        super.useParams();
        useOverlay();
    }

    private void useOverlay(){
        hOverlayBitmap = Bitmap.createBitmap(hWidth,hHeight, Bitmap.Config.ARGB_8888);
        hOverPix = new int[hWidth*hHeight];
    }

    @Override
    protected void filingUnder(int index) {
//            hOverPix[index] = hFillColor;
            hOverPix[index] = RepDraw.get().getColor();
            hPixelsChecked[index] = true;
    }

    @Override
    protected Bitmap endFill() {
        hOverlayBitmap.setPixels(hOverPix,0, hWidth, 0, 0, hWidth , hHeight );
        clear();
       return hOverlayBitmap;
    }

    @Override
    public HelpFill clear() {
        if(hOverPix!=null)hOverPix = new int[1];
        return super.clear();
    }
}
