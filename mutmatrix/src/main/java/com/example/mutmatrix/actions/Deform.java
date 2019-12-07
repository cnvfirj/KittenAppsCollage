package com.example.mutmatrix.actions;

import android.content.Context;
import android.graphics.PointF;
import android.view.MotionEvent;

import com.example.mutmatrix.DeformMat;

public class Deform extends Base {


    private enum Sector {
        LEFT_TOP,
        RIGHT_TOP,
        RIGHT_BOTTOM,
        LEFT_BOTTOM,
        NON
    }

    public enum Coordinates{
        DEFAULT,
        DISPLAY_ROTATE_DEFORM
    }

    private Sector sector;

    public static final int DEF_CENTER = 9;
    public static final int R_CENTER = 0;
    public static final int R_L_TOP = 1;
    public static final int R_R_TOP = 2;
    public static final int R_R_BOTTOM = 3;
    public static final int R_L_BOTTOM = 4;

    public static final int R_SIDE_TOP = 5;
    public static final int R_SIDE_RIGHT = 6;
    public static final int R_SIDE_BOTTOM = 7;
    public static final int R_SIDE_LEFT = 8;

    private PointF start, fin;

    private boolean quadrangle;

    public Deform(Context context) {
        super(context);
        quadrangle = true;
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
    public void reset() {
       sector = Sector.NON;
       start = null;
       fin = null;
}

    @Override
    public void start(PointF p) {
        rep.findLoc();
        start = getPointBitmap(p);
        sector = getTouch(start);
    }

    @Override
    public void move(PointF p) {
        if(fin!=null)start = new PointF(fin.x,fin.y);
        fin = getPointBitmap(p);
        deform();
    }

    @Override
    public void fin(PointF p) {
        fin = getPointBitmap(p);
        deform();
        start = null;
        fin = null;
        sector = Sector.NON;
    }

    @Override
    public void resetMutable() {
        super.resetMutable();
        rep.setDst(rep.getSrc());
    }

    @Override
    public PointF getPointBitmap(PointF point){
        rep.findLoc();
        float[]p = new float[]{point.x,point.y};
        final float side_P_L = rep.sideTriangle(p,rep.getLoc()[R_L_TOP]);
        final float side_P_R = rep.sideTriangle(p,rep.getLoc()[R_R_TOP]);

        final float side_base = rep.sideTriangle(rep.getLoc()[R_L_TOP],rep.getLoc()[R_R_TOP]);
        final float side_left = rep.sideTriangle(rep.getLoc()[R_L_TOP],rep.getLoc()[R_L_BOTTOM]);

        final float h_p = (side_P_L+side_P_R+side_base)/2;

        final float y = (2/side_base)*(float) Math.sqrt(h_p*(h_p-side_P_L)*(h_p-side_P_R)*(h_p-side_base));
        final float x = rep.getPythagorasKat(side_P_L,y);

        final PointF vector_base = rep.calculateVector(rep.getLoc()[R_L_TOP],rep.getLoc()[R_R_TOP]);
        final PointF vector_left = rep.calculateVector(rep.getLoc()[R_L_TOP],rep.getLoc()[R_L_BOTTOM]);
        final PointF vector_point = rep.calculateVector(rep.getLoc()[R_L_TOP],p);

        final float scalar_base = rep.scalarProduct(vector_base,vector_point);
        final float scalar_left = rep.scalarProduct(vector_left,vector_point);

        int iX=1;
        int iY=1;

        if(rep.cosAngle(scalar_base,side_P_L*side_base)<0)iX=-1;
        if(rep.cosAngle(scalar_left,side_P_L*side_left)<0)iY=-1;

        return new PointF(iX*x/rep.getScale(),iY*y/rep.getScale());
    }

    private Sector getTouch(PointF touch){

        PointF[]control = muteDeformLoc(Coordinates.DEFAULT);
        sector = Sector.NON;

        if(touch.x>control[0].x&&touch.x<rep.getBitmap().x/2){
            if(touch.y>control[0].y&&touch.y<rep.getBitmap().y/2){
                return Sector.LEFT_TOP;
            }
        }
        if (touch.x<control[1].x&&touch.x>rep.getBitmap().x/2){
            if(touch.y>control[1].y&&touch.y<rep.getBitmap().y/2){
                return Sector.RIGHT_TOP;
            }
        }
        if (touch.x<control[2].x&&touch.x>rep.getBitmap().x/2){
            if(touch.y<control[2].y&&touch.y>rep.getBitmap().y/2){
                return Sector.RIGHT_BOTTOM;
            }
        }
        if (touch.x>control[3].x&&touch.x<rep.getBitmap().x/2){
            if(touch.y<control[3].y&&touch.y>rep.getBitmap().y/2){
                return Sector.LEFT_BOTTOM;
            }
        }
        return Sector.NON;
    }

    public PointF[] muteDeformLoc(Coordinates c){
        PointF[]p = new PointF[4];
        int j =0;
        for (int i=0;i<rep.getDst().length;i+=2){

            float x = rep.getDst()[i];
            float y = rep.getDst()[i+1];
            p[j] = new PointF(x,y);
            j++;
        }
        if(c.equals(Coordinates.DEFAULT))return p;
        return calculateDisplayDeformRotate(p);
    }
    private PointF[] calculateDisplayDeformRotate(PointF[] points){
        PointF[] p = new PointF[4];
        for (int i=0;i<points.length;i++) {
            float diagonal = rep.getPythagorasGip(points[i].x , points[i].y );
            float angle = rep.getAngleDiagonal(points[i].x,diagonal);
            if(Float.isNaN(angle))angle=0;
            if(points[i].y<0)angle *=-1;
            float[] point = rep.getPointAngle(diagonal,angle,rep.getScale());
            p[i] = new PointF(point[0], point[1]);
        }
        return p;
    }

    private void deform(){

        if(!sector.equals(Sector.NON)) {
            PointF bitmap = rep.getBitmap();
            float cX = bitmap.x/2;
            float cY = bitmap.y/2;
            float bordX = bitmap.x/5;
            float bordY = bitmap.y/5;
            float[] dst = rep.getDst().clone();
            if (quadrangle&&sector.equals(Sector.LEFT_TOP)) {
                float x = dst[0] + (fin.x-start.x);
                float y = dst[1] + (fin.y-start.y);
                if(x>cX-bordX)x = cX-bordX;
                if(y>cY-bordY)y = cY-bordY;
                dst[0] = x;
                dst[1] = y;
            } else if (sector.equals(Sector.RIGHT_TOP)) {
                float x = dst[2] + (fin.x-start.x);
                float y = dst[3] + (fin.y-start.y);
                if(x<cX+bordX)x = cX+bordX;
                if(y>cY-bordY)y = cY-bordY;
                dst[2] = x;
                dst[3] = y;
            } else if (sector.equals(Sector.RIGHT_BOTTOM)) {
                float x = dst[4] + (fin.x-start.x);
                float y = dst[5] + (fin.y-start.y);
                if(x<cX+bordX)x = cX+bordX;
                if(y<cY+bordY)y = cY+bordY;
                dst[4] = x;
                dst[5] = y;
            } else if (sector.equals(Sector.LEFT_BOTTOM)) {
                float x = dst[6] + (fin.x-start.x);
                float y = dst[7] + (fin.y-start.y);
                if(x>cX-bordX)x = cX-bordX;
                if(y<cY+bordY)y = cY+bordY;
                dst[6] = x;
                dst[7] = y;
            }
            rep.setDst(dst);
        }
    }
}
