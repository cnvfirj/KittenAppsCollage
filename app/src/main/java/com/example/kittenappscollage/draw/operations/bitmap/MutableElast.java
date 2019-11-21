package com.example.kittenappscollage.draw.operations.bitmap;

import android.graphics.PointF;

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
                mStart = start;
            return this;
        }else return super.start(start);
    }

    @Override
    public MutableBit move(PointF move) {
        if(mCommand.equals(Command.ELAST_1)||mCommand.equals(Command.ELAST_2)||
                mCommand.equals(Command.ELAST_3)||mCommand.equals(Command.ELAST_4)){
               mFin = move;
            return this;
        }else return super.move(move);
    }

    @Override
    public MutableBit fin(PointF fin) {
        if(mCommand.equals(Command.ELAST_1)||mCommand.equals(Command.ELAST_2)||
                mCommand.equals(Command.ELAST_3)||mCommand.equals(Command.ELAST_4)){
               mFin = fin;
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
}
