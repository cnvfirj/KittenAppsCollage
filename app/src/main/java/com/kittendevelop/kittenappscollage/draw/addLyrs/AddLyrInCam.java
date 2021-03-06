package com.kittendevelop.kittenappscollage.draw.addLyrs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.kittendevelop.kittenappscollage.R;
import com.kittendevelop.kittenappscollage.draw.addLyrs.loadImage.DecodeCamera;
import com.example.targetviewnote.TargetView;
import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.Facing;
import com.otaliastudios.cameraview.Flash;

import static com.kittendevelop.kittenappscollage.helpers.Massages.LYTE;

public class AddLyrInCam extends SelectedFragment{

    private ImageView aDone, aExit, aChange, aFlash;

    private ProgressBar aProgress;

    private CameraView aCamera;

    private Flash aCameraFlash = Flash.OFF;

    private Facing aFacing = Facing.BACK;

    private FrameLayout aFrame;

    private final String KEY_STEP_TUTORIAL = "AddLyrInCam step tutorial_122";

    @Override
    public void onResume() {
        setChapter(KEY_STEP_TUTORIAL);
        super.onResume();
        if(aCamera!=null) {
            aCamera.start();
            aCamera.setFacing(aFacing);
            aCamera.setFlash(aCameraFlash);
        }

    }

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
        excurs(getPreferences().getInt(KEY_STEP_TUTORIAL,0));
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
        return new Integer[]{R.id.cam_click,R.id.cam_change,R.id.cam_flash,R.id.cam_exit};
    }

    @Override
    public int[] sizesWin() {
        return new int[]{TargetView.MINI_VEIL,TargetView.MINI_VEIL,TargetView.MINI_VEIL,TargetView.MINI_VEIL};
    }

    @Override
    public String[] getTitles() {
        return getContext().getResources().getStringArray(R.array.addlyr_cam_title);
    }

    @Override
    public String[] getNotes() {
        return  getContext().getResources().getStringArray(R.array.addlyr_cam_note);
    }

    @Override
    public int[] icTitles() {
        return getIconSoftKeyAll(targetsEx().length);
    }

    @Override
    public int[] icSoftKey() {
        return null;
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
                if(selector!=null){
                    selector.backInAddLyr(aDone,new DecodeCamera.CameraProperties(jpeg,aCamera.getFacing().name(),orientation));
                }
                else Toast.makeText(getContext(),getContext().getResources().getText(R.string.restart_window),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onOrientationChanged(int orientation) {
                super.onOrientationChanged(orientation);
                this.orientation = orientation;
            }
        });

    }
}
