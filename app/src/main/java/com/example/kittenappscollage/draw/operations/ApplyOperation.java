package com.example.kittenappscollage.draw.operations;

import android.graphics.PointF;
import android.view.MotionEvent;

import com.example.kittenappscollage.draw.RepDraw;
import com.example.kittenappscollage.helpers.App;
import com.example.mutablebitmap.DeformMat;

import static com.example.kittenappscollage.helpers.Massages.LYTE;

public class ApplyOperation {

       private Operation aOperation;

       private PointF aView;

       private Operation.Event aCommandEvent;

       private boolean aGroupingLyrs;


    public ApplyOperation event(Operation.Event event){
        aCommandEvent = event;
        aOperation = assignOperation(event);
        return this;
    }

    public ApplyOperation point(MotionEvent event){
        if(belongMat(getEvent())){
            if(aGroupingLyrs)matGroupOperation(event);
            else matSoloOperation(event);
        }
        return this;
    }
    public ApplyOperation view(PointF view){
        aView = view;
        return this;
    }

    public ApplyOperation grouping(boolean gr){
        aGroupingLyrs = gr;
        return this;
    }



    private Operation.Event getEvent(){
        return aCommandEvent;
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
        return event.name().substring(0,4);
    }

}
