package com.example.kittenappscollage.draw.operations;

import android.graphics.Bitmap;
import android.graphics.PointF;
import android.view.MotionEvent;
import com.example.kittenappscollage.draw.repozitoryDraw.RepDraw;
import com.example.mutmatrix.DeformMat;
import com.example.mutmatrix.actions.Deform;

import static com.example.kittenappscollage.draw.repozitoryDraw.Repozitory.ALL;
import static com.example.kittenappscollage.draw.repozitoryDraw.Repozitory.LYR_IMG;
import static com.example.kittenappscollage.draw.repozitoryDraw.Repozitory.SINGLE;

public class ApplyOperationSelector implements Operation.ResultMutable {

    private static ApplyOperationSelector single;

    public static ApplyOperationSelector get(){
        if(single==null){
            synchronized (ApplyOperationSelector.class){
                single = new ApplyOperationSelector();
            }
        }
        return single;
    }

    public ApplyOperationSelector registerList(Operation operation) {
        operation.resultMutable(this);
        return this;
    }

    @Override
    public void result(Bitmap img, DeformMat mat, int index) {
        if(index==RepDraw.LYR_LYR){

            RepDraw.get().mutableLyr(img,mat.getRepository(),RepDraw.MUTABLE_SIZE,true);
        }
        if(index== LYR_IMG){
            RepDraw.get().mutableImg(img,mat.getRepository(),RepDraw.MUTABLE_SIZE,true);
        }
    }

    @Override
    public void result(Bitmap img, DeformMat mat, int index, int lyr, int mutable) {

             if(lyr== LYR_IMG){
                 RepDraw.get().mutableImg(img,mat.getRepository(),mutable,index==RepDraw.SINGLE);
             }
             if(lyr==RepDraw.LYR_LYR){
                 RepDraw.get().mutableLyr(img,mat.getRepository(),mutable,index==RepDraw.SINGLE);
             }

    }

    @Override
    public void repers(PointF[] points, boolean is) {
        RepDraw.get().correctRepers(is).repers(points);
    }

    void singleMat(Operation operation, MotionEvent event){
        PointF p = new PointF(event.getX(),event.getY());
        int action = event.getAction();
        int end = 0;
        boolean touch = false;
        if(RepDraw.get().isImg()){
            if(action==MotionEvent.ACTION_UP){
                touch = true;
            }
            if(belongingRegion(RepDraw.get().getIMat(),p)){

                if(action==MotionEvent.ACTION_DOWN){
                    operation.mat(RepDraw.get().getIMat()).ready(true);
                }
            }else end++;
            if(RepDraw.get().isLyr()){
                if(belongingRegion(RepDraw.get().getLMat(),p)){

                    if(action==MotionEvent.ACTION_DOWN){
                        operation.mat(RepDraw.get().getLMat()).ready(true);
                    }
                }else end++;
            }
            if(end==2){
                event.setAction(MotionEvent.ACTION_UP);
                touch = true;
            }
            if(operation.isReady()) operation.point(event).apply();
            if(touch&&operation.isReady()){
                RepDraw.get().mutableMatrix();
                operation.ready(false);
            }
        }
    }

    void groupMat(Operation operation, MotionEvent event){

        PointF p = new PointF(event.getX(),event.getY());
        int action = event.getAction();
        if(RepDraw.get().isImg()){
            if(RepDraw.get().isLyr()){
                if(belongingRegion(RepDraw.get().getIMat(),p)){
                    operation.mat(RepDraw.get().getIMat()).point(event).applyAll();
                    operation.mat(RepDraw.get().getLMat()).point(event).applyAll();
                    if(action==MotionEvent.ACTION_UP){
                        RepDraw.get().mutableMatrix();

                    }
                }else if(belongingRegion(RepDraw.get().getLMat(),p)){
                    operation.mat(RepDraw.get().getIMat()).point(event).applyAll();
                    operation.mat(RepDraw.get().getLMat()).point(event).applyAll();
                    if(action==MotionEvent.ACTION_UP){
                        RepDraw.get().mutableMatrix();
                    }
                }

            }else {

                if(belongingRegion(RepDraw.get().getIMat(),p)){
                    operation.mat(RepDraw.get().getIMat()).point(event).applyAll();
                    if(action==MotionEvent.ACTION_UP){
                        RepDraw.get().mutableMatrix();
                    }
                }
            }

        }
    }

