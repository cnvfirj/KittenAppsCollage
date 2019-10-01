package com.example.kittenappscollage.draw;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PointF;

import androidx.annotation.Nullable;

import com.example.kittenappscollage.draw.operations.bitmap.CoercionBitmap;
import com.example.kittenappscollage.draw.operations.bitmap.DrawBitmap;
import com.example.mutablebitmap.CompRep;
import com.example.mutablebitmap.DeformMat;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static com.example.kittenappscollage.helpers.Massages.LYTE;

public class RepDraw {

    public final static int MUTABLE_SIZE = 1;

    public final static int MUTABLE_CONTENT = 2;

    public final static int LYR_IMG = 11;

    public final static int LYR_LYR = 12;

    private final String NONAME = "noname";

    private static RepDraw singleton;

    private Bitmap rImg, rLyr, rTemp;

    private DeformMat rImgMat, rLyrMat;

    private CompRep rTempRep;

    private Canvas rImgC, rLyrC;

    private Adding rAdd;

    private Appling rApp;

    private String rNameProj;

    private PointF rView;

    private PointF[]rRepers;

    private boolean rCorrectRepers;

    private RepDraw() {
        rLyrMat = new DeformMat();
        rImgMat = new DeformMat();
        rNameProj = NONAME;
    }

    public static RepDraw get(){
        if(singleton==null){
            synchronized (RepDraw.class){
                singleton = new RepDraw();
            }
        }
        return singleton;
    }

    public RepDraw listenerAdd(Adding add){
        rAdd = add;
        return this;
    }

    public RepDraw listenerApp(Appling app){
        rApp = app;
        return this;
    }

    public RepDraw setName(String name){
        rNameProj = name;
        return this;
    }

    public RepDraw viewDraw(PointF view){
        rView = view;
        rLyrMat.view(view);
        rImgMat.view(view);
        return this;
    }

    public RepDraw repers(PointF[]points){
        rRepers = points;
        return this;
    }

    public RepDraw correctRepers(boolean is){
        rCorrectRepers = is;
        return this;
    }

    public void mutableImg(Bitmap b, @Nullable CompRep rep, int type, boolean single){
        if(testBitmap(b)){
            rImg = b;
            if(type==MUTABLE_SIZE){
                rImgMat.reset().setRepository(rep.copy());
                rImgC = new Canvas(rImg);
            }
            if(single)rAdd.readinessImg(true);
        }
    }

    public void mutableLyr(Bitmap b, @Nullable CompRep rep, int type, boolean single){
        if(testBitmap(b)){
            rLyr = b;
            if(type==MUTABLE_SIZE){
                rLyrMat.reset().setRepository(rep.copy());
                rLyrC = new Canvas(rLyr);
            }
            if(single)rAdd.readinessLyr(true);
        }
    }

    public void mutableAll(Bitmap img, CompRep repImg, int typImg, Bitmap lyr, CompRep repLyr, int typLyr){
        mutableImg(img,repImg,typImg,false);
        mutableLyr(lyr,repLyr,typLyr,false);
        rAdd.readinessAll(true);
    }

    public void addLyr(Bitmap b){
        if(isImg())setLyr(b,null,true);
        else setImg(b,null,true);
    }
    /*single - это если добавляем только один элемент*/

    public void setImg(Bitmap b, CompRep rep, boolean single){
        zeroingBitmap(rImg);
        rImgMat.reset();
        if(testBitmap(b)) {

            if(rNameProj.equals(NONAME))rNameProj = PropertiesImage.NAME_IMAGE();
            rImg = b.copy(Bitmap.Config.ARGB_8888, true);
            rImgC = new Canvas(rImg);
            if (rep != null) rImgMat.setRepository(rep);
            rImgMat.bitmap(new PointF(b.getWidth(),b.getHeight()));
            if (single&&rAdd!=null) {
                rAdd.readinessImg(true);
            }
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
            rLyrMat.bitmap(new PointF(b.getWidth(),b.getHeight()));
            if (single&&rAdd != null) rAdd.readinessLyr(true);
        }
        zeroingBitmap(b);
    }

    public void setAll(Bitmap img, CompRep repImg, Bitmap lyr, CompRep repLyr){
        setImg(img,repImg,false);
        setLyr(lyr,repLyr,false);
        if(rNameProj.equals(NONAME))rNameProj = PropertiesImage.NAME_IMAGE();
        if(rAdd!=null)rAdd.readinessAll(true);

    }

    public void union(){
        if(isLyr()){
            correctImg();
            DrawBitmap.create(rImgC,rImgMat).draw(rLyr,rLyrMat);
            zeroingLyr();
            rAdd.readinessImg(true);
        }
    }

