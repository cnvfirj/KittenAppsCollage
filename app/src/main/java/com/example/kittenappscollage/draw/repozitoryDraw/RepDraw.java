package com.example.kittenappscollage.draw.repozitoryDraw;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PointF;

import com.example.kittenappscollage.draw.operations.bitmap.CoercionBitmap;
import com.example.kittenappscollage.draw.operations.bitmap.DrawBitmap;
import com.example.kittenappscollage.draw.saveSteps.BackNextStep;
import com.example.mutablebitmap.CompRep;
import com.example.mutablebitmap.DeformMat;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static com.example.kittenappscollage.helpers.Massages.LYTE;

public class RepDraw extends RepMutable{

    private static RepDraw singleton;



    public static RepDraw get(){
        if(singleton==null){
            synchronized (RepDraw.class){
                singleton = new RepDraw();
            }
        }
        return singleton;
    }




        public void stepLoadImg(Bitmap b, CompRep rep,boolean single){
        if(testBitmap(b)){
            rImg = b;
                rImgMat.reset().setRepository(rep.copy());
                rImgMat.view(rView).bitmap(rep.getBitmap());
                rImgC = new Canvas(rImg);
            if(single){
                rAdd.readinessImg(true);
            }else {
                rReadiness++;
                if (rReadiness == 2) {
                    rAdd.readinessAll(true);
                }
            }
        }else {
            zeroingImg();
            if(single){
                rAdd.readinessImg(true);
            }else {
                rReadiness++;
                if (rReadiness == 2) {
                    rAdd.readinessAll(true);
                }
            }
        }
    }

    public void stepLoadLyr(Bitmap b, CompRep rep, boolean single){
        if(testBitmap(b)){
            rLyr = b;
                rLyrMat.reset().setRepository(rep.copy());
                rLyrMat.view(rView).bitmap(rep.getBitmap());
                rLyrC = new Canvas(rLyr);
            if(single){
                rAdd.readinessLyr(true);
            }else {
                rReadiness++;
                if (rReadiness == 2) {
                    rAdd.readinessAll(true);
                }
            }
        }else {
            zeroingLyr();
            if(single){
                rAdd.readinessLyr(true);
            }else {
                rReadiness++;
                if (rReadiness == 2) {
                    rAdd.readinessAll(true);
                }
            }
        }
    }

    public void stepLoadMatr(CompRep img, CompRep lyr){
        if(img!=null)rImgMat.reset().setRepository(img.copy());
        if(lyr!=null)rLyrMat.reset().setRepository(lyr.copy());
        rAdd.readinessAll(true);
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
