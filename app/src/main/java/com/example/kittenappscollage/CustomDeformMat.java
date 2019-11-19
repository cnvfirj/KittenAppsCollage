package com.example.kittenappscollage;

import com.example.mutablebitmap.DeformMat;

public class CustomDeformMat extends DeformMat {

    public CustomDeformMat() {
    }

    public CustomDeformMat endStepDeform(){
        bInter = new float[]{0,0,0,0,0,0,0,0};
        return this;
    }
}
