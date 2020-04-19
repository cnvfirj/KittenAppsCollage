package com.example.targetviewnote;

import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class TargetView {

    public static final String KEY_COLOR_BACK = "key color back";

    public static final String KEY_TARGET_VIEW = "key target view";

    public static final String KEY_SOURCE_TARGET = "key source target";

    public static final String KEY_TARGET_FORM = "key target form";

    public static final String KEY_TARGET_FRAME = "key target frame";



    public static final int FORM_RECT = 0;

    public static final int FORM_OVAL = 1;

    public static final int FORM_CIRC = 2;

    public static final int TOUCH_TARGET = 10;

    public static final int TOUCH_VEIL = 11;

    public static final int TOUCH_UOT = 12;

    public static final int SOURCE_ACTIVITY = 21;

    public static final int SOURCE_FRAGMENT = 22;

    public static final int MAXI_VEIL = 30;

    public static final int MIDI_VEIL = 31;

    public static final int MINI_VEIL = 32;

    private AppCompatActivity activity;

    private Fragment fragment;

    private Bundle bundle;

    private VeilField veilField;

    private boolean readiness;

    private boolean show;


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
        readiness = false;
        show = false;
    }

    private TargetView(Fragment fragment) {
        this.fragment = fragment;
        bundle = new Bundle();
        bundle.putInt(KEY_TARGET_VIEW,SOURCE_FRAGMENT);
        veilField = new VeilField();
        readiness = false;
        show = false;
    }

    public TargetView target(int id){
        bundle.putInt(KEY_TARGET_VIEW,id);
        if (fragment != null) paramView(fragment.getView().findViewById(id));
        else if (activity != null) paramView(activity.findViewById(id));
        return this;
    }

    public TargetView target(View view){
        paramView(view);
        return this;
    }

    public TargetView targetFrame(int frame){
        bundle.putInt(KEY_TARGET_FRAME,frame);
        return this;
    }

    public TargetView colorBackground(int color){
        bundle.putInt(KEY_COLOR_BACK,color);
        return this;
    }

    public TargetView targetForm(int form){
        bundle.putInt(KEY_TARGET_FORM,form);
        return this;
    }

    public void show(){
        show = true;
        if(readiness) {
            veilField.setArguments(bundle);
            FragmentManager fm = null;
            if (fragment != null) fm = fragment.getFragmentManager();
            else if (activity != null) fm = activity.getSupportFragmentManager();
            veilField.show(fm, veilField.getClass().getName());
        }
    }

        private void paramView(final View view){
        ViewTreeObserver viewTreeObserver = view.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    ViewGroup.LayoutParams params = view.getLayoutParams();
                    int[] l = new int[2];
                    view.getLocationOnScreen(l);
                    int h = l[1]+hShift();
                    int w = l[0];
                    final int[]t = new int[]{w,h,w+view.getWidth(),h+view.getHeight()};
                    bundle.putIntArray(KEY_TARGET_VIEW,t);
                    readiness = true;
                    if(show)show();
                }
            });
        }
    }

    /*сдвиг на статус бар*/
    private int hShift(){
        Rect rectangle = new Rect();
        Window window = null;
        if(fragment!=null)window = fragment.getActivity().getWindow();
        else if(activity!=null)window = activity.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(rectangle);
        int statusBarHeight = rectangle.top;
        int contentViewTop =
                window.findViewById(Window.ID_ANDROID_CONTENT).getTop();
        return contentViewTop - statusBarHeight;
    }

    public interface OnClickTargetViewNoleListener{
        void onClick(int i);
    }
}
