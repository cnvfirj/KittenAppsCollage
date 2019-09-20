package com.example.kittenappscollage.draw.operations.bitmap;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.Region;
import android.os.Build;

import com.example.mutablebitmap.DeformMat;


public class DrawBitmap {
    private Canvas dCanvas;
    private DeformMat dMat;
    private Paint dPaint;
    private Rect dRect;

    private DrawBitmap(Canvas canvas, DeformMat mat) {
        dCanvas = canvas;
        dMat = mat;
        dPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    private DrawBitmap(Bitmap img, DeformMat mat, DeformMat clip){
        dCanvas = new Canvas(img);
        dMat = mat;
        dPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        PointF[] pClip = clip.muteDeformLoc(DeformMat.Coordinates.DISPLAY_ROTATE_DEFORM);

        for (int i=0;i<pClip.length;i++){
            pClip[i] = mat.getPointBitmap(pClip[i]);
            if(pClip[i].x<0){
                pClip[i].x=0;
            }else if(pClip[i].x>img.getWidth()){
                pClip[i].x=img.getWidth();
            }
            if(pClip[i].y<0){
                pClip[i].y=0;
            }else if(pClip[i].y>img.getHeight()){
                pClip[i].y=img.getHeight();
            }

        }
        dRect = new Rect((int)pClip[0].x,(int)pClip[0].y,(int)pClip[2].x,(int)pClip[2].y);

    }

    public static DrawBitmap create(Canvas canvas, DeformMat mat){
        return new DrawBitmap(canvas,mat);
    }

    public static DrawBitmap create(Bitmap img, DeformMat mat, DeformMat clip){
        return new DrawBitmap(img,mat,clip);
    }

    public void draw(Bitmap bitmap, DeformMat mat){
        float scaleImg = dMat.getRepository().getScale();
        float scaleLyr = mat.getRepository().getScale();
        float scaleCom = scaleLyr/scaleImg;
        Matrix matrix = new Matrix();
        matrix.setPolyToPoly(mat.getSrc(),0,mat.getRepository().getDeform(),0,4);
        matrix.postScale(scaleCom,scaleCom);
        matrix.postRotate(mat.getRepository().getRotate());
        PointF pointImg = dMat.getPointBitmap(mat.getRepository().getTranslate());
        matrix.postTranslate(pointImg.x,pointImg.y);
        dCanvas.drawBitmap(bitmap,matrix,dPaint);
    }

    public void drawSolo(Bitmap ov, DeformMat ovl){
        float scaleImg = dMat.getRepository().getScale();
        float scaleLyr = ovl.getRepository().getScale();
        float scaleCom = scaleLyr/scaleImg;
        Matrix matrix = new Matrix();
        matrix.setPolyToPoly(ovl.getSrc(),0,ovl.getRepository().getDeform(),0,4);
        matrix.postScale(scaleCom,scaleCom);
        matrix.postRotate(ovl.getRepository().getRotate());
        PointF pointImg = dMat.getPointBitmap(ovl.getRepository().getTranslate());
        matrix.postTranslate(pointImg.x,pointImg.y);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            dCanvas.clipOutRect(dRect);
        } else {
            dCanvas.clipRect(dRect, Region.Op.DIFFERENCE);
        }

        dCanvas.drawBitmap(ov,matrix,dPaint);

    }


}
