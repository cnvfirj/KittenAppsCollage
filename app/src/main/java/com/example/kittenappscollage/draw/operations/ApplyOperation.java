package com.example.kittenappscollage.draw.operations;

import android.view.MotionEvent;

public class ApplyOperation {

    public ApplyOperation event(MotionEvent event){
        return this;
    }

    public ApplyOperation command(Operation.Command command){
        return this;
    }


}
