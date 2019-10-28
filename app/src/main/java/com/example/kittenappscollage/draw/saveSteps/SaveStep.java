package com.example.kittenappscollage.draw.saveSteps;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;

import com.example.kittenappscollage.draw.RepDraw;
import com.example.kittenappscollage.helpers.rx.ThreadTransformers;
import com.example.mutablebitmap.CompRep;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Calendar;
import java.util.GregorianCalendar;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;

public class SaveStep {

    public static int SAVE_MATR = 2;
    public static int SAVE_SINGLE = 1;
    public static int SAVE_ALL = 0;

    private State state;

    private int stepSaved;

    public SaveStep register(State state){
        this.state = state;

        return this;
    }

    public void save(){
        saveState();
        if(state.getTarget()==BackNextStep.TARGET_ALL){
            if(state.getMut()==BackNextStep.MUT_MATRIX){
                stepSaved = SAVE_MATR;
                saveState();
            }else if (state.getMut()==BackNextStep.MUT_SCALAR||state.getMut()==BackNextStep.MUT_CONTENT){
                stepSaved = SAVE_ALL;
                saveImage(RepDraw.get().getImg(),state.getPathImg());
                saveImage(RepDraw.get().getLyr(),state.getPathLyr());

            }
        }else if(state.getTarget()==BackNextStep.TARGET_IMG){
            if(state.getMut()==BackNextStep.MUT_MATRIX){
                stepSaved = SAVE_MATR;
                saveState();
            }else if (state.getMut()==BackNextStep.MUT_SCALAR||state.getMut()==BackNextStep.MUT_CONTENT){
                stepSaved = SAVE_SINGLE;
                saveImage(RepDraw.get().getImg(),state.getPathImg());
            }
        }else if(state.getTarget()==BackNextStep.TARGET_LYR){
            if(state.getMut()==BackNextStep.MUT_MATRIX){
                stepSaved = SAVE_MATR;
                saveState();
            }else if (state.getMut()==BackNextStep.MUT_SCALAR||state.getMut()==BackNextStep.MUT_CONTENT){
                stepSaved = SAVE_SINGLE;
                saveImage(RepDraw.get().getLyr(),state.getPathLyr());
            }
        }
    }

    @SuppressLint("CheckResult")
    private void saveState(){
        requestSaveState()
                .subscribe(aBoolean -> {
                    if(aBoolean) {
                        stepSaved++;
                        if (stepSaved == 3) state.setReadiness(aBoolean);
                    }
                });

    }

    @SuppressLint("CheckResult")
    private void saveImage(Bitmap bitmap, String path){
        requestSaveBitm(path, bitmap)
                .subscribe(aBoolean -> {
                    if(aBoolean) {
                        stepSaved++;
                        if (stepSaved == 3) state.setReadiness(aBoolean);
                    }
                });
    }

    private Observable<Boolean> requestSaveState(){
        return Observable.create((ObservableOnSubscribe<Boolean>) emitter -> {
            emitter.onNext(saveSt());
            emitter.onComplete();
        }).compose(new ThreadTransformers.InputOutput<>());
    }

    private Observable<Boolean> requestSaveBitm(String path, Bitmap bitmap){
        return Observable.create((ObservableOnSubscribe<Boolean>) emitter -> {
            emitter.onNext(saveImg(path, bitmap));
            emitter.onComplete();
        }).compose(new ThreadTransformers.InputOutput<>());
    }

    private boolean saveImg(String path, Bitmap bitmap){
        final File file = new File(path);
        OutputStream os = null;
        try {
            os = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
            os.flush();
            os.close();
        } catch (IOException e) {

        }
        return file.exists();

    }
    private boolean saveSt(){

        final File file = new File(state.getPathData());
        if(file.exists()){
            file.delete();
        }
        try {
            OutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(state);
            oos.flush();
            oos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file.exists();
    }

    private boolean testFolder(File file){
        boolean success = true;
        if(!file.exists()){
            success = file.mkdirs();
        }
        return success;
    }

}
