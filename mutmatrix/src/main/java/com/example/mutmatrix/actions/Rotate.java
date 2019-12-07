package com.example.mutmatrix.actions;

import android.content.Context;
import android.graphics.PointF;
import android.view.MotionEvent;

import com.example.mutmatrix.DeformMat;

import static com.example.mutmatrix.CompRep.P_X;
import static com.example.mutmatrix.CompRep.P_Y;
import static com.example.mutmatrix.CompRep.R_CENTER;

public class Rotate extends Base {

    private enum Sector {
        LEFT_TOP,
        RIGHT_TOP,
        RIGHT_BOTTOM,
        LEFT_BOTTOM,
        NON
    }

    private PointF start, fin, center;

    public Rotate(Context context) {
        super(context);
    }

    @Override
    public void reset() {
        start = null;
        fin = null;
        center = null;
    }

    @Override
    public Base touch(MotionEvent event) {
        int action = event.getAction();
        int index = event.getActionIndex();
        int id = event.getPointerId(index);
        PointF p = new PointF(event.getX(),event.getY());
        switch (action){
            case MotionEvent.ACTION_DOWN:
                if(id==0)
                    start(p);
                break;
            case MotionEvent.ACTION_MOVE:
                if(id==0)
                    move(p);
                break;
            case MotionEvent.ACTION_UP:
                if(id==0)
                    fin(p);
                break;
        }
        return this;
    }

    @Override
    public void start(PointF p) {
       rep.findLoc();
       start = p;
       float[]c = rep.getLoc()[R_CENTER];
       center = new PointF(c[P_X],c[P_Y]);
    }

    @Override
    public void move(PointF p) {
        fin = p;
        calculateRotate();
    }

    @Override
    public void fin(PointF p) {
        fin = p;
        calculateRotate();
        start = null;
        fin = null;
        center = null;
        rep.findLoc();
    }

    @Override
    public void resetMutable() {
        super.resetMutable();
        rep.setRotate(0);
        rep.setTranslate(new PointF(rep.getTranslate().x-rep.getStep().x,rep.getTranslate().y-rep.getStep().y));
        rep.setStep(new PointF(0,0));
    }

    private void calculateRotate(){
        float angle = rep.getRotate();
        float top =((start.x- center.x)*(fin.x- center.x))+((start.y- center.y)*(fin.y- center.y));
        double botCS = Math.sqrt((start.x- center.x)*(start.x- center.x)+(start.y- center.y)*(start.y- center.y));
        double botCF = Math.sqrt((fin.x- center.x)*(fin.x- center.x)+(fin.y- center.y)*(fin.y- center.y));
        double step = Math.toDegrees(Math.acos(top/(botCS*botCF)));
        if(!Double.isNaN(step)){
            angle+=getSignAngle((float) step);
            start = fin;
            if(angle>180)angle-=360;//делаем формат(180/-180)
            if(angle<-180)angle+=360;
        }
        rep.setRotate(angle);
        rep.findLoc();
        float stepX = center.x-rep.getLoc()[R_CENTER][P_X];
        float stepY = center.y-rep.getLoc()[R_CENTER][P_Y];
        rep.setStep(new PointF(rep.getStep().x+stepX,rep.getStep().y+stepY));
        rep.setTranslate(new PointF(rep.getTranslate().x+stepX,rep.getTranslate().y+stepY));
    }

    private float getSignAngle(float angle){
        Sector sector = getSector();
        if(sector.equals(Sector.RIGHT_TOP)){
            if(start.x>=fin.x&&start.y>=fin.y)return -angle;
        }else if (sector.equals(Sector.RIGHT_BOTTOM)){
            if(start.x<=fin.x&&start.y>=fin.y)return -angle;
        }else if (sector.equals(Sector.LEFT_BOTTOM)){
            if(start.x<=fin.x&&start.y<=fin.y)return -angle;
        }else if (sector.equals(Sector.LEFT_TOP)){
            if(start.x>=fin.x&&start.y<=fin.y)return -angle;
        }
        return angle;
    }

    private Sector getSector(){
        if(start.x> center.x){
            if(start.y< center.y)return Sector.RIGHT_TOP;
            else return Sector.RIGHT_BOTTOM;
        }else {
            if(start.y> center.y)return Sector.LEFT_BOTTOM;
            else return Sector.LEFT_TOP;
        }
    }
}
