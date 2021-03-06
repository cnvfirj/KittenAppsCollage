package com.kittendevelop.kittenappscollage.draw.fragment;

import android.animation.Animator;
import android.widget.ImageView;

import com.kittendevelop.kittenappscollage.R;
import com.kittendevelop.kittenappscollage.draw.tutorial.ExcursInTutorial;
import com.example.targetviewnote.TargetView;

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
//        if(i==TargetView.TOUCH_SOFT_KEY||i==TargetView.TOUCH_TARGET||i==TargetView.TOUCH_VEIL){
            if(excursInTutorial.next()) {
                getEditor().putInt(chapter, excursInTutorial.getStep()).apply();
            }else {
                getEditor().putInt(chapter, 999).apply();
                chapter = null;
            }
//        }
    }

    @Override
    public void onAnimationStart(Animator animation) {
    }

    @Override
    public void onAnimationEnd(Animator animation) {
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



    public void startTutorial(){
        getEditor().putInt(KEY_STEP_TUTORIAL,0).apply();
        startExkurs(0);
    }


    public boolean isTutorial(){
        return excursInTutorial.isWinVis();
    }


    private void startExkurs(int step){
        if(step>=0&&step<999){
            if(getContext()!=null){
                chapter = KEY_STEP_TUTORIAL;
                initExcurs();
                if(!excursInTutorial.getOngoing()){
                    chapter = KEY_STEP_TUTORIAL;
                    excursInTutorial
                            .targets(new Integer[]{R.id.slide_add_lyr,R.id.slide_all_tools,R.id.slide_save_img})
                            .iconsTitle(new int[]{R.drawable.ic_matrix_reset_image,R.drawable.icon_edit,R.drawable.ic_save})
                            .sizeWin(new int[]{TargetView.MIDI_VEIL,TargetView.MINI_VEIL,TargetView.MINI_VEIL})
                            .titles(getContext().getResources().getStringArray(R.array.draw_main_buttons_title))
                            .iconsSoftKey(new int[]{R.drawable.ic_icon_next,R.drawable.ic_icon_next,R.drawable.ic_icon_next})
                            .notes(getContext().getResources().getStringArray(R.array.draw_main_buttons_note))
                            .ongoing(true)
                            .setStep(step);
                    excursInTutorial.start();
                }
            }
        }
    }


    @Override
    protected void slideSave(ImageView view) {
//            int step = getPreferences().getInt(KEY_TUTORIAL_SAVE_IMG, 0);
//            /*отключаем*/
        int step = 999;
            if (step < 999) {
                initExcurs();
                if (!excursInTutorial.getOngoing()) {
                    if (tutorialSave(step)) {
                        chapter = KEY_TUTORIAL_SAVE_IMG;
                        super.slideSave(view);
                    }
                }
            } else super.slideSave(view);
    }

    @Override
    protected void slideTools() {
//        int step = getPreferences().getInt(KEY_TUTORIAL_REDACT_IMG,0);
        int step = 999;
        if(step<999){
            initExcurs();
            if(!excursInTutorial.getOngoing()){
                if(tutorialTools(step)){
                    chapter = KEY_TUTORIAL_REDACT_IMG;
                    super.slideTools();
                }
            }
        }else super.slideTools();
    }

    @Override
    protected void slideAdd() {

//        int step = getPreferences().getInt(KEY_TUTORIAL_ADD_LYR,0);
        int step = 999;
        if(step<999){
            initExcurs();
            if(!excursInTutorial.getOngoing()){
                if(tutorialAdd(step)) {
                    chapter = KEY_TUTORIAL_ADD_LYR;
                    super.slideAdd();
                }
            }
        }else super.slideAdd();
    }

    private boolean tutorialAdd(int step){
        if(chapter==null||!chapter.equals(KEY_TUTORIAL_ADD_LYR)) {
            excursInTutorial
                    .targets(new Integer[]{R.id.add_camera,R.id.add_collect,R.id.add_created})
                    .titles(getContext().getResources().getStringArray(R.array.draw_add_title))
                    .notes(getContext().getResources().getStringArray(R.array.draw_add_note))
                    .sizeWin(new int[]{TargetView.MIDI_VEIL,TargetView.MINI_VEIL,TargetView.MINI_VEIL})
                    .iconsTitle(new int[]{R.drawable.icon_camera,R.drawable.icon_collect,R.drawable.icon_create_new})
                    .iconsSoftKey(new int[]{R.drawable.ic_icon_next,R.drawable.ic_icon_next,R.drawable.ic_icon_exit})
                    .setStep(step)
                    .ongoing(true);
            return true;
        }else return false;
    }

    private boolean tutorialTools(int step){
        if(chapter==null||!chapter.equals(KEY_TUTORIAL_REDACT_IMG)) {
            excursInTutorial
                    .targets(getTargetToolsAll())
                    .titles(getContext().getResources().getStringArray(R.array.draw_tools_title))
                    .notes(getContext().getResources().getStringArray(R.array.draw_tools_note))
                    .iconsSoftKey(getIconSoftKeyAll(getTargetToolsAll().length))
                    .iconsTitle(getIconsTitleToolsAll())
                    .sizeWin(getWinToolsAll())
                    .setStep(step)
                    .ongoing(true);
            return true;
        }else return false;
    }

    private boolean tutorialSave(int step){
        if(chapter==null||!chapter.equals(KEY_TUTORIAL_SAVE_IMG)) {
            excursInTutorial
                    .targets(new Integer[]{R.id.save_net,R.id.save_is,R.id.save_tel})
                    .titles(getContext().getResources().getStringArray(R.array.draw_save_title))
                    .notes(getContext().getResources().getStringArray(R.array.draw_save_note))
                    .sizeWin(new int[]{TargetView.MINI_VEIL,TargetView.MIDI_VEIL,TargetView.MINI_VEIL})
                    .iconsTitle(new int[]{R.drawable.ic_share,R.drawable.ic_save_as,R.drawable.ic_save})
                    .iconsSoftKey(new int[]{R.drawable.ic_icon_next,R.drawable.ic_icon_next,R.drawable.ic_icon_exit})
                    .setStep(step)
                    .ongoing(true);
            return true;
        }else return false;
    }

    private Integer[]getTargetToolsAll(){
        return new Integer[]{R.id.tool_properties,R.id.tool_scale,R.id.tool_translate,R.id.tool_cut,R.id.tool_deform_rotate,
        R.id.tool_text,R.id.tool_fill,R.id.tool_erase,R.id.tool_draw,R.id.tool_color,
        R.id.tool_undo,R.id.tool_redo,R.id.tool_info,R.id.tool_del_all,
                R.id.tool_all_lyrs,R.id.tool_change,R.id.tool_union,R.id.tool_del_lyr};
    }

    private int[]getIconSoftKeyAll(int size){
            int[]ic = new int[size];
            for (int i=0;i<ic.length;i++){
                ic[i]=R.drawable.ic_icon_next;
                if(i==ic.length-1)ic[i]=R.drawable.ic_icon_exit;
            }
            return ic;
    }

    private int[]getIconsTitleToolsAll(){
        return new int[]{R.drawable.icon_settings,R.drawable.ic_scale_all, R.drawable.ic_all_arrows,R.drawable.ic_cut,R.drawable.ic_matrix_reset_image,
        R.drawable.ic_text,R.drawable.ic_fill_to_color,R.drawable.ic_erase_step_1,R.drawable.ic_brush_step_1,R.drawable.ic_get_color,
        R.drawable.icon_tool_undo,R.drawable.icon_tool_redo,R.drawable.ic_info_on,R.drawable.ic_delete,
        R.drawable.ic_all_lyrs_on,R.drawable.icon_change_lyrs,R.drawable.icon_union_lyrs,R.drawable.icon_delete_lyr};
    }

    private int[]getWinToolsAll(){
        return new int[]{TargetView.MIDI_VEIL,TargetView.MIDI_VEIL,TargetView.MIDI_VEIL,TargetView.MIDI_VEIL,TargetView.MINI_VEIL,TargetView.MINI_VEIL,
                TargetView.MINI_VEIL,TargetView.MINI_VEIL,TargetView.MINI_VEIL,TargetView.MINI_VEIL,TargetView.MINI_VEIL,TargetView.MINI_VEIL,
                TargetView.MINI_VEIL,TargetView.MINI_VEIL,TargetView.MINI_VEIL,TargetView.MINI_VEIL,TargetView.MINI_VEIL,TargetView.MINI_VEIL};
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
