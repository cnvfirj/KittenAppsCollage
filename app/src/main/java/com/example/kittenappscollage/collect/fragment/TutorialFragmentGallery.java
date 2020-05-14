package com.example.kittenappscollage.collect.fragment;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.kittenappscollage.R;
import com.example.kittenappscollage.draw.tutorial.ExcursInTutorial;
import com.example.targetviewnote.TargetView;

import static com.example.kittenappscollage.collect.adapters.ListenLoadFoldAdapter.ROOT_ADAPTER;
import static com.example.kittenappscollage.helpers.Massages.LYTE;

public class TutorialFragmentGallery extends FragmentGalleryAddFolder implements TargetView.OnClickTargetViewNoleListener {

    public final static String KEY_ACTIVATE_COLLECT = "activate fragment";

    private final String KEY_STEP_TUTORIAL = "TutorialFragmentGallery step tutorial_24";

    private final String KEY_STEP_BACK_COLL = "TutorialFragmentGallery step tback coll_1124";

    private final String KEY_STEP_MENU_ROOT = "TutorialFragmentGallery step menu root_11124";

    private final String KEY_STEP_MENU_ADAPT = "TutorialFragmentGallery step menu adapt_121124";

    private ExcursInTutorial excursInTutorial;

    private SharedPreferences preferences;

    private SharedPreferences.Editor editor;

    private boolean activeFragment;

    private Bundle args;

