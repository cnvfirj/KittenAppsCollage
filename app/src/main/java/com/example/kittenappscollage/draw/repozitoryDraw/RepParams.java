package com.example.kittenappscollage.draw.repozitoryDraw;

import android.graphics.Color;

import static com.example.kittenappscollage.helpers.Massages.LYTE;

public class RepParams extends Repozitory {

    private float rWidthTool = 50;

    private int rColorPaint = Color.BLACK;

    private int rAlphaErase = 0;

    private String rText = "Enter TEXT";

    public void allParams(float width, int color, int alpha, String text){
        setWidth(width);
        setColor(color);
        setAlpha(alpha);
        setText(text);
    }
    public float getWidth() {
        return rWidthTool;
    }

    public void setWidth(float width) {
        if(width>0)rWidthTool = width;
        else rWidthTool = 1;
        if(width<=50)rWidthTool = width;
        else rWidthTool = 50;
//        rWidthTool = width;
    }

    public int getColor() {
        return rColorPaint;
    }

    public void setColor(int color) {
        this.rColorPaint = color;
    }

    public int getAlpha() {
        return rAlphaErase;
    }

    public void setAlpha(int alpha) {
        if(alpha>=0)rAlphaErase = alpha;
        else rAlphaErase = 0;
        if(alpha<=255)rAlphaErase = alpha;
        else rAlphaErase = 255;
    }

    public String getText() {
        return rText;
    }

    public void setText(String text) {
        if(text.length()>0)rText = text;
    }
}