    /*для отреза ключевым методом является doneCut()*/
    void singleLay(Operation operation, MotionEvent event){
        if(operation.getEvent().equals(Operation.Event.LAYERS_CUT))operation.point(event);
        else if(isLine(operation)){
            lineOperation(operation,event,SINGLE);
        } else {
              if(!operation.isReady()){
                  if(isFill(operation)){
                      if(event.getAction()==MotionEvent.ACTION_DOWN) {
                          operation.ready(TouchBitmap
                                  .ifIGotBit(
                                          getOverMat().muteDeformLoc(Deform.Coordinates.DISPLAY_ROTATE_DEFORM),
                                          new PointF(event.getX(), event.getY())
                                  ));
                                    }
                  }else if(isElast(operation)){
                      if(event.getAction()==MotionEvent.ACTION_DOWN||event.getAction()==MotionEvent.ACTION_MOVE){
                          operation.ready(TouchBitmap
                                    .ifIGotBitBord(getOverMat().muteDeformLoc(Deform.Coordinates.DISPLAY_ROTATE_DEFORM),
                                            new PointF(event.getX(), event.getY()),RepDraw.get().getWidth()/2));
                                      }
                  }
                  if(operation.isReady()) {
                      int lyr = RepDraw.get().isLyr() ? RepDraw.LYR_LYR : LYR_IMG;
                      operation.index(SINGLE);
                      readySingle(operation, lyr);
                  }
              }
            if(operation.isReady())operation.point(event);

            if(event.getAction()==MotionEvent.ACTION_UP)operation.ready(false);
        }
    }

    void groupLay(Operation operation,MotionEvent event){
        if(isCut(operation))operation.point(event);
        else if(isLine(operation)){
            lineOperation(operation,event,ALL);
        }
        else {
            if(!operation.isReady()){
                if(isFill(operation)){
                    operation.ready(TouchBitmap
                            .ifIGotBit(getOverMat()
                                    .muteDeformLoc(Deform
                                            .Coordinates.DISPLAY_ROTATE_DEFORM), new PointF(event.getX(), event.getY())));
                    if(operation.isReady()){
                        int index = RepDraw.get().isLyr() ? ALL : LYR_IMG;
                        if (index == LYR_IMG) {
                            operation.index(SINGLE);
                            singleLay(operation, event);
                            return;
                        } else {
                            if (belongingOverlay()) {
                                operation.index(ALL);
                                readyAll(operation);
                            } else {
                                operation.index(SINGLE);
                                singleLay(operation, event);
                                return;
                            }
                        }
                    }
                }else if(isElast(operation)){
                    singleLay(operation,event);
                    return;
                }
            }
            if(operation.isReady())operation.point(event);
            if(event.getAction()==MotionEvent.ACTION_UP)operation.ready(false);
        }

    }

    void singleCanv(Operation operation, MotionEvent event){

    }

    void groupCanv(Operation operation, MotionEvent event){

    }

    private boolean isCut(Operation operation){
        return operation.getEvent().equals(Operation.Event.LAYERS_CUT);
    }

    private boolean isFill(Operation operation){
        return operation.getEvent().equals(Operation.Event.LAYERS_FILL_TO_COLOR)||
                operation.getEvent().equals(Operation.Event.LAYERS_FILL_TO_BORDER);
    }

