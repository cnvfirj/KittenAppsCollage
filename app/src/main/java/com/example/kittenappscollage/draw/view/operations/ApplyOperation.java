package com.example.kittenappscollage.draw.view.operations;

import android.graphics.Canvas;
import android.view.MotionEvent;

public class ApplyOperation {

    public ApplyOperation event(MotionEvent event){
        return this;
    }

    public ApplyOperation command(Operation.Command command){
        return this;
    }


}
