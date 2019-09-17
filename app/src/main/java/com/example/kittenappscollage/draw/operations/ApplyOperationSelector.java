package com.example.kittenappscollage.draw.operations;

import android.graphics.PointF;
import android.view.MotionEvent;

import com.example.kittenappscollage.draw.RepDraw;
import com.example.kittenappscollage.draw.SaveStep;
import com.example.mutablebitmap.DeformMat;

import static com.example.kittenappscollage.helpers.Massages.LYTE;

public class ApplyOperationSelector {

    private static ApplyOperationSelector single;

    public static ApplyOperationSelector get(){
        if(single==null){
            synchronized (ApplyOperationSelector.class){
                single = new ApplyOperationSelector();
            }
        }
        return single;
    }

    void singleMat(Operation operation, MotionEvent event){
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
            if(touch) SaveStep.get().save();
        }
    }

    void groupMat(Operation operation, MotionEvent event){
        PointF p = new PointF(event.getX(),event.getY());
        if(RepDraw.get().isImg()){
            if(RepDraw.get().isLyr()){
                if(belongingRegion(RepDraw.get().getIMat(),p)){
                    operation.mat(RepDraw.get().getIMat()).point(event).applyAll();
                    operation.mat(RepDraw.get().getLMat()).point(event).applyAll();
                    if(event.getAction()==MotionEvent.ACTION_UP) {
                        SaveStep.get().save();
                    }
                }else if(belongingRegion(RepDraw.get().getLMat(),p)){
                    operation.mat(RepDraw.get().getIMat()).point(event).applyAll();
                    operation.mat(RepDraw.get().getLMat()).point(event).applyAll();
                    if(event.getAction()==MotionEvent.ACTION_UP) {
                        SaveStep.get().save();
                    }
                }
            }else {

                if(belongingRegion(RepDraw.get().getIMat(),p)){
                    operation.mat(RepDraw.get().getIMat()).point(event).applyAll();
                    if(event.getAction()==MotionEvent.ACTION_UP) {
                        SaveStep.get().save();
                    }
                }
            }

        }
    }


    private boolean belongingRegion(DeformMat mat, PointF p){
        PointF[]region = mat.muteDeformLoc(DeformMat.Coordinates.DISPLAY_ROTATE_DEFORM);
        return TouchBitmap.ifIGotBit(region,p);
    }
}
