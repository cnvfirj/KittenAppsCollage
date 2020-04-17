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

    private AppCompatActivity activity;

    private Fragment fragment;

    private Bundle bundle;


    public static TargetView build(AppCompatActivity activity){
        return new TargetView(activity);
    }

    public static TargetView build(Fragment fragment){
        return new TargetView(fragment);
    }

    private TargetView(AppCompatActivity activity) {
        this.activity = activity;
        bundle = new Bundle();
    }

    private TargetView(Fragment fragment) {
        this.fragment = fragment;
        bundle = new Bundle();
    }

    public TargetView target(int id){
        paramView(fragment.getView().findViewById(id));
        return this;
    }

    public TargetView target(View view){
        paramView(view);
        return this;
    }

    public TargetView colorBackground(int color){
        bundle.putInt(KEY_COLOR_BACK,color);
        return this;
    }

    public void show(){
        VeilField d = new VeilField();
        d.setArguments(bundle);
        FragmentManager fm = null;
        if(fragment!=null) fm  = fragment.getChildFragmentManager();
        else if(activity!=null)fm = activity.getSupportFragmentManager();
        d.show(fm,d.getClass().getName());
    }

    protected void paramView(final View view){
        ViewTreeObserver viewTreeObserver = view.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    Log.d("TAGTAG ", ""+view.getWidth());
                }
            });
        }
    }
}
