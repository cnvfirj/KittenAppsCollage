package com.kittendevelop.kittenappscollage.draw.addLyrs;

import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.example.dynamikseekbar.DynamicSeekBar;
import com.kittendevelop.kittenappscollage.R;
import com.example.targetviewnote.TargetView;
import com.madrapps.pikolo.HSLColorPicker;
import com.madrapps.pikolo.listeners.SimpleColorSelectionListener;

public class AddLyrInCreator extends SelectedFragment implements DynamicSeekBar.OnSeekBarChangeListener{

    private final String KEY_COLOR_FON = "key color fon AddLyrInCreator";
    private final String KEY_WIDTH_BLANK = "width blank AddLyrInCreator";
    private final String KEY_HEIGHT_BLANK = "height blank AddLyrInCreator";

    private final String KEY_STEP_TUTORIAL = "AddLyrInCreator step tutorial_112235472";

    private PreviewBlankBitmp aPreview;

    private HSLColorPicker aSelectColor;

    private ImageView aColorPickCall, aDoneParams, aExitAll;

    private int aColor;

    private int aNumb;

    private boolean aBlock;

    private DynamicSeekBar aSeekWidth, aSeekHeight;

    public AddLyrInCreator() {
        aColor = Color.WHITE;
        aBlock = false;
    }

    @Override
    public void onResume() {
        setChapter(KEY_STEP_TUTORIAL);
        super.onResume();
            aColor = getPreferences().getInt(KEY_COLOR_FON, Color.WHITE);
            aPreview.fon(aColor);
            aSelectColor.setColor(aColor);
            aColorPickCall.setImageTintList(ColorStateList.valueOf(aColor));
            int width = getPreferences().getInt(KEY_WIDTH_BLANK, 0);
            int height = getPreferences().getInt(KEY_HEIGHT_BLANK, 0);
            aPreview.size(new Size(width, height));
            paramView(aPreview);
    }

    @Override
    public void onPause() {
        super.onPause();
            SharedPreferences.Editor e = getEditor();
            e.putInt(KEY_COLOR_FON, aPreview.getColorFon());
            e.putInt(KEY_WIDTH_BLANK, aPreview.getSize().getWidth());
            e.putInt(KEY_HEIGHT_BLANK, aPreview.getSize().getHeight());
            e.apply();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_lyr_in_creator,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        aPreview = view.findViewById(R.id.creator_blank_btmp);
        aSelectColor = view.findViewById(R.id.color_pick);
        aColorPickCall = view.findViewById(R.id.color_pick_call);
        aDoneParams = view.findViewById(R.id.creator_lyr_done);
        aExitAll = view.findViewById(R.id.creator_lyr_back);
        aSeekWidth = view.findViewById(R.id.creator_width);
        aSeekHeight = view.findViewById(R.id.creator_height);
        aNumb = 0;
        paramView(aPreview);
        paramView(aSelectColor);
        paramView(aColorPickCall);
        addListen();


    }

    @Override
    protected void readinessView(View v) {
        aNumb++;
        if(aNumb==3){
            applyTransform(true,0);
            applyTransformTools();
        }
        if(v.getId()==R.id.creator_blank_btmp){
            aSeekWidth.setProgress(aPreview.getSize().getWidth());
            aSeekHeight.setProgress(aPreview.getSize().getHeight());
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.creator_lyr_back:
                if(!aBlock) {
                    selector = (SelectorFrameFragments) getParentFragment();
                    assert selector != null;
                    selector.exitAll();
                }
                break;
            case R.id.creator_lyr_done:
                if(!aBlock) {
                    if (memoryBitmap(aPreview.getSize()) < 40) {
                        selector = (SelectorFrameFragments) getParentFragment();
                        String way = aPreview.getSize().getWidth() + "_" + aPreview.getSize().getHeight() + "_" + aPreview.getColorFon();
                        selector.backInAddLyr(view, way);
                    } else {
//                        SHOW_MASSAGE(getActivity(), getActivity().getResources().getString(R.string.big_big_papper) +
//                                aPreview.getSize().getWidth() + ":" + aPreview.getSize().getHeight());
                    }
                }
                break;
            case R.id.color_pick_call:
                callPalette();
                break;
        }
    }

    @Override
    public void onProgressChanged(DynamicSeekBar seekBar, int progress, boolean isTouch) {
        switch (seekBar.getId()){
            case R.id.creator_width:
                 aPreview.size(new Size(seekBar.getProgress(),aPreview.getSize().getHeight()));
                break;
            case R.id.creator_height:
                aPreview.size(new Size(aPreview.getSize().getWidth(),seekBar.getProgress()));
                break;
        }
    }

