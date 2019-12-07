package com.example.kittenappscollage.draw.operations;

import android.view.MotionEvent;


public class ApplyOperation {

       private Operation aOperation;

       private Operation.Event aCommandEvent;

       private boolean aGroupingLyrs;



    public ApplyOperation event(Operation.Event event){
        aCommandEvent = event;
        aOperation = assignOperation(event);
        ApplyOperationSelector.get().registerList(aOperation);
        return this;
    }

    public ApplyOperation point(MotionEvent event){
        if(belongMat(getEvent())){
            if(aGroupingLyrs)matGroupOperation(event);
            else matSoloOperation(event);
        }else if (belongLay(getEvent())){
            if(aGroupingLyrs)lyrGroupOperation(event);
            else lyrSoloOperation(event);
        }else if(belongCan(getEvent())){
            if(aGroupingLyrs)canvGroupOperation(event);
            else canvSoloOperation(event);


        }
        return this;
    }

    public ApplyOperation grouping(boolean gr){
        aGroupingLyrs = gr;
        return this;
    }

    public void doneCut(){
        ApplyOperationSelector.get().doneCut(aOperation, aGroupingLyrs);
    }

    public Operation.Event getEvent(){
        return aCommandEvent;
    }

    private Operation assignOperation(Operation.Event event){
        if(belongCan(event)){
            return OperationCanvas
                    .get()
                    .event(event);
        }
        else if(belongMat(event)){
            return OperationMatrix
                    .get()
                    .event(event);
        }
        else if(belongLay(event)){
            return OperationBitmap
                    .get()
                    .event(event);
        }
        return null;
    }


    private void lyrGroupOperation(MotionEvent event){
        ApplyOperationSelector.get().groupLay(aOperation,event);
    }

    private void lyrSoloOperation(MotionEvent event){
        ApplyOperationSelector.get().singleLay(aOperation,event);
    }

    private void canvGroupOperation(MotionEvent event){
        ApplyOperationSelector.get().groupCanv(aOperation,event);
    }

    private void canvSoloOperation(MotionEvent event){
        ApplyOperationSelector.get().singleCanv(aOperation,event);
    }

    private void matSoloOperation(MotionEvent event){
         ApplyOperationSelector.get().singleMat(aOperation,event);
    }

    private void matGroupOperation(MotionEvent event){
        if(aCommandEvent.equals(Operation.Event.MATRIX_T)||
                        aCommandEvent.equals(Operation.Event.MATRIX_S_M)||
                        aCommandEvent.equals(Operation.Event.MATRIX_S_P))
        ApplyOperationSelector.get().groupMat(aOperation,event);
        else matSoloOperation(event);
    }



    private boolean belongLay(Operation.Event event){
        if(sectionEvent(event).equals(Operation.Event.LAYE.name()))return true;
        else return false;
    }

    private boolean belongMat(Operation.Event event){
        if(sectionEvent(event).equals(Operation.Event.MATR.name()))return true;
        else return false;
    }

    private boolean belongCan(Operation.Event event){
        if(sectionEvent(event).equals(Operation.Event.DRAW.name()))return true;
        else return false;
    }

    private String sectionEvent(Operation.Event event){
        if(event==null)return Operation.Event.NULLABLE.name();
        return event.name().substring(0,4);
    }

}
