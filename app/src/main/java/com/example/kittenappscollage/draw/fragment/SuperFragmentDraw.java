package com.example.kittenappscollage.draw.fragment;


import android.animation.Animator;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.kittenappscollage.R;
import com.example.kittenappscollage.draw.ViewDraw;
import com.example.kittenappscollage.draw.saveSteps.BackNextStep;
import com.example.kittenappscollage.draw.tutorial.ExcursInTutorial;

/*описываем основную анимацию движения кнопок и панели
* инструментов. Присваиваем им иконки*/

public abstract class SuperFragmentDraw extends Fragment implements View.OnClickListener, Animator.AnimatorListener {

    private SharedPreferences dPreferences;

    private SharedPreferences.Editor dEditor;


    private final String KEY_INDEX_TOOL = "key index tool";
    private final String KEY_UNDER_DRAW = "key under draw";
    private final String KEY_UNDER_FILL = "key under fill";
    private final String KEY_UNDER_ELAST = "key under elast";
    private final String KEY_UNDER_SCALAR = "key under scalar";
    private final String KEY_UNDER_DEFROT = "key under defrot";

    protected final int TOOL_PAINT = 1;
    public final static int OP_PAINT_1 = 11;
    public final static int OP_PAINT_2 = 12;
    public final static int OP_PAINT_3 = 13;
    public final static int OP_PAINT_4 = 14;
    protected int dIndexPaint;

    protected final int TOOL_ERASE = 2;
    public final static int OP_ERASE_1 = 21;
    public final static int OP_ERASE_2 = 22;
    public final static int OP_ERASE_3 = 23;
    protected int dIndexErase;

    protected final int TOOL_FILL = 3;
    public final static int OP_FILL_1 = 31;
    public final static int OP_FILL_2 = 32;
    protected int dIndexFill;

    protected final int TOOL_DEF_ROT = 4;
    public final static int OP_DEF_ROT_1 = 41;
    public final static int OP_DEF_ROT_2 = 42;
    public final static int OP_DEF_ROT_3 = 43;
    protected int dIndexDefRot;

    protected final int TOOL_SCALE = 5;
    public final static int OP_SCALE_1 = 51;
    public final static int OP_SCALE_2 = 52;
    public final static int OP_SCALE_3 = 53;
        protected int dIndexScale;

    protected final int TOOL_TRANS = 6;
//    public final static int OP_TRANS_1 = 61;
    protected final int TOOL_CUT = 7;
//    public final static int OP_CUT_1 = 71;
//    public final static int OP_CUT_2 = 72;

    protected final int TOOL_TEXT = 8;
//    public final static int OP_TEXT_1 = 81;
//    public final static int OP_TEXT_2 = 82;

    protected ViewDraw dViewDraw;

    private boolean dVisibleTools, dVisibleSave, dVisibleAdd;

    private boolean dSelectInfo, dSelectAllLyrs;

    private ImageView dSlideTools,dSlideSave, dSlideAdd;

    private ImageView dSaveTel, dSaveNet, dSaveIs;

    private ImageView dAddCreated, dAddCam, dAddColl;

    protected ImageView dUndo, dRedo, dInfo, dAllLyrs, dUnion, dDeleteLyr, dChangeLyrs, dDeleteAll;

    private ImageView dPaint, dFil, dEraser, dText, dCut, dTrans, dScale, dDeformRotate,dProperties, dGetColor;

    private float dSlideStep;

    public SuperFragmentDraw() {
        dVisibleTools = false;
        dVisibleSave = false;
        dVisibleAdd = false;
        dSelectAllLyrs = false;
        dSelectInfo = false;
        dIndexPaint = OP_PAINT_1;
        dIndexErase = OP_ERASE_1;
        dIndexFill = OP_FILL_1;
        dIndexDefRot = OP_DEF_ROT_1;
        dIndexScale = OP_SCALE_1;
    }



