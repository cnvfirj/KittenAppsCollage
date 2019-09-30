package com.example.kittenappscollage.draw.operations.bitmap;

import android.graphics.Bitmap;
import android.graphics.PointF;
import android.view.MotionEvent;

import com.example.kittenappscollage.draw.RepDraw;
import com.example.kittenappscollage.draw.operations.Operation;
import com.example.kittenappscollage.helpers.Massages;
import com.example.mutablebitmap.DeformMat;

public class MutableCut extends MutableBit {

    private HelpCut mCut;

    protected Bitmap mBitmap;

    protected DeformMat mMat;

    protected Command mCommand;

    protected Operation.ResultMutable mListener;

    public MutableCut() {
        super();
        mCut = new HelpCut();
    }

    @Override
    public void apply() {
        if(mBitmap!=null&&!mBitmap.isRecycled()){
            if(mListener!=null)mListener.result(mCut.cut(null).apply(mBitmap),mCut.getMat());
        }
        else Massages.ERROR("bed bitmap",getClass());

    }

    @Override
    public MutableBit bitmap(Bitmap bitmap) {
        mBitmap = bitmap;
        return super.bitmap(bitmap);
    }

    @Override
    public MutableBit mat(DeformMat mat) {
        mMat = mat;
        mCut.mat(mat);
        return super.mat(mat);
    }

    @Override
    public MutableBit listener(Operation.ResultMutable listener) {
        mListener = listener;
        return super.listener(listener);
    }

    @Override
    public MutableBit command(Command command) {
        if(command.equals(Operation.Event.LAYERS_CUT))mCommand = Command.CUT;
        else if(command.equals(Operation.Event.LAYERS_ELASTIC_1))mCommand = Command.ELAST_1;
        else if(command.equals(Operation.Event.LAYERS_ELASTIC_2))mCommand = Command.ELAST_2;
        else if(command.equals(Operation.Event.LAYERS_ELASTIC_3))mCommand = Command.ELAST_3;
        else if(command.equals(Operation.Event.LAYERS_ELASTIC_4))mCommand = Command.ELAST_4;
        else if(command.equals(Operation.Event.LAYERS_FILL_TO_BORDER))mCommand = Command.FILL_B;
        else if(command.equals(Operation.Event.LAYERS_FILL_TO_COLOR))mCommand = Command.FILL_C;

        return super.command(command);
    }

    @Override
    public MutableBit start(PointF start) {

        return super.start(start);
    }

    @Override
    public MutableBit move(PointF move) {
        return super.move(move);
    }

    @Override
    public MutableBit fin(PointF fin) {
        return super.fin(fin);
    }

    @Override
    public MutableBit point(PointF point, int action) {
        if(action== MotionEvent.ACTION_DOWN)start(point);
        else if(action==MotionEvent.ACTION_MOVE)move(point);
        else if(action==MotionEvent.ACTION_UP)fin(point);
        return super.point(point, action);
    }


}
