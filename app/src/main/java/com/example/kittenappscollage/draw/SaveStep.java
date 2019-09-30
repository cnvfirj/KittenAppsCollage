package com.example.kittenappscollage.draw;

public class SaveStep  {

    private static SaveStep single;

    public static final int MUTABLE_MATRIX = 0;
    public static final int DRAW_IMG = 1;
    public static final int DRAW_LYR = 2;
    public static final int DRAW_ALL = 3;
    public static final int MUTABLE_IMG = 4;
    public static final int MUTABLE_LYR = 5;
    public static final int MUTABLE_ALL = 6;

    public static SaveStep get(){
        if(single==null){
            synchronized (SaveStep.class){
                single = new SaveStep();
            }
        }
        return single;

    }

    public void save(int type){

    }
}