    private String chapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        editor = preferences.edit();
        args = new Bundle();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getArguments() != null) {
            activeFragment = getArguments().getBoolean(KEY_ACTIVATE_COLLECT, false);
            if (activeFragment) startExkurs(preferences.getInt(KEY_STEP_TUTORIAL, -1));
        }
    }

    @Override
    protected void readinessScan() {
        if(preferences.getInt(KEY_STEP_TUTORIAL, -1)==999)super.readinessScan();
    }

    @Override
    public void onClickTarget(int i) {
        if(i==TargetView.TOUCH_SOFT_KEY||i==TargetView.TOUCH_TARGET){
            if(excursInTutorial.next()) {
                editor.putInt(chapter, excursInTutorial.getStep()).apply();
            }else {
                editor.putInt(chapter, 999).apply();
                if(chapter.equals(KEY_STEP_TUTORIAL))readinessScan();
                chapter = null;
            }
        }
    }

    public void startTutorial() {
        editor.putInt(KEY_STEP_TUTORIAL, 0).apply();
    }

    public boolean isTutorial(){
        return excursInTutorial.isWinVis();
    }

    public void activateExcurs() {
        startExkurs(preferences.getInt(KEY_STEP_TUTORIAL, -1));
    }

    @Override
    public void onAnimationStart(Animator animation) {

    }

    @Override
    public void onAnimationEnd(Animator animation) {
      /*listen end animation */
        if(chapter!=null){
            if(!excursInTutorial.getOngoing())excursInTutorial.start();
        }
    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }

    @Override
    protected void setIndexAdapter(int i) {
        super.setIndexAdapter(i);
        if(i!=ROOT_ADAPTER){
            int step = preferences.getInt(KEY_STEP_BACK_COLL,0);
            if(step<999){
                initExcurs();
                if(!excursInTutorial.getOngoing()){
                    if(tutorialEx(step)){
                        chapter = KEY_STEP_BACK_COLL;
                    }
                }
            }
        }
    }

    @Override
    protected void visibleMenu() {
        super.visibleMenu();
        if(getIndexAdapter()==ROOT_ADAPTER){
            int step = preferences.getInt(KEY_STEP_MENU_ROOT,0);
            if(step<999){
                initExcurs();
                if(!excursInTutorial.getOngoing()){
                    if(tutorialMenuRoot(step)){
                        chapter = KEY_STEP_MENU_ROOT;
                    }
                }
            }
        }else {
            int step = preferences.getInt(KEY_STEP_MENU_ADAPT,0);
            if(step<999){
                initExcurs();
                if(!excursInTutorial.getOngoing()){
                    if(tutorialMenuAdapt(step)){
                        chapter = KEY_STEP_MENU_ADAPT;
                    }
                }
            }
        }


    }

    @Override
    public void setArguments(@Nullable Bundle args) {
        super.setArguments(args);
        activeFragment = args.getBoolean(KEY_ACTIVATE_COLLECT, false);
    }

    public void setArguments(boolean b) {
        args.putBoolean(KEY_ACTIVATE_COLLECT, b);
        setArguments(args);
    }

    private boolean tutorialMenuRoot(int step){
        if(chapter==null||!chapter.equals(KEY_STEP_MENU_ROOT)) {
            excursInTutorial
                    .targets(new Integer[]{R.id.selected_collect_exit_mode,R.id.selected_collect_1,R.id.selected_collect_3,R.id.selected_collect_4})
                    .titles(getContext().getResources().getStringArray(R.array.collect_root_menu_title))
                    .notes(getContext().getResources().getStringArray(R.array.collect_root_menu_note))
                    .iconsSoftKey(getIconSoftKeyAll(4))
                    .sizeWin(new int[]{TargetView.MINI_VEIL,TargetView.MINI_VEIL,TargetView.MINI_VEIL,TargetView.MINI_VEIL})
                    .setStep(step);
            return true;
        }else return false;
    }

    private boolean tutorialMenuAdapt(int step){
        if(chapter==null||!chapter.equals(KEY_STEP_MENU_ADAPT)) {
            excursInTutorial
                    .targets(new Integer[]{R.id.selected_collect_exit_mode, R.id.selected_collect_3,R.id.selected_collect_4})
                    .titles(getContext().getResources().getStringArray(R.array.collect_adapt_menu_title))
                    .notes(getContext().getResources().getStringArray(R.array.collect_adapt_menu_note))
                    .iconsSoftKey(getIconSoftKeyAll(3))
                    .sizeWin(new int[]{TargetView.MINI_VEIL,TargetView.MINI_VEIL,TargetView.MINI_VEIL})
                    .setStep(step);
            return true;
        }else return false;
    }

    private boolean tutorialEx(int step){
        if(chapter==null||!chapter.equals(KEY_STEP_BACK_COLL)) {
            excursInTutorial
                    .targets(new Integer[]{R.id.selected_collect_exit_mode})
                    .titles(new String[]{getString(R.string.images)})
                    .notes(new String[]{getString(R.string.collet_images)})
//                    .iconsTitle(new int[]{R.drawable.ic_matrix_reset_image})
                    .iconsSoftKey(new int[]{R.drawable.ic_icon_exit})
                    .sizeWin(new int[]{TargetView.MINI_VEIL})
                    .setStep(step);
            return true;
        }else return false;
    }

    private void startExkurs(int step) {
        if (step >= 0 && step < 2) {
            if (getContext() != null) {
                chapter = KEY_STEP_TUTORIAL;
                initExcurs();
                excursInTutorial
                        .targets(new Integer[]{R.id.gallery_add_folds,R.id.gallery_main_menu})
                        .iconsTitle(new int[]{R.drawable.ic_add,R.drawable.ic_menu})
                        .sizeWin(new int[]{TargetView.MIDI_VEIL,TargetView.MINI_VEIL})
                        .titles(getContext().getResources().getStringArray(R.array.collect_title))
                        .iconsSoftKey(getIconSoftKeyAll(2))
                        .notes(getContext().getResources().getStringArray(R.array.collect_note))
                        .setStep(step);
                excursInTutorial.start();
            }

            /*общая инструкция*/

        }
    }

    private void initExcurs(){
//        if(excursInTutorial==null){
            if(getContext()!=null) {
                TargetView t = TargetView.build(this)
                        .touchExit(TargetView.NON_TOUCH)
                        .dimmingBackground(getContext().getResources().getColor(R.color.colorDimenPrimaryDarkTransparent));
                excursInTutorial = new ExcursInTutorial(t);
//            }
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
}