    private boolean isElast(Operation operation){
        return operation.getEvent().equals(Operation.Event.LAYERS_ELASTIC_1)||
                operation.getEvent().equals(Operation.Event.LAYERS_ELASTIC_2)||
                operation.getEvent().equals(Operation.Event.LAYERS_ELASTIC_3);
    }

    private boolean isLine(Operation operation){
        return operation.getEvent().equals(Operation.Event.LAYERS_LINE_1)||
                operation.getEvent().equals(Operation.Event.LAYERS_LINE_2)||
                operation.getEvent().equals(Operation.Event.LAYERS_LINE_3);
    }

    private void lineOperation(Operation operation,MotionEvent event, int index){
        readyOver(operation);
        if(index==SINGLE) {
//          if(event.getAction()==MotionEvent.ACTION_DOWN){
//                int lyr = RepDraw.get().isLyr() ? RepDraw.LYR_LYR : LYR_IMG;
//                operation.lyr(lyr).index(index);
//          }
//            if(!operation.isReady())operation.ready(TouchBitmap
//                    .ifIGotBitBord(getOverMat().muteDeformLoc(Deform.Coordinates.DISPLAY_ROTATE_DEFORM),
//                            new PointF(event.getX(), event.getY()),RepDraw.get().getWidth()/2));
//            if(operation.isReady()){
//
//            }
        }

        operation.point(event);
    }

    void doneCut(Operation operation, boolean isGroup){
        if(!isGroup) {
            int lyr = RepDraw.get().isLyr()?RepDraw.LYR_LYR: LYR_IMG;
            operation.index(RepDraw.SINGLE);
            readySingle(operation, lyr);
            operation.apply();
        }else {
            RepDraw.get().startAllMutable();
            operation.index(ALL);
            readySingle(operation,RepDraw.LYR_LYR);
            operation.apply();
            readySingle(operation, LYR_IMG);
            operation.apply();
        }
    }



    private void readyOver(Operation operation){
        operation.mat(RepDraw.get().getOMat())
                .bitmap(RepDraw.get().getOverlay());
    }

    private void readyAll(Operation operation){

        RepDraw.get().correctImg();
        RepDraw.get().correctLyr();
        operation
                .mat(RepDraw.get().getLMat())
                .bitmap(RepDraw.get().getLyr());
    }

    private void readySingle(Operation operation,int lyr){
        if(lyr== LYR_IMG){

            RepDraw.get().correctImg();
            operation
                    .mat(RepDraw.get().getIMat())
                    .bitmap(RepDraw.get().getImg())
                    .lyr(LYR_IMG);
        }else if(lyr==RepDraw.LYR_LYR){
            RepDraw.get().correctLyr();
            operation
                    .mat(RepDraw.get().getLMat())
                    .bitmap(RepDraw.get().getLyr())
                    .lyr(RepDraw.LYR_LYR);
        }
    }

    private boolean belongingRegion(DeformMat mat, PointF p){
        PointF[]region = mat.muteDeformLoc(Deform.Coordinates.DISPLAY_ROTATE_DEFORM);
        return TouchBitmap.ifIGotBit(region,p);
    }

    private boolean belongingOverlay(){
        PointF[]lyr = getLMat().muteDeformLoc(Deform.Coordinates.DISPLAY_ROTATE_DEFORM);
        PointF[]img = getIMat().muteDeformLoc(Deform.Coordinates.DISPLAY_ROTATE_DEFORM);
       for(PointF l:lyr) {
               if(TouchBitmap.ifIGotBit(img,l)) {
                   return true;
               }
       }
       for (PointF i:img){
           if(TouchBitmap.ifIGotBit(lyr,i)) {
               return true;
           }
       }
        return false;
    }

    private DeformMat getOverMat(){
        if(RepDraw.get().isLyr())return getLMat();
        else return getIMat();
    }
    private DeformMat getLMat(){
        return RepDraw.get().getLMat();
    }

    private DeformMat getIMat(){
        return RepDraw.get().getIMat();
    }
}
