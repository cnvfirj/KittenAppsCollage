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

    private final String KEY_TUTORIAL_ADD_LYR = "TutorialFragmentDraw tutorial add lyr";

    private final String KEY_TUTORIAL_SAVE_IMG = "TutorialFragmentDraw tutorial save img";

    private final String KEY_TUTORIAL_REDACT_IMG = "TutorialFragmentDraw tutorial redact img";

    private String chapter;


    @Override
    public void onResume() {
        startExkurs(getPreferences().getInt(KEY_STEP_TUTORIAL,-1));
        super.onResume();
    }

    @Override
    public void onClickTarget(int i) {
        if(i==TargetView.TOUCH_SOFT_KEY){

            if(excursInTutorial.next()) {
                getEditor().putInt(chapter, excursInTutorial.getStep()).apply();
            }else {
                LYTE(chapter+ " cansel");
                getEditor().putInt(chapter, 999).apply();
                chapter = null;
            }

        }
    }

    @Override
    public void onAnimationStart(Animator animation) {
    }

    @Override
    public void onAnimationEnd(Animator animation) {
           if(chapter!=null)excursInTutorial.start();
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
                chapter = KEY_STEP_TUTORIAL;
                TargetView t = TargetView.build(this)
                        .touchExit(TargetView.NON_TOUCH)
                        .dimmingBackground(getContext().getResources().getColor(R.color.colorDimenPrimaryDarkTransparent));
                excursInTutorial =  new ExcursInTutorial(t)
                        .targets(new Integer[]{R.id.slide_add_lyr,R.id.slide_all_tools,R.id.slide_save_img})
                        .titles(getContext().getResources().getStringArray(R.array.draw_main_buttons_title))
                        .notes(getContext().getResources().getStringArray(R.array.draw_main_buttons_note))
                        .setStep(step)
                        .ongoing(true);

                excursInTutorial.start();
            }
        }
    }


    @Override
    protected void slideSave(ImageView view) {
        int step = getPreferences().getInt(KEY_TUTORIAL_SAVE_IMG,0);
        if(step<999){
            initExcurs();
            if(!excursInTutorial.getOngoing()){
                tutorialSave(step);
                super.slideSave(view);
            }LYTE("block save");
        }else super.slideSave(view);
    }

    @Override
    protected void slideTools() {
        super.slideTools();
    }

    @Override
    protected void slideAdd() {
        int step = getPreferences().getInt(KEY_TUTORIAL_ADD_LYR,0);
        if(step<999){
            initExcurs();
            if(!excursInTutorial.getOngoing()){
                tutorialAdd(step);
                super.slideAdd();
            }else LYTE("block add");
        }else super.slideAdd();
    }

    private void tutorialAdd(int step){
        chapter = KEY_TUTORIAL_ADD_LYR;
        excursInTutorial.targets(getTargetsAdd())
                        .titles(getContext().getResources().getStringArray(R.array.draw_add_title))
                        .notes(getContext().getResources().getStringArray(R.array.draw_add_note))
                        .setStep(step)
                        .ongoing(true);


    }

    private void tutorialTools(int step,int index){

    }

    private void tutorialSave(int step){
        chapter = KEY_TUTORIAL_SAVE_IMG;
        excursInTutorial.targets(getTargetSave())
                        .titles(getContext().getResources().getStringArray(R.array.draw_add_title))
                        .notes(getContext().getResources().getStringArray(R.array.draw_add_note))
                        .setStep(step)
                        .ongoing(true);
    }

    private Integer[] getTargetsAdd(){
        return new Integer[]{R.id.add_camera,R.id.add_collect,R.id.add_created};
    }

    private Integer[]getTargetSave(){
        return new Integer[]{R.id.save_net,R.id.save_is,R.id.save_tel};
    }

    private Integer[]getTargetTools(){
        return null;
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
