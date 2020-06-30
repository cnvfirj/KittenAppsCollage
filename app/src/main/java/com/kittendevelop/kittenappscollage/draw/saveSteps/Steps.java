package com.kittendevelop.kittenappscollage.draw.saveSteps;

import com.kittendevelop.kittenappscollage.helpers.App;
import com.kittendevelop.kittenappscollage.helpers.RequestFolder;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Stack;

public class Steps {

    public static final int MUT_MATRIX = 210;//изменение касается только матрицы
    public static final int MUT_CONTENT = 211;//изменение касается только внутреннего содержания
    public static final int MUT_SCALAR = 212;//изменения касаются матрицы и содержания

    public static final int TARGET_ALL = 333;
    public static final int TARGET_IMG = 334;
    public static final int TARGET_LYR = 335;

    protected final String PR_IMG = "img_";
    protected final String PR_LYR = "lyr_";
    protected final String PR_FILE = ".png";
    protected final String PR_DATA = "step.data";

    protected final String FOLD_STEPS = "/steps/";
    protected final String FOLD_DATA = "/data/";
    protected final String COMMON_FOLD;
    public static final String PR_NON = "non";

    protected SaveStep saveStep;

    protected Stack<State> statesBack, statesNext;

    protected OnBackNextStepListen listenSteps;

    protected Steps() {
        statesBack = new Stack<>();
        statesNext = new Stack<>();
        COMMON_FOLD = RequestFolder.getPersonalFolder(App.getMain());
        saveStep = new SaveStep();
    }

    public Steps setOnBackNextStepsListen(OnBackNextStepListen listen){
        listenSteps = listen;
        return this;
    }

    protected String name(String target){
        Calendar calendar = new GregorianCalendar();
        int day = calendar.get(Calendar.DAY_OF_YEAR);
        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        int second_of_day = ((hour*60)+minute)*60+second;
        return target+day+"_"+second_of_day+PR_FILE;
    }

    public void clearStacks(){
        statesNext.clear();
        statesBack.clear();
    }

    public void remove(){
        clearStacks();
        ClearCatch.clearAll(COMMON_FOLD+FOLD_STEPS);
        ClearCatch.clearAll(COMMON_FOLD+FOLD_DATA);
    }

    public String getFoldData(){
        return COMMON_FOLD+FOLD_DATA;
    }

    public interface OnBackNextStepListen{

        void back(boolean enable, int size);
        void next(boolean enable, int size);
    }

}
