package com.example.kittenappscollage.draw.operations.bitmap;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PointF;

import com.example.kittenappscollage.draw.operations.TouchBitmap;
import com.example.kittenappscollage.draw.repozitoryDraw.RepDraw;
import com.example.kittenappscollage.draw.repozitoryDraw.Repozitory;
import com.example.mutablebitmap.DeformMat;

import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

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
//            hPixels[index] = hFillColor;
            hOverPix[index] = hFillColor;
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
