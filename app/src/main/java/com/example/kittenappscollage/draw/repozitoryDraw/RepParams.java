package com.example.kittenappscollage.draw.repozitoryDraw;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;

import com.example.kittenappscollage.helpers.App;

import java.util.Objects;

import static com.example.kittenappscollage.helpers.Massages.ERROR;
import static com.example.kittenappscollage.helpers.Massages.LYTE;

public class RepParams extends Repozitory {

    public final static String KEY_SAVE_WIDTH = "save width";

    public final static String KEY_SAVE_COLOR = "save color";

    public final static String KEY_SAVE_ALPHA = "save alpha";

    private float rWidthTool = 50;

    private float rItalicText = 0;

    private int rColorPaint = Color.BLACK;

    private int rAlphaErase = 0;

    private int rAlphaInColor = 255;

    private boolean rTextItalic;

    private boolean rTextFill;

    private String rText = "Your Enter Text";

    private ListParams rList;

    private Typeface rShrift;

    private int sourceFont;

    private String pathFont;

    public void drawParams(float width, int color, int alpha){
        setWidth(width);
        setColor(color);
        setAlpha(alpha);
    }

    public void textParams(String text,boolean italis, boolean fill){
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
        if(width<=200)rWidthTool = width;
        else rWidthTool = 200;
        if(rList!=null)rList.width(rWidthTool);
        return this;
    }

    public int getColor() {
        return rColorPaint;
    }

    public  RepParams setColor(int color) {
        this.rColorPaint = color;
        rAlphaInColor = Color.alpha(color);
        if(rList!=null)rList.color(color);
        return this;
    }

    public int getAlpha() {
        return rAlphaErase;
    }

    public int getAlphaInColor(){
        return rAlphaInColor;
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

    public RepParams textItalic(boolean i){
        rTextItalic = i;
        return this;
    }

    public boolean isTextItalic(){
        return rTextItalic;
    }

    public RepParams textFill(boolean f){
        rTextFill = f;
        return this;
    }

    public boolean isTextFill(){
        return rTextFill;
    }

    public RepParams setShrift(Typeface shrift){
        rShrift = shrift;
        return this;
    }

    public Typeface getShrift(){
        return rShrift;
    }

    public RepParams setItalicText(float angle){
        rItalicText = angle;
        return this;
    }

    public float getItalicText(){
        return rItalicText;
    }

    public RepParams setSourceFont(int s){
        sourceFont = s;
        return this;
    }

    public int getSourceFont(){
        return sourceFont;
    }

    public RepParams setPathFont(String path){
        pathFont = path;
        return this;
    }

    public String getPathFont(){
        return pathFont;
    }



    public interface ListParams{
        public void color(int c);
        public void alpha(int a);
        public void width(float w);
        public void text(String t);
    }
}
