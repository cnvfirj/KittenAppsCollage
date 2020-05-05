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

    private final String KEY_STEP_TUTORIAL = "TutorialFragmentGallery step tutorial_122q";

    private final String KEY_STEP_BACK_COLL = "TutorialFragmentGallery step tback coll";

    private final String KEY_STEP_MENU_ROOT = "TutorialFragmentGallery step menu root";

    private final String KEY_STEP_MENU_ADAPT = "TutorialFragmentGallery step menu adapt";

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
    public void onClickTarget(int i) {
        if(i==TargetView.TOUCH_SOFT_KEY||i==TargetView.TOUCH_TARGET){
            if(excursInTutorial.next()) {
                editor.putInt(chapter, excursInTutorial.getStep()).apply();
            }else {
                editor.putInt(chapter, 999).apply();
                chapter = null;
            }
        }
    }

    public void startTutorial() {
        chapter = KEY_STEP_TUTORIAL;
        editor.putInt(chapter, 0).apply();
    }

    public void activateExcurs() {
        startExkurs(preferences.getInt(KEY_STEP_TUTORIAL, -1));
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
    public void onAnimationStart(Animator animation) {

    }

    @Override
    public void onAnimationEnd(Animator animation) {
      /*listen en animation */
        if(chapter!=null){
            excursInTutorial.start();
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

        }else {

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

    private boolean tutorialEx(int step){
        if(chapter==null||!chapter.equals(KEY_STEP_BACK_COLL)) {
            excursInTutorial
                    .targets(new Integer[]{R.id.selected_collect_exit_mode})
                    .titles(new String[]{"Изображения"})
                    .notes(new String[]{"Просматривай изображения, длительное нажатие на одно из них, вызывает дополнительные функции. Этой кнопкой вернись назад."})
                    .sizeWin(new int[]{TargetView.MINI_VEIL})
                    .setStep(step)
                    .ongoing(true);
            return true;
        }else return false;
    }

    private void startExkurs(int step) {
        if (step >= 0 && step < 2) {
            if (getContext() != null) {
                initExcurs();
                excursInTutorial
                        .targets(new Integer[]{R.id.gallery_add_folds,R.id.gallery_main_menu})
                        .iconsTitle(new int[]{R.drawable.ic_add,R.drawable.ic_menu})
                        .sizeWin(new int[]{TargetView.MIDI_VEIL,TargetView.MINI_VEIL})
                        .titles(getContext().getResources().getStringArray(R.array.collect_title))
                        .iconsSoftKey(new int[]{R.drawable.ic_icon_next,R.drawable.ic_icon_next})
                        .notes(getContext().getResources().getStringArray(R.array.collect_note))
                        .setStep(step);
                excursInTutorial.start();
            }

            /*общая инструкция*/

        }
    }

    private void initExcurs(){
        if(excursInTutorial==null){
            if(getContext()!=null) {
                TargetView t = TargetView.build(this)
                        .touchExit(TargetView.NON_TOUCH)
                        .dimmingBackground(getContext().getResources().getColor(R.color.colorDimenPrimaryDarkTransparent));
                excursInTutorial = new ExcursInTutorial(t);
            }
        }
    }
}