    @SuppressLint("CommitPrefEdits")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        dPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        dEditor = dPreferences.edit();
        return inflater.inflate(R.layout.fragment_draw,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dViewDraw = view.findViewById(R.id.view_draw);
        initViews(view);
        BackNextStep.get().setOnBackNextStepsListen(new BackNextStep.OnBackNextStepListen() {
            @Override
            public void back(boolean enable, int size) {
                dUndo.setEnabled(enable);
            }

            @Override
            public void next(boolean enable, int size) {
                dRedo.setEnabled(enable);
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        dIndexErase = getPreferences().getInt(KEY_UNDER_ELAST,OP_ERASE_1);
        dIndexFill = getPreferences().getInt(KEY_UNDER_FILL,OP_FILL_1);
        dIndexPaint = getPreferences().getInt(KEY_UNDER_DRAW,OP_PAINT_1);
        dIndexDefRot = getPreferences().getInt(KEY_UNDER_DEFROT,OP_DEF_ROT_1);
        dIndexScale = getPreferences().getInt(KEY_UNDER_SCALAR,OP_SCALE_1);
        int index = getPreferences().getInt(KEY_INDEX_TOOL,TOOL_TRANS);
        selectorIconsDefRot(dDeformRotate);
        dDeformRotate.setSelected(true);
        selectorIconsErase(dEraser);
        dEraser.setSelected(true);
        selectorIconsFill(dFil);
        dFil.setSelected(true);
        selectorIconsPaint(dPaint);
        dPaint.setSelected(true);
        selectorIconsScale(dScale);
        dScale.setSelected(true);
        selectorButtons(index);
    }



    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.slide_all_tools:
                slideTools();
                break;
            case R.id.slide_save_img:
                slideSave((ImageView)view);
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
            case R.id.save_is:
                saveIs((ImageView)view);
                break;
            case R.id.add_created:
                addCreated((ImageView)view);
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
            case R.id.tool_properties:
                toolProperties((ImageView)view);
                break;
            case R.id.tool_color:
                toolColor((ImageView)view);
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
        dProperties = view.findViewById(R.id.tool_properties);
        dGetColor = view.findViewById(R.id.tool_color);
        shiftTools(dSlideTools);

        dSlideSave = view.findViewById(R.id.slide_save_img);
//        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.Q)dSlideSave.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_save,null));
        dSaveNet = view.findViewById(R.id.save_net);
        dSaveTel = view.findViewById(R.id.save_tel);
        dSaveIs = view.findViewById(R.id.save_is);
        dSlideAdd = view.findViewById(R.id.slide_add_lyr);
        dAddCreated = view.findViewById(R.id.add_created);
//        dAddLink = view.findViewById(R.id.add_link);
        dAddCam = view.findViewById(R.id.add_camera);
        dAddColl = view.findViewById(R.id.add_collect);
        addListener();
    }

    protected SharedPreferences getPreferences(){
        return dPreferences;
    }

    protected SharedPreferences.Editor getEditor(){
        return dEditor;
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
    protected void slideSave(ImageView view){
        if(!dVisibleSave){
            dVisibleSave = true;
            dSaveNet.animate().translationX(-getSlideSave()).setDuration(getTimeSlide()).setListener(this).start();
            dSaveTel.animate().translationY(getSlideSave()).setDuration(getTimeSlide()).start();
            dSaveIs.animate().translationX(-getSlideSave()).translationY(getSlideSave()).setDuration(getTimeSlide()).start();
        }else {
            dVisibleSave = false;
            dSaveNet.animate().translationX(0).setDuration(getTimeSlide()).start();
            dSaveTel.animate().translationY(0).setDuration(getTimeSlide()).start();
            dSaveIs.animate().translationX(0).translationY(0).setDuration(getTimeSlide()).start();
        }
        dSlideSave.setSelected(dVisibleSave);
    }

    protected void slideAdd(){
        if(!dVisibleAdd){
            dAddCreated.animate().translationX(-getSlideAdd1()).setDuration(getTimeSlide()).setListener(this).start();
            dAddCam.animate().translationY(-getSlideAdd1()).setDuration(getTimeSlide()).start();
            dAddColl.animate().translationY(-getSlideAdd1()).translationX(-getSlideAdd1()).setDuration(getTimeSlide()).start();
            dVisibleAdd = true;

        }else {
            dAddCreated.animate().translationX(0).setDuration(getTimeSlide()).start();
            dAddCam.animate().translationY(0).setDuration(getTimeSlide()).start();
            dAddColl.animate().translationY(0).translationX(0).setDuration(getTimeSlide()).start();
            dVisibleAdd = false;
        }

        dSlideAdd.setSelected(dVisibleAdd);
    }

    protected boolean isdVisibleTools() {
        return dVisibleTools;
    }

    protected boolean isdVisibleSave() {
        return dVisibleSave;
    }

    protected boolean isdVisibleAdd() {
        return dVisibleAdd;
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
        dProperties.animate().translationY(step*9).setDuration(time).setListener(this).start();
        dGetColor.animate().translationY(step).translationX(step).setDuration(time).start();
        if(step==0)toolColor(null);
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
        dProperties.setOnClickListener(this);

        dPaint.setOnClickListener(this);
        dFil.setOnClickListener(this);
        dEraser.setOnClickListener(this);
        dText.setOnClickListener(this);
        dCut.setOnClickListener(this);
        dTrans.setOnClickListener(this);
        dScale.setOnClickListener(this);
        dDeformRotate.setOnClickListener(this);
        dGetColor.setOnClickListener(this);

        dSlideSave.setOnClickListener(this);
        dSaveTel.setOnClickListener(this);
        dSaveNet.setOnClickListener(this);
        dSaveIs.setOnClickListener(this);
        dSlideAdd.setOnClickListener(this);
        dAddCreated.setOnClickListener(this);
//        dAddLink.setOnClickListener(this);
        dAddCam.setOnClickListener(this);
        dAddColl.setOnClickListener(this);
//         addLongClick();
    }


    protected void saveNet(ImageView v){
        slideSave(null);
    }

    protected void saveIs(ImageView v){
        slideSave(null);
    }
    protected void saveTel(ImageView v){
        slideSave(null);
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
       dSelectInfo=!v.isActivated();
        infoActive();
    }
    protected void toolAllLyrs(ImageView v){
        dSelectAllLyrs = !v.isActivated();
        groupActive();
    }

    protected boolean isdSelectInfo() {
        return dSelectInfo;
    }

    protected void setdSelectInfo(boolean select) {
        this.dSelectInfo = select;
        infoActive();
    }

    protected boolean isdSelectAllLyrs() {
        return dSelectAllLyrs;
    }

    protected void setdSelectAllLyrs(boolean select) {
        this.dSelectAllLyrs = select;
        groupActive();
    }
    private void infoActive(){
        dInfo.setActivated(dSelectInfo);
    }

    private void groupActive(){
        dAllLyrs.setActivated(dSelectAllLyrs);
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
        if(v.isActivated()){
            dIndexPaint++;
            if(dIndexPaint>OP_PAINT_4)dIndexPaint=OP_PAINT_1;
            v.setSelected(false);
            selectorIconsPaint(v);
            v.setSelected(true);
        }else selectorButtons(TOOL_PAINT);


        /*дальше передаем индекс*/
    }

    protected void toolErase(ImageView v){
        if(v.isActivated()){
            dIndexErase++;
            if(dIndexErase>OP_ERASE_3)dIndexErase=OP_ERASE_1;
            v.setSelected(false);
            selectorIconsErase(v);
            v.setSelected(true);
        }else selectorButtons(TOOL_ERASE);
    }

    protected void toolFill(ImageView v){
        if(v.isActivated()){
            dIndexFill++;
            if(dIndexFill>OP_FILL_2)dIndexFill = OP_FILL_1;
            v.setSelected(false);
            selectorIconsFill(v);
            v.setSelected(true);
        }else selectorButtons(TOOL_FILL);
    }

    protected void toolText(ImageView v){

        if(v.isActivated()){
            v.setSelected(false);
            enterText();
            v.setSelected(true);
        }else selectorButtons(TOOL_TEXT);
    }

    protected void toolCut(ImageView v){
        if(v.isActivated()){
            v.setSelected(false);
            doneCut();
            v.setSelected(true);
        }else selectorButtons(TOOL_CUT);
    }

    protected void toolDeformRotate(ImageView v){

        if(v.isActivated()){
            dIndexDefRot++;
            if(dIndexDefRot>OP_DEF_ROT_3)dIndexDefRot=OP_DEF_ROT_1;
            v.setSelected(false);
            selectorIconsDefRot(v);
            v.setSelected(true);
        }else selectorButtons(TOOL_DEF_ROT);
    }

    protected void toolTranslate(ImageView v){
        if(v.isActivated()){
            v.setSelected(false);
            v.setSelected(true);
        }else selectorButtons(TOOL_TRANS);

    }

    protected void toolScale(ImageView v){
        if(v.isActivated()){
            dIndexScale++;
            if(dIndexScale>OP_SCALE_3)dIndexScale=OP_SCALE_1;
            v.setSelected(false);
            selectorIconsScale(v);
            v.setSelected(true);
        }else selectorButtons(TOOL_SCALE);


    }

    protected void toolProperties(ImageView v){
        v.setSelected(!v.isSelected());
    }

    protected void toolColor(ImageView v){
        if(v!=null) v.setActivated(!v.isActivated());
        else dGetColor.setActivated(false);
    }

    protected void selectorButtons(int index){
        dPaint.setActivated(index == TOOL_PAINT);
        dEraser.setActivated(index == TOOL_ERASE);
        dFil.setActivated(index == TOOL_FILL);
        dText.setActivated(index == TOOL_TEXT);
        dCut.setActivated(index == TOOL_CUT);
        dTrans.setActivated(index == TOOL_TRANS);
        dDeformRotate.setActivated(index == TOOL_DEF_ROT);
        dScale.setActivated(index == TOOL_SCALE);

        dEditor.putInt(KEY_INDEX_TOOL,index);
    }

    protected void selectorIconsPaint(ImageView v){
        if(dIndexPaint==OP_PAINT_1)v.setImageDrawable(getContext().getResources().getDrawable(R.drawable.icon_paint_4_to_1,null));
        else if(dIndexPaint==OP_PAINT_2)v.setImageDrawable(getContext().getResources().getDrawable(R.drawable.icon_paint_1_to_2,null));
        else if(dIndexPaint==OP_PAINT_3)v.setImageDrawable(getContext().getResources().getDrawable(R.drawable.icon_paint_2_to_3,null));
        else if(dIndexPaint==OP_PAINT_4)v.setImageDrawable(getContext().getResources().getDrawable(R.drawable.icon_paint_3_to_4,null));
        dEditor.putInt(KEY_UNDER_DRAW,dIndexPaint);
    }

    protected void selectorIconsErase(ImageView v){
        if(dIndexErase==OP_ERASE_1)v.setImageDrawable(getContext().getResources().getDrawable(R.drawable.icon_erase_3_to_1,null));
        else if(dIndexErase==OP_ERASE_2)v.setImageDrawable(getContext().getResources().getDrawable(R.drawable.icon_erase_1_to_2,null));
        else if(dIndexErase==OP_ERASE_3)v.setImageDrawable(getContext().getResources().getDrawable(R.drawable.icon_erase_2_to_3,null));
        dEditor.putInt(KEY_UNDER_ELAST,dIndexErase);
    }



    protected void selectorIconsScale(ImageView v){
        if(dIndexScale==OP_SCALE_1)v.setImageDrawable(getContext().getResources().getDrawable(R.drawable.icon_scale_1,null));
        else if(dIndexScale==OP_SCALE_2)v.setImageDrawable(getContext().getResources().getDrawable(R.drawable.icon_scale_2,null));
        else if(dIndexScale==OP_SCALE_3)v.setImageDrawable(getContext().getResources().getDrawable(R.drawable.icon_scale_3,null));

        dEditor.putInt(KEY_UNDER_SCALAR,dIndexScale);
    }

    protected void selectorIconsDefRot(ImageView v){
        if(dIndexDefRot==OP_DEF_ROT_1)v.setImageDrawable(getContext().getResources().getDrawable(R.drawable.icon_deform_rotate_3_to_1,null));
        else if(dIndexDefRot==OP_DEF_ROT_2)v.setImageDrawable(getContext().getResources().getDrawable(R.drawable.icon_deform_rotate_1_to_2,null));
        else if(dIndexDefRot==OP_DEF_ROT_3)v.setImageDrawable(getContext().getResources().getDrawable(R.drawable.icon_deform_rotate_2_to_3,null));
        dEditor.putInt(KEY_UNDER_DEFROT,dIndexDefRot);
    }

    protected void selectorIconsFill(ImageView v){
        if(dIndexFill==OP_FILL_1)v.setImageDrawable(getContext().getResources().getDrawable(R.drawable.icon_fill_2,null));
        else if(dIndexFill==OP_FILL_2)v.setImageDrawable(getContext().getResources().getDrawable(R.drawable.icon_fill_1,null));
        dEditor.putInt(KEY_UNDER_FILL,dIndexFill);
    }
    /*нажатие на ок запускает отрез по контуру
    * или диалог*/
    protected void doneCut(){

    }

    /*нажатие на ввод запускает окно
    * с выбором текста*/
    protected void enterText(){

    }

    protected void enabledGrouping(boolean enable){
        dAllLyrs.setEnabled(enable);
    }

    protected void enabledInfo(boolean enable){
        dInfo.setEnabled(enable);
    }

    protected void enabledOperLyr(boolean enable){
        dChangeLyrs.setEnabled(enable);
        dUnion.setEnabled(enable);
        dDeleteLyr.setEnabled(enable);
    }


    protected void enabledDeleteAll(boolean enable){
        dDeleteAll.setEnabled(enable);
    }

    protected void enableUndo(boolean enable){
        dUndo.setEnabled(enable);
    }

    protected void enableRedo(boolean enable){
        dRedo.setEnabled(enable);
    }

    protected void enableDraw(boolean enable){
        dPaint.setEnabled(enable);
        dEraser.setEnabled(enable);
        dFil.setEnabled(enable);
        dText.setEnabled(enable);
        dDeformRotate.setEnabled(enable);
        dCut.setEnabled(enable);
        dTrans.setEnabled(enable);
        dScale.setEnabled(enable);
        dGetColor.setEnabled(enable);
    }


    public boolean isSlideTools(){
        return dVisibleTools;
    }

    public boolean isSlideSave(){
        return dVisibleSave;
    }

    public boolean isSlideAdd(){
        return dVisibleAdd;
    }


}
