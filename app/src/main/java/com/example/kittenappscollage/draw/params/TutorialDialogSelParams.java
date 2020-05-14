package com.example.kittenappscollage.draw.params;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.kittenappscollage.R;
import com.example.kittenappscollage.draw.addLyrs.Tutorials;
import com.example.kittenappscollage.draw.tutorial.ExcursInTutorial;
import com.example.targetviewnote.TargetView;

public class TutorialDialogSelParams extends DialogSelectParams implements Tutorials, TargetView.OnClickTargetViewNoleListener {

    private final String KEY_STEP_TUTORIAL = "TutorialDialogSelParams step tutorial_";

    protected ExcursInTutorial excurs;

    @Override
    public void onResume() {
        super.onResume();
        excurs(getPreferences().getInt(KEY_STEP_TUTORIAL,0));
    }

    @Override
    public void onClickTarget(int i) {
        if(i==TargetView.TOUCH_SOFT_KEY||i==TargetView.TOUCH_TARGET){
            if(excurs.next()) {
                getEditor().putInt(KEY_STEP_TUTORIAL, excurs.getStep()).apply();
            }else {
                getEditor().putInt(KEY_STEP_TUTORIAL, 999).apply();
            }
        }
    }


    @Override
    public Integer[] targetsEx() {
        return new Integer[]{R.id.present_paint,R.id.present_erase,R.id.present_text,R.id.color_pick,
                R.id.prop_paint_alpha,R.id.prop_paint_width,R.id.prop_erase_alpha,
                 R.id.prop_done,R.id.prop_back};
    }

    @Override
    public int[] sizesWin() {
        return new int[]{TargetView.MIDI_VEIL,TargetView.MIDI_VEIL,TargetView.MIDI_VEIL,TargetView.MINI_VEIL,
                TargetView.MINI_VEIL,TargetView.MINI_VEIL,TargetView.MINI_VEIL,
                TargetView.MINI_VEIL,TargetView.MINI_VEIL};
    }

    @Override
    public String[] getTitles() {
        return getContext().getResources().getStringArray(R.array.prop_title);
    }

    @Override
    public String[] getNotes() {
        return getContext().getResources().getStringArray(R.array.prop_note);
    }

    @Override
    public int[] icTitles() {
        return null;
    }

    @Override
    public int[] icSoftKey() {
        return null;
    }

    private void excurs(int step){
        if(step<999){
            initExcurs();
            if(!excurs.getOngoing()) {
                excurs
                        .targets(targetsEx())
                        .titles(getTitles())
                        .notes(getNotes())
                        .sizeWin(sizesWin())
                        .iconsTitle(icTitles())
                        .iconsSoftKey(icSoftKey())
                        .ongoing(true)
                        .setStep(step);
                excurs.start();
            }
        }
    }

    private void initExcurs(){
        if(getContext()!=null&&excurs==null) {
            TargetView t = TargetView.build(this)
                    .touchExit(TargetView.NON_TOUCH)
                    .dimmingBackground(getContext().getResources().getColor(R.color.colorDimenPrimaryDarkTransparent));
            excurs = new ExcursInTutorial(t);
        }
    }

    protected SharedPreferences getPreferences(){
        return getActivity().getPreferences(Context.MODE_PRIVATE);
    }

    protected SharedPreferences.Editor getEditor(){
        return getPreferences().edit();
    }
}
