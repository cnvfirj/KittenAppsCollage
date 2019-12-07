package com.example.kittenappscollage.draw.saveSteps;
import com.example.mutmatrix.CompRep;

import java.io.Serializable;

public class State implements Serializable {

    private int target;

    private int mut;

    private String nameImg;

    private String nameLyr;

    private String pathData;

    private String pathFoldImg;

    private String pathFoldData;

    private CompRep repImg;

    private CompRep repLyr;

    private boolean readiness;

    public State() {
        readiness = false;
    }

    public State setNameImg(String name) {
        this.nameImg = name;
        return this;
    }

    public State setNameLyr(String name) {
        this.nameLyr = name;
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

    public State setPathFoldImg(String fold){
        this.pathFoldImg = fold;
        return this;
    }

    public State setPathData(String path){
        this.pathData = path;
        return this;
    }

    public State setPathFoldData(String fold){
        this.pathFoldData = fold;
        return this;
    }

    public String getPathFoldData(){
        return pathFoldData;
    }
    public String getPathFoldImg(){
        return pathFoldImg;
    }

    public String getPathLyr(){
        return pathFoldImg+nameLyr;
    }

    public String getPathImg(){
        return pathFoldImg+nameImg;
    }

    public String getPathData() {
        return pathFoldData+pathData;
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

    public String getNameImg() {
        return nameImg;
    }

    public String getNameLyr() {
        return nameLyr;
    }

    public CompRep getRepImg() {
        return repImg;
    }

    public CompRep getRepLyr() {
        return repLyr;
    }
}
