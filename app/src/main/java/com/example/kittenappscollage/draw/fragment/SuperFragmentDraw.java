package com.example.kittenappscollage.draw.fragment;


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

import static com.example.kittenappscollage.helpers.Massages.MASSAGE;


/*описываем основную анимацию движения кнопок и панели
* инструментов. Присваиваем им иконки*/

public class SuperFragmentDraw extends Fragment implements View.OnClickListener {

    protected ViewDraw dViewDraw;

    private boolean dVisibleTools, dVisibleSave, dVisibleAdd;

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
        return inflater.inflate(R.layout.fragment_draw,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dViewDraw = view.findViewById(R.id.view_draw);
        initViews(view);
    }

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
                saveNet();
                break;
            case R.id.save_tel:
                saveTel();
                break;
            case R.id.add_created:
                addCreated();
                break;
            case R.id.add_link:
                addLink();
                break;
            case R.id.add_camera:
                addCam();
                break;
            case R.id.add_collect:
                addColl();
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


        addListener();
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
                    computeSlide(dSlideStep,0);
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



    private float getSlideAdd1(){
        return getResources().getDimension(R.dimen.SLIDE_ADD);
    }

    private float getSlideAdd2(){
        return getSlideAdd1()*2;
    }

    private float getSlideTools(){
        if(dVisibleTools){
            dVisibleTools = false;
            return 0;
        } else{
            dVisibleTools = true;
            return dSlideStep;
        }
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


    protected void saveNet(){
        slideSave();
    }

    protected void saveTel(){
        slideSave();
    }

    protected void addCreated(){
        slideAdd();
    }

    protected void addLink(){
        slideAdd();
    }

    protected void addCam(){
        slideAdd();
    }

    protected void addColl(){
        slideAdd();
    }
}
