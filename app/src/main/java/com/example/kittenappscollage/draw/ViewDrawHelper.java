package com.example.kittenappscollage.draw;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;

import com.example.kittenappscollage.draw.operations.TouchPoints;
import com.example.mutmatrix.DeformMat;
import com.example.mutmatrix.actions.Deform;

public class ViewDrawHelper {
    private Paint hPaint;
    private Path hPath;


    public ViewDrawHelper() {
        hPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        hPath = new Path();
    }

    public void drawShadow(Canvas c, PointF[]repers,int color){
        hPath.reset();
        hPaint.setStyle(Paint.Style.FILL);
        hPaint.setColor(color);
        hPaint.setStrokeWidth(1);
        if(repers!=null) {
            hPath.reset();
            hPath.moveTo(0, 0);
            hPath.lineTo(repers[TouchPoints.TOP_LEFT].x, 0);
            hPath.lineTo(repers[TouchPoints.BOTTOM_LEFT].x, repers[TouchPoints.BOTTOM_LEFT].y);
            hPath.lineTo(repers[TouchPoints.BOTTOM_RIGHT].x, repers[TouchPoints.BOTTOM_RIGHT].y);
            hPath.lineTo(repers[TouchPoints.TOP_RIGHT].x, repers[TouchPoints.TOP_RIGHT].y);
            hPath.lineTo(repers[TouchPoints.TOP_LEFT].x, repers[TouchPoints.TOP_LEFT].y);
            hPath.lineTo(repers[TouchPoints.TOP_LEFT].x, 0);
            hPath.lineTo(c.getWidth(), 0);
            hPath.lineTo(c.getWidth(), c.getHeight());
            hPath.lineTo(0, c.getHeight());
            hPath.close();
        }else {
            hPath.moveTo(0,0);
            hPath.lineTo(c.getWidth(),0);
            hPath.lineTo(c.getWidth(),c.getHeight());
            hPath.lineTo(0,c.getHeight());
            hPath.close();
        }
        c.drawPath(hPath,hPaint);
        if(repers!=null&&repers[TouchPoints.CENTER]!=null){
            addRepers(c,repers);
        }
    }

    public void drawInfo(Canvas c, DeformMat img, String text, int color){
        hPaint.setStyle(Paint.Style.STROKE);
        hPaint.setColor(color);
        hPaint.setAlpha(255);
        hPaint.setStrokeWidth(2);
        hPaint.setTextSize(50);
        PointF[]p = img.muteDeformLoc(Deform.Coordinates.DISPLAY_ROTATE_DEFORM);
        createPathInfo(p);
        c.drawPath(hPath,hPaint);
        String info = text+":"+createInfo(img);
        hPaint.setTextSize(25);
        hPaint.setStrokeWidth(1);
        hPaint.setStyle(Paint.Style.FILL);
        c.drawTextOnPath(info,hPath,0,0,hPaint);
    }

    private void createPathInfo(PointF[]points){
        hPath.reset();
        /*sersh min*/
        PointF min = new PointF();
        PointF max = new PointF();
        for (int i=0;i<points.length;i++){
            if(i==0){
                min.set(points[i].x,points[i].y);
                max.set(points[i].x,points[i].y);
                continue;
            }
            if(points[i].x<min.x)min.set(points[i].x,min.y);
            if(points[i].y<min.y)min.set(min.x,points[i].y);

            if(points[i].x>max.x)max.set(points[i].x,max.y);
            if(points[i].y>max.y)max.set(max.x,points[i].y);
        }

        hPath.addRect(min.x,min.y,max.x,max.y, Path.Direction.CW);

    }