    @Override
    public void onStartTrackingTouch(DynamicSeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(DynamicSeekBar seekBar) {

    }

    @Override
    public Integer[] targetsEx() {
        return new Integer[]{R.id.creator_lyr_done,R.id.creator_width,R.id.creator_height,R.id.color_pick_call,R.id.creator_lyr_back};
    }

    @Override
    public int[] sizesWin() {
        return new int[]{TargetView.MIDI_VEIL,TargetView.MINI_VEIL,TargetView.MIDI_VEIL,TargetView.MINI_VEIL,TargetView.MINI_VEIL};
    }

    @Override
    public String[] getTitles() {
        return getContext().getResources().getStringArray(R.array.creator_title);
    }

    @Override
    public String[] getNotes() {
        return getContext().getResources().getStringArray(R.array.creator_note);
    }

    @Override
    public int[] icTitles() {
        return null;
    }

    @Override
    public int[] icSoftKey() {
        return getIconSoftKeyAll(targetsEx().length);
    }

    private void addListen(){
        aSelectColor.setColor(aColor);
        aSelectColor.setColorSelectionListener(new SimpleColorSelectionListener(){
            @Override
            public void onColorSelected(int color) {
                super.onColorSelected(color);
                aColor = color;
                aColorPickCall.setImageTintList(ColorStateList.valueOf(color));
                aPreview.fon(aColor);

            }
        });
        aColorPickCall.setOnClickListener(this);
        aDoneParams.setOnClickListener(this);
        aExitAll.setOnClickListener(this);
        aSeekHeight.setOnSeekBarChangeListener(this);
        aSeekWidth.setOnSeekBarChangeListener(this);
    }

    private void callPalette(){
        if(!aColorPickCall.isActivated()){
            applyTransform(false,500);
            aBlock = true;
            /******************************************************************/
            aSeekHeight.animate().translationX(-aSeekHeight.getRight()).setDuration(500).start();
            aSeekWidth.animate().translationY(-aSeekWidth.getBottom()).setDuration(500).start();
            aExitAll.animate().translationX(aExitAll.getWidth()+aDoneParams.getLeft()).setDuration(500).start();
            aDoneParams.animate().translationX(-aDoneParams.getRight()).setDuration(500).start();
            aPreview.shadow(true);
        }else {
            applyTransform(true,500);
            aPreview.fon(aColor);
            aBlock = false;
            aSeekHeight.animate().translationX(0).setDuration(500).start();
            aSeekWidth.animate().translationY(0).setDuration(500).start();
            aExitAll.animate().translationX(0).setDuration(500).start();
            aDoneParams.animate().translationX(0).setDuration(500).start();
            aPreview.shadow(false);
        }
        aColorPickCall.setActivated(!aColorPickCall.isActivated());
    }


    private void applyTransformTools(){
        int margin = aColorPickCall.getTop();
        int side = aColorPickCall.getWidth();
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) aSeekWidth.getLayoutParams();
        params.rightMargin = margin*2+side;
        params.leftMargin = side;
        aSeekWidth.setLayoutParams(params);
        params = (CoordinatorLayout.LayoutParams) aSeekHeight.getLayoutParams();
        params.bottomMargin = margin*2+side;
        params.topMargin = side;
        aSeekHeight.setLayoutParams(params);

        excurs(getPreferences().getInt(KEY_STEP_TUTORIAL,0));
    }

    private void applyTransform(boolean hide, long time){
       float scale = (float) aColorPickCall.getWidth() / (float) aSelectColor.getWidth();
       float transX = (aColorPickCall.getLeft()+(aColorPickCall.getRight()-aColorPickCall.getLeft())/2)-
                (aSelectColor.getLeft()+(aSelectColor.getRight()-aSelectColor.getLeft())/2);
       float transY = (aColorPickCall.getTop()+(aColorPickCall.getBottom()-aColorPickCall.getTop())/2)-
                (aSelectColor.getTop()+(aSelectColor.getBottom()-aSelectColor.getTop())/2);
        if(hide) {
            aSelectColor.animate().scaleX(scale).scaleY(scale).translationX(transX).translationY(transY).setDuration(time).start();
            aColorPickCall.animate().scaleX(1).scaleY(1).translationX(0).translationY(0).setDuration(time).start();
        }else{
            aSelectColor.animate().scaleX(1).scaleY(1).translationX(0).translationY(0).setDuration(time).start();
            aColorPickCall.animate().scaleX(1.5f).scaleY(1.5f).translationX(-transX).translationY(-transY).setDuration(time).start();
        }


    }

    private float memoryBitmap(Size s){
        float memory = (s.getWidth()*4*s.getHeight())/(1024*1000);
        return memory;

    }




}
