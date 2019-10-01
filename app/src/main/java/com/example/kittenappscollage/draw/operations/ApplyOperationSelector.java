package com.example.kittenappscollage.draw.operations;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.view.MotionEvent;

import com.example.kittenappscollage.draw.RepDraw;
import com.example.kittenappscollage.draw.SaveStep;
import com.example.kittenappscollage.draw.operations.bitmap.CoercionBitmap;
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
            if(touch) SaveStep.get().save(0);
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
                        SaveStep.get().save(0);
                    }
                }else if(belongingRegion(RepDraw.get().getLMat(),p)){
                    operation.mat(RepDraw.get().getIMat()).point(event).applyAll();
                    operation.mat(RepDraw.get().getLMat()).point(event).applyAll();
                    if(event.getAction()==MotionEvent.ACTION_UP) {
                        SaveStep.get().save(0);
                    }
                }
            }else {

                if(belongingRegion(RepDraw.get().getIMat(),p)){
                    operation.mat(RepDraw.get().getIMat()).point(event).applyAll();
                    if(event.getAction()==MotionEvent.ACTION_UP) {
                        SaveStep.get().save(0);
                    }
                }
            }

        }
    }

    void singleLay(Operation operation,MotionEvent event){
        if(event.getAction()==MotionEvent.ACTION_DOWN)selectImg(operation);
        operation.point(event);

        if(event.getAction()==MotionEvent.ACTION_UP) {
            if (!event.equals(Operation.Event.LAYERS_CUT) &&
                    !event.equals(Operation.Event.LAYERS_CUT_BORD)) {
                if(RepDraw.get().isImg()) {
                    if (RepDraw.get().isLyr()) {
                        SaveStep.get().save(0);
                    } else {
                        SaveStep.get().save(0);
                    }
                }
            }

        }
    }

    void groupLay(Operation operation,MotionEvent event){

    }

    void singleCanv(Operation operation, MotionEvent event){

    }

    void groupCanv(Operation operation, MotionEvent event){

    }

    private void selectImg(Operation operation){
        if(RepDraw.get().isLyr()){
            operation.mat(RepDraw.get().getLMat());
            operCorrectLyr(RepDraw.get().getLyr(), RepDraw.get().getLMat());
            operation.bitmap(RepDraw.get().getImg());

        } else {
            operation.mat(RepDraw.get().getIMat());
            operCorrectLyr(RepDraw.get().getImg(), RepDraw.get().getIMat());
            operation.bitmap(RepDraw.get().getImg());

        }
    }

    private Bitmap operCorrectLyr(Bitmap bit, DeformMat mat){
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
