package com.example.kittenappscollage.draw.operations.canvas;

import android.graphics.Canvas;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;

import com.example.kittenappscollage.draw.repozitoryDraw.RepDraw;

public class HelperDrawLine {

        protected PointF hPoint;

        protected Canvas hCanvas;

        protected Paint hPaint;

        protected Path hPath;

        protected float hSize;

        public HelperDrawLine() {
            hPath = new Path();
            hPaint = new Paint();
        }

        public void draw(){
            hCanvas.drawPath(hPath,hPaint);
        }

        public void reset(){
            hPath.reset();
        }

        public HelperDrawLine start(PointF start){
            reset();
            hPoint = new PointF(start.x,start.y);
            hPath.moveTo(start.x,start.y);
            return this;
        }

        public HelperDrawLine move(PointF move){
            hPoint = new PointF(move.x,move.y);
            hPath.lineTo(move.x,move.y);
            return this;
        }

        public HelperDrawLine fin(PointF fin){
            hPoint = fin;
            return this;
        }

        public HelperDrawLine fill(Paint.Style style){
            hPaint.setStyle(style);
            return this;
        }

        public HelperDrawLine size(float size){
            hSize = size;
            hPaint.setStrokeWidth(size);
            hPaint.setPathEffect(new CornerPathEffect(size));
            return this;
        }

        public HelperDrawLine color(int color){
            hPaint.setColor(color);
            return this;
        }

        public HelperDrawLine canvas(Canvas canvas){
            hCanvas = canvas;
            return this;
        }

        public PointF getPoint(){
            if(hPoint==null){
                hPoint = new PointF(300,300);
            }
            return hPoint;
        }

        public float getSize() {
            return hSize;
        }

}
