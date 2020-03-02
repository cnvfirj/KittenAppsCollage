package com.example.kittenappscollage.collect.reviewImage;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.kittenappscollage.draw.addLyrs.AddLyr;

public class DialogReviewEdit extends AddLyr {

    public void setBitmap(Bitmap bitmap){
        aLyr = bitmap;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        awaitingPresent(aPresent,aLyr);
    }

    @Override
    protected void createBitmap(Object way, int source) {

    }

    private void awaitingPresent(final View view, Bitmap bitmap){
        ViewTreeObserver viewTreeObserver = view.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    aPresent.presentBitmap(bitmap);
                    aProgress.setVisibility(View.INVISIBLE);
                    ongoingProgress(false);
                }
            });
        }
    }
}
