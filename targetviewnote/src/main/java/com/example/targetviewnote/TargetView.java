package com.example.targetviewnote;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class TargetView {

    public static final String KEY_COLOR_BACK = "key color back";

    public static final String KEY_TARGET_VIEW = "key target view";

    public static final String KEY_SOURCE_TARGET = "key source tsrget";

    public static final int FORM_RECT = 0;

    public static final int FORM_OVAL = 1;

    public static final int FORM_CIRC = 2;

    public static final int TOUCH_TARGET = 10;

    public static final int TOUCH_VEIL = 11;

    public static final int TOUCH_UOT = 12;

    public static final int SOURCE_ACTIVITY = 21;

    public static final int SOURCE_FRAGMENT = 22;

    private AppCompatActivity activity;

    private Fragment fragment;

    private Bundle bundle;

    private VeilField veilField;


    public static TargetView build(AppCompatActivity activity){
        return new TargetView(activity);
    }

    public static TargetView build(Fragment fragment){
        return new TargetView(fragment);
    }

    private TargetView(AppCompatActivity activity) {
        this.activity = activity;
        bundle = new Bundle();
        bundle.putInt(KEY_SOURCE_TARGET,SOURCE_ACTIVITY);
        veilField = new VeilField();
    }

    private TargetView(Fragment fragment) {
        this.fragment = fragment;
        bundle = new Bundle();
        bundle.putInt(KEY_TARGET_VIEW,SOURCE_FRAGMENT);
        veilField = new VeilField();
        veilField.setTargetFragment(fragment,0);
    }

    public TargetView target(int id){
//        if(fragment!=null)paramView(fragment.getView().findViewById(id));
//        else if(activity!=null)paramView(activity.findViewById(id));
        bundle.putInt(KEY_TARGET_VIEW,id);
        return this;
    }

    public TargetView target(View view){
        bundle.putInt(KEY_TARGET_VIEW, view.getId());
        return this;
    }

    public TargetView colorBackground(int color){
        bundle.putInt(KEY_COLOR_BACK,color);
        return this;
    }

    public void show(){
        veilField.setArguments(bundle);
        FragmentManager fm = null;
        if(fragment!=null) fm  = fragment.getFragmentManager();
        else if(activity!=null)fm = activity.getSupportFragmentManager();
        veilField.show(fm,veilField.getClass().getName());
    }


    public interface OnClickTargetViewNoleListener{
        void onClick(int i);
    }
}
