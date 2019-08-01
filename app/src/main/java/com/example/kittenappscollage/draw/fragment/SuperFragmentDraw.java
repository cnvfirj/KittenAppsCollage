package com.example.kittenappscollage.draw.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.kittenappscollage.R;
import com.example.kittenappscollage.draw.view.ViewDraw;


/*инициируем анимацию нажатия на кнопки
* так же обрабатываем некоторые нажатия*/

public class SuperFragmentDraw extends Fragment implements View.OnClickListener {

    protected ViewDraw dViewDraw;

    private boolean dVisibleTools, dVisibleSave, dVisibleAdd;

    private LinearLayout dToolsLayout;

    private ImageView dSlideTools,dSlideSave, dSlideAdd;

    private ImageView dSaveTel, dSaveNet;

    private ImageView dAddCreated, dAddLink, dAddCam, dAddColl;


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
            case R.id.slide_tools:
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

        dToolsLayout =  view.findViewById(R.id.layout_tools);
        dSlideTools = view.findViewById(R.id.slide_tools);
        dSlideSave = view.findViewById(R.id.slide_save_img);
        dSaveNet = view.findViewById(R.id.save_net);
        dSaveTel = view.findViewById(R.id.save_tel);

        dSlideAdd = view.findViewById(R.id.slide_add_lyr);
        dAddCreated = view.findViewById(R.id.add_created);
        dAddLink = view.findViewById(R.id.add_link);
        dAddCam = view.findViewById(R.id.add_camera);
        dAddColl = view.findViewById(R.id.add_collect);

        shiftTools();
        addListener();
    }



    /*анимация выдвижения панели инструментов для рисования*/
    protected void slideTools(){
        if(!dVisibleTools) {
            dToolsLayout.animate().translationY(-getSlideTools()).setDuration(getTimeSlide()).start();
//            dSlideTools.animate().rotation(180).setDuration(getTimeSlide()).start();
            dVisibleTools = true;
        }else {
            dToolsLayout.animate().translationY(0).setDuration(getTimeSlide()).start();
//            dSlideTools.animate().rotation(0).setDuration(getTimeSlide()).start();
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

    private void shiftTools(){
        if(dVisibleTools) {
            dToolsLayout.animate().translationY(-getSlideTools()).setDuration(0).start();
//            dSlideTools.animate().rotation(180).setDuration(0).start();
        }else {
            dToolsLayout.animate().translationY(0).setDuration(0).start();
//            dSlideTools.animate().rotation(0).setDuration(0).start();
        }
        dSlideTools.setSelected(dVisibleTools);

    }

    private float getSlideAdd1(){
        return getResources().getDimension(R.dimen.SLIDE_ADD);
    }

    private float getSlideAdd2(){
        return getSlideAdd1()*2;
    }

    private float getSlideTools(){
        return getResources().getDimension(R.dimen.SLIDE_TOOLS);
    }

    private float getSlideSave(){
        return getResources().getDimension(R.dimen.SLIDE_SAVE);
    }

    private long getTimeSlide(){
        return 500;
    }


    private void addListener(){
         dSlideTools.setOnClickListener(this);

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
