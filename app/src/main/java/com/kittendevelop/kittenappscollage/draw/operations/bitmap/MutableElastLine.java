package com.kittendevelop.kittenappscollage.draw.operations.bitmap;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PointF;

import com.kittendevelop.kittenappscollage.draw.repozitoryDraw.RepDraw;
import com.example.mutmatrix.DeformMat;
import com.kittendevelop.kittenappscollage.draw.repozitoryDraw.Repozitory;

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
            if(mListener!=null){
                if(mIndex== RepDraw.ALL){
                    RepDraw.get().startAllMutable();

                    DrawBitmap.create(new Canvas(getImg()),getIMat()).antiAlias(true).draw(mBitmap,mMat);
                    mListener.result(getImg(), getIMat(),RepDraw.ALL,RepDraw.LYR_IMG,RepDraw.MUTABLE_SIZE);

                    DrawBitmap.create(new Canvas(getLyr()),getLMat()).antiAlias(true).draw(mBitmap,mMat);
                    mListener.result(getLyr(), getLMat(),RepDraw.ALL,RepDraw.LYR_LYR,RepDraw.MUTABLE_SIZE);
                } else{
                    RepDraw.get().startSingleMutable();
                    if(mLyr== Repozitory.LYR_IMG){
                        DrawBitmap.create(new Canvas(getImg()),getIMat()).antiAlias(true).draw(mBitmap,mMat);
                        mListener.result(getImg(), getIMat(), mIndex,mLyr,RepDraw.MUTABLE_SIZE);
                    }else if(mLyr== Repozitory.LYR_LYR){
                        DrawBitmap.create(new Canvas(getLyr()),getLMat()).antiAlias(true).draw(mBitmap,mMat);
                        mListener.result(getLyr(), getLMat(), mIndex,mLyr,RepDraw.MUTABLE_SIZE);
                    }
                }
            }
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
