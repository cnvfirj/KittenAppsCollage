package com.example.kittenappscollage.draw.operations.bitmap;

import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PointF;

import com.example.kittenappscollage.draw.repozitoryDraw.RepDraw;

import java.util.ArrayList;

public class HelpElast extends HelperSershPoints {

    @Override
    protected void applyElastPixel(int[] p, int value) {
        if(hCommand.equals(MutableBit.Command.ELAST_1))setPixel(p, value);
        else if(hCommand.equals(MutableBit.Command.ELAST_2))setGradPixel(p, value);
        else if(hCommand.equals(MutableBit.Command.ELAST_3))setSideGradPixel(p, value);
    }

    @Override
    protected void variableParams(PointF p,ArrayList<PointF> points, ArrayList<Integer> values,Point a, Point b) {
        super.variableParams(p,points,values,a,b);
        if(hCommand.equals(MutableBit.Command.ELAST_2)) {
            fillPointsAlpha(values, p,
                    RepDraw.get().getWidth()/2.0f / hMat.getRepository().getScale());
        }else if(hCommand.equals(MutableBit.Command.ELAST_3)){
            fillAlterPointsAlpha(values,p,a,b);
        }
    }

    private void fillAlterPointsAlpha(ArrayList<Integer> arr, PointF p, Point a, Point b ){
        /*заполнение с односторонней прозрачностью*/
        float r = RepDraw.get().getWidth()/ hMat.getRepository().getScale();
        PointF fA = new PointF(b.x,b.y);
        if(arr!=null) {
            if (hCreateAngle) {
                /*закрашиваем угол в зависимости от поворота отрезка*/
                if(hRightSegment)arr.add(computeAlpha(ounCirc(vector(hStartAnglePoint, p), r)));
                else arr.add(computeAlpha(ounCirc(vector(hFinAnglePoint, p), r)));
            } else {
                    arr.add(computeAlpha(ounCirc(vector(fA, p), r)));

            }
        }


    }

    private void fillPointsAlpha(ArrayList<Integer> arr, PointF p, float r){
        if(arr!=null) {
            arr.add(computeAlpha(ounCirc(p, r)));
        }
    }

    private int computeAlpha(float power){
        int segm = 255 - RepDraw.get().getAlpha();
        return (int)(segm*power)+RepDraw.get().getAlpha();
    }

    private void setPixel(int []p, int alpha){
        final int index = p[1]*hWidth+p[0];
        if(p[0]>=0&&p[0]<hWidth&&p[1]>=0&&p[1]<hHeight){
            if(!hCheckPixel[index])implementElast1(index,alpha);
        }
    }
    private void implementElast1(int index,int alpha){
        hAllPixels[index] = alphaColor(hAllPixels[index], alpha);
        hCheckPixel[index]=true;
    }

    private void setGradPixel(int []p,int alpha){
        final int index = p[1]*hWidth+p[0];
        if(p[0]>=0&&p[0]<hWidth&&p[1]>=0&&p[1]<hHeight){
            implementElast2(index,alpha);
        }
    }

    private void setSideGradPixel(int []p,int alpha){
        final int index = p[1]*hWidth+p[0];
        if(p[0]>=0&&p[0]<hWidth&&p[1]>=0&&p[1]<hHeight){
            implementElast3(index,alpha);
        }
    }
    private void implementElast2(int index,int alpha){
        hAllPixels[index] = alphaColor(hAllPixels[index], alpha);
    }

    private void implementElast3(int index,int alpha){
        hAllPixels[index] = alphaColor(hAllPixels[index], alpha);
    }

    private float ounCirc(PointF p, float r){
        return (p.x*p.x)/(r*r)+(p.y*p.y)/(r*r);
    }


    private float widthVector(PointF vector){
        return (float) Math.sqrt(vector.x*vector.x+vector.y*vector.y);
    }
    private PointF vector(PointF one, PointF two){
        return new PointF(two.x-one.x,two.y-one.y);
    }

            protected int alphaColor(int color,int alpha){
            if(Color.alpha(color)<alpha)return color;
            else return Color.argb(alpha, Color.red(color), Color.green(color), Color.blue(color));
        }
}
