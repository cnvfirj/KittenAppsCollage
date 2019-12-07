package com.example.mutmatrix;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.view.MotionEvent;

import com.example.mutmatrix.actions.Base;
import com.example.mutmatrix.actions.Deform;
import com.example.mutmatrix.actions.Rotate;
import com.example.mutmatrix.actions.Scale;
import com.example.mutmatrix.actions.Translate;

public class DeformMat {

    public enum Command{
        TRANSLATE,
        SCALE,
        ROTATE,
        DEFORM,
        NON
    }

    public enum SpecialCommand{
        MAX,
        MIN,
        ADAPT,
        COMMON,
        RESET_ROTATE,
        RESET_DEFORM
    }

    private SpecialCommand[]spec;

    private Base base, translate, scale, rotate, deform;

    private CompRep repository;

    public DeformMat(Context context) {
        repository = new CompRep();
        translate = new Translate(context).rep(repository);
        rotate = new Rotate(context).rep(repository);
        scale = new Scale(context).rep(repository);
        deform = new Deform(context).rep(repository);
    }

    public DeformMat reset(){
        repository = new CompRep();
        translate.rep(repository);
        rotate.rep(repository);
        scale.rep(repository);
        deform.rep(repository);
        return this;
    }

    public Matrix matrix(Matrix matrix){
        if(matrix==null)matrix = new Matrix();
        matrix.reset();
        matrix.setPolyToPoly(repository.getSrc(),0,repository.getDst(),0,4);
        matrix.postScale(repository.getScale(),repository.getScale());
        matrix.postRotate(repository.getRotate());
        matrix.postTranslate(repository.getTranslate().x,repository.getTranslate().y);
        return matrix;
    }


    public DeformMat command(Command command){
        spec = null;
        switch (command){
            case TRANSLATE:
                base = translate;
                break;
            case ROTATE:
                base = rotate;
                break;
            case SCALE:
                base = scale;
                break;
            case DEFORM:
                base = deform;
        }
        return this;
    }

    public DeformMat event(MotionEvent event){
        if(base!=null){
            if(spec==null) {
                base.touch(event);
            }else {
                calculateSpec(new PointF(event.getX(),event.getY()));
            }
        }
        return this;
    }

    public DeformMat bitmap(PointF b){
        repository.setBitmap(b);
        return this;
    }

    public DeformMat view(PointF v){
        scale.view(v);
        return this;
    }

    public void setRepository(CompRep rep) {
        this.repository = rep;
        translate.rep(rep);
        rotate.rep(rep);
        scale.rep(rep);
        deform.rep(rep);
    }

    public CompRep getRepository(){
        return repository;
    }

    public PointF getPointBitmap(PointF p){
        return deform.getPointBitmap(p);
    }

    public PointF[] muteDeformLoc(Deform.Coordinates c){
        return deform.muteDeformLoc(c);
    }


    public void special(SpecialCommand[]resets){
        spec = resets;
    }

     private void calculateSpec(PointF p){
         for (SpecialCommand c:spec){
             if(c.equals(SpecialCommand.RESET_DEFORM)){
                 deform.specialCommand(c,p);
             }else
                 if(c.equals(SpecialCommand.RESET_ROTATE)){
                 rotate.specialCommand(c,p);
             }else
                 if(c.equals(SpecialCommand.MAX)||
                     c.equals(SpecialCommand.MIN)||
                     c.equals(SpecialCommand.COMMON)||
                     c.equals(SpecialCommand.ADAPT)){
                 scale.specialCommand(c,p);
             }
         }
     }

}
