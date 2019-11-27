package com.example.kittenappscollage.draw.repozitoryDraw;

import android.graphics.Color;

public class RepParams extends Repozitory {

    private float rWidthTool = 20;

    private int rColorPaint = Color.BLACK;

    private int rAlphaErase = 0;

    private String rText = "Enter TEXT";


    public float getWidthTool() {
        return rWidthTool;
    }

    public void setWidthTool(float rWidthTool) {
        this.rWidthTool = rWidthTool;
    }

    public int getColorPaint() {
        return rColorPaint;
    }

    public void setColorPaint(int rColorPaint) {
        this.rColorPaint = rColorPaint;
    }

    public int getAlphaErase() {
        return rAlphaErase;
    }

    public void setAlphaErase(int rAlphaErase) {
        this.rAlphaErase = rAlphaErase;
    }

    public String getText() {
        return rText;
    }

    public void setText(String rText) {
        this.rText = rText;
    }
}
