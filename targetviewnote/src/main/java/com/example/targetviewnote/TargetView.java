package com.example.targetviewnote;

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

//    public static final String KEY_TARGET_FORM = "key target form";

    public static final String KEY_TARGET_FRAME = "key target frame";

    public static final String KEY_COLOR_BACKGROUND_CONTENT = "color background content";

    public static final String KEY_ACTION_EXIT = "action exit";

    public static final String KEY_TEXT_TITLE = "text title";

    public static final String KEY_ICON_TITLE = "icon title";

    public static final String KEY_SIZE_TITLE = "size title";

    public static final String KEY_COLOR_TEXT_TITLE = "color text title";

    public static final String KEY_TEXT_NOTE = "text note";

    public static final String KEY_SIZE_NOTE = "size note";

    public static final String KEY_COLOR_TEXT_NOTE = "color text note";

    public static final String KEY_ICON_SOFT_KEY = "icon soft key";

    public static final String KEY_SIZE_CONTENT_WINDOW = "size content window";

    public static final String KEY_DIMMING_BACKGROUND = "dimming background";

    public static final int NON_IKON = 0;

    public static final int TOUCH_TARGET = 10;

    public static final int TOUCH_VEIL = 11;

    public static final int TOUCH_UOT = 12;

    public static final int TOUCH_SOFT_KEY = 13;

    public static final int NON_TOUCH = 14;

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

    private boolean step;

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
        bundle.putInt(KEY_SOURCE_TARGET,SOURCE_FRAGMENT);
        veilField = new VeilField();
        readiness = false;
        show = false;
    }

    public TargetView target(int id){
        readiness = false;
        if (fragment != null) paramView(fragment.getView().findViewById(id));
        else if (activity != null) paramView(activity.findViewById(id));
        return this;
    }

    public TargetView target(View view){
        readiness = false;
        paramView(view);
        return this;
    }

    /*затемнение фона*/
    public TargetView dimmingBackground(int color){
        bundle.putInt(KEY_DIMMING_BACKGROUND,color);
        return this;
    }

    /*шрифт заметки*/
    public TargetView sizeNote(float size){
        bundle.putFloat(KEY_SIZE_NOTE,size);
        return this;
    }

    /*иконка в конце заметки*/
    public TargetView iconSoftKey(int idDrawable){
        bundle.putInt(KEY_ICON_SOFT_KEY,idDrawable);
        return this;
    }

    /*текст заметки*/
    public TargetView textNote(String text){
        bundle.putString(KEY_TEXT_NOTE,text);
        return this;
    }

    /*цвет текста заметки*/
    public TargetView colorNote(int color){
        bundle.putInt(KEY_COLOR_TEXT_NOTE,color);
        return this;
    }

    /*шрифт заголовка*/
    public TargetView sizeTitle(float size){
        bundle.putFloat(KEY_SIZE_TITLE,size);
        return this;
    }

    /*иконка заголовка*/
    public TargetView iconTitle(int idDrawable){
        bundle.putInt(KEY_ICON_TITLE,idDrawable);
        return this;
    }

    /*текст заголовка*/
    public TargetView textTitle(String text){
        bundle.putString(KEY_TEXT_TITLE,text);
        return this;
    }
    /*цвет текста заголовка*/
    public TargetView colorTitle(int color){
        bundle.putInt(KEY_COLOR_TEXT_TITLE,color);
     return this;
    }

    /*отступ от цели(рамка)*/
    public TargetView targetFrame(int frame){
        bundle.putInt(KEY_TARGET_FRAME,frame);
        return this;
    }

    /*способ выхода из диалога*/
    public TargetView touchExit(int touch){
        bundle.putInt(KEY_ACTION_EXIT,touch);
        return this;
    }

    /*величина окна*/
    public TargetView sizeContentWindow(int s){
        bundle.putInt(KEY_SIZE_CONTENT_WINDOW,s);
        return this;
    }

    /*цвет рамки окна*/
    public TargetView colorBackgroundFrame(int color){
        bundle.putInt(KEY_COLOR_BACK,color);
        return this;
    }

    /*цвет фона контента*/
    public TargetView colorBackgroundContent(int color){
        bundle.putInt(KEY_COLOR_BACKGROUND_CONTENT,color);
        return this;
    }

    public boolean isWinVis(){
        return veilField.isVisible();
    }

    public void show(){
        show = true;
        step = false;
        if(readiness) {
            veilField.setArguments(bundle);
            if(!veilField.isAdded()) {

                FragmentManager fm = null;
//                Log.d("WWWW TargetView","create fm");
                if (fragment != null) {
                    fm = fragment.getFragmentManager();
//                    Log.d("WWWW TargetView","set target");
                    veilField.setTargetFragment(fragment, 0);
                } else if (activity != null) {
                    fm = activity.getSupportFragmentManager();
                }
//                Log.d("WWWW TargetView","show");
                veilField.setCancelable(false);
                veilField.show(fm, veilField.getClass().getName());
            }
        }

    }

    public void step(){
        step = true;
        show = false;
            if (readiness) {
                    veilField.setArguments(bundle);

            }
    }

    public void reset(){
        bundle = new Bundle();
        close();
    }

    public void close(){
        if(veilField.isVisible()){
            veilField.dismiss();
        }
    }

    private void paramView(final View view){
        if(veilField.isVisible()||(view.getWidth()>0&&view.getHeight()>0)){
            addParamsTarget(view);
            if(step)step();
            else if(show)show();
        }else {
            ViewTreeObserver viewTreeObserver = view.getViewTreeObserver();
            if (viewTreeObserver.isAlive()) {
                viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        addParamsTarget(view);
                        if (show) show();
                    }
                });
            }
        }
    }

    private void addParamsTarget(View view){
        int[] l = new int[2];
        view.getLocationOnScreen(l);
        int h = l[1] + hShift();
        int w = l[0];
        final int[] t = new int[]{w, h, w + view.getWidth(), h + view.getHeight()};
        bundle.putIntArray(KEY_TARGET_VIEW, t);
        readiness = true;
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
        void onClickTarget(int i);
    }
}
