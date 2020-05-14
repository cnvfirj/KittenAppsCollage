package com.example.kittenappscollage.draw.textProp;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.kittenappscollage.R;
import com.example.kittenappscollage.draw.addLyrs.Tutorials;
import com.example.kittenappscollage.draw.tutorial.ExcursInTutorial;
import com.example.targetviewnote.TargetView;

public class TutorialSelectShrift extends DialogSelectShrift implements Tutorials, TargetView.OnClickTargetViewNoleListener {

    private final String KEY_STEP_TUTORIAL = "TutorialSelectShrift step tutorial__22";

    private final String KEY_STEP_ADD_FONT = "TutorialSelectShrift step add font__22";

    private String chapter;

    public static TutorialSelectShrift get(){
        return new TutorialSelectShrift();
    }

    protected ExcursInTutorial excurs;

    @Override
    public void onResume() {
        super.onResume();
        startExcurs(getPreferences().getInt(KEY_STEP_TUTORIAL,0),KEY_STEP_TUTORIAL);
    }

    @Override
    protected void slideListShrift(boolean open) {
        super.slideListShrift(open);
        startExcurs(getPreferences().getInt(KEY_STEP_ADD_FONT,0),KEY_STEP_ADD_FONT);
    }

    @Override
    public Integer[] targetsEx() {
        if(chapter.equals(KEY_STEP_TUTORIAL)) {
            return new Integer[]{R.id.dialog_edit_text, R.id.action_2, R.id.item_present_draw,
                    R.id.dialog_edit_text_fill_text, R.id.dialog_edit_text_reset_angle,
                    R.id.dialog_edit_text_angle, R.id.action_1, R.id.action_3};
        }else {
            return new Integer[]{R.id.item_present_draw_list_shrift, R.id.dialog_edit_text_down_fonts};
        }
    }

    @Override
    public int[] sizesWin() {
        if(chapter.equals(KEY_STEP_TUTORIAL)) {
            return new int[]{TargetView.MINI_VEIL, TargetView.MINI_VEIL, TargetView.MINI_VEIL,
                    TargetView.MINI_VEIL, TargetView.MINI_VEIL, TargetView.MINI_VEIL,
                    TargetView.MINI_VEIL, TargetView.MINI_VEIL};
        }else {
            return new int[]{TargetView.MINI_VEIL, TargetView.MINI_VEIL};
        }
    }

    @Override
    public String[] getTitles() {
        if(chapter.equals(KEY_STEP_TUTORIAL)) {
            return getContext().getResources().getStringArray(R.array.edit_text_title);
        }else {
            return getContext().getResources().getStringArray(R.array.edit_text_fonts_title);
        }
    }

    @Override
    public String[] getNotes() {
        if(chapter.equals(KEY_STEP_TUTORIAL)) {
            return getContext().getResources().getStringArray(R.array.edit_text_note);
        }else {
            return getContext().getResources().getStringArray(R.array.edit_text_fonts_note);
        }
    }

    @Override
    public int[] icTitles() {
        if(chapter.equals(KEY_STEP_TUTORIAL)) {
            return new int[]{R.drawable.ic_text_enter,0,R.drawable.ic_text,
                    R.drawable.ic_text_stroke, R.drawable.ic_text_italic,
                    R.drawable.ic_text_italic,0,0
            };
        }else {
            return new int[]{R.drawable.ic_icon_list,0};
        }
    }

    @Override
    public int[] icSoftKey() {
        return getIconSoftKeyAll(targetsEx().length);
    }

    @Override
    public void onClickTarget(int i) {
//        if(i==TargetView.TOUCH_SOFT_KEY||i==TargetView.TOUCH_TARGET){
            if(excurs.next()) {
                getEditor().putInt(chapter, excurs.getStep()).apply();
            }else {
                getEditor().putInt(chapter, 999).apply();
                chapter = null;
            }
//        }
    }



    private void startExcurs(int step, String chapter){
        if(step<999){
                initExcurs();
            if(!excurs.getOngoing()) {
                this.chapter = chapter;
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
    private int[]getIconSoftKeyAll(int size){
        int[]ic = new int[size];
        for (int i=0;i<ic.length;i++){
            ic[i]=R.drawable.ic_icon_next;
            if(i==ic.length-1)ic[i]=R.drawable.ic_icon_exit;
        }
        return ic;
    }
    protected SharedPreferences getPreferences(){
        return getActivity().getPreferences(Context.MODE_PRIVATE);
    }

    protected SharedPreferences.Editor getEditor(){
        return getPreferences().edit();
    }
}
