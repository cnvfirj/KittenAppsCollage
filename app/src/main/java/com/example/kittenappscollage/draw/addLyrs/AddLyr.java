package com.example.kittenappscollage.draw.addLyrs;

import android.annotation.SuppressLint;
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
import com.example.kittenappscollage.draw.MutableBitmap;
import com.example.kittenappscollage.draw.addLyrs.loadImage.LoadProjectListener;
import com.example.kittenappscollage.draw.addLyrs.loadImage.SelectorLoadProject;
import com.example.kittenappscollage.view.ExtendsSeekBar;
import com.example.kittenappscollage.view.PresentLyr;
import com.mohammedalaa.seekbar.RangeSeekBarView;

import static com.example.kittenappscollage.helpers.Massages.MASSAGE;
import static com.example.kittenappscollage.helpers.Massages.SHOW_MASSAGE;

public class AddLyr extends Fragment implements View.OnClickListener, ExtendsSeekBar.TrackSeekBar  {

    public static final String KEY_EXTRACTOR_WAY = "extractor";

    public static final String KEY_SOURCE = "source";

    private final String NULL_WAY = "null";

    private SelectorFrameFragments selector;

    private ImageView aBack, aDone, aClose, aMirror;

    private ProgressBar aProgress;

    private ExtendsSeekBar aScale, aAlpha;

    private int aPercentScale, aPercentAlpha;

    private Bitmap aLyr;

    private String aWay;

    private int aSource;

    private PresentLyr aPresent;

    public AddLyr() {
        aWay = NULL_WAY;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        aPercentAlpha = 255;
        aPercentScale = 100;
        Bundle b = getArguments();
        if(b!=null){
            aWay = b.getString(KEY_EXTRACTOR_WAY);
            aSource = b.getInt(KEY_SOURCE,-897);
        }
        return inflater.inflate(R.layout.dialog_add_lyr,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);

    }

    public void setBitmap(Bitmap bitmap){
        aLyr = bitmap;
        aPresent.presentBitmap(bitmap);
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

        if(!aWay.equals(NULL_WAY))createBitmap(aWay,aSource);
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

    protected void createBitmap(Object way, int source){
        aProgress.setVisibility(View.VISIBLE);
        ongoingProgress(true);
        SelectorLoadProject
                .selector(getContext())
                .listen(new LoadProjectListener() {
                    @Override
                    public void loadImage(Bitmap image) {
                        if(image.getHeight()==1||image==null) {
                            showToast(source);
                        }
                        aLyr = image;
                        aPresent.presentBitmap(image);
                        aProgress.setVisibility(View.INVISIBLE);
                        ongoingProgress(false);

                    }

                }).data(way,source);
    }

    private void showToast(int source){
        switch (source){
            case R.dimen.PATH_NET:
                SHOW_MASSAGE(getContext(),getContext().getResources().getString(R.string.wrong_link));
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
        applyMutable(false);
    }


     private void pressMirror(ImageView view){
        view.setSelected(!view.isSelected());
        applyMutable(true);
    }

    @SuppressLint("CheckResult")
    private void applyMutable(boolean mirr){
        aProgress.setVisibility(View.VISIBLE);
        ongoingProgress(true);
        if(mirr){

            aPresent.presentBitmap(MutableBitmap.mirror(aPresent.getPresent()));
            aLyr = MutableBitmap.mirror(aLyr);
        }
        MutableBitmap
                .requestMutable(aLyr,aPercentScale,aPercentAlpha)
                .subscribe(bitmap ->{
                    aPresent.presentBitmap((Bitmap) bitmap);
                    aProgress.setVisibility(View.INVISIBLE);
                    ongoingProgress(false);
                } );
    }

    private void ongoingProgress(boolean ongoing){
       aBack.setEnabled(!ongoing);
       aDone.setEnabled(!ongoing);
       aClose.setEnabled(!ongoing);
       aMirror.setEnabled(!ongoing);
       aAlpha.setEnabled(!ongoing);
       aScale.setEnabled(!ongoing);
    }

    public void clear(){
        if(aLyr!=null&&!aLyr.isRecycled())aLyr.recycle();
        if(aPresent!=null)aPresent.clear();
    }

     private void pressDone(ImageView view){
        view.setSelected(!view.isSelected());
        selector = (SelectorFrameFragments)getParentFragment();
        selector.doneLyr(aPresent.getPresent());
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
