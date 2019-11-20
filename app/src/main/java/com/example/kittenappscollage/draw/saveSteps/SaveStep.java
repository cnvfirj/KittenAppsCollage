package com.example.kittenappscollage.draw.saveSteps;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;

import com.example.kittenappscollage.draw.repozitoryDraw.RepDraw;
import com.example.kittenappscollage.helpers.rx.ThreadTransformers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;

import static com.example.kittenappscollage.helpers.Massages.LYTE;

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
           switch (state.getTarget()){
            case BackNextStep.TARGET_ALL:
                if(state.getMut()==BackNextStep.MUT_MATRIX){
                    stepSaved = SAVE_MATR;
                    saveState();
                }else if (state.getMut()==BackNextStep.MUT_SCALAR||state.getMut()==BackNextStep.MUT_CONTENT){
                    stepSaved = SAVE_ALL;
                    if(RepDraw.get().isLyr()){
                        saveImage(RepDraw.get().getLyr(),state.getPathLyr());
                    }else {
                        stepSaved++;
                    }
                    if(RepDraw.get().isImg()){
                        saveImage(RepDraw.get().getImg(),state.getPathImg());
                    }

                }
                break;
            case BackNextStep.TARGET_IMG:
                if(state.getMut()==BackNextStep.MUT_MATRIX){
                    stepSaved = SAVE_MATR;
                    saveState();
                }else if (state.getMut()==BackNextStep.MUT_SCALAR||state.getMut()==BackNextStep.MUT_CONTENT){
                    stepSaved = SAVE_SINGLE;
                    if(RepDraw.get().isImg())saveImage(RepDraw.get().getImg(),state.getPathImg());
                }
                break;
            case BackNextStep.TARGET_LYR:
                if(state.getMut()==BackNextStep.MUT_MATRIX){
                    stepSaved = SAVE_MATR;
                    saveState();
                }else if (state.getMut()==BackNextStep.MUT_SCALAR||state.getMut()==BackNextStep.MUT_CONTENT){
                    stepSaved = SAVE_SINGLE;
                    if(RepDraw.get().isLyr())saveImage(RepDraw.get().getLyr(),state.getPathLyr());
                    else {
                        stepSaved++;
                        saveState();
                    }
                }
                break;
        }

    }

    public void saveState(State state){
        this.state = state;
        saveState();
    }

    @SuppressLint("CheckResult")
    private void saveState(){
        state.setReadiness(true);/*для сохранения только мптричных изменений*/
        if(testFolder(new File(state.getPathFoldData()))) {
            requestSaveState()
                    .subscribe(aBoolean -> {
                            state.setReadiness(aBoolean);
                    });
        }else state.setReadiness(false);

    }

    @SuppressLint("CheckResult")
    private void saveImage(Bitmap bitmap, String path){
        if(testFolder(new File(state.getPathFoldImg()))) {
            requestSaveBitm(path, bitmap)
                    .subscribe(aBoolean -> {
                        if (aBoolean) {
                            stepSaved++;
                            if (stepSaved == 2) {
                                state.setReadiness(aBoolean);
                                saveState();
                            }
                        }/*проверить память устройства*/
                    });
        }else state.setReadiness(false);

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
            if (file.exists()) {
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
