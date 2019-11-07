package com.example.kittenappscollage.draw.saveSteps;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.kittenappscollage.draw.repozitoryDraw.RepDraw;
import com.example.kittenappscollage.helpers.rx.ThreadTransformers;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;

import static com.example.kittenappscollage.helpers.Massages.LYTE;


public class LoadStep {


    @SuppressLint("CheckResult")
    public void loadImg(State state){
        requestLoadImg(state.getPathImg()).subscribe(bitmap->{
            RepDraw.get().stepLoadImg(bitmap,state.getRepImg(),true);
        });
    }

    @SuppressLint("CheckResult")
    public void loadLyr(State state){
       requestLoadImg(state.getPathLyr()).subscribe(bitmap->{
           RepDraw.get().stepLoadLyr(bitmap,state.getRepLyr(),true);
       });
    }

    @SuppressLint("CheckResult")
    public void loadAll(State state){
        requestLoadImg(state.getPathImg()).subscribe(bitmap->{
            RepDraw.get().stepLoadImg(bitmap,state.getRepImg(),false);
        });
        requestLoadImg(state.getPathLyr()).subscribe(bitmap->{
            RepDraw.get().stepLoadLyr(bitmap,state.getRepLyr(),false);
        });
    }

    private Observable<Bitmap> requestLoadImg(final String path){
        return Observable.create((ObservableOnSubscribe<Bitmap>) emitter -> {
            emitter.onNext(BitmapFactory.decodeFile(path).copy(Bitmap.Config.ARGB_8888,true));
            emitter.onComplete();
        }).compose(new ThreadTransformers.InputOutput<>());
    }


}
