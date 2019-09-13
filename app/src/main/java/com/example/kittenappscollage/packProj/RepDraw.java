package com.example.kittenappscollage.packProj;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.example.kittenappscollage.draw.fragment.AddLyrsFragmentDraw;
import com.example.mutablebitmap.CompRep;
import com.example.mutablebitmap.DeformMat;

import static com.example.kittenappscollage.helpers.Massages.MASSAGE;

public class RepDraw {

    private static RepDraw singleton;

    private Bitmap rImg, rLyr;

    private DeformMat rIMat, rLMat;

    private Canvas rImgC, rLyrC;

    private Adding rAdd;


    private RepDraw() {
        rLMat = new DeformMat();
        rIMat = new DeformMat();
    }

    public static RepDraw get(){
        if(singleton==null){
            synchronized (RepDraw.class){
                singleton = new RepDraw();
            }
        }
        return singleton;
    }

    public RepDraw listener(Adding add){
        rAdd = add;
        return this;
    }

    public void addLyr(Bitmap b){
        if(isImg())setLyr(b,null,true);
        else setImg(b,null,true);
    }
    /*single - это если добавляем только один элемент*/

    public void setImg(Bitmap b, CompRep rep, boolean single){
        zeroingBitmap(rImg);
        if(testBitmap(b)) {
            rImg = b.copy(Bitmap.Config.ARGB_8888, true);
            rImgC = new Canvas(rImg);
            rIMat.reset();
            if (rep != null) rIMat.setRepository(rep);
            if (single&&rAdd!=null) {
                rAdd.readinessImg(true);
            }
        }
        zeroingBitmap(b);
    }

    public void setLyr(Bitmap b, CompRep rep, boolean single){
        zeroingBitmap(rLyr);
        if(testBitmap(b)) {
            rLyr = b.copy(Bitmap.Config.ARGB_8888, true);
            rLyrC = new Canvas(rLyr);
            rLMat.reset();
            if (rep != null) rLMat.setRepository(rep);
            if (single&&rAdd != null) rAdd.readinessLyr(true);
        }
        zeroingBitmap(b);
    }

    public RepDraw setAll(Bitmap img, CompRep repImg, Bitmap lyr, CompRep repLyr){
        setImg(img,repImg,false);
        setLyr(lyr,repLyr,false);
        if(rAdd!=null)rAdd.readinessAll(true);
        return this;
    }

    public Canvas getImgCanv(){
        return rImgC;
    }

    public Canvas getrLyrCanv(){
        return rLyrC;
    }

    public Bitmap getImg() {
        return rImg;
    }

    public Bitmap getLyr() {
        return rLyr;
    }

    public DeformMat getIMat() {
        return rIMat;
    }

    public DeformMat getLMat() {
        return rLMat;
    }

    public void zeroingImg(){
        zeroingBitmap(rImg);
    }

    public void zeroingLyr(){
        zeroingBitmap(rLyr);
    }

    private void zeroingBitmap(Bitmap b){
        if(testBitmap(b)){
            b.recycle();
            b = null;
        }
    }

    public boolean isImg(){
        return testBitmap(rImg);
    }

    public boolean isLyr(){
        return testBitmap(rLyr);
    }

    private boolean testBitmap(Bitmap b){
        return b!=null&&!b.isRecycled();
    }



    public interface Adding{
        void readinessImg(boolean is);
        void readinessLyr(boolean is);
        void readinessAll(boolean is);
    }
}
