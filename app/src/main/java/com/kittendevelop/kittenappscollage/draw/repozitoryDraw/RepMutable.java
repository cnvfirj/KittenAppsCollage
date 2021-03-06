package com.kittendevelop.kittenappscollage.draw.repozitoryDraw;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PointF;

import com.kittendevelop.kittenappscollage.draw.saveSteps.BackNextStep;
import com.example.mutmatrix.CompRep;

//import static com.example.kittenappscollage.helpers.Massages.LYTE;

public class RepMutable extends RepCommonFunctions {

    protected PointF[]rRepers;

    protected boolean rCorrectRepers;

    public RepMutable repers(PointF[]points){
        rRepers = points;
        return this;
    }

    public RepMutable correctRepers(boolean is){
        rCorrectRepers = is;
        return this;
    }

    public void startAllMutable(){
        rReadiness = 0;
    }

    public void startSingleMutable(){
        rReadiness = 1;
    }

    public void mutableMatrix(){

        BackNextStep.get().save(BackNextStep.TARGET_ALL,BackNextStep.MUT_MATRIX);
    }

    public void mutableImg(Bitmap b, CompRep rep, int type, boolean single){
        if(testBitmap(b)){
            rImg = b;
            rImgC = new Canvas(rImg);
            if(type==MUTABLE_SIZE){
                rImgMat
                        .setRepository(rep.copy());
                rImgC = new Canvas(rImg);
            }

            if(single){
                rAdd.readinessImg(true);


                BackNextStep.get().save(BackNextStep.TARGET_IMG,BackNextStep.MUT_SCALAR);
            }else {
                rReadiness++;
                if (rReadiness == 2) {
                    rAdd.readinessAll(true);

                    BackNextStep.get().save(BackNextStep.TARGET_ALL,BackNextStep.MUT_SCALAR);
                }
            }
        }

    }

    public void mutableLyr(Bitmap b, CompRep rep, int type, boolean single){
//        LYTE("mut lyr");
        if(testBitmap(b)){
//            zeroingBitmap(rLyr);
            rLyr = b;
            if(type==MUTABLE_SIZE){
                rLyrMat
//                        .reset()
                        .setRepository(rep.copy());
                rLyrC = new Canvas(rLyr);
            }
            if(single){
                rAdd.readinessLyr(true);
                BackNextStep.get().save(BackNextStep.TARGET_LYR,BackNextStep.MUT_SCALAR);
            }else {
                rReadiness++;
                if (rReadiness == 2) {
                    rAdd.readinessAll(true);
                    BackNextStep.get().save(BackNextStep.TARGET_ALL,BackNextStep.MUT_SCALAR);
                }
            }
        }
//        else LYTE("bed lyr");
    }

    public void zeroingRepers(){
        rRepers = null;
        rCorrectRepers = false;
    }

    public PointF[] getRepers(){
        return rRepers;
    }

    public boolean isCorrectRepers(){
        return rCorrectRepers;
    }
}
