package com.example.kittenappscollage.draw.fragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.kittenappscollage.R;
import com.example.kittenappscollage.draw.view.ViewDraw;

import java.util.Objects;

import static com.example.kittenappscollage.helpers.Massages.MASSAGE;


/*описываем основную анимацию движения кнопок и панели
* инструментов. Присваиваем им иконки*/

public class SuperFragmentDraw extends Fragment implements View.OnClickListener {

    protected final int TOOL_PAINT = 1;
    protected final int TOOL_ERASE = 2;
    protected final int TOOL_FILL = 3;
    protected final int TOOL_CUT = 4;
    protected final int TOOL_TRANS = 5;
    protected final int TOOL_TEXT = 6;
    protected final int TOOL_DEF_ROT = 7;
    protected final int TOOL_SCALE = 8;

    protected final String SAVE_STATE = "state";

    private final String KEY_STATE_INDEX_TOOL = "tool";
    private final String KEY_STATE_INFO = "info";
    private final String KEY_STATE_ALLLYRS = "alllyrs";

    private int dIndexTool;


    protected ViewDraw dViewDraw;

    private boolean dVisibleTools, dVisibleSave, dVisibleAdd;

    private boolean dSelectInfo, dSelectAllLyrs;

    private ImageView dSlideTools,dSlideSave, dSlideAdd;

    private ImageView dSaveTel, dSaveNet;

    private ImageView dAddCreated, dAddLink, dAddCam, dAddColl;

    private ImageView dUndo, dRedo, dInfo, dAllLyrs, dUnion, dDeleteLyr, dChangeLyrs, dDeleteAll;

    private ImageView dPaint, dFil, dEraser, dText, dCut, dTrans, dScale, dDeformRotate;

    private float dSlideStep;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        dVisibleTools = false;
        dVisibleSave = false;
        dVisibleAdd = false;
        dSelectAllLyrs = false;
        dSelectInfo = false;
        dIndexTool = TOOL_PAINT;

