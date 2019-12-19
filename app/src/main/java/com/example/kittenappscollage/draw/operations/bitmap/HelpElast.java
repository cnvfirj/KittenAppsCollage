package com.example.kittenappscollage.draw.operations.bitmap;

import android.graphics.Color;

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
    protected void variableParams(int[] p, ArrayList<int[]> points, ArrayList<Integer> values, int[] reperA, int[] reperB) {
        super.variableParams(p, points, values, reperA, reperB);
        if (getCommand().equals(MutableBit.Command.ELAST_2)) {
            fillPointsAlpha(values, p,
                    getRadiusLine());
        } else if (getCommand().equals(MutableBit.Command.ELAST_3)) {
            fillAlterPointsAlpha(values, p, reperA, reperB);
        }
    }

    @Override
    protected int selectValue() {
        return RepDraw.get().getAlpha();
    }

    @Override
    protected boolean condition() {
        return getCommand().equals(MutableBit.Command.ELAST_2)||getCommand().equals(MutableBit.Command.ELAST_3);
    }

    private void fillAlterPointsAlpha(ArrayList<Integer> arr, int[] p, int[] a, int[] b ){
        /*заполнение с односторонней прозрачностью*/
//        float r = getDiameterLine();
        if(arr!=null) {
            if (isCreateAngle()) {
                /*закрашиваем угол в зависимости от поворота отрезка*/
                if(isRightSegment())arr.add(computeAlpha(ounCirc(vector(getStartAnglePoint(), p), getWidthStartToFinAngleSegment())));
                else arr.add(computeAlpha(ounCirc(vector(gethFinAnglePoint(), p), getWidthStartToFinAngleSegment())));
            } else {
                    arr.add(computeAlpha(ounCirc(vector(b, p), getDiameterLine())));
            }
        }
    }

    private void fillPointsAlpha(ArrayList<Integer> arr, int[] p, float r){
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

    private void setGradPixel(int []p,int alpha){
        final int index = p[1]*gethWidth()+p[0];
        if(p[0]>=0&&p[0]<gethWidth()&&p[1]>=0&&p[1]<gethHeight()){
                implementElast2(index, alpha);
        }
    }

    private void setSideGradPixel(int []p,int alpha){
        final int index = p[1]*gethWidth()+p[0];
        if(p[0]>=0&&p[0]<gethWidth()&&p[1]>=0&&p[1]<gethHeight()){
            implementElast2(index,alpha);
        }
    }

    private void implementElast1(int index,int alpha){
        int b = getPixels()[index];
            getPixels()[index] = Color.argb(alpha, Color.red(b), Color.green(b), Color.blue(b));
            getCheckeds()[index]=true;
    }

    private void implementElast2(int index,int alpha){
        alpha -= alpha%5;
        int b = getPixels()[index];
        if(Color.alpha(b)>alpha)
            getPixels()[index] = Color.argb(alpha, Color.red(b), Color.green(b), Color.blue(b));
    }


    protected float ounCirc(int[] p, float r){
        return (float)Math.sqrt(p[X]*p[X]+p[Y]*p[Y])/r;
//        return (p[X]*p[X])/(r*r)+(p[Y]*p[Y])/(r*r);
    }

    protected int[] vector(int[] one, int[] two){
        return new int[]{two[X]-one[X],two[Y]-one[Y]};
    }

}
