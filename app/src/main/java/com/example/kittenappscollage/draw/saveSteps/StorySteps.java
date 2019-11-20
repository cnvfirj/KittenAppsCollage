package com.example.kittenappscollage.draw.saveSteps;


import com.example.kittenappscollage.draw.repozitoryDraw.RepDraw;
import com.example.kittenappscollage.helpers.App;
import com.example.kittenappscollage.helpers.RequestFolder;

import static com.example.kittenappscollage.helpers.Massages.LYTE;

public class StorySteps extends CommonSteps {


    /*сохраняем шаг при действиях*/
    public void save(int target, int mut){
        State state = new State()
                .setTarget(target)
                .setMut(mut)
                .setPathFoldImg(RequestFolder.getPersonalFolder(App.getMain())+FOLD_STEPS)
                .setPathFoldData(RequestFolder.getPersonalFolder(App.getMain())+FOLD_DATA)
                .setNameData(PR_DATA);

        if(target==TARGET_ALL){
            if(mut==MUT_MATRIX)saveMatrix(state);
            if(mut==MUT_CONTENT||mut==MUT_SCALAR){
                saveAll(state);
            }
        }else if(target==TARGET_IMG){
            if(mut==MUT_MATRIX)saveMatrix(state);
            if(mut==MUT_CONTENT||mut==MUT_SCALAR){
                saveImg(state);
            }
        }else if(target==TARGET_LYR){
            if(mut==MUT_MATRIX)saveMatrix(state);
            if(mut==MUT_CONTENT||mut==MUT_SCALAR){
                saveLyr(state);
            }
        }
        resetStatesNext();
    }

    private void saveImg(State state){
        if(statesBack.empty()) {
            startProject(state);
            save();
        }else {
            state.setNameImg(name(PR_IMG))
                    .setNameLyr(statesBack.peek().getNameLyr())
                    .setRepImg(getCopy(RepDraw.get().getIMat().getRepository()))
                    .setRepLyr(getCopy(RepDraw.get().getLMat().getRepository()));
            addBack(state);
            save();
        }
    }

    private void saveLyr(State state){
        if(statesBack.empty()) {
            startProject(state);
            save();
        }else {
            state.setNameImg(statesBack.peek().getNameImg())
                    .setNameLyr(name(PR_LYR))
                    .setRepImg(getCopy(RepDraw.get().getIMat().getRepository()))
                    .setRepLyr(getCopy(RepDraw.get().getLMat().getRepository()));
            addBack(state);
            save();
        }

    }

    private void saveAll(State state){
        startProject(state);
        save();
    }

    private void saveMatrix(State state){
        if(statesBack.empty()) {
            startProject(state);
            save();
        }else{
            state.setNameImg(statesBack.peek().getNameImg())
                    .setNameLyr(statesBack.peek().getNameLyr())
                    .setRepImg(getCopy(RepDraw.get().getIMat().getRepository()))
                    .setRepLyr(getCopy(RepDraw.get().getLMat().getRepository()));
            addBack(state);
            save();
        }
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
