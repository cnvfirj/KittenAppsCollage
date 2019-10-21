package com.example.kittenappscollage.draw.saveSteps;

import com.example.mutablebitmap.CompRep;

import java.io.Serializable;

public class State implements Serializable {

    private int target;

    private int mut;

    private String pathImg;

    private String pathLyr;

    private String pathData;

    private String pathFold;

    private CompRep repImg;

    private CompRep repLyr;

    private boolean readiness;

    public State() {
        readiness = false;
    }

    public State(String pathImg, String pathLyr, CompRep repImg, CompRep repLyr) {
        this.pathImg = pathImg;
        this.pathLyr = pathLyr;
        this.repImg = repImg;
        this.repLyr = repLyr;
    }

    public State setPathImg(String pathImg) {
        this.pathImg = pathImg;
        return this;
    }

    public State setPathLyr(String pathLyr) {
        this.pathLyr = pathLyr;
        return this;
    }

    public State setRepImg(CompRep repImg) {
        this.repImg = repImg;
        return this;
    }

    public State setRepLyr(CompRep repLyr) {
        this.repLyr = repLyr;
        return this;
    }

    public State setTarget(int target) {
        this.target = target;
        return this;
    }

    public State setMut(int mut) {
        this.mut = mut;
        return this;
    }

    public State setNameData(String data) {
        this.pathData = data;
        return this;
    }

    public State setPathFold(String fold){
        this.pathFold = fold;
        return this;
    }
    public String getPathFolder(){
        return pathFold;
    }
    public String getPathData() {
        return pathFold+pathData;
    }

    public void setReadiness(boolean readiness) {
        this.readiness = readiness;
    }

    public boolean isReadiness() {
        return readiness;
    }

    public int getTarget() {
        return target;
    }

    public int getMut() {
        return mut;
    }

    public String getPathImg() {
        return pathImg;
    }

    public String getPathLyr() {
        return pathLyr;
    }

    public CompRep getRepImg() {
        return repImg;
    }

    public CompRep getRepLyr() {
        return repLyr;
    }
}
