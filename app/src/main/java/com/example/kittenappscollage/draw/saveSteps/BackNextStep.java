package com.example.kittenappscollage.draw.saveSteps;

import com.example.kittenappscollage.draw.RepDraw;
import com.example.kittenappscollage.helpers.App;
import com.example.kittenappscollage.helpers.RequestFolder;
import com.example.mutablebitmap.CompRep;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Stack;

import static com.example.kittenappscollage.helpers.Massages.LYTE;

public class BackNextStep extends StorySteps{

    private static BackNextStep single;

    public static BackNextStep get(){
        if(single==null){
            synchronized (BackNextStep.class){
                single = new BackNextStep();
            }
        }
        return single;
    }

    private void load(int target, int mut, State state){
        if(target==TARGET_ALL){
            if(mut==MUT_MATRIX)loadMatrix(state);
            if(mut==MUT_CONTENT||mut==MUT_SCALAR){
                loadAll(state);
            }
        }else if(target==TARGET_IMG){
            if(mut==MUT_MATRIX)loadMatrix(state);
            if(mut==MUT_CONTENT||mut==MUT_SCALAR){
                loadImg(state);
            }
        }else if(target==TARGET_LYR){
            if(mut==MUT_MATRIX)loadMatrix(state);
            if(mut==MUT_CONTENT||mut==MUT_SCALAR){
                loadLyr(state);
            }
        }
    }

 /******************************************************************/
    public void inBackToNext(){
        if(statesBack.size()>1){
            statesNext.push(statesBack.pop());
            if(statesBack.peek().isReadiness()){
                load(statesNext.peek().getTarget(),statesNext.peek().getMut(),statesBack.peek());
                saveStep.saveState(statesBack.peek());
            }else {
                inBackToNext();
            }
        }
        if(listenSteps!=null){
            listenSteps.back(statesBack.size()>1,statesBack.size());
            listenSteps.next(!statesNext.empty(),statesNext.size());
        }
    }

    public void inNextToBack(){
        if(statesNext.size()>0){
            statesBack.push(statesNext.pop());
            if(statesBack.peek().isReadiness()){
                load(statesBack.peek().getTarget(),statesBack.peek().getMut(),statesBack.peek());
                saveStep.saveState(statesBack.peek());
            }else {
                inNextToBack();
            }
        }
        if(listenSteps!=null){
            listenSteps.back(statesBack.size()>1,statesBack.size());
            listenSteps.next(!statesNext.empty(),statesNext.size());
        }
    }
    /*********************************************************************/

    private void loadMatrix(State state){
         RepDraw.get().mutableImg(null,state.getRepImg(),0,false);
         RepDraw.get().mutableLyr(null,state.getRepLyr(),0,false);

    }

    private void loadImg(State state){

    }

    private void loadLyr(State state){

    }

    private void loadAll(State state){

    }

    private void startProject(State state){
        if(RepDraw.get().isImg()){
            if(RepDraw.get().isLyr()){
                state.setNameImg(name(PR_IMG))
                        .setNameLyr(name(PR_LYR))
                        .setRepImg(getCopy(RepDraw.get().getIMat().getRepository()))
                        .setRepLyr(getCopy(RepDraw.get().getLMat().getRepository()));
            }else{
                state.setNameImg(name(PR_IMG))
                        .setNameLyr(PR_NON)
                        .setRepImg(getCopy(RepDraw.get().getIMat().getRepository()))
                        .setRepLyr(null);
            }
            addBack(state);
        }
    }


}