    public void delAll(){
        zeroingBitmap(rImg);
        zeroingBitmap(rLyr);
        rImgMat.reset();
        rLyrMat.reset();
        if(rApp!=null)rApp.delAll(true);
        rNameProj = NONAME;
    }

    public void delLyr(){
        zeroingBitmap(rLyr);
        rLyrMat.reset();
        if(rApp!=null)rApp.delLyr(true);
    }

    public void change(){
        if(isImg()&&isLyr()) {
            rTemp = rImg.copy(Bitmap.Config.ARGB_8888, true);
            rTempRep = rImgMat.getRepository().copy();
            zeroingBitmap(rImg);
            rImgMat.reset();

            rImg = rLyr.copy(Bitmap.Config.ARGB_8888, true);
            rImgC = new Canvas(rImg);
            rImgMat.setRepository(rLyrMat.getRepository().copy());
            zeroingBitmap(rLyr);
            rLyrMat.reset();

            rLyr = rTemp.copy(Bitmap.Config.ARGB_8888, true);
            rLyrC = new Canvas(rLyr);
            rLyrMat.setRepository(rTempRep.copy());
            zeroingBitmap(rTemp);
            rTempRep.reset();
            if (rApp != null) rApp.change(true);
        }
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
        return rImgMat;
    }

    public DeformMat getLMat() {
        return rLyrMat;
    }

    public PointF getView() {
        return rView;
    }

    public PointF[] getRepers(){
        return rRepers;
    }

    public boolean isCorrectRepers(){
        return rCorrectRepers;
    }

    public String getrNameProj(){
        return rNameProj;
    }

    public void zeroingImg(){
        zeroingBitmap(rImg);
    }

    public void zeroingLyr(){
        zeroingBitmap(rLyr);
    }

    public void zeroingRepers(){
        rRepers = null;
        rCorrectRepers = false;
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




    private void correctImg(){
        if(isImg()){
            Bitmap temp = CoercionBitmap.blankBitmap(rImgMat);
            CoercionBitmap.drawBitmap(new Canvas(temp), CoercionBitmap.matrixBitmap(rImgMat), rImg);
            rImg = temp.copy(Bitmap.Config.ARGB_8888, true);
            rImgC = new Canvas(rImg);
            float scale = rImgMat.getRepository().getScale();
            PointF translate = CoercionBitmap.transBitmap(rImgMat);
            rImgMat.reset();
            rImgMat.view(rView).bitmap(new PointF(temp.getWidth(), temp.getHeight()));
            rImgMat.getRepository().setScale(scale);
            rImgMat.getRepository().setTranslate(translate);
            zeroingBitmap(temp);
        }
    }

    private void correctLyr(){
        if(isLyr()){
            Bitmap temp = CoercionBitmap.blankBitmap(rLyrMat);
            CoercionBitmap.drawBitmap(new Canvas(temp), CoercionBitmap.matrixBitmap(rLyrMat), rLyr);
            rLyr = temp.copy(Bitmap.Config.ARGB_8888, true);
            rLyrC = new Canvas(rImg);
            float scale = rLyrMat.getRepository().getScale();
            PointF translate = CoercionBitmap.transBitmap(rLyrMat);
            rLyrMat.reset();
            rLyrMat.view(rView).bitmap(new PointF(temp.getWidth(), temp.getHeight()));
            rLyrMat.getRepository().setScale(scale);
            rLyrMat.getRepository().setTranslate(translate);
            zeroingBitmap(temp);
        }
    }



    public boolean isZeroing(Bitmap b){
        if(b!=null&&!b.isRecycled())return true;
        else return false;
    }

    private void zeroingBitmap(Bitmap b){
        if(testBitmap(b)){
            b.recycle();
            b = null;
        }
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

    public interface Mutable{
        void mutLyr(boolean is);
        void mutAll(boolean is);
        void mutImg(boolean is);
    }


    public static class PropertiesImage {

        public static String MIME_TYPE_IMAGE = "image/png";

        public static String NAME_IMAGE(){

            return date()+".png";
        }

        public static String NAME_PROJECT(){
            return date();
        }

        private static String date(){
            Calendar calendar = new GregorianCalendar();

            int year = calendar.get(Calendar.YEAR);
            int day = calendar.get(Calendar.DAY_OF_YEAR);
            int hour = calendar.get(Calendar.HOUR);
            int minute = calendar.get(Calendar.MINUTE);
            int second = calendar.get(Calendar.SECOND);

            int second_of_day = ((hour*60)+minute)*60+second;

            return year+"_"+day+"_"+second_of_day;
        }
    }
}
