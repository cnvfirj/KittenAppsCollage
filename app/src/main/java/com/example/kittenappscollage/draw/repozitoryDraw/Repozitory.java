package com.example.kittenappscollage.draw.repozitoryDraw;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PointF;

import com.example.kittenappscollage.draw.saveSteps.BackNextStep;
import com.example.kittenappscollage.helpers.App;
import com.example.mutmatrix.CompRep;
import com.example.mutmatrix.DeformMat;


public class Repozitory {

    public final static int MUTABLE_SIZE = 1;

    public final static int LYR_IMG = 11;

    public final static int LYR_LYR = 12;

    public final static int SINGLE = 13;

    public final static int ALL = 14;

    protected final String NONAME = "noname";

    protected Bitmap rImg, rLyr, rTemp, rOverlay;

    protected DeformMat rImgMat, rLyrMat, rOverMat;

    protected CompRep rTempRep;

    protected Canvas rImgC, rLyrC, rOverC;

    protected RepDraw.Adding rAdd;

    protected RepDraw.Appling rApp;

    protected String rNameProj;

    protected PointF rView;

    protected int rReadiness;

    protected Repozitory() {
        rLyrMat = new DeformMat(App.getMain());
        rImgMat = new DeformMat(App.getMain());
        rOverMat = new DeformMat(App.getMain());
        rNameProj = NONAME;
        rReadiness = 0;
    }

    public void listenerAdd(Adding add){
        rAdd = add;
    }

    public void listenerApp(Appling app){
        rApp = app;
    }

    public Repozitory setName(String name){
        rNameProj = name;
        return this;
    }

    public void viewDraw(PointF view){
        rView = view;
        rLyrMat.view(view);
        rImgMat.view(view);
    }

    public void createOverlay(){
        zeroingBitmap(rOverlay);
        rOverlay = Bitmap.createBitmap((int) rView.x,(int) rView.y, Bitmap.Config.ARGB_8888);
        rOverMat.bitmap(new PointF(rOverlay.getWidth(),rOverlay.getHeight()));
        rOverC = new Canvas(rOverlay);

    }

    public void recycleOverlay(){
            zeroingBitmap(rOverlay);

    }

    public void setImg(Bitmap b, CompRep rep, boolean single){
        zeroingBitmap(rImg);
        zeroingBitmap(rLyr);
        rLyrMat.reset();
        rImgMat.reset();
        if(testBitmap(b)) {
            rImg = b.copy(Bitmap.Config.ARGB_8888, true);
            rImgC = new Canvas(rImg);
            if (rep != null) rImgMat.setRepository(rep);
            else rImgMat.getRepository().setTranslate(new PointF(100,100));
            rImgMat.bitmap(new PointF(b.getWidth(),b.getHeight()));
            if (single&&rAdd!=null) rAdd.readinessImg(true);
            if(single) BackNextStep.get().save(BackNextStep.TARGET_IMG,BackNextStep.MUT_SCALAR);
        }
        zeroingBitmap(b);
    }

    public void setLyr(Bitmap b, CompRep rep, boolean single){
        zeroingBitmap(rLyr);
        rLyrMat.reset();
        if(testBitmap(b)) {
            rLyr = b.copy(Bitmap.Config.ARGB_8888, true);
            rLyrC = new Canvas(rLyr);
            if (rep != null) rLyrMat.setRepository(rep);
            else rLyrMat.getRepository().setTranslate(new PointF(100,100));
            rLyrMat.bitmap(new PointF(b.getWidth(),b.getHeight()));
            if (single&&rAdd != null) rAdd.readinessLyr(true);
            if(single)BackNextStep.get().save(BackNextStep.TARGET_LYR,BackNextStep.MUT_SCALAR);
        }
        zeroingBitmap(b);
    }

    public void addLyr(Bitmap b){
        if(isImg()){
            setLyr(b,null,true);

        } else {
            setImg(b,null,true);

        }
    }

    public boolean isImg(){
        return testBitmap(rImg);
    }

    public boolean isLyr(){
        return testBitmap(rLyr);
    }

    public boolean isOver(){
        return testBitmap(rOverlay);
    }

    protected void zeroingBitmap(Bitmap b){
        if(testBitmap(b)){
            b.recycle();
            b = null;
        }
    }

    protected void zeroingLyr(){
        zeroingBitmap(rLyr);
        rLyrMat.reset();
    }

    protected void zeroingImg(){
        zeroingBitmap(rImg);
        rLyrMat.reset();
    }

    protected boolean testBitmap(Bitmap b){
        return b!=null&&!b.isRecycled();
    }

    public Canvas getOverCanv(){
        return rOverC;
    }

    public Bitmap getImg() {
        return rImg;
    }

    public Bitmap getLyr() {
        return rLyr;
    }

    public Bitmap getOverlay(){
        return rOverlay;
    }

    public DeformMat getIMat() {
        return rImgMat;
    }

    public DeformMat getLMat() {
        return rLyrMat;
    }

    public DeformMat getOMat(){
        return rOverMat;
    }
    public PointF getView() {
        return rView;
    }

    public interface Adding{
        void readinessImg(boolean is);
        void readinessLyr(boolean is);
        void readinessAll(boolean is);
    }

    public interface Appling{
        void delLyr(boolean is);
        void delAll(boolean is);
        void change(boolean is);
    }
}
