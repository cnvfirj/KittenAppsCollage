package com.example.kittenappscollage.draw.operations;

import android.graphics.PointF;
import android.graphics.RectF;
import android.view.MotionEvent;

import java.util.ArrayList;

import static com.example.kittenappscollage.helpers.Massages.LYTE;

public class TouchPoints {

    public final static int TOP_LEFT = 0;
    public final static int TOP_RIGHT = 1;
    public final static int BOTTOM_RIGHT = 2;
    public final static int BOTTOM_LEFT = 3;
    public final static int TOP = 4;
    public final static int RIGHT = 5;
    public final static int BOTTOM = 6;
    public final static int LEFT = 7;
    public final static int CENTER = 8;


    private ArrayList<PointF> tAllPoints;

    private PointF[]tRepersArrPoints;

    private PointF[]tRepersStartFinPoints;

    private RectF tStartFin;

    private RectF tArrPoints;

    private int tTouch;

    private boolean tResetPoints;

    private boolean tReadyCorrectStartFin;

    private boolean tReadyCorrectArr;



    /*минимальный диаметр касания*/
    public TouchPoints(){
        tTouch = 30;
        reset();
    }


    /*добавляем все точки без исключения*/
    protected void allPoints(MotionEvent event){
        tAllPoints.add(new PointF(event.getX(),event.getY()));
        changeOnArrPoint();
        changeStartFinPoints();
    }

    /*сбрасываем все*/
    protected void reset(){
        resetRepers();
        resetPoints();
    }

    /*сбрасываем только реперные точки*/
    protected void resetRepers(){
        tRepersArrPoints = new PointF[9];
        tRepersStartFinPoints = new PointF[9];
        tReadyCorrectStartFin = false;
        tReadyCorrectArr = false;
        tStartFin = new RectF();
        tArrPoints = new RectF();
    }

    /*сбрасываем тоько точки из списка*/
    protected void resetPoints(){
        if(tAllPoints!=null)tAllPoints.clear();
        else tAllPoints = new ArrayList<>();
        tResetPoints = true;
    }

    public void zoneTouch(int touch){
        tTouch = touch;
    }

    public int zoneTouch(){
        return tTouch;
    }
    public ArrayList<PointF> getAllPoints() {
        return tAllPoints;
    }

    public PointF[] getRepersArrPoints() {
        return tRepersArrPoints;
    }

    public PointF[] getRepersStartFinPoints() {
        return tRepersStartFinPoints;
    }

    public RectF getStartFin() {
        return tStartFin;
    }

    public RectF getArrPoints() {
        return tArrPoints;
    }


    public boolean isResetPoints(){
        return tResetPoints;
    }

    public boolean istReadyCorrectStartFin(){
        return tReadyCorrectStartFin;
    }

    public boolean istReadyCorrectArr(){
        return tReadyCorrectArr;
    }

    /*область занятая всеми точками*/
    private void changeOnArrPoint(){
        PointF p = tAllPoints.get(tAllPoints.size()-1);
        if(tAllPoints.size()==1){
           tArrPoints.left = p.x;
           tArrPoints.right = p.x;
           tArrPoints.top = p.y;
           tArrPoints.bottom = p.y;
        }
        if(tAllPoints.size()>1){
            tArrPoints.left = tArrPoints.left<p.x?tArrPoints.left:p.x;
            tArrPoints.right = tArrPoints.right>p.x?tArrPoints.right:p.x;
            tArrPoints.top = tArrPoints.top<p.y?tArrPoints.top:p.y;
            tArrPoints.bottom = tArrPoints.bottom>p.y?tArrPoints.bottom:p.y;
        }
        createRepersArr();
    }

    /*область из первой и последней точки*/
    private void changeStartFinPoints(){
        if(tAllPoints.size()==1){
            PointF p = tAllPoints.get(tAllPoints.size()-1);
            tStartFin.left = p.x;
            tStartFin.right = p.x;
            tStartFin.top = p.y;
            tStartFin.bottom = p.y;
        }
        if(tAllPoints.size()>1){
            PointF start = tAllPoints.get(0);
            PointF fin = tAllPoints.get(tAllPoints.size()-1);
            tStartFin.left = start.x<fin.x?start.x:fin.x;
            tStartFin.right = start.x>fin.x?start.x:fin.x;
            tStartFin.top = start.y<fin.y?start.y:fin.y;
            tStartFin.bottom = start.y>fin.y?start.y:fin.y;

        }
        createReperStartFin();
    }

