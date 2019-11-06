package com.example.kittenappscollage.draw.operations.bitmap;

import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;

import com.example.kittenappscollage.draw.repozitoryDraw.RepDraw;
import com.example.mutablebitmap.DeformMat;

public class HelpCut {

    private Rect hCut;
    private DeformMat hMat;

    /*даем сюда матрицу*/
    public HelpCut mat(DeformMat mat){
        this.hMat = mat;
        return this;
    }
    /*даем область обреза по экрану*/
    public HelpCut cut(Rect cut) {
        this.hCut = cut;
        return this;
    }

    public HelpCut cut(RectF cut) {
        this.hCut = new Rect((int)cut.left,(int)cut.top,(int)cut.right,(int)cut.bottom);
        return this;
    }


    /*применяем обрезание по присланному битмап*/
    public Bitmap apply(Bitmap bitmap){
        Rect r = correctionRect(editCut(),bitmap);
        if(r.left>bitmap.getWidth()||r.right<0||r.top>bitmap.getHeight()||r.bottom<0)return bitmap;
        correctMat(r);
        return Bitmap.createBitmap(bitmap,r.left,r.top,r.width(),r.height());
    }

    /*получаем матрицу*/
    public DeformMat getMat(){
        return hMat;
    }

    /*получаем область обрезания по экрану*/
    public Rect getCut() {
        return hCut;
    }

    /*корректируем область обреза соответственную с границами*/
    private Rect correctionRect(RectF rect, Bitmap bitmap){
        int sX = rect.left>=0?(int) rect.left:0;
        int sY = rect.top>=0?(int) rect.top:0;
        int fX = rect.right<bitmap.getWidth()?(int) rect.right:bitmap.getWidth();
        int fY = rect.bottom<bitmap.getHeight()?(int) rect.bottom:bitmap.getHeight();

        return new Rect(sX,sY,fX,fY);
    }

    /*создаем область обреза соответственно с масштабом и положением битмап*/
    private RectF editCut(){
        PointF p_1 = pointBitmap(new PointF(getCut().left,getCut().top));
        PointF p_2 = pointBitmap(new PointF(getCut().right, getCut().bottom));
        return new RectF(p_1.x,p_1.y,p_2.x,p_2.y);
    }

    /*получаем из точки на экране, точку соответствующую битмап*/
    private PointF pointBitmap(PointF p){
        return hMat.getPointBitmap(p);
    }

    /*изменяем матрицу соответственно с обрезом*/
    private void correctMat(Rect r){
        final float scale = hMat.getRepository().getScale();
        final PointF trans = hMat.getRepository().getTranslate();
        hMat.reset();
        hMat.view(RepDraw.get().getView()).bitmap(new PointF(r.width(),r.height()));
        hMat.getRepository().setScale(scale);
        hMat.getRepository().setTranslate(trans(r,trans));
    }

    /*формируем сдвиг*/
    private PointF trans(Rect r, PointF trans){
        final float scale = hMat.getRepository().getScale();
        trans.x = trans.x+r.left*scale;
        trans.y = trans.y+r.top*scale;
        return trans;
    }
}
