package com.kittendevelop.kittenappscollage.draw.tutorial;

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
        reset();
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

    public boolean isWinVis(){
        return targetView.isWinVis();
    }

    public boolean next(){
        step++;
        if(step<targets.length) {
            targetView.target(targets[step])
                    .textTitle(getTitle())
                    .textNote(getNote())
                    .sizeContentWindow(getSizeWindow())
                    .iconTitle(icTitle())
                    .iconSoftKey(icSoftKey())
                    .step();
        }else {
            targetView.close();
            ongoing = false;
        }
        return ongoing;
    }

    public void stop(){
        ongoing = false;
        reset();
        targetView.close();
    }

    public void start(){
        ongoing = true;
       targetView.target(targets[step])
               .textTitle(getTitle())
               .textNote(getNote())
               .sizeContentWindow(getSizeWindow())
               .iconTitle(icTitle())
               .iconSoftKey(icSoftKey())
               .show();
    }

    private int getSizeWindow(){
        if(sizeWindow!=null)return sizeWindow[step];
        else return TargetView.MAXI_VEIL;
    }

    private String getTitle(){
        if(titles!=null)return titles[step];
        else return null;
    }

    private String getNote(){
        if(titles!=null)return notes[step];
        else return null;
    }

    private int icTitle(){
        if(iconsTitle!=null)return iconsTitle[step];
        else return 0;
    }

    private int icSoftKey(){
        if(iconsSoftKey!=null)return iconsSoftKey[step];
        else return 0;
    }

    private void reset(){
        sizeWindow = null;
        titles = null;
         step = 0;
         notes = null;
         iconsTitle = null;
         iconsSoftKey = null;
    }
}
