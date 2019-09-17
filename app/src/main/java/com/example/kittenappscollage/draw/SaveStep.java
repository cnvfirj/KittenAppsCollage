package com.example.kittenappscollage.draw;

public class SaveStep  {

    private static SaveStep single;

    public static SaveStep get(){
        if(single==null){
            synchronized (SaveStep.class){
                single = new SaveStep();
            }
        }
        return single;

    }

    public void save(){

    }
}
