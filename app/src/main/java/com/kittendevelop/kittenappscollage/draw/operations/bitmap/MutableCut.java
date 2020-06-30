package com.kittendevelop.kittenappscollage.draw.operations.bitmap;

import android.graphics.Bitmap;
import android.graphics.PointF;
import android.view.MotionEvent;

import com.kittendevelop.kittenappscollage.draw.repozitoryDraw.RepDraw;
import com.kittendevelop.kittenappscollage.draw.operations.Operation;
import com.kittendevelop.kittenappscollage.helpers.Massages;
import com.example.mutmatrix.DeformMat;

public class MutableCut extends MutableBit {

    private HelpCut mCut;

    protected Bitmap mBitmap;

    protected DeformMat mMat;

    protected Command mCommand;

    protected Operation.ResultMutable mListener;

    protected PointF mStart;

    protected int mIndex;

    private int mZone;

    protected int mLyr;

    public MutableCut() {
        super();
        mCut = new HelpCut();
        mIndex = 999;
    }

    @Override
    public void apply() {
        if(mBitmap!=null&&!mBitmap.isRecycled()&&mCommand.equals(Command.CUT)){
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
        zoneTouch((int) RepDraw.get().getView().x/20);
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
                allPoints(move,MotionEvent.ACTION_MOVE);
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
                allPoints(fin,MotionEvent.ACTION_UP);
            }

        }
        return super.fin(fin);
    }

    @Override
    public MutableBit point(MotionEvent event) {
        int action = event.getActionMasked();
        int index = event.getActionIndex();
        int id = event.getPointerId(index);
        if(action== MotionEvent.ACTION_DOWN)start(new PointF(event.getX(),event.getY()));
        else if(action==MotionEvent.ACTION_MOVE&&id==0)move(new PointF(event.getX(),event.getY()));
        else if(action==MotionEvent.ACTION_UP)fin(new PointF(event.getX(),event.getY()));

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
