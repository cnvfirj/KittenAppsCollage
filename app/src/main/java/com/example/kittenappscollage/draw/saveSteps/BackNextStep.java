package com.example.kittenappscollage.draw.saveSteps;

import com.example.kittenappscollage.draw.repozitoryDraw.RepDraw;

import static com.example.kittenappscollage.helpers.Massages.LYTE;

public class BackNextStep extends StorySteps{

    private static BackNextStep single;

    private LoadStep loadStep;

    private BackNextStep() {
        loadStep = new LoadStep();
    }

    public static BackNextStep get(){
        if(single==null){
            synchronized (BackNextStep.class){
                single = new BackNextStep();
            }
        }
        return single;
    }

    public BackNextStep setOldState(State state){
        statesBack.push(state);
        return this;
    }

    public void load(int target, int mut, State state){
        if(target==TARGET_ALL){
            if(mut==MUT_MATRIX)loadMatrix(state);
            else if(mut==MUT_CONTENT||mut==MUT_SCALAR){
                loadAll(state);
            }
        }else if(target==TARGET_IMG){
            if(mut==MUT_MATRIX)loadMatrix(state);
            else if(mut==MUT_CONTENT||mut==MUT_SCALAR){
                loadImg(state);
            }
        }else if(target==TARGET_LYR){
            if(mut==MUT_MATRIX)loadMatrix(state);
            else if(mut==MUT_CONTENT||mut==MUT_SCALAR){
                loadLyr(state);
            }
        }
    }

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

    private void loadMatrix(State state){
         RepDraw.get().stepLoadMatr(state.getRepImg(),state.getRepLyr());

    }

    private void loadImg(State state){
        RepDraw.get().startSingleMutable();
       if(state.getNameImg().equals(PR_NON))RepDraw.get().stepLoadImg(null,null,true);
       else {
           loadStep.loadImg(state);
       }
    }

    private void loadLyr(State state){
        RepDraw.get().startSingleMutable();
        if(state.getNameLyr().equals(PR_NON))RepDraw.get().stepLoadLyr(null,null,true);
       else {
           loadStep.loadLyr(state);
        }
    }

    private void loadAll(State state){
        RepDraw.get().startAllMutable();
        if(!state.getNameImg().equals(PR_NON)){
//            if(!state.getNameLyr().equals(PR_NON)){
                loadStep.loadAll(state);
//            }else {
//                loadStep.loadImg(state);
//            }
        }
    }

}