    private void addRepers(Canvas c, PointF[] repers){
        float width = 5;
        float touch = c.getWidth()/20;

        hPath.reset();
        hPath.moveTo(repers[TouchPoints.CENTER].x-touch,repers[TouchPoints.CENTER].y-touch);
        hPath.lineTo(repers[TouchPoints.CENTER].x+touch,repers[TouchPoints.CENTER].y-touch);
        hPath.lineTo(repers[TouchPoints.CENTER].x+touch,repers[TouchPoints.CENTER].y+touch);
        hPath.lineTo(repers[TouchPoints.CENTER].x-touch,repers[TouchPoints.CENTER].y+touch);
        hPath.close();

        c.drawPath(hPath,hPaint);

        hPaint.setStrokeWidth(width);
        hPaint.setColor(Color.WHITE);
        hPaint.setStyle(Paint.Style.STROKE);
        hPath.reset();

        hPath.moveTo(repers[TouchPoints.CENTER].x,repers[TouchPoints.CENTER].y+width*2-touch);
        hPath.lineTo(repers[TouchPoints.CENTER].x,repers[TouchPoints.CENTER].y-width*2+touch);

        hPath.moveTo(repers[TouchPoints.CENTER].x+width*2-touch,repers[TouchPoints.CENTER].y);
        hPath.lineTo(repers[TouchPoints.CENTER].x-width*2+touch,repers[TouchPoints.CENTER].y);

        hPath.moveTo(repers[TouchPoints.TOP_LEFT].x-width*2,repers[TouchPoints.TOP_LEFT].y+touch);
        hPath.lineTo(repers[TouchPoints.TOP_LEFT].x-width*2,repers[TouchPoints.TOP_LEFT].y-width*2);
        hPath.lineTo(repers[TouchPoints.TOP_LEFT].x+touch,repers[TouchPoints.TOP_LEFT].y-width*2);

        hPath.moveTo(repers[TouchPoints.TOP].x-touch,repers[TouchPoints.TOP].y-width*2);
        hPath.lineTo(repers[TouchPoints.TOP].x+touch,repers[TouchPoints.TOP].y-width*2);

        hPath.moveTo(repers[TouchPoints.TOP_RIGHT].x-touch,repers[TouchPoints.TOP_RIGHT].y-width*2);
        hPath.lineTo(repers[TouchPoints.TOP_RIGHT].x+width*2,repers[TouchPoints.TOP_RIGHT].y-width*2);
        hPath.lineTo(repers[TouchPoints.TOP_RIGHT].x+width*2,repers[TouchPoints.TOP_RIGHT].y+touch);

        hPath.moveTo(repers[TouchPoints.RIGHT].x+width*2,repers[TouchPoints.RIGHT].y-touch);
        hPath.lineTo(repers[TouchPoints.RIGHT].x+width*2,repers[TouchPoints.RIGHT].y+touch);

        hPath.moveTo(repers[TouchPoints.BOTTOM_RIGHT].x+width*2,repers[TouchPoints.BOTTOM_RIGHT].y-touch);
        hPath.lineTo(repers[TouchPoints.BOTTOM_RIGHT].x+width*2,repers[TouchPoints.BOTTOM_RIGHT].y+width*2);
        hPath.lineTo(repers[TouchPoints.BOTTOM_RIGHT].x-touch,repers[TouchPoints.BOTTOM_RIGHT].y+width*2);

        hPath.moveTo(repers[TouchPoints.BOTTOM].x-touch,repers[TouchPoints.BOTTOM].y+width*2);
        hPath.lineTo(repers[TouchPoints.BOTTOM].x+touch,repers[TouchPoints.BOTTOM].y+width*2);

        hPath.moveTo(repers[TouchPoints.BOTTOM_LEFT].x+touch,repers[TouchPoints.BOTTOM_LEFT].y+width*2);
        hPath.lineTo(repers[TouchPoints.BOTTOM_LEFT].x-width*2,repers[TouchPoints.BOTTOM_LEFT].y+width*2);
        hPath.lineTo(repers[TouchPoints.BOTTOM_LEFT].x-width*2,repers[TouchPoints.BOTTOM_LEFT].y-touch);

        hPath.moveTo(repers[TouchPoints.LEFT].x-width*2,repers[TouchPoints.LEFT].y-touch);
        hPath.lineTo(repers[TouchPoints.LEFT].x-width*2,repers[TouchPoints.LEFT].y+touch);


        c.drawPath(hPath,hPaint);
    }


    private String createInfo(DeformMat m){
        String scale = ""+m.getRepository().getScale();
        if(scale.length()>4)scale = (scale).substring(0,4);
        String rotate = ""+(int)m.getRepository().getRotate();
        return ""+(int)m.getRepository().getBitmap().x+"/"+(int)m.getRepository().getBitmap().y+" |scale: "+scale+" |rotate: "+rotate;
    }
}
