package com.example.kittenappscollage.draw.operations;

import android.graphics.PointF;
import android.view.MotionEvent;

import com.example.mutmatrix.DeformMat;
import com.example.mutmatrix.actions.Deform;

public class OperationMatrix extends Operation {

    private final int ACTION_NULL = -111;

    private DeformMat mMat;
    private PointF mPoint;
    private DeformMat.Command mCommand;
    private int mAction;

    private MotionEvent mMotion;




    private static OperationMatrix singleton;

    private OperationMatrix() {
        mCommand = null;
        mAction = ACTION_NULL;
    }

    public static OperationMatrix get(){
        if(singleton==null){
//            synchronized (OperationMatrix.class){
                singleton = new OperationMatrix();
//            }
        }
        return singleton;
    }

    public static OperationMatrix create(){
        return new OperationMatrix();
    }

    @Override
    public Operation mat(DeformMat mat) {
        mMat = mat;
        return super.mat(mat);
    }


    @Override
    public Operation event(Event event) {
        this.event = event;
        if(event.equals(Event.MATRIX_T))mCommand = DeformMat.Command.TRANSLATE;
        else if(event.equals(Event.MATRIX_S))mCommand = DeformMat.Command.SCALE;
        else if(event.equals(Event.MATRIX_S_P))mCommand = DeformMat.Command.SCALE_PLUS;
        else if (event.equals(Event.MATRIX_S_M))mCommand = DeformMat.Command.SCALE_MINUS;
        else if (event.equals(Event.MATRIX_R))mCommand = DeformMat.Command.ROTATE;
        else if (event.equals(Event.MATRIX_D))mCommand = DeformMat.Command.DEFORM;
        else if (event.equals(Event.MATRIX_RESET_DR))mCommand = DeformMat.Command.RESET;
        else mCommand = DeformMat.Command.NON;
        return this;
    }

    @Override
    public Operation point(MotionEvent m) {
        mPoint = new PointF(m.getX(),m.getY());
        mAction = m.getAction();
        mMotion = m;
        return this;
    }

    @Override
    public Operation point(PointF p, int action) {
        mPoint = p;
        mAction = action;
        return this;
    }

    @Override
    public void apply() {
        if(mMat==null||mPoint==null||mAction==ACTION_NULL||mCommand==null||mCommand.equals(DeformMat.Command.NON)){
            return;
        }
        if(TouchBitmap.ifIGotBit(mMat.muteDeformLoc(Deform.Coordinates.DISPLAY_ROTATE_DEFORM),mPoint)) {

            mMat.command(mCommand)
                    .event(mMotion);
            zeroing();
        }

    }

    @Override
    public void applyAll() {
        super.applyAll();
        if(mMat==null||mPoint==null||mAction==ACTION_NULL||mCommand==null||mCommand.equals(DeformMat.Command.NON)){
            return;
        }

        mMat.command(mCommand).event(mMotion);
        zeroing();

    }

    private void zeroing(){
        mAction = ACTION_NULL;
        mPoint = null;
    }

}
