package com.example.kittenappscollage.draw.saveSteps;

import com.example.kittenappscollage.draw.RepDraw;
import com.example.kittenappscollage.helpers.App;
import com.example.kittenappscollage.helpers.RequestFolder;
import com.example.mutablebitmap.CompRep;

import java.io.File;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Stack;

import static com.example.kittenappscollage.helpers.Massages.LYTE;

public class BackNextStep {

    private static BackNextStep single;

//    public static final int STEP_IMG = 110;//изменяется только имг
//    public static final int STEP_LYR = 111;//изменяется только лир
//    public static final int STEP_ALL = 112;//изменяется все

    public static final int MUT_MATRIX = 210;//изменение касается только матрицы
    public static final int MUT_CONTENT = 211;//изменение касается только внутреннего содержания
    public static final int MUT_SCALAR = 212;//изменения касаются матрицы и содержания

    public static final int TARGET_ALL = 333;
    public static final int TARGET_IMG = 334;
    public static final int TARGET_LYR = 335;

    private final String PR_IMG = "img_";
    private final String PR_LYR = "lyr_";
    private final String PR_FILE = ".png";
    private final String PR_DATA = "step.data";

    private final String FOLD_STEPS = "/steps/";
    private final String FOLD_DATA = "/data/";
    private final String COMMON_FOLD;
    public static final String PR_NON = "non";



    private SaveStep saveStep;



    private Stack<State>statesBack, statesNext;

    private BackNextStep() {
        statesBack = new Stack<>();
        statesNext = new Stack<>();
        COMMON_FOLD = RequestFolder.getPersonalFolder(App.getMain());
        saveStep = new SaveStep();
    }

    public static BackNextStep get(){
        if(single==null){
            synchronized (BackNextStep.class){
                single = new BackNextStep();
            }
        }
        return single;

    }

    /*сохраняем шаг при действиях*/
    public BackNextStep save(int target, int mut){
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
        return this;
    }

    public void union(){
        /*сохраняем шаг при объединении*/
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
        statesBack.push(state);
        save();
    }
    public void change(){
        /*сохраняем шаг при обмене*/
        State state = new State();
        state.setNameImg(statesBack.peek().getNameLyr())
                .setNameLyr(statesBack.peek().getNameImg())
                .setRepImg(getCopy(statesBack.peek().getRepImg()))
                .setRepLyr(getCopy(statesBack.peek().getRepLyr()))
                .setPathFoldImg(RequestFolder.getPersonalFolder(App.getMain())+FOLD_STEPS)
                .setPathFoldData(RequestFolder.getPersonalFolder(App.getMain())+FOLD_DATA)
                .setNameData(PR_DATA)
                .setTarget(TARGET_ALL)
                .setMut(MUT_SCALAR);
        resetStatesNext();
        statesBack.push(state);
        save();
    }

    public void deleteLyr(){
        /*сохраняем шаг при удалении верхнего слоя*/
        State state = new State();

        state.setNameImg(name(PR_IMG))
                .setNameLyr(PR_NON)
                .setRepImg(getCopy(RepDraw.get().getIMat().getRepository()))
                .setRepLyr(null)
                .setPathFoldImg(RequestFolder.getPersonalFolder(App.getMain())+FOLD_STEPS)
                .setPathFoldData(RequestFolder.getPersonalFolder(App.getMain())+FOLD_DATA)
                .setNameData(PR_DATA)
                .setTarget(TARGET_LYR)
                .setMut(MUT_SCALAR);
        resetStatesNext();
        statesBack.push(state);
        save();
    }

    private void save(){
         /*сохраняем на у-ве последний State и слои*/
        saveStep.register(statesBack.peek()).save();

    }

    public void remove(){
        statesNext.clear();
        statesBack.clear();
        /*чистим все стеки и директорию сохранения*/
    }

    private void saveImg(State state){
        if(statesBack.empty()) {
            startProject(state);
        }else {
            state.setNameImg(name(PR_IMG))
                    .setNameLyr(statesBack.peek().getNameLyr())
                    .setRepImg(getCopy(RepDraw.get().getIMat().getRepository()))
                    .setRepLyr(getCopy(RepDraw.get().getLMat().getRepository()));
            statesBack.push(state);
        }
        save();
    }

    private void saveLyr(State state){
        if(statesBack.empty()) {
            startProject(state);
        }else {
            state.setNameImg(statesBack.peek().getNameImg())
                    .setNameLyr(name(PR_LYR))
                    .setRepImg(getCopy(RepDraw.get().getIMat().getRepository()))
                    .setRepLyr(getCopy(RepDraw.get().getLMat().getRepository()));
            statesBack.push(state);
            save();
        }

    }

    private void saveAll(State state){
            startProject(state);

    }

    private void saveMatrix(State state){
        if(statesBack.empty()) {
            startProject(state);
        }else{
            state.setNameImg(statesBack.peek().getNameImg())
                    .setNameLyr(statesBack.peek().getNameLyr())
                    .setRepImg(getCopy(RepDraw.get().getIMat().getRepository()))
                    .setRepLyr(getCopy(RepDraw.get().getLMat().getRepository()));
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
            statesBack.push(state);
            save();
        }
    }

    private CompRep getCopy(CompRep rep){
        if (rep!=null)return rep.copy();
        else return null;
    }

    private void resetStatesNext(){
        if(!statesNext.empty()){
            statesNext.clear();
        }
    }
    private String name(String target){
        Calendar calendar = new GregorianCalendar();
        int day = calendar.get(Calendar.DAY_OF_YEAR);
        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        int second_of_day = ((hour*60)+minute)*60+second;
        return target+day+"_"+second_of_day+PR_FILE;
    }


}
