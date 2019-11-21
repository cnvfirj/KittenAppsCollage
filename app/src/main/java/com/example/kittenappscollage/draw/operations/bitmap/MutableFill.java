package com.example.kittenappscollage.draw.operations.bitmap;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PointF;
import com.example.kittenappscollage.draw.repozitoryDraw.RepDraw;
import com.example.mutablebitmap.DeformMat;

public class MutableFill extends MutableCut {

    private HelpUnderFill mFill;

    public MutableFill() {
        mFill = new HelpUnderFill();
    }

    @Override
    public MutableBit command(Command command) {
        if(command.equals(Command.FILL_B)||command.equals(Command.FILL_C)){
            mCommand = command;
            mFill.setTypeFill(mCommand);
            return this;
        }else return super.command(command);
    }

    @Override
    public MutableBit start(PointF start) {

        if(mCommand.equals(Command.FILL_B)||mCommand.equals(Command.FILL_C)){
            applyFill(start);
            return this;
        }else return super.start(start);
    }

    @Override
    public void apply() {
        if(mCommand.equals(Command.FILL_B)||mCommand.equals(Command.FILL_C)){

        }else super.apply();
    }

    private void applyFill(PointF p){
        Bitmap temp = mFill.useImg(mBitmap)
                .setPoint(mMat.getPointBitmap(p))
                .fill();


        if(mIndex==RepDraw.ALL){
            RepDraw.get().startAllMutable();

            DrawBitmap.create(new Canvas(getImg()),getIMat()).antiAlias(false).draw(temp,mMat);
            mListener.result(getImg(), getIMat(),RepDraw.ALL,RepDraw.LYR_IMG,RepDraw.MUTABLE_SIZE);

            DrawBitmap.create(new Canvas(getLyr()),getLMat()).antiAlias(false).draw(temp,mMat);
            mListener.result(getLyr(), getLMat(),RepDraw.ALL,RepDraw.LYR_LYR,RepDraw.MUTABLE_SIZE);

        } else{
            RepDraw.get().startSingleMutable();
            DrawBitmap.create(new Canvas(mBitmap),mMat).antiAlias(false).draw(temp,mMat);
            mListener.result(mBitmap, mMat,mIndex,mLyr,RepDraw.MUTABLE_SIZE);
        }

        if(temp!=null&&!temp.isRecycled())temp.recycle();
    }

    protected DeformMat getLMat(){
        return RepDraw.get().getLMat();
    }

    protected DeformMat getIMat(){
        return RepDraw.get().getIMat();
    }

    protected boolean isLyr(){
        return RepDraw.get().isLyr();
    }

    private boolean isImg(){
        return RepDraw.get().isImg();
    }

    protected Canvas getLyrCanv(){
        return RepDraw.get().getrLyrCanv();
    }

    protected Canvas getImgCanv(){
        return RepDraw.get().getImgCanv();
    }

    protected Bitmap getImg(){
        return RepDraw.get().getImg();
    }

    protected Bitmap getLyr(){
        return RepDraw.get().getLyr();
    }
}