    /*реперные точки области из первой и последней точки*/
    protected void createReperStartFin(){

        if(tStartFin.right-tStartFin.left>tTouch*4&&
                tStartFin.bottom-tStartFin.top>tTouch*4)tReadyCorrectStartFin = true;
        else tReadyCorrectStartFin = false;
        if(tReadyCorrectStartFin){
            tRepersStartFinPoints[TOP_LEFT] = new PointF(tStartFin.left,tStartFin.top);
            tRepersStartFinPoints[TOP_RIGHT] = new PointF(tStartFin.right,tStartFin.top);
            tRepersStartFinPoints[BOTTOM_RIGHT] = new PointF(tStartFin.right,tStartFin.bottom);
            tRepersStartFinPoints[BOTTOM_LEFT] = new PointF(tStartFin.left,tStartFin.bottom);
            tRepersStartFinPoints[TOP] = new PointF((tStartFin.left+tStartFin.right)/2,tStartFin.top);
            tRepersStartFinPoints[RIGHT] = new PointF(tStartFin.right,(tStartFin.bottom+tStartFin.top)/2);
            tRepersStartFinPoints[BOTTOM] = new PointF((tStartFin.left+tStartFin.right)/2,tStartFin.bottom);
            tRepersStartFinPoints[LEFT] = new PointF(tStartFin.left,(tStartFin.bottom+tStartFin.top)/2);
            tRepersStartFinPoints[CENTER] = new PointF((tStartFin.left+tStartFin.right)/2,(tStartFin.bottom+tStartFin.top)/2);

        }else{
            tRepersStartFinPoints[TOP_LEFT] = new PointF(tStartFin.left,tStartFin.top);
            tRepersStartFinPoints[TOP_RIGHT] = new PointF(tStartFin.right,tStartFin.top);
            tRepersStartFinPoints[BOTTOM_RIGHT] = new PointF(tStartFin.right,tStartFin.bottom);
            tRepersStartFinPoints[BOTTOM_LEFT] = new PointF(tStartFin.left,tStartFin.bottom);
            tRepersStartFinPoints[TOP] = null;
            tRepersStartFinPoints[RIGHT] = null;
            tRepersStartFinPoints[BOTTOM] = null;
            tRepersStartFinPoints[LEFT] = null;
            tRepersStartFinPoints[CENTER] = null;

        }

    }

    /*реперные точки из области всех точек*/
    protected void createRepersArr(){
        if(tArrPoints.right-tArrPoints.left>tTouch*4&&
                tArrPoints.bottom-tArrPoints.top>tTouch*4)tReadyCorrectArr = true;
        else tReadyCorrectArr = false;
        if(tReadyCorrectArr){
            tRepersArrPoints[TOP_LEFT] = new PointF(tArrPoints.left,tArrPoints.top);
            tRepersArrPoints[TOP_RIGHT] = new PointF(tArrPoints.right,tArrPoints.top);
            tRepersArrPoints[BOTTOM_RIGHT] = new PointF(tArrPoints.right,tArrPoints.bottom);
            tRepersArrPoints[BOTTOM_LEFT] = new PointF(tArrPoints.left,tArrPoints.bottom);
            tRepersArrPoints[TOP] = new PointF((tArrPoints.left+tArrPoints.right)/2,tArrPoints.top);
            tRepersArrPoints[RIGHT] = new PointF(tArrPoints.right,(tArrPoints.bottom+tArrPoints.top)/2);
            tRepersArrPoints[BOTTOM] = new PointF((tArrPoints.left+tArrPoints.right)/2,tArrPoints.bottom);
            tRepersArrPoints[LEFT] = new PointF(tArrPoints.left,(tArrPoints.bottom+tArrPoints.top)/2);
            tRepersArrPoints[CENTER] = new PointF((tArrPoints.left+tArrPoints.right)/2,(tArrPoints.bottom+tArrPoints.top)/2);
        }else {
            tRepersArrPoints[TOP_LEFT] = new PointF(tArrPoints.left,tArrPoints.top);
            tRepersArrPoints[TOP_RIGHT] = new PointF(tArrPoints.right,tArrPoints.top);
            tRepersArrPoints[BOTTOM_RIGHT] = new PointF(tArrPoints.right,tArrPoints.bottom);
            tRepersArrPoints[BOTTOM_LEFT] = new PointF(tArrPoints.left,tArrPoints.bottom);
            tRepersArrPoints[TOP] = null;
            tRepersArrPoints[RIGHT] = null;
            tRepersArrPoints[BOTTOM] = null;
            tRepersArrPoints[LEFT] = null;
            tRepersArrPoints[CENTER] = null;
        }
    }
}
