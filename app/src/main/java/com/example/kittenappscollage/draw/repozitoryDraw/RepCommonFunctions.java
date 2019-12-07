package com.example.kittenappscollage.draw.repozitoryDraw;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PointF;

import com.example.kittenappscollage.draw.operations.bitmap.CoercionBitmap;
import com.example.kittenappscollage.draw.operations.bitmap.DrawBitmap;
import com.example.kittenappscollage.draw.saveSteps.BackNextStep;
import com.example.kittenappscollage.helpers.App;
import com.example.mutmatrix.CompRep;
import com.example.mutmatrix.DeformMat;

public class RepCommonFunctions extends RepParams {

    public void union(){
        if(isLyr()){
            correctImg();
            DrawBitmap.create(rImgC,rImgMat).draw(rLyr,rLyrMat);
            zeroingLyr();
            rAdd.readinessImg(true);
            BackNextStep.get().union();
        }
    }

    public void delAll(){
        zeroingBitmap(rImg);
        zeroingBitmap(rLyr);
        rImgMat.reset();
        rLyrMat.reset();
        if(rApp!=null)rApp.delAll(true);
        rNameProj = NONAME;
        BackNextStep.get().remove();
    }

    public void clearRep(){
        zeroingBitmap(rImg);
        zeroingBitmap(rLyr);
        rImgMat.reset();
        rLyrMat.reset();
    }

    public void delLyr(){
        zeroingBitmap(rLyr);
        rLyrMat.reset();
        if(rApp!=null)rApp.delLyr(true);
        BackNextStep.get().deleteLyr();
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
            BackNextStep.get().change();
        }
    }

    public void correctImg(){
        correctOpImg();
//        if(isImg()){
//            Bitmap temp = CoercionBitmap.blankBitmap(rImgMat);
//            CoercionBitmap.drawBitmap(new Canvas(temp), CoercionBitmap.matrixBitmap(rImgMat), rImg);
//            rImg = temp.copy(Bitmap.Config.ARGB_8888, true);
//            rImgC = new Canvas(rImg);
//            float scale = rImgMat.getRepository().getScale();
//            PointF translate = CoercionBitmap.transBitmap(rImgMat);
//            rImgMat.reset();
//            rImgMat.view(rView).bitmap(new PointF(temp.getWidth(), temp.getHeight()));
//            rImgMat.getRepository().setScale(scale);
//            rImgMat.getRepository().setTranslate(translate);
//            zeroingBitmap(temp);
//        }
    }

    private void correctOpImg(){
        if(isImg()){
            Bitmap temp = CoercionBitmap.correctBlank(rImgMat);
            PointF trans = CoercionBitmap.correctTrans(rImgMat);
            DeformMat mat = new DeformMat(App.getMain());
            mat.view(rView).bitmap(new PointF(temp.getWidth(), temp.getHeight())).getRepository().setScale(rImgMat.getRepository().getScale());
            mat.getRepository().setTranslate(trans);
            DrawBitmap.create(new Canvas(temp),mat).draw(rImg, rImgMat);
            rImg = temp.copy(Bitmap.Config.ARGB_8888, true);
            rImgC = new Canvas(rImg);
            rImgMat.setRepository(mat.getRepository());
            zeroingBitmap(temp);
        }
    }

    public void correctLyr(){
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
}
