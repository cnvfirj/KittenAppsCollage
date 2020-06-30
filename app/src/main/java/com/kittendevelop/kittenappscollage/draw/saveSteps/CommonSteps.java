package com.kittendevelop.kittenappscollage.draw.saveSteps;

import com.kittendevelop.kittenappscollage.draw.repozitoryDraw.RepDraw;
import com.kittendevelop.kittenappscollage.helpers.App;
import com.kittendevelop.kittenappscollage.helpers.RequestFolder;
import com.example.mutmatrix.CompRep;

public class CommonSteps extends Steps {

    public void union(){
        State state = new State();
        state.setNameImg(name(PR_IMG))
                .setNameLyr(PR_NON)
                .setRepImg(getCopy(RepDraw.get().getIMat().getRepository()))
                .setRepLyr(null)
                .setPathFoldImg(RequestFolder.getPersonalFolder(App.getMain())+FOLD_STEPS)
                .setPathFoldData(RequestFolder.getPersonalFolder(App.getMain())+FOLD_DATA)
                .setNameData(PR_DATA)
                .setTarget(TARGET_ALL)
                .setMut(MUT_SCALAR);
        resetStatesNext();
        addBack(state);
        save();
    }

    public void change(){
        State state = new State();
        state.setNameImg(statesBack.peek().getNameLyr())
                .setNameLyr(statesBack.peek().getNameImg())
                .setRepImg(getCopy(RepDraw.get().getIMat().getRepository()))
                .setRepLyr(getCopy(RepDraw.get().getLMat().getRepository()))
                .setPathFoldImg(RequestFolder.getPersonalFolder(App.getMain())+FOLD_STEPS)
                .setPathFoldData(RequestFolder.getPersonalFolder(App.getMain())+FOLD_DATA)
                .setNameData(PR_DATA)
                .setTarget(TARGET_ALL)
                .setMut(MUT_SCALAR);
        resetStatesNext();
        addBack(state);
        save();
    }

    public void deleteLyr(){
        State state = new State();
        state.setNameImg(statesBack.peek().getNameImg())
                .setNameLyr(PR_NON)
                .setRepImg(getCopy(RepDraw.get().getIMat().getRepository()))
                .setRepLyr(null)
                .setPathFoldImg(RequestFolder.getPersonalFolder(App.getMain())+FOLD_STEPS)
                .setPathFoldData(RequestFolder.getPersonalFolder(App.getMain())+FOLD_DATA)
                .setNameData(PR_DATA)
                .setTarget(TARGET_LYR)
                .setMut(MUT_SCALAR);
        resetStatesNext();
        addBack(state);
        save();
    }

    protected CompRep getCopy(CompRep rep){
        if (rep!=null)return rep.copy();
        else return null;
    }

    protected void addBack(State state){
        statesBack.push(state);
        if(listenSteps!=null&&statesBack.size()>1)listenSteps.back(!statesBack.empty(),statesBack.size());
    }

    protected void resetStatesNext(){
        if(!statesNext.empty()){
            statesNext.clear();
            if(listenSteps!=null){
                listenSteps.next(!statesNext.empty(),statesNext.size());
            }
        }
    }

    protected void save(){
        saveStep.register(statesBack.peek()).save();
    }
}

