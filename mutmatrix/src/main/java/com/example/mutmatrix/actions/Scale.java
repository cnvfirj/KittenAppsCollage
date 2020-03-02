package com.example.mutmatrix.actions;

import android.content.Context;
import android.graphics.PointF;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import com.example.mutmatrix.DeformMat;


import static com.example.mutmatrix.DeformMat.Command.SCALE_ADAPT;
import static com.example.mutmatrix.DeformMat.Command.SCALE_COMMON;
import static com.example.mutmatrix.DeformMat.Command.SCALE_MAX;
import static com.example.mutmatrix.DeformMat.Command.SCALE_MIN;
import static com.example.mutmatrix.DeformMat.Command.SCALE_MINUS;
import static com.example.mutmatrix.DeformMat.Command.SCALE_PLUS;
import static com.example.mutmatrix.Massages.ERROR;

public class Scale extends Base{



    private PointF view;

    private ScaleGestureDetector detector;

    public Scale(Context context) {
        super(context);
    }

    @Override
    public void reset() {
        detector = new ScaleGestureDetector(context,new ScaleListener());
    }

    @Override
    public Base touch(MotionEvent event) {
        int index = event.getActionIndex();
        int id = event.getPointerId(index);
        if(command.equals(DeformMat.Command.SCALE)) {
            if (id == 0 || id == 1) {
                detector.onTouchEvent(event);
                if (event.getAction() == MotionEvent.ACTION_UP) fin(null);
            }
        }else {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                start(new PointF(event.getX(),event.getY()));
            }
        }
        return this;
    }

    @Override
    public Base view(PointF v) {
        view = v;
        return super.view(v);
    }

    @Override
    public void start(PointF p) {
      calculate(command, p);
    }

    @Override
    public void move(PointF p) {

    }

    @Override
    public void fin(PointF p) {
        rep.findLoc();
    }

    private void calculate(DeformMat.Command c,PointF p){
        float scale = rep.getScale();
        PointF trans = rep.getTranslate();
        if(c.equals(SCALE_MAX))rep.setScale(10.f);
        else if(c.equals(SCALE_MIN))rep.setScale(0.1f);
        else if(c.equals(SCALE_ADAPT))rep.setScale(adapt());
        else if(c.equals(SCALE_COMMON))rep.setScale(1.0f);
        else if(c.equals(SCALE_MINUS))rep.setScale(scaleM());
        else if(c.equals(SCALE_PLUS))rep.setScale(scaleP());
        scale = rep.getScale()/scale;
        final float x = (p.x-trans.x)*scale;
        final float y = (p.y-trans.y)*scale;
        rep.setTranslate(new PointF(p.x-x,p.y-y));
        fin(null);
    }
    private float scaleP(){
        float scale = rep.getScale();
        scale = scale+0.1f;
        if(scale>10)scale = 10;
        return scale;
    }

    private float scaleM(){
        float scale = rep.getScale();
        scale = scale-0.1f;
        if(scale<0.1f)scale = 0.1f;
        return scale;
    }

    private float adapt(){
        if(view!=null){
            float sV = view.x<=view.y ? view.x : view.y;
            float sB = rep.getWidthBit()<=rep.getHeightBit() ? rep.getWidthBit() : rep.getHeightBit();
            return   sV/sB;
        }else {
            ERROR("add params view");
            return 1.0f;
        }
    }
    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {

        private float scale = 1.0f;

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            scale = rep.getScale();
            return super.onScaleBegin(detector);
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
            super.onScaleEnd(detector);
            scale = rep.getScale();
        }

        @Override
        public boolean onScale(ScaleGestureDetector detector){
            final float temp = rep.getScale();
            scale *= detector.getScaleFactor();
            scale = Math.max(0.1f, Math.min(scale, 10.0f));
            rep.setScale(scale);

            PointF f = new PointF(detector.getFocusX(),detector.getFocusY());
            PointF t = rep.getTranslate();

            float stepX = (f.x-t.x)*(rep.getScale()/temp);
            float stepY = (f.y-t.y)*(rep.getScale()/temp);

            rep.setTranslate(new PointF(f.x-stepX,f.y-stepY));
            return true;
        }
    }
}
