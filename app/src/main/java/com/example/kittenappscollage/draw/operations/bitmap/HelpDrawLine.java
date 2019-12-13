package com.example.kittenappscollage.draw.operations.bitmap;

import android.graphics.Color;
import android.graphics.PointF;

import com.example.kittenappscollage.draw.repozitoryDraw.RepDraw;

import java.util.ArrayList;

import static com.example.kittenappscollage.helpers.Massages.LYTE;

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
                    RepDraw.get().getWidth() / 2.0f / gethMat().getRepository().getScale());
        } else if (getCommand().equals(MutableBit.Command.LINE_3)) {
            fillAlterPointsGrad(values, p, reperA, reperB);
        }
    }

    @Override
    protected int selectValue() {
        return RepDraw.get().getColor();
    }
    @Override
    protected boolean condition() {
        return getCommand().equals(MutableBit.Command.LINE_2)||getCommand().equals(MutableBit.Command.LINE_3);
    }

    private void fillPointsGrad(ArrayList<Integer> arr, int[] p, float r){
        if(arr!=null) {
            int color = RepDraw.get().getColor();
            int alpha = computeGrad(ounCirc(p, r));
            color  = Color.argb(alpha,Color.red(color),Color.green(color),Color.blue(color));

            arr.add(color);
        }
    }

    private void fillAlterPointsGrad(ArrayList<Integer> arr, int[] p, int[] a, int[] b){
        float r = RepDraw.get().getWidth()/ gethMat().getRepository().getScale();
        if(arr!=null) {
            if (isCreateAngle()) {
                /*закрашиваем угол в зависимости от поворота отрезка*/
                if(isRightSegment())arr.add(computeGrad(ounCirc(vector(getStartAnglePoint(), p), r)));
                else arr.add(computeGrad(ounCirc(vector(gethFinAnglePoint(), p), r)));
            } else {
                arr.add(computeGrad(ounCirc(vector(b, p), r)));
            }
        }
    }

    private int computeGrad(float power){
        int segm =  RepDraw.get().getAlphaInColor();
        return (segm -(int)(segm*power));
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
                implementColor2(index,value);
        }
    }

    private void implementColor1(int index,int value){

        getPixels()[index] = RepDraw.get().getColor();
        getCheckeds()[index]=true;
    }

    private void implementColor2(int index,int value){

        getPixels()[index] = value;
        getCheckeds()[index]=true;
    }

    protected float ounCirc(int[] p, float r){
        return (p[X]*p[X])/(r*r)+(p[Y]*p[Y])/(r*r);
    }

    protected int[] vector(int[] one, int[] two){
        return new int[]{two[X]-one[X],two[Y]-one[Y]};
    }
}
