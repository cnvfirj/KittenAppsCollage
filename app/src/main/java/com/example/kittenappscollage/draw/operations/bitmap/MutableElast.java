package com.example.kittenappscollage.draw.operations.bitmap;

import android.graphics.Bitmap;
import android.graphics.PointF;

import com.example.kittenappscollage.draw.repozitoryDraw.RepDraw;
import com.example.mutmatrix.DeformMat;

public class MutableElast extends MutableFill {

    private HelpElast mElast;

    protected PointF mFin;

    public MutableElast() {
        mElast = new HelpElast();
    }

    @Override
    public MutableBit command(Command command) {
        if(command.equals(Command.ELAST_1)||command.equals(Command.ELAST_2)||
                command.equals(Command.ELAST_3)||command.equals(Command.ELAST_4)){
            mCommand = command;
            mElast.command(command);
            return this;
        }else return super.command(command);
    }

    @Override
    public MutableBit start(PointF start) {
        if(mCommand.equals(Command.ELAST_1)||mCommand.equals(Command.ELAST_2)||
                mCommand.equals(Command.ELAST_3)||mCommand.equals(Command.ELAST_4)){
                if(testBitmap(mBitmap)){
                 mElast.convert().resetOun().point(start).apply();
                }
            return this;
        }else return super.start(start);
    }

    @Override
    public MutableBit move(PointF move) {
        if(mCommand.equals(Command.ELAST_1)||mCommand.equals(Command.ELAST_2)||
                mCommand.equals(Command.ELAST_3)||mCommand.equals(Command.ELAST_4)){
            if(!mElast.isConvert())mElast.convert().resetOun();
            mElast.point(move).apply();
            return this;
        }else return super.move(move);
    }

    @Override
    public MutableBit fin(PointF fin) {
        if(mCommand.equals(Command.ELAST_1)||mCommand.equals(Command.ELAST_2)||
                mCommand.equals(Command.ELAST_3)||mCommand.equals(Command.ELAST_4)){
            mElast.point(fin).apply().fin();
            if(mListener!=null)mListener.result(mBitmap,mMat,mIndex, mLyr, RepDraw.MUTABLE_SIZE);
            return this;
        }else return super.fin(fin);
    }


    public MutableElast size(float size){
        mElast.radius(size);
        return this;
    }

    public MutableElast alpha(int alpha){
        mElast.alpha(alpha);
        return this;
    }

    @Override
    public MutableBit bitmap(Bitmap bitmap) {
        mElast.bitmap(bitmap);
        return super.bitmap(bitmap);
    }

    @Override
    public MutableBit mat(DeformMat mat) {
        mElast.mat(mat);
        return super.mat(mat);
    }
}
