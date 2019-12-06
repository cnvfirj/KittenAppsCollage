package com.example.kittenappscollage.draw.repozitoryDraw;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;

import com.example.kittenappscollage.helpers.App;

import java.util.Objects;

import static com.example.kittenappscollage.helpers.Massages.ERROR;
import static com.example.kittenappscollage.helpers.Massages.LYTE;

public class RepParams extends Repozitory {

    public final static String KEY_SAVE_WIDTH = "save width";

    public final static String KEY_SAVE_TEXT = "save text";

    public final static String KEY_SAVE_COLOR = "save color";

    public final static String KEY_SAVE_ALPHA = "save alpha";

    private float rWidthTool = 50;

    private int rColorPaint = Color.BLACK;

    private int rAlphaErase = 0;

    private String rText = "Your Enter Text";

    private ListParams rList;

    public void allParams(float width, int color, int alpha, String text){
        setWidth(width);
        setColor(color);
        setAlpha(alpha);
        setText(text);
    }

    public float getWidth() {
        return rWidthTool;
    }

    public RepParams listParams(ListParams list){
        rList = list;
        return this;
    }

    public RepParams setWidth(float width) {
        if(width>0)rWidthTool = width;
        else rWidthTool = 1;
        if(width<=50)rWidthTool = width;
        else rWidthTool = 50;
        if(rList!=null)rList.width(rWidthTool);
        return this;
    }

    public int getColor() {
        return rColorPaint;
    }

    public  RepParams setColor(int color) {
        this.rColorPaint = color;
        if(rList!=null)rList.color(color);
        return this;
    }

    public int getAlpha() {
        return rAlphaErase;
    }

    public  RepParams setAlpha(int alpha) {
        if(alpha>=0)rAlphaErase = alpha;
        else rAlphaErase = 0;
        if(alpha<=255)rAlphaErase = alpha;
        else rAlphaErase = 255;
        if(rList!=null)rList.alpha(alpha);
        return this;
    }

    public String getText() {
        return rText;
    }

    public  RepParams setText(String text) {
        if(text.length()>0)rText = text;
        if(rList!=null)rList.text(text);
        return this;
    }

    public interface ListParams{
        public void color(int c);
        public void alpha(int a);
        public void width(float w);
        public void text(String t);
    }
}
