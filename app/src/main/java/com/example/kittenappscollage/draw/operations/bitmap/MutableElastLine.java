package com.example.kittenappscollage.draw.operations.bitmap;

import android.graphics.Bitmap;
import android.graphics.PointF;

import com.example.kittenappscollage.draw.repozitoryDraw.RepDraw;
import com.example.mutmatrix.DeformMat;

import static com.example.kittenappscollage.draw.repozitoryDraw.Repozitory.LYR_IMG;
import static com.example.kittenappscollage.draw.repozitoryDraw.Repozitory.LYR_LYR;
import static com.example.kittenappscollage.draw.repozitoryDraw.Repozitory.SINGLE;

public class MutableElastLine extends MutableElast {

    private HelpDrawLine mLine;

    private boolean mReady;

    public MutableElastLine() {
        mLine = new HelpDrawLine();
        mReady = false;
    }

    @Override
    public MutableBit command(Command command) {
        if(command.equals(Command.LINE_1)||command.equals(Command.LINE_2)||
                command.equals(Command.LINE_3)){
            mCommand = command;
            mLine.command(command);
            return this;
        }else return super.command(command);
    }

    @Override
    public MutableBit start(PointF start) {
        if(mCommand.equals(Command.LINE_1)||mCommand.equals(Command.LINE_2)||
                mCommand.equals(Command.LINE_3)){
            if(testBitmap(mBitmap)){
                mLine.convert().resetOun().point(start).apply();
            }
            return this;
        }else return super.start(start);
    }

    @Override
    public MutableBit move(PointF move) {
        if(mCommand.equals(Command.LINE_1)||mCommand.equals(Command.LINE_2)||
                mCommand.equals(Command.LINE_3)){
            if(!mLine.isConvert())mLine.convert().resetOun();
            mLine.point(move).apply();
            return this;
        }else return super.move(move);
    }

    @Override
    public MutableBit fin(PointF fin) {
        if(mCommand.equals(Command.LINE_1)||mCommand.equals(Command.LINE_2)||
                mCommand.equals(Command.LINE_3)){
            mLine.fin();
//            if(mListener!=null)mListener.result(mBitmap,mMat,mIndex, mLyr, RepDraw.MUTABLE_SIZE);
//            if(mReady){
//                if(mListener!=null){
//                    if(mIndex==SINGLE){
//                        if(mLyr==LYR_IMG){
//                            if(mListener!=null)DrawBitmap.create(RepDraw.get().getImgCanv(),RepDraw.get().getIMat())
//                                                         .draw(mBitmap,mMat);
//                        }else {
//
//                        }
//                    }else {
//
//                    }
//                }
//                mReady = false;
//            }
            return this;
        }else return super.fin(fin);
    }




    @Override
    public MutableBit bitmap(Bitmap bitmap) {
        mLine.bitmap(bitmap);
        return super.bitmap(bitmap);
    }

    @Override
    public MutableBit mat(DeformMat mat) {
        mLine.mat(mat);
        return super.mat(mat);
    }

    public MutableElastLine ready(boolean ready){
        return this;
    }

    public int getIndexLyr(){
        return mLyr;
    }
}
