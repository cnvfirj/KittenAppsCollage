package com.example.kittenappscollage.collect.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.targetviewnote.TargetView;

import static com.example.kittenappscollage.helpers.Massages.LYTE;

public class TutorialFragmentGallery extends FragmentGalleryAddFolder implements TargetView.OnClickTargetViewNoleListener {

    public final static String KEY_ACTIVATE_COLLECT = "activate fragment";

    private final String KEY_STEP_TUTORIAL = "TutorialFragmentGallery step tutorial";

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

    }

    public void startTutorial(){
        editor.putInt(KEY_STEP_TUTORIAL,0).apply();
    }

    public void activateExcurs(){
        startExkurs(preferences.getInt(KEY_STEP_TUTORIAL,-1));
    }

    @Override
    public void onResume() {
        super.onResume();
        if(getArguments()!=null) {
            activeFragment = getArguments().getBoolean(KEY_ACTIVATE_COLLECT, false);
            if(activeFragment)startExkurs(preferences.getInt(KEY_STEP_TUTORIAL,-1));
        }
    }

    @Override
    public void setArguments(@Nullable Bundle args) {
        super.setArguments(args);
        activeFragment = args.getBoolean(KEY_ACTIVATE_COLLECT,false);
    }

    public void setArguments(boolean b){
        args.putBoolean(KEY_ACTIVATE_COLLECT,b);
        setArguments(args);
    }

    private void startExkurs(int step){
        LYTE("start collect");
        if(step>=0&&step<2) {
          /*общая инструкция*/
        }
    }
}
