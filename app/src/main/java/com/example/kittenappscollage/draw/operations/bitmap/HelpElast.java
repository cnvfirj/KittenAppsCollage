package com.example.kittenappscollage.draw.operations.bitmap;

import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PointF;

import com.example.kittenappscollage.draw.repozitoryDraw.RepDraw;

import java.util.ArrayList;

public class HelpElast extends HelperSershPoints {

    @Override
    protected void applyElastPixel(int[] p, int value) {
        if(getCommand().equals(MutableBit.Command.ELAST_1))setPixel(p, value);
        else if(getCommand().equals(MutableBit.Command.ELAST_2))setGradPixel(p, value);
        else if(getCommand().equals(MutableBit.Command.ELAST_3))setSideGradPixel(p, value);
    }

    @Override
    protected void variableParams(PointF p,ArrayList<PointF> points, ArrayList<Integer> values,Point a, Point b) {
        super.variableParams(p,points,values,a,b);
        if(getCommand().equals(MutableBit.Command.ELAST_2)) {
            fillPointsAlpha(values, p,
                    RepDraw.get().getWidth()/2.0f / gethMat().getRepository().getScale());
        }else if(getCommand().equals(MutableBit.Command.ELAST_3)){
            fillAlterPointsAlpha(values,p,a,b);
        }
    }

    private void fillAlterPointsAlpha(ArrayList<Integer> arr, PointF p, Point a, Point b ){
        /*заполнение с односторонней прозрачностью*/
        float r = RepDraw.get().getWidth()/ gethMat().getRepository().getScale();
        PointF fA = new PointF(b.x,b.y);
        if(arr!=null) {
            if (isCreateAngle()) {
                /*закрашиваем угол в зависимости от поворота отрезка*/
                if(isRightSegment())arr.add(computeAlpha(ounCirc(vector(getStartAnglePoint(), p), r)));
                else arr.add(computeAlpha(ounCirc(vector(gethFinAnglePoint(), p), r)));
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
        final int index = p[1]*gethWidth()+p[0];
        if(p[0]>=0&&p[0]<gethWidth()&&p[1]>=0&&p[1]<gethHeight()){
            if(!getCheckeds()[index])implementElast1(index,alpha);
        }
    }
    private void implementElast1(int index,int alpha){
        getPixels()[index] = alphaColor(getPixels()[index], alpha);
        getCheckeds()[index]=true;
    }

    private void setGradPixel(int []p,int alpha){
        final int index = p[1]*gethWidth()+p[0];
        if(p[0]>=0&&p[0]<gethWidth()&&p[1]>=0&&p[1]<gethHeight()){
            implementElast2(index,alpha);
        }
    }

    private void setSideGradPixel(int []p,int alpha){
        final int index = p[1]*gethWidth()+p[0];
        if(p[0]>=0&&p[0]<gethWidth()&&p[1]>=0&&p[1]<gethHeight()){
            implementElast3(index,alpha);
        }
    }
    private void implementElast2(int index,int alpha){
        getPixels()[index] = alphaColor(getPixels()[index], alpha);
    }

    private void implementElast3(int index,int alpha){
        getPixels()[index] = alphaColor(getPixels()[index], alpha);
    }

    protected float ounCirc(PointF p, float r){
        return (p.x*p.x)/(r*r)+(p.y*p.y)/(r*r);
    }

    protected PointF vector(PointF one, PointF two){
        return new PointF(two.x-one.x,two.y-one.y);
    }

    private int alphaColor(int color,int alpha){
        if(Color.alpha(color)<alpha)return color;
        else return Color.argb(alpha, Color.red(color), Color.green(color), Color.blue(color));
    }
}
