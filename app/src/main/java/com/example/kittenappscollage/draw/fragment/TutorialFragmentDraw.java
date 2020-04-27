package com.example.kittenappscollage.draw.fragment;

import android.animation.Animator;
import android.widget.ImageView;

import com.example.kittenappscollage.R;
import com.example.kittenappscollage.draw.tutorial.ExcursInTutorial;
import com.example.targetviewnote.TargetView;

import static com.example.kittenappscollage.helpers.Massages.LYTE;

public class TutorialFragmentDraw extends ApplyDrawToolsFragmentDraw implements TargetView.OnClickTargetViewNoleListener {

    private ExcursInTutorial excursInTutorial;

    private final String KEY_STEP_TUTORIAL = "TutorialFragmentDraw step tutorial";

    private final String KEY_CHAPTER_TUTORIAL = "TutorialFragmentDraw step tutorial";

    @Override
    public void onResume() {
        startExkurs(getPreferences().getInt(KEY_STEP_TUTORIAL,-1));
        super.onResume();
    }

    @Override
    public void onClickTarget(int i) {
        if(i==TargetView.TOUCH_SOFT_KEY){
            excursInTutorial.next();
            getEditor().putInt(KEY_STEP_TUTORIAL,excursInTutorial.getStep()).apply();
        }
    }

    @Override
    public void onAnimationStart(Animator animation) {
        LYTE("start");
    }

    @Override
    public void onAnimationEnd(Animator animation) {
         LYTE("end");
    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }



    public void startTutorial(){
        getEditor().putInt(KEY_STEP_TUTORIAL,0).apply();
        startExkurs(0);
    }

    private void startExkurs(int step){
        if(step>=0&&step<3){
            if(getContext()!=null){
                TargetView t = TargetView.build(this)
                        .touchExit(TargetView.NON_TOUCH)
                        .dimmingBackground(getContext().getResources().getColor(R.color.colorDimenPrimaryDarkTransparent));
                excursInTutorial =  new ExcursInTutorial(t)
                        .targets(new Integer[]{R.id.slide_add_lyr,R.id.slide_all_tools,R.id.slide_save_img})
                        .titles(getContext().getResources().getStringArray(R.array.draw_main_buttons_title))
                        .notes(getContext().getResources().getStringArray(R.array.draw_main_buttons_note))
                        .setStep(step);
                excursInTutorial.start();
            }
        }
    }

    private void chapterAddLyrs(){

    }



    @Override
    protected void slideSave(ImageView view) {
        super.slideSave(view);
    }

    @Override
    protected void slideTools() {
        super.slideTools();
    }

    @Override
    protected void slideAdd() {
        super.slideAdd();

    }
}
