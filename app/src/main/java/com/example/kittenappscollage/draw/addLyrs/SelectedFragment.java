package com.example.kittenappscollage.draw.addLyrs;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.ViewTreeObserver;

import androidx.fragment.app.Fragment;

import com.example.kittenappscollage.R;
import com.example.kittenappscollage.draw.tutorial.ExcursInTutorial;
import com.example.targetviewnote.TargetView;

public abstract class SelectedFragment extends Fragment implements View.OnClickListener, TargetView.OnClickTargetViewNoleListener {

    protected SelectorFrameFragments selector;

    private SharedPreferences preferences;

    protected ExcursInTutorial excurs;

    private String chapter;


    protected void paramView(final View view){
        ViewTreeObserver viewTreeObserver = view.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    readinessView(view);

                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        excurs(preferences.getInt(chapter,0));
    }

    @Override
    public void onClickTarget(int i) {
        if(i==TargetView.TOUCH_SOFT_KEY||i==TargetView.TOUCH_TARGET){
            if(excurs.next()) {
                getEditor().putInt(chapter, excurs.getStep()).apply();
            }else {
                getEditor().putInt(chapter, 999).apply();
            }
        }
    }

    protected void excurs(int step){
        if(step<999){
            initExcurs();
            excurs
                    .targets(targetsEx())
                    .titles(getTitles())
                    .notes(getNotes())
                    .sizeWin(sizesWin())
                    .iconsTitle(icTitles())
                    .iconsSoftKey(icSoftKey())
                    .setStep(step);
            excurs.start();
        }
    }

    private void initExcurs(){
        if(getContext()!=null) {
            TargetView t = TargetView.build(this)
                    .touchExit(TargetView.NON_TOUCH)
                    .dimmingBackground(getContext().getResources().getColor(R.color.colorDimenPrimaryDarkTransparent));
            excurs = new ExcursInTutorial(t);
        }
    }



    protected SharedPreferences getPreferences(){
        return preferences;
    }

    protected SharedPreferences.Editor getEditor(){
        return preferences.edit();
    }

    protected void setChapter(String c){
        chapter = c;
    }

    protected abstract Integer[]targetsEx();

    protected abstract int[]sizesWin();

    protected abstract String[]getTitles();

    protected abstract String[]getNotes();

    protected abstract int[]icTitles();

    protected abstract int[]icSoftKey();

    protected abstract void readinessView(View v);



}
