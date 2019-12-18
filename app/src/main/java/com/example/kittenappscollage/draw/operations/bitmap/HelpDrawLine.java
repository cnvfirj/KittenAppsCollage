package com.example.kittenappscollage.draw.operations.bitmap;

import android.graphics.Color;
import com.example.kittenappscollage.draw.repozitoryDraw.RepDraw;
import java.util.ArrayList;

public class HelpDrawLine extends HelperSershPoints {

    @Override
    protected void applyElastPixel(int[] p, int value) {
        if(getCommand().equals(MutableBit.Command.LINE_1))setPixel(p, value);
        else if(getCommand().equals(MutableBit.Command.LINE_2))setGradPixel(p, value);
        else if(getCommand().equals(MutableBit.Command.LINE_3))setSideGradPixel(p, value);
    }

    @Override
    protected void variableParams(int[] p, ArrayList<int[]> points, ArrayList<Integer> values, int[] reperA, int[] reperB) {
        super.variableParams(p, points, values, reperA, reperB);
        if (getCommand().equals(MutableBit.Command.LINE_2)) {
            fillPointsGrad(values, p,
                    getRadiusLine());
        } else if (getCommand().equals(MutableBit.Command.LINE_3)) {
            fillAlterPointsGrad(values, p, reperA, reperB);
        }
    }

    @Override
    protected int selectValue() {
        return RepDraw.get().getAlphaInColor();
    }

    @Override
    protected boolean condition() {
        return getCommand().equals(MutableBit.Command.LINE_2)||getCommand().equals(MutableBit.Command.LINE_3);
    }

    private void fillPointsGrad(ArrayList<Integer> arr, int[] p, float r){
        if(arr!=null) {
            int alpha = computeGrad(ounCirc(p, r));
            arr.add(alpha);
        }
    }

    private void fillAlterPointsGrad(ArrayList<Integer> arr, int[] p, int[] a, int[] b){
        if(arr!=null) {
            if (isCreateAngle()) {
                if(isRightSegment())arr.add(computeGrad(ounCirc(vector(getStartAnglePoint(), p), getWidthStartToFinAngleSegment())));
                else arr.add(computeGrad(ounCirc(vector(gethFinAnglePoint(), p), getWidthStartToFinAngleSegment())));
            } else {
                arr.add(computeGrad(ounCirc(vector(b, p), getDiameterLine())));
            }
        }
    }

    private int computeGrad(float power){
        float segm =  RepDraw.get().getAlphaInColor();
        return (int)(segm-(segm*power));
    }

    private void setPixel(int[] p, int value){
        final int index = p[1]*gethWidth()+p[0];
        if(p[0]>=0&&p[0]<gethWidth()&&p[1]>=0&&p[1]<gethHeight()){
            if(!getCheckeds()[index])implementColor1(index,value);
        }
    }

    private void setGradPixel(int[] p, int value){
        final int index = p[1]*gethWidth()+p[0];
        if(p[0]>=0&&p[0]<gethWidth()&&p[1]>=0&&p[1]<gethHeight()){
            if(!getCheckeds()[index])
                implementColor2(index,value);
        }
    }

    private void setSideGradPixel(int[] p, int value){
        final int index = p[1]*gethWidth()+p[0];
        if(p[0]>=0&&p[0]<gethWidth()&&p[1]>=0&&p[1]<gethHeight()){
            if(!getCheckeds()[index])
                implementColor3(index,value);
        }
    }

    private void implementColor1(int index,int value){
        getPixels()[index]=RepDraw.get().getColor();
        getCheckeds()[index]=true;
    }

    private void implementColor2(int index,int value){
        value -= value%5;
        int b = RepDraw.get().getColor();
        if(Color.alpha(getPixels()[index])<value){
            getPixels()[index] = Color.argb(value, Color.red(b),Color.green(b),Color.blue(b));
        }
    }

    private void implementColor3(int index, int value){
        value -= value%5;
        int b = RepDraw.get().getColor();
        if(Color.alpha(getPixels()[index])<value){
            getPixels()[index] = Color.argb(value, Color.red(b),Color.green(b),Color.blue(b));
        }
    }

    private float ounCirc(int[] p, float r){
        return (float)(p[X]*p[X])/(r*r)+(float) (p[Y]*p[Y])/(r*r);
    }

    private int[] vector(int[] one, int[] two){
        return new int[]{two[X]-one[X],two[Y]-one[Y]};
    }
}
