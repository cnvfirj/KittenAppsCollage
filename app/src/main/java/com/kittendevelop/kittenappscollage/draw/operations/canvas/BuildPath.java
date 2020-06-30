package com.kittendevelop.kittenappscollage.draw.operations.canvas;

import android.graphics.Path;
import android.graphics.PointF;
import android.view.MotionEvent;

public class BuildPath  {

    public enum Command{
        SPOT,
        TEXT
    }

    private Path hPath;

    public BuildPath() {
        hPath = new Path();
    }

    public BuildPath point(MotionEvent event){
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                start(new PointF(event.getX(),event.getY()));
                break;
            case MotionEvent.ACTION_MOVE:
                move(new PointF(event.getX(),event.getY()));
                break;
            case MotionEvent.ACTION_UP:
                fin(new PointF(event.getX(),event.getY()));
                break;
        }
        return this;
    }

    protected void start(PointF s){
        hPath.reset();
        hPath.moveTo(s.x,s.y);
    }

    protected void move(PointF m){
        hPath.lineTo(m.x,m.y);
    }

    protected void fin(PointF f){
//        hPath.close();
    }

    protected Path getPath(){
        return hPath;
    }
}
