package com.kittendevelop.kittenappscollage.draw.operations;

import android.graphics.PointF;
import android.graphics.RectF;
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

    public static boolean ifIGotRect(PointF[]points, PointF point){
        return vectorRect(points,point);
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
        Pair<PointF, PointF> minMax = minMaxBitBord(points,r);
        if(p.x>minMax.first.x&&p.y>minMax.first.y&&p.x<minMax.second.x&&p.y<minMax.second.y){
            float vec1 = vecBitBordProductVec(points[0],points[1],p,r,1);
            float vec2 = vecBitBordProductVec(points[1],points[2],p,r,2);
            float vec3 = vecBitBordProductVec(points[2],points[3],p,r,3);
            float vec4 = vecBitBordProductVec(points[3],points[0],p,r,4);
            if(vec1>=0&&vec2>=0&&vec3>=0&&vec4>=0){
                return true;
            }else if(vec1<=0&&vec2<=0&&vec3<=0&&vec4<=0){
                return true;
            }else return false;
        }return false;
    }


    private static float vecBitBordProductVec(PointF p1, PointF p2, PointF p, float r,int index ){
        float x1 = p1.x;
        float y1 = p1.y;
        float x2 = p2.x;
        float y2 = p2.y;

        if(index==1){
            x1-=r;y1-=r;x2+=r;y2-=r;
        }else if(index==2){
            x1+=r;y1-=r;x2+=r;y2+=r;
        }else if(index==3){
            x1+=r;y1+=r;x2-=r;y2+=r;
        } else if(index==4){
            x1-=r;y1+=r;x2-=r;y2-=r;
        }
        return (x2 - x1)*(p.y - y1) - (p.x - x1)*(y2 - y1);
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

    private static Pair<PointF, PointF> minMaxBitBord(PointF[]ver,float r){
        float[]arrX = new float[4];
        float[]arrY = new float[4];
        for (int i=0;i<ver.length;i++){
            float x1 = ver[i].x;
            float y1 = ver[i].y;

            if(i==0){
                x1-=r;y1-=r;
            }else if(i==1){
                x1+=r;y1-=r;
            }else if(i==2){
                x1+=r;y1+=r;
            } else if(i==3){
                x1-=r;y1+=r;
            }
            arrX[i] = x1;
            arrY[i] = y1;
        }
        Arrays.sort(arrX);
        Arrays.sort(arrY);

        return new Pair<>(new PointF(arrX[0],arrY[0]),new PointF(arrX[3],arrY[3]));
    }

    private static boolean vectorRect(PointF[]points,PointF point){

        PointF min = new PointF();
        PointF max = new PointF();
        for (int i=0;i<points.length;i++){
            if(i==0){
                min.set(points[i].x,points[i].y);
                max.set(points[i].x,points[i].y);
                continue;
            }
            if(points[i].x<min.x)min.set(points[i].x,min.y);
            if(points[i].y<min.y)min.set(min.x,points[i].y);

            if(points[i].x>max.x)max.set(points[i].x,max.y);
            if(points[i].y>max.y)max.set(max.x,points[i].y);
        }
        RectF r = new RectF(min.x,min.y,max.x,max.y);


        return r.contains(point.x,point.y);
    }


}
