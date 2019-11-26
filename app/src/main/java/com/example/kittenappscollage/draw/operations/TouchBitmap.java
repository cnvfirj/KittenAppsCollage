package com.example.kittenappscollage.draw.operations;

import android.graphics.PointF;
import android.util.Pair;

import java.util.Arrays;

public class TouchBitmap {



    /*поработать со знаком координат точек*/
    public static PointF center(PointF[]points){
        PointF p1 = points[0];
        PointF p2 = points[2];
        PointF p3 = points[1];
        PointF p4 = points[3];

        float x =((p1.x*p2.y-p2.x*p1.y)*(p4.x-p3.x)-(p3.x*p4.y-p4.x*p3.y)*(p2.x-p1.x))/
                 ((p1.y-p2.y)*(p4.x-p3.x)-(p3.y-p4.y)*(p2.x-p1.x));
        float y = ((p3.y-p4.y)*x-(p3.x*p4.y-p4.x*p3.y))/(p4.x-p3.x);

        return new PointF(-x,y);
    }

    public static boolean ifIGotBit(PointF[]points, PointF point){

        return vectorCheck(points,point);
    }

    private static boolean vectorCheck(PointF[]points, PointF p){
        Pair<PointF, PointF> minMax = minMax(points);
        if(p.x>minMax.first.x&&p.y>minMax.first.y&&p.x<minMax.second.x&&p.y<minMax.second.y){
            float vec1 = vecProductVec(points[0],points[1],p);
            float vec2 = vecProductVec(points[1],points[2],p);
            float vec3 = vecProductVec(points[2],points[3],p);
            float vec4 = vecProductVec(points[3],points[0],p);

            if(vec1>=0&&vec2>=0&&vec3>=0&&vec4>=0){
                return true;
            }else if(vec1<=0&&vec2<=0&&vec3<=0&&vec4<=0){
                return true;
            }else return false;
        }return false;
    }

    private static float vecProductVec(PointF p1, PointF p2, PointF p){
        return (p2.x - p1.x)*(p.y - p1.y) - (p.x - p1.x)*(p2.y - p1.y);
    }

    public static boolean ifIGotBitBord(PointF[]points, PointF point, float r){
           return vectorBitBordCheck(points,point,r);
    }

    private static boolean vectorBitBordCheck(PointF[]points, PointF p, float r){
        Pair<PointF, PointF> minMax = minMax(points);
        if(p.x>minMax.first.x&&p.y>minMax.first.y&&p.x<minMax.second.x&&p.y<minMax.second.y){
            float vec1 = vecBitBordProductVec(points[0],points[1],p,r);
            float vec2 = vecBitBordProductVec(points[1],points[2],p,r);
            float vec3 = vecBitBordProductVec(points[2],points[3],p,r);
            float vec4 = vecBitBordProductVec(points[3],points[0],p,r);

            if(vec1>=0&&vec2>=0&&vec3>=0&&vec4>=0){
                return true;
            }else if(vec1<=0&&vec2<=0&&vec3<=0&&vec4<=0){
                return true;
            }else return false;
        }return false;
    }


    private static float vecBitBordProductVec(PointF p1, PointF p2, PointF p, float r){
        return ((p2.x+r) - (p1.x+r))*((p.y+r) - (p1.y+r)) - ((p.x+r) - (p1.x+r))*((p2.y+r) - (p1.y+r));
    }


    /*выяснием минимальные и мксимальные значения
     * x и y из всех вершин*/
    private static Pair<PointF, PointF> minMax(PointF[]ver){
        float[]arrX = new float[4];
        float[]arrY = new float[4];
        for (int i=0;i<ver.length;i++){
            arrX[i] = ver[i].x;
            arrY[i] = ver[i].y;
        }
        Arrays.sort(arrX);
        Arrays.sort(arrY);

        return new Pair<>(new PointF(arrX[0],arrY[0]),new PointF(arrX[3],arrY[3]));
    }



}