        return inflater.inflate(R.layout.fragment_draw,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dViewDraw = view.findViewById(R.id.view_draw);
        initViews(view);

    }

//    @Override
//    public void onPause() {
//        super.onPause();
//        SharedPreferences states = getContext().getSharedPreferences(SAVE_STATE, Context.MODE_PRIVATE);
//        SharedPreferences.Editor edit = states.edit();
//        edit.putInt(KEY_STATE_INDEX_TOOL,dIndexTool);
//        edit.putBoolean(KEY_STATE_INFO,dSelectInfo);
//        edit.putBoolean(KEY_STATE_ALLLYRS,dSelectAllLyrs);
//        edit.apply();
//    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.slide_all_tools:
                slideTools();
                break;
            case R.id.slide_save_img:
                slideSave();
                break;
            case R.id.slide_add_lyr:
                slideAdd();
                break;
            case R.id.save_net:
                saveNet((ImageView)view);
                break;
            case R.id.save_tel:
                saveTel((ImageView)view);
                break;
            case R.id.add_created:
                addCreated((ImageView)view);
                break;
            case R.id.add_link:
                addLink((ImageView)view);
                break;
            case R.id.add_camera:
                addCam((ImageView)view);
                break;
            case R.id.add_collect:
                addColl((ImageView)view);
                break;
                default:
                    toolsDrive(view);
                    break;
        }
    }

    private void toolsDrive(View view){
        switch (view.getId()){
            case R.id.tool_undo:
                toolUndo((ImageView)view);
                break;
            case R.id.tool_redo:
                toolRedo((ImageView)view);
                break;
            case R.id.tool_info:
                toolInfo((ImageView)view);
                break;
            case R.id.tool_all_lyrs:
                toolAllLyrs((ImageView)view);
                break;
            case R.id.tool_change:
                toolChange((ImageView)view);
                break;
            case R.id.tool_union:
                toolUnion((ImageView)view);
                break;
            case R.id.tool_del_lyr:
                toolDelLyr((ImageView)view);
                break;
            case R.id.tool_del_all:
                toolDelAll((ImageView)view);
                break;
            case R.id.tool_draw:
                toolPaint((ImageView)view);
                break;
            case R.id.tool_fill:
                toolFill((ImageView)view);
                break;
            case R.id.tool_erase:
                toolErase((ImageView)view);
                break;
            case R.id.tool_text:
                toolText((ImageView)view);
                break;
            case R.id.tool_cut:
                toolCut((ImageView)view);
                break;
            case R.id.tool_deform_rotate:
                toolDeformRotate((ImageView)view);
                break;
            case R.id.tool_translate:
                toolTranslate((ImageView)view);
                break;
            case R.id.tool_scale:
                toolScale((ImageView)view);
                break;


        }
    }

    private void  initViews(View view){
        dViewDraw = view.findViewById(R.id.view_draw);

        dSlideTools = view.findViewById(R.id.slide_all_tools);
        dUndo = view.findViewById(R.id.tool_undo);
        dRedo = view.findViewById(R.id.tool_redo);
        dInfo = view.findViewById(R.id.tool_info);
        dAllLyrs = view.findViewById(R.id.tool_all_lyrs);
        dUnion = view.findViewById(R.id.tool_union);
        dDeleteLyr = view.findViewById(R.id.tool_del_lyr);
        dChangeLyrs = view.findViewById(R.id.tool_change);
        dDeleteAll = view.findViewById(R.id.tool_del_all);

        dPaint = view.findViewById(R.id.tool_draw);
        dFil = view.findViewById(R.id.tool_fill);
        dEraser = view.findViewById(R.id.tool_erase);
        dText = view.findViewById(R.id.tool_text);
        dCut = view.findViewById(R.id.tool_cut);
        dTrans = view.findViewById(R.id.tool_translate);
        dScale = view.findViewById(R.id.tool_scale);
        dDeformRotate = view.findViewById(R.id.tool_deform_rotate);

        shiftTools(dSlideTools);

        dSlideSave = view.findViewById(R.id.slide_save_img);
        dSaveNet = view.findViewById(R.id.save_net);
        dSaveTel = view.findViewById(R.id.save_tel);

        dSlideAdd = view.findViewById(R.id.slide_add_lyr);
        dAddCreated = view.findViewById(R.id.add_created);
        dAddLink = view.findViewById(R.id.add_link);
        dAddCam = view.findViewById(R.id.add_camera);
        dAddColl = view.findViewById(R.id.add_collect);

        appStates();
        addListener();
    }

    protected void enabledSlideTools(boolean enable){
        dSlideTools.setEnabled(enable);
    }

    protected void enabledSlideSave(boolean enable){
        dSlideSave.setEnabled(enable);
    }

    protected void enabledSlideAdd(boolean enable){
        dSlideAdd.setEnabled(enable);
    }





    /*анимация выдвижения панели инструментов для рисования*/
    protected void slideTools(){

        if(!dVisibleTools) {
            computeSlide(dSlideStep,getTimeSlide());
            dVisibleTools = true;
        }else {
            computeSlide(0,getTimeSlide());
            dVisibleTools = false;
        }
        dSlideTools.setSelected(dVisibleTools);
    }

    /*анимация выдвижения сохранений*/
    protected void slideSave(){
        if(!dVisibleSave){
            dVisibleSave = true;
            dSaveNet.animate().translationX(-getSlideSave()).setDuration(getTimeSlide()).start();
            dSaveTel.animate().translationY(getSlideSave()).setDuration(getTimeSlide()).start();
        }else {
            dVisibleSave = false;
            dSaveNet.animate().translationX(0).setDuration(getTimeSlide()).start();
            dSaveTel.animate().translationY(0).setDuration(getTimeSlide()).start();
        }
        dSlideSave.setSelected(dVisibleSave);
    }

    protected void slideAdd(){
        if(!dVisibleAdd){
            dAddCreated.animate().translationX(-getSlideAdd1()).setDuration(getTimeSlide()).start();
            dAddLink.animate().translationX(-getSlideAdd2()).setDuration(getTimeSlide()).start();
            dAddCam.animate().translationY(-getSlideAdd1()).setDuration(getTimeSlide()).start();
            dAddColl.animate().translationY(-getSlideAdd2()).setDuration(getTimeSlide()).start();
            dVisibleAdd = true;

        }else {
            dAddCreated.animate().translationX(0).setDuration(getTimeSlide()).start();
            dAddLink.animate().translationX(0).setDuration(getTimeSlide()).start();
            dAddCam.animate().translationY(0).setDuration(getTimeSlide()).start();
            dAddColl.animate().translationY(0).setDuration(getTimeSlide()).start();
            dVisibleAdd = false;
        }

        dSlideAdd.setSelected(dVisibleAdd);
    }

    private void shiftTools(final View view){
        /*здесь дожидаемся создания вью для расчета расстояний между кнопками*/
        ViewTreeObserver viewTreeObserver = view.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    Point size = new Point();
                    getActivity().getWindowManager().getDefaultDisplay().getSize(size);
                    float border = dSlideTools.getLeft();
                    int display = size.x;
                    dSlideStep = (display-(border+dSlideTools.getWidth())-border)/8;
                    if(dVisibleTools)computeSlide(dSlideStep,0);
                }
            });
        }
    }

    private void computeSlide(float step, long time){
        dUndo.animate().translationX(step).setDuration(time).start();
        dRedo.animate().translationX(step*2).setDuration(time).start();
        dInfo.animate().translationX(step*3).setDuration(time).start();
        dAllLyrs.animate().translationX(step*4).setDuration(time).start();
        dChangeLyrs.animate().translationX(step*5).setDuration(time).start();
        dUnion.animate().translationX(step*6).setDuration(time).start();
        dDeleteLyr.animate().translationX(step*7).setDuration(time).start();
        dDeleteAll.animate().translationX(step*8).setDuration(time).start();
        dPaint.animate().translationY(step).setDuration(time).start();
        dEraser.animate().translationY(step*2).setDuration(time).start();
        dFil.animate().translationY(step*3).setDuration(time).start();
        dText.animate().translationY(step*4).setDuration(time).start();
        dDeformRotate.animate().translationY(step*5).setDuration(time).start();
        dCut.animate().translationY(step*6).setDuration(time).start();
        dTrans.animate().translationY(step*7).setDuration(time).start();
        dScale.animate().translationY(step*8).setDuration(time).start();
    }


    private void appStates(){

//        SharedPreferences pref = getContext().getSharedPreferences(SAVE_STATE, Context.MODE_PRIVATE);
//        dIndexTool = pref.getInt(KEY_STATE_INDEX_TOOL,TOOL_PAINT);
//        dSelectInfo = pref.getBoolean(KEY_STATE_INFO,false);
//        dSelectAllLyrs = pref.getBoolean(KEY_STATE_ALLLYRS,false);

        selectorButtons(dIndexTool);
        dInfo.setSelected(dSelectInfo);
        dAllLyrs.setSelected(dSelectAllLyrs);
    }

    private float getSlideAdd1(){
        return getResources().getDimension(R.dimen.SLIDE_ADD);
    }

    private float getSlideAdd2(){
        return getSlideAdd1()*2;
    }

    private float getSlideSave(){
        return getResources().getDimension(R.dimen.SLIDE_SAVE);
    }

    private long getTimeSlide(){
        return 500;
    }

    private long getTymeAnimView(){
        return 300;
    }


    private void addListener(){
        dSlideTools.setOnClickListener(this);
        dSlideTools.setOnClickListener(this);
        dUndo.setOnClickListener(this);
        dRedo.setOnClickListener(this);
        dInfo.setOnClickListener(this);
        dAllLyrs.setOnClickListener(this);
        dUnion.setOnClickListener(this);
        dDeleteLyr.setOnClickListener(this);
        dChangeLyrs.setOnClickListener(this);
        dDeleteAll.setOnClickListener(this);

        dPaint.setOnClickListener(this);
        dFil.setOnClickListener(this);
        dEraser.setOnClickListener(this);
        dText.setOnClickListener(this);
        dCut.setOnClickListener(this);
        dTrans.setOnClickListener(this);
        dScale.setOnClickListener(this);
        dDeformRotate.setOnClickListener(this);

        dSlideSave.setOnClickListener(this);
        dSaveTel.setOnClickListener(this);
        dSaveNet.setOnClickListener(this);

        dSlideAdd.setOnClickListener(this);
        dAddCreated.setOnClickListener(this);
        dAddLink.setOnClickListener(this);
        dAddCam.setOnClickListener(this);
        dAddColl.setOnClickListener(this);

    }


    protected void saveNet(ImageView v){
        slideSave();
    }

    protected void saveTel(ImageView v){
        slideSave();
    }

    protected void addCreated(ImageView v){
        slideAdd();
    }

    protected void addLink(ImageView v){
        slideAdd();
    }

    protected void addCam(ImageView v){
        slideAdd();
    }

    protected void addColl(ImageView v){
        slideAdd();
    }

    protected void toolUndo(ImageView v){
        v.setSelected(!v.isSelected());
    }
    protected void toolRedo(ImageView v){
        v.setSelected(!v.isSelected());
    }
    protected void toolInfo(ImageView v){
       dSelectInfo=!dSelectInfo;
        v.setSelected(dSelectInfo);
    }
    protected void toolAllLyrs(ImageView v){
        dSelectAllLyrs = !dSelectAllLyrs;
        v.setSelected(dSelectAllLyrs);
    }
    protected void toolUnion(ImageView v){
        v.setSelected(!v.isSelected());
    }
    protected void toolChange(ImageView v){
        v.setSelected(!v.isSelected());
    }
    protected void toolDelLyr(ImageView v){
        v.setSelected(!v.isSelected());
    }
    protected void toolDelAll(ImageView v){
        v.setSelected(!v.isSelected());
    }

    protected void toolPaint(ImageView v){
        selectorButtons(TOOL_PAINT);
    }

    protected void toolErase(ImageView v){
       selectorButtons(TOOL_ERASE);
    }

    protected void toolFill(ImageView v){
       selectorButtons(TOOL_FILL);
    }

    protected void toolText(ImageView v){
       selectorButtons(TOOL_TEXT);
    }

    protected void toolCut(ImageView v){
       selectorButtons(TOOL_CUT);
    }

    protected void toolDeformRotate(ImageView v){
       selectorButtons(TOOL_DEF_ROT);
    }

    protected void toolTranslate(ImageView v){
        selectorButtons(TOOL_TRANS);

    }

    protected void toolScale(ImageView v){
          selectorButtons(TOOL_SCALE);
    }

    protected boolean compareIndex(int index){
        return dIndexTool==index;
    }

    private void selectorButtons(int index){
        dIndexTool = index;
        dPaint.setSelected(index==TOOL_PAINT);
        dEraser.setSelected(index==TOOL_ERASE);
        dFil.setSelected(index==TOOL_FILL);
        dText.setSelected(index==TOOL_TEXT);
        dCut.setSelected(index==TOOL_CUT);
        dTrans.setSelected(index==TOOL_TRANS);
        dDeformRotate.setSelected(index==TOOL_DEF_ROT);
        dScale.setSelected(index==TOOL_SCALE);
    }


}
