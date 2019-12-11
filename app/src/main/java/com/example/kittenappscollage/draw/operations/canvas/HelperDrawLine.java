package com.example.kittenappscollage.draw.operations.canvas;

import com.example.kittenappscollage.draw.operations.bitmap.HelperSershPoints;

import java.util.ArrayList;

public class HelperDrawLine  extends HelperSershPoints {


    @Override
    protected void variableParams(int[] p, ArrayList<int[]> points, ArrayList<Integer> values, int[] reperA, int[] reperB) {
        super.variableParams(p, points, values, reperA, reperB);
    }

    @Override
    protected void applyElastPixel(int[] p, int value) {

    }
}
