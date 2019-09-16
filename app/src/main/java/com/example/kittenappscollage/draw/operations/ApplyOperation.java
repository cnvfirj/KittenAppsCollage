package com.example.kittenappscollage.draw.operations;

import android.view.MotionEvent;

import static com.example.kittenappscollage.helpers.Massages.LYTE;

public class ApplyOperation {

       private Operation aOperation;


    public ApplyOperation event(Operation.Event event){
        aOperation = assignOperation(event);
        return this;
    }




    private Operation assignOperation(Operation.Event event){
        if(belongCan(event)){
            return OperationCanvas.get().event(event);
        }
        else if(belongMat(event)){
            return OperationMatrix.get().event(event);
        }
        else if(belongLay(event)){
            return OperationBitmap.get().event(event);

        }
        return null;
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
        return event.name().substring(0,4);
    }

}
