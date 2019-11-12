package com.example.kittenappscollage.draw.operations.bitmap;

import android.graphics.Bitmap;
import android.graphics.PointF;
import android.view.MotionEvent;

import com.example.kittenappscollage.draw.repozitoryDraw.RepDraw;
import com.example.kittenappscollage.draw.operations.Operation;
import com.example.kittenappscollage.helpers.Massages;
import com.example.mutablebitmap.DeformMat;


public class MutableCut extends MutableBit {

    private HelpCut mCut;

    protected Bitmap mBitmap;

    protected DeformMat mMat;

    protected Command mCommand;

    protected Operation.ResultMutable mListener;

    protected PointF mStart;

    private int mZone;

    private int mIndex;

    private int mLyr;

    public MutableCut() {
        super();
        mCut = new HelpCut();
        mIndex = 999;
    }

    @Override
    public void apply() {
        if(mBitmap!=null&&!mBitmap.isRecycled()&&mCommand.equals(Command.CUT)){
//            if(mListener!=null)mListener.result(mCut.cut(getStartFin()).apply(mBitmap),mCut.getMat(),mLyr);
            if(mListener!=null)mListener.result(mCut.cut(getStartFin()).apply(mBitmap),mCut.getMat(),mIndex, mLyr, RepDraw.MUTABLE_SIZE);

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
    public MutableBit index(int index) {
        mIndex = index;
        return super.index(index);
    }

    @Override
    public MutableBit lyr(int lyr) {
        mLyr = lyr;
        return super.lyr(lyr);
    }

    @Override
    public MutableBit listener(Operation.ResultMutable listener) {
        mListener = listener;
        return super.listener(listener);
    }

    @Override
    public MutableBit command(Command command) {
        mCommand = command;
        if(mCommand.equals(Command.CUT))createReperStartFin();
        return super.command(command);
    }

    @Override
    public MutableBit start(PointF start) {
        if(mCommand.equals(Command.CUT)){
            mZone = touchZone(start);
            if(mZone!=NON_ZONE){
                mStart = start;
                resetPoints();
            }else {
                reset();
                if(mCommand.equals(Command.CUT))allPoints(start, MotionEvent.ACTION_UP);
            }
        }
        return super.start(start);
    }

    @Override
    public MutableBit move(PointF move) {
        if(mCommand.equals(Command.CUT)){
            if(mZone!=NON_ZONE){

               correctRepersStartFin(mZone,getCorrect(mStart,move));
               mStart = move;
            }else {
                if(mCommand.equals(Command.CUT))allPoints(move,MotionEvent.ACTION_MOVE);
            }
        }
        return super.move(move);
    }

    @Override
    public MutableBit fin(PointF fin) {
        if(mCommand.equals(Command.CUT)){
            if(mZone!=NON_ZONE){
                correctRepersStartFin(mZone,getCorrect(mStart,fin));
                mStart = fin;

            }else {
                if(mCommand.equals(Command.CUT))allPoints(fin,MotionEvent.ACTION_UP);
            }

//            mStart = null;
        }
        return super.fin(fin);
    }

    @Override
    public MutableBit point(MotionEvent event) {
        if(event.getAction()== MotionEvent.ACTION_DOWN)start(new PointF(event.getX(),event.getY()));
        else if(event.getAction()==MotionEvent.ACTION_MOVE)move(new PointF(event.getX(),event.getY()));
        else if(event.getAction()==MotionEvent.ACTION_UP)fin(new PointF(event.getX(),event.getY()));

        return super.point(event);
    }

    private PointF getCorrect(PointF start, PointF fin){
        return new PointF(fin.x-start.x,
                fin.y-start.y);
    }
    private boolean ifTouchZone(PointF t){
        boolean target = false;
        if (RepDraw.get().isCorrectRepers()){
            int r = zoneTouch();
            for(PointF p:RepDraw.get().getRepers()){

                if(p.x+r>t.x&&p.x-r<t.x){
                   if(p.y+r>t.y&&p.y-r<t.y){
                       target = true;
                       break;
                   }
                }
            }
        }
        return target;
    }

    private int touchZone(PointF t){
        int index = NON_ZONE;
        if (RepDraw.get().isCorrectRepers()){
            int r = zoneTouch();
            for (int i = 0;i<getRepersStartFinPoints().length;i++){
                PointF p = getRepersStartFinPoints()[i];
                if(p.x+r>t.x&&p.x-r<t.x){
                    if(p.y+r>t.y&&p.y-r<t.y){
                        index = i;
                        break;
                    }
                }
            }
        }
        return index;
    }

    @Override
    protected void createReperStartFin() {
        super.createReperStartFin();
        if(mListener!=null)mListener.repers(getRepersStartFinPoints(), istReadyCorrectStartFin());
    }
}
