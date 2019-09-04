package com.example.kittenappscollage.draw.addLyrs;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.kittenappscollage.R;
import com.example.kittenappscollage.view.ExtendsSeekBar;
import com.example.kittenappscollage.view.PresentLyr;
import com.mohammedalaa.seekbar.RangeSeekBarView;

import static com.example.kittenappscollage.helpers.Massages.MASSAGE;

public class AddLyr extends Fragment implements View.OnClickListener, ExtendsSeekBar.TrackSeekBar {

    private SelectorFrameFragments selector;

    private ImageView aBack, aDone, aClose, aMirror;

    private ProgressBar aProgress;

    private ExtendsSeekBar aScale, aAlpha;

    private int aPercentScale, aPercentAlpha;

    private Bitmap aLyr;

    private PresentLyr aPresent;

    public AddLyr() {
        aPercentAlpha = 100;
        aPercentScale = 100;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_add_lyr,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
    }

    public void setBitmap(Bitmap bitmap){
        aLyr = bitmap;
    }

    private void initViews(View v){
        aBack = v.findViewById(R.id.dialog_back);
        aBack.setOnClickListener(this);
        aDone = v.findViewById(R.id.dialog_apply);
        aDone.setOnClickListener(this);
        aClose = v.findViewById(R.id.dialog_close);
        aClose.setOnClickListener(this);
        aMirror = v.findViewById(R.id.dialog_mirror);
        aMirror.setOnClickListener(this);
        aScale = v.findViewById(R.id.dialog_seek_scale);
        aScale.setTracker(this);
        aScale.setValue(aPercentScale);
        aAlpha = v.findViewById(R.id.dialog_seek_alpha);
        aAlpha.setTracker(this);
        aAlpha.setValue(aPercentAlpha);
        aProgress = v.findViewById(R.id.dialog_add_progress);
        aProgress.setVisibility(View.INVISIBLE);
        aPresent = v.findViewById(R.id.dialog_add_present_lyr);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.dialog_back:
                pressBack((ImageView) view);
                break;
            case R.id.dialog_apply:
                pressDone((ImageView) view);
                break;
            case R.id.dialog_close:
                pressClose((ImageView) view);
                break;
            case R.id.dialog_mirror:
                pressMirror((ImageView) view);
                break;

        }
    }

    @Override
    public void touch(SeekBar s) {
        switch (s.getId()){
            case R.id.dialog_seek_alpha:
                aPercentAlpha = aAlpha.getValue();
                break;
            case R.id.dialog_seek_scale:
                aPercentScale = aScale.getValue();
                break;
        }
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private void applyParams(boolean mirror){
         aProgress.setVisibility(View.VISIBLE);
         if(mirror){

         }
    }

    public void clear(){
        if(aLyr!=null&&!aLyr.isRecycled()){
            aLyr.recycle();
        }
    }
    private void pressMirror(ImageView view){
        view.setSelected(!view.isSelected());
    }

    private void pressDone(ImageView view){
        view.setSelected(!view.isSelected());
        selector = (SelectorFrameFragments)getParentFragment();
        selector.doneLyr(aLyr);
    }

    private void pressClose(ImageView view){
        view.setSelected(!view.isSelected());
        selector = (SelectorFrameFragments)getParentFragment();
        selector.exitAll();
    }

    private void pressBack(ImageView view){
        view.setSelected(!view.isSelected());
        selector = (SelectorFrameFragments)getParentFragment();
        selector.backInSelectedLyr();
    }





}
