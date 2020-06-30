package com.kittendevelop.kittenappscollage.draw.addLyrs.loadImage;

import android.content.Context;

import com.kittendevelop.kittenappscollage.R;


public class SelectorLoadProject {

    private LoadForFile sLoadFile;
    private LoadForCam sLoadCam;
    private LoadForNet sLoadNet;
    private LoadForNew sLoadNew;
    private LoadForDrive sLoadDrive;



    private WayLoad sWay;


    public static SelectorLoadProject selector(Context context){
        return new SelectorLoadProject(context);
    }

    private SelectorLoadProject(Context context) {
        sLoadCam = new LoadForCam(context);
        sLoadDrive = new LoadForDrive(context);
        sLoadFile = new LoadForFile(context);
        sLoadNet = new LoadForNet(context);
        sLoadNew = new LoadForNew(context);



        sWay = new WayLoad();
    }

    public SelectorLoadProject listen(LoadProjectListener listener){
        sLoadNew.setListener(listener);
        sLoadNet.setListener(listener);
        sLoadFile.setListener(listener);
        sLoadDrive.setListener(listener);
        sLoadCam.setListener(listener);

        return this;
    }

    public SelectorLoadProject data(Object way, int source){
        switch (source){
            case R.dimen.PATH_NET:
                sWay.setStrategy(sLoadNet);
                break;
            case R.dimen.PATH_NEW:
                sWay.setStrategy(sLoadNew);
                break;
            case R.dimen.PATH_CAM:
                sWay.setStrategy(sLoadCam);
                break;
            case R.dimen.PATH_FILE:
                sWay.setStrategy(sLoadFile);
                break;
            case R.dimen.PATH_DRIVE:
                sWay.setStrategy(sLoadDrive);
                break;
   

        }
        sWay.setWayProject(way);

        return this;
    }
}
