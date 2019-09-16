package com.example.kittenappscollage.draw.operations;

import android.graphics.PointF;
import android.view.MotionEvent;

import com.example.mutablebitmap.BaseMat;
import com.example.mutablebitmap.DeformMat;
import com.example.mutablebitmap.ScaleMat;

import static com.example.kittenappscollage.helpers.Massages.LYTE;

public class OperationMatrix extends Operation {

    private final int ACTION_NULL = -111;

    private DeformMat mMat;
    private PointF mPoint;
    private BaseMat.Command mCommand;
    private int mAction;




    private static OperationMatrix singleton;

    private OperationMatrix() {
        mCommand = null;
        mAction = ACTION_NULL;
    }

    public static OperationMatrix get(){
        if(singleton==null){
            synchronized (OperationMatrix.class){
                singleton = new OperationMatrix();
            }
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
        LYTE("matrix "+event.name());
        this.event = event;
        if(event.equals(Event.MATRIX_T))mCommand = BaseMat.Command.TRANSLATE;
        else if(event.equals(Event.MATRIX_S_P))mCommand = BaseMat.Command.SCALE_PLUS;
        else if (event.equals(Event.MATRIX_S_M))mCommand = BaseMat.Command.SCALE_MINUS;
        else if (event.equals(Event.MATRIX_R))mCommand = BaseMat.Command.ROTATE;
        else if (event.equals(Event.MATRIX_D))mCommand = BaseMat.Command.DEFORM;
        else if (event.equals(Event.MATRIX_MIRR))mCommand = BaseMat.Command.MIRROR;
        else if (event.equals(Event.MATRIX_RESET_DR))mCommand = BaseMat.Command.RESET;
        else mCommand = BaseMat.Command.NULLABLE;
        return this;
    }

    @Override
    public Operation point(MotionEvent m) {
        mPoint = new PointF(m.getX(),m.getY());
        mAction = m.getAction();
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
        if(mMat==null||mPoint==null||mAction==ACTION_NULL||mCommand==null||mCommand.equals(BaseMat.Command.NULLABLE)){
            return;
        }
        if(TouchBitmap.ifIGotBit(mMat.muteDeformLoc(DeformMat.Coordinates.DISPLAY_ROTATE_DEFORM),mPoint)) {
            mMat.command(mCommand).point(mPoint, mAction);
            zeroing();
        }

    }

    @Override
    public void applyAll() {
        super.applyAll();
        if(mMat==null||mPoint==null||mAction==ACTION_NULL||mCommand==null||mCommand.equals(BaseMat.Command.NULLABLE)){
            return;
        }
        mMat.command(mCommand).point(mPoint, mAction);
        zeroing();

    }

    private void zeroing(){
        mAction = ACTION_NULL;
        mPoint = null;
    }

}
