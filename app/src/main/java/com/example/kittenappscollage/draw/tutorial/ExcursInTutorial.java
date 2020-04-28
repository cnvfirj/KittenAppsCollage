package com.example.kittenappscollage.draw.tutorial;

import android.content.Context;
import android.util.Log;

import com.example.kittenappscollage.R;
import com.example.targetviewnote.TargetView;

public class ExcursInTutorial {


    private int step;

    private Integer[]targets;

    private String[]titles;

    private String[]notes;

    private int[]iconsTitle;

    private int[]iconsSoftKey;

    private int[]sizeWindow;

    private TargetView targetView;

    private boolean ongoing;


    public ExcursInTutorial(TargetView target){
        targetView = target;
    }

    public ExcursInTutorial ongoing(boolean o){
        ongoing = o;
        return this;
    }

    public ExcursInTutorial targets(Integer[]targets){
        this.targets = targets;
        return this;
    }

    public ExcursInTutorial sizeWin(int[]sizes){
        sizeWindow = sizes;
        return this;
    }

    public ExcursInTutorial titles(String[]titles){
        this.titles = titles;
        return this;
    }

    public ExcursInTutorial notes(String[] notes){
        this.notes = notes;
        return this;
    }

    public ExcursInTutorial iconsTitle(int[]icons){
        iconsTitle = icons;
        return this;
    }

    public ExcursInTutorial iconsSoftKey(int[]icons){
        iconsSoftKey = icons;
        return this;
    }

    public ExcursInTutorial setStep(int s){
        step = s;
        return this;
    }

    public int getStep(){
        return step;
    }

    public int countTutorial(){
        return targets.length;
    }

    public boolean getOngoing(){
        return ongoing;
    }

    public boolean next(){
        step++;
        if(step<targets.length) {
            targetView.target(targets[step])
                    .textTitle(titles[step])
                    .textNote(notes[step])
                    .iconTitle(R.drawable.icon_camera)
                    .iconSoftKey(R.drawable.ic_icon_next)
                    .step();
        }else {
            targetView.close();
            ongoing = false;
        }
        return ongoing;
    }

    public void start(){
        ongoing = true;
       targetView.target(targets[step])
               .textTitle(titles[step])
               .textNote(notes[step])
               .iconTitle(R.drawable.icon_camera)
               .iconSoftKey(R.drawable.ic_icon_next)
               .show();
    }
}