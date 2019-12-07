package com.example.mutmatrix.actions;

import android.content.Context;
import android.graphics.PointF;
import android.view.MotionEvent;

import com.example.mutmatrix.actions.Base;

public class Translate extends Base {

    private PointF start;

    private PointF fin;

    public Translate(Context context) {
        super(context);
    }

    @Override
    public void reset() {
        start = null;
        fin = null;
    }

    @Override
    public Base touch(MotionEvent event) {
        int action = event.getActionMasked();
        int index = event.getActionIndex();
        int id = event.getPointerId(index);
        PointF p = new PointF(event.getX(),event.getY());
        switch (action){
            case MotionEvent.ACTION_DOWN:
                if(id==0)start(p);
                break;
            case MotionEvent.ACTION_MOVE:
                if(id==0)move(p);
                break;
            case MotionEvent.ACTION_UP:
                if(id==0)fin(p);
                break;
        }
        return this;
    }

    @Override
    public void start(PointF p) {
        start = p;
    }

    @Override
    public void move(PointF p) {
        fin = p;
        calculateTranslate();
    }

    @Override
    public void fin(PointF p) {
        fin = p;
        calculateTranslate();
        start = null;
        fin = null;
        rep.findLoc();
    }

    protected void calculateTranslate(){
        float x = fin.x-start.x;
        float y = fin.y-start.y;
        start = fin;
        final PointF t = rep.getTranslate();
        rep.setTranslate(new PointF(t.x+x,t.y+y));
    }
}
