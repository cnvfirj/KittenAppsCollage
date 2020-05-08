package com.example.kittenappscollage.draw.addLyrs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.kittenappscollage.R;
import com.example.kittenappscollage.draw.addLyrs.loadImage.DecodeCamera;
import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.Facing;
import com.otaliastudios.cameraview.Flash;

public class AddLyrInCam extends SelectedFragment{

    private ImageView aDone, aExit, aChange, aFlash;

    private ProgressBar aProgress;

    private CameraView aCamera;

    private Flash aCameraFlash = Flash.OFF;

    private Facing aFacing = Facing.BACK;

    private FrameLayout aFrame;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_lyr_in_cam,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        aDone = view.findViewById(R.id.cam_click);
        aExit = view.findViewById(R.id.cam_exit);
        aChange = view.findViewById(R.id.cam_change);
        aFlash = view.findViewById(R.id.cam_flash);
        aProgress = view.findViewById(R.id.progress_cam);
        aProgress.setVisibility(View.INVISIBLE);
        aFrame = view.findViewById(R.id.camera_frame);
        aCamera = new CameraView(getContext());
        aCamera.setCropOutput(true);
        aFrame.addView(aCamera);
        addListen();
    }

    @Override
    protected void readinessView(View v) {

    }

    @Override
    public void onClick(View view) {
       switch (view.getId()){
           case R.id.cam_click:
               pressClick();
               break;
           case R.id.cam_exit:
               pressExit();
               break;
           case R.id.cam_change:
               pressChange();
               break;
           case R.id.cam_flash:
               pressFlash();
               break;

       }
    }

    @Override
    public Integer[] targetsEx() {
        return new Integer[0];
    }

    @Override
    public int[] sizesWin() {
        return new int[0];
    }

    @Override
    public String[] getTitles() {
        return new String[0];
    }

    @Override
    public String[] getNotes() {
        return new String[0];
    }

    @Override
    public int[] icTitles() {
        return new int[0];
    }

    @Override
    public int[] icSoftKey() {
        return new int[0];
    }

    private void pressClick(){
        aCamera.captureSnapshot();
    }

    private void pressExit(){
        selector = (SelectorFrameFragments) getParentFragment();
        selector.exitAll();
    }

    private void pressChange(){
        aChange.setSelected(!aChange.isSelected());
        if(aCamera.getFacing().equals(Facing.FRONT)){
            aCamera.setFacing(Facing.BACK);
        }else {
            aCamera.setFacing(Facing.FRONT);
        }
    }

    private void pressFlash(){
            aFlash.setSelected(!aFlash.isSelected());
            if (aFlash.isSelected()) {
               aCameraFlash = Flash.TORCH;
            } else {
               aCameraFlash = Flash.OFF;
            }
            aCamera.setFlash(aCameraFlash);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(aCamera!=null) {
            aCamera.start();
            aCamera.setFacing(aFacing);
            aCamera.setFlash(aCameraFlash);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(aCamera!=null) {
            aFacing = aCamera.getFacing();
            aCamera.setFlash(Flash.OFF);
            aCamera.stop();
        }

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if(aCamera!=null)
            aCamera.destroy();

    }

    private void addListen(){
        aDone.setOnClickListener(this);
        aExit.setOnClickListener(this);
        aChange.setOnClickListener(this);
        aFlash.setOnClickListener(this);
        aCamera.addCameraListener(new CameraListener() {
            private int orientation;
            @Override
            public void onPictureTaken(byte[] jpeg) {
                super.onPictureTaken(jpeg);
                selector = (SelectorFrameFragments) getParentFragment();
                selector.backInAddLyr(aDone,new DecodeCamera.CameraProperties(jpeg,aCamera.getFacing().name(),orientation));

            }

            @Override
            public void onOrientationChanged(int orientation) {
                super.onOrientationChanged(orientation);
                this.orientation = orientation;
            }
        });

    }
}
