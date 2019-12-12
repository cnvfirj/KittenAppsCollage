package com.example.kittenappscollage.draw.operations.bitmap;

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
            fillPointsAlpha(values, p,
                    RepDraw.get().getWidth() / 2.0f / gethMat().getRepository().getScale());
        } else if (getCommand().equals(MutableBit.Command.LINE_3)) {
            fillAlterPointsAlpha(values, p, reperA, reperB);
        }
    }

    @Override
    protected int selectValue() {
        return RepDraw.get().getColor();
    }

    private void fillPointsAlpha(ArrayList<Integer> arr, int[] p, float r){

    }

    private void fillAlterPointsAlpha(ArrayList<Integer> arr, int[] p, int[] a, int[] b){

    }

    private void setPixel(int[] p, int value){
        final int index = p[1]*gethWidth()+p[0];
        if(p[0]>=0&&p[0]<gethWidth()&&p[1]>=0&&p[1]<gethHeight()){
            if(!getCheckeds()[index])implementColor1(index,value);
        }
    }

    private void setGradPixel(int[] p, int value){

    }

    private void setSideGradPixel(int[] p, int value){

    }

    private void implementColor1(int index,int value){
        getPixels()[index] = value;
        getCheckeds()[index]=true;
    }

}
