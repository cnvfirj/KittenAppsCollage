package com.example.kittenappscollage.draw.operations.bitmap;

import android.graphics.Bitmap;
import android.graphics.PointF;
import android.view.MotionEvent;

import com.example.kittenappscollage.draw.RepDraw;
import com.example.kittenappscollage.draw.operations.Operation;
import com.example.kittenappscollage.helpers.Massages;
import com.example.mutablebitmap.DeformMat;

import static com.example.kittenappscollage.helpers.Massages.LYTE;


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
            if(mListener!=null)mListener.result(mCut.cut(getStartFin()).apply(mBitmap),mCut.getMat());
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
        zoneTouch((int) mMat.getCanvas().x/20);
        return super.mat(mat);
    }

    @Override
    public MutableBit listener(Operation.ResultMutable listener) {
        mListener = listener;
        return super.listener(listener);
    }



    @Override
    public MutableBit command(Command command) {
        mCommand = command;
        return super.command(command);
    }

    @Override
    public MutableBit start(PointF start) {
        if(mCommand.equals(Command.CUT)){
            if(ifTouchZone(start)){
                LYTE("Mutable cut touch zone");
            } else {
                LYTE("Mutable cut reset");
                reset();
            }
        }
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
    public MutableBit point(MotionEvent event) {
        if(event.getAction()== MotionEvent.ACTION_DOWN)start(new PointF(event.getX(),event.getY()));
        else if(event.getAction()==MotionEvent.ACTION_MOVE)move(new PointF(event.getX(),event.getY()));
        else if(event.getAction()==MotionEvent.ACTION_UP)fin(new PointF(event.getX(),event.getY()));

        if(mCommand.equals(Command.CUT))allPoints(event);
        return super.point(event);
    }

    private boolean ifTouchZone(PointF t){
        boolean target = false;
        if (RepDraw.get().isCorrectRepers()){
            for(PointF p:RepDraw.get().getRepers()){
                int r = zoneTouch();
                if(p.x+r>t.x&&p.x-r<t.x){

                }
            }
        }
        return target;
    }



    private boolean isCircle(){
        return false;
    }
    @Override
    protected void createReperStartFin() {
        super.createReperStartFin();
        mListener.repers(getRepersStartFinPoints(), istReadyCorrectStartFin());
    }
}
