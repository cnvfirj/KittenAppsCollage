package com.example.kittenappscollage.draw.operations;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.PointF;
import android.view.MotionEvent;

import com.example.kittenappscollage.draw.RepDraw;
import com.example.kittenappscollage.draw.SaveStep;
import com.example.kittenappscollage.draw.operations.bitmap.CoercionBitmap;
import com.example.mutablebitmap.DeformMat;

import static com.example.kittenappscollage.helpers.Massages.LYTE;

public class ApplyOperationSelector implements Operation.ResultMutable {

    private final int SINGLE_IMG = 0;
    private final int SINGLE_LYR = 1;
    private final int ALL = 2;

    private static ApplyOperationSelector single;

    public static ApplyOperationSelector get(){
        if(single==null){
            synchronized (ApplyOperationSelector.class){
                single = new ApplyOperationSelector();
            }
        }
        return single;
    }


    @Override
    public void result(Bitmap img, DeformMat mat,int index) {
        if(index==SINGLE_LYR){
            RepDraw.get().mutableLyr(img,mat.getRepository(),RepDraw.MUTABLE_SIZE,true);
        }
        if(index==SINGLE_IMG){
            RepDraw.get().mutableImg(img,mat.getRepository(),RepDraw.MUTABLE_SIZE,true);
        }
    }

    @Override
    public void repers(PointF[] points, boolean is) {
        RepDraw.get().correctRepers(is).repers(points);
    }


    void singleMat(Operation operation, MotionEvent event){
        operation.resultMutable(this).view(RepDraw.get().getView());
        PointF p = new PointF(event.getX(),event.getY());
        int action = event.getAction();
        boolean touch = false;
        if(RepDraw.get().isImg()){
            if(belongingRegion(RepDraw.get().getIMat(),p)){
                if(action==MotionEvent.ACTION_UP){
                    touch = true;
                }
                if(action==MotionEvent.ACTION_DOWN){
                    operation.mat(RepDraw.get().getIMat());
                }
            }
            if(RepDraw.get().isLyr()){
                if(belongingRegion(RepDraw.get().getLMat(),p)){
                    if(action==MotionEvent.ACTION_UP){
                        touch = true;
                    }
                    if(action==MotionEvent.ACTION_DOWN){
                        operation.mat(RepDraw.get().getLMat());
                    }
                }

            }
            operation.point(event).apply();
//            if(touch) SaveStep.get().save(0);
        }
    }

    void groupMat(Operation operation, MotionEvent event){
        operation.resultMutable(this).view(RepDraw.get().getView());
        PointF p = new PointF(event.getX(),event.getY());
        if(RepDraw.get().isImg()){
            if(RepDraw.get().isLyr()){
                if(belongingRegion(RepDraw.get().getIMat(),p)){
                    operation.mat(RepDraw.get().getIMat()).point(event).applyAll();
                    operation.mat(RepDraw.get().getLMat()).point(event).applyAll();
                    if(event.getAction()==MotionEvent.ACTION_UP) {
//                        SaveStep.get().save(0);
                    }
                }else if(belongingRegion(RepDraw.get().getLMat(),p)){
                    operation.mat(RepDraw.get().getIMat()).point(event).applyAll();
                    operation.mat(RepDraw.get().getLMat()).point(event).applyAll();
                    if(event.getAction()==MotionEvent.ACTION_UP) {
//                        SaveStep.get().save(0);
                    }
                }
            }else {

                if(belongingRegion(RepDraw.get().getIMat(),p)){
                    operation.mat(RepDraw.get().getIMat()).point(event).applyAll();
                    if(event.getAction()==MotionEvent.ACTION_UP) {
//                        SaveStep.get().save(0);
                    }
                }
            }

        }
    }

    void singleLay(Operation operation, MotionEvent event){
        operation.resultMutable(this).view(RepDraw.get().getView());
        if(event.getAction()==MotionEvent.ACTION_DOWN){
            int index = RepDraw.get().isLyr()?SINGLE_LYR:SINGLE_IMG;
            readySingle(operation,index);
        }
        operation.point(event);

    }

    void groupLay(Operation operation,MotionEvent event){
          operation.resultMutable(this).view(RepDraw.get().getView());
          operation.point(event);
    }

    void singleCanv(Operation operation, MotionEvent event){

    }

    void groupCanv(Operation operation, MotionEvent event){

    }

    void doneCut(Operation operation, boolean isGroup){
        if(!isGroup) {
            int index = RepDraw.get().isLyr() ? SINGLE_LYR : SINGLE_IMG;
            readySingle(operation, index);
            operation.apply();
        }else {
            readySingle(operation,SINGLE_IMG);
            operation.apply();
            readySingle(operation,SINGLE_LYR);
            operation.apply();
        }

    }

    private void readySingle(Operation operation,int index){
        if(index==SINGLE_IMG){
            operCorrectLyr(RepDraw.get().getImg(), RepDraw.get().getIMat(),SINGLE_IMG);
            operation.mat(RepDraw.get().getIMat()).bitmap(RepDraw.get().getImg()).index(SINGLE_IMG);
        }else if(index==SINGLE_LYR){
            operCorrectLyr(RepDraw.get().getLyr(), RepDraw.get().getLMat(),SINGLE_LYR);
            operation.mat(RepDraw.get().getLMat()).bitmap(RepDraw.get().getLyr()).index(SINGLE_LYR);
        }
    }


    private Bitmap operCorrectLyr(Bitmap bit, DeformMat mat, int index){
        if(isZeroing(bit)) {
            Bitmap temp = CoercionBitmap.blankBitmap(mat);
            CoercionBitmap.drawBitmap(new Canvas(temp), CoercionBitmap.matrixBitmap(mat), bit);
            bit = temp.copy(Bitmap.Config.ARGB_8888, true);
            float scale = mat.getRepository().getScale();
            PointF translate = CoercionBitmap.transBitmap(mat);
            mat.reset();
            mat.view(RepDraw.get().getView()).bitmap(new PointF(temp.getWidth(), temp.getHeight()));
            mat.getRepository().setScale(scale);
            mat.getRepository().setTranslate(translate);
            zeroingBitmap(temp);
            if(index==SINGLE_IMG){
                RepDraw.get().mutableImg(bit,mat.getRepository(),RepDraw.MUTABLE_SIZE,true);
            }else if(index==SINGLE_LYR)RepDraw.get().mutableLyr(bit,mat.getRepository(),RepDraw.MUTABLE_SIZE,true);
        }
        return bit;
    }

    private boolean isZeroing(Bitmap b){
        if(b!=null&&!b.isRecycled())return true;
        else return false;
    }

    private void zeroingBitmap(Bitmap b){
        if(b!=null){
            b.recycle();
            b = null;
        }
    }
    private boolean belongingRegion(DeformMat mat, PointF p){
        PointF[]region = mat.muteDeformLoc(DeformMat.Coordinates.DISPLAY_ROTATE_DEFORM);
        return TouchBitmap.ifIGotBit(region,p);
    }
}
