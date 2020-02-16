package com.example.kittenappscollage.collect.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.kittenappscollage.R;

import java.io.File;
import java.util.ArrayList;

import static com.example.kittenappscollage.collect.adapters.ListenLoadFoldAdapter.ROOT_ADAPTER;
import static com.example.kittenappscollage.helpers.Massages.LYTE;

public class FragmentSlideGallery extends FragmentGallery implements View.OnClickListener {

    /*обработка кнопок*/
    /* - для главной галереи:
            режим выбора -  скрыть папку, удалить папку, переместить папку на карту памяти, переименовать папку.
             выйти из режима*/
    /* - для подгаллереи:
    *       режим выбора - поделиться коллажем, удалить коллаж
    *         выйти из режима
    *       режим клика - выйти в главную галлерею  */
    private ImageView selectExitMode,selected_1, selected_2, selected_3, selected_4;

    private float slide;

    private ArrayList<String>dirs;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dirs = new ArrayList<>();
        checkNamesDirs();
        slide = getContext().getResources().getDimension(R.dimen.param_save)+getContext().getResources().getDimension(R.dimen.margin_save);
        init(view);
        hideMenu();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.selected_collect_exit_mode:
                clickExit((ImageView)v);
                break;
            case R.id.selected_collect_1:
                clickSel_1((ImageView)v);
                break;
            case R.id.selected_collect_2:
                clickSel_2((ImageView)v);
                break;
            case R.id.selected_collect_3:
                clickSel_3((ImageView)v);
                break;
            case R.id.selected_collect_4:
                clickSel_4((ImageView)v);
                break;

        }
    }




    private void checkNamesDirs(){
        File[] d = ContextCompat.getExternalFilesDirs(getContext(), null);
        for (File f:d){
            dirs.add(f.getAbsolutePath().split("Android")[0]);
        }
    }
    private void init(View v){
        selectExitMode = v.findViewById(R.id.selected_collect_exit_mode);/*выход или отмена*/
        selectExitMode.setOnClickListener(this);
        selected_1 = v.findViewById(R.id.selected_collect_1);
        selected_1.setOnClickListener(this);
        selected_2 = v.findViewById(R.id.selected_collect_2);
        selected_2.setOnClickListener(this);
        selected_3 = v.findViewById(R.id.selected_collect_3);
        selected_3.setOnClickListener(this);
        selected_4 = v.findViewById(R.id.selected_collect_4);
        selected_4.setOnClickListener(this);
    }

    @Override
    protected void setIndexAdapter(int i) {
        super.setIndexAdapter(i);
        if(i==ROOT_ADAPTER){
            slideExit(false);
        }else {
            selectExitMode.setSelected(false);
            selectExitMode.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_select_back,null));
            slideExit(true);
        }
    }

    protected void clickExit(ImageView v){
        if(!modeSel()){
            if(getIndexAdapter()!= ROOT_ADAPTER){
                setIndexAdapter(ROOT_ADAPTER);
                getGridLayoutManager().setSpanCount(2);
                getRecycler().setAdapter(getFoldAdapt());
             }
        }else {
            invisibleMenu();
        }
    }

    protected void clickSel_1(ImageView v){

    }
    protected void clickSel_2(ImageView v){

    }

    protected void clickSel_3(ImageView v){

    }
    protected void clickSel_4(ImageView v){

    }


    @Override
    protected void visibleMenu() {
      if(getIndexAdapter()==ROOT_ADAPTER){
          selectExitMode.setImageDrawable(getContext().getResources().getDrawable(R.drawable.icon_collect_selected_menu,null));
          selectExitMode.setSelected(true);
          slideExit(true);
          slideSel_1(true);
          slideSel_2(true);
          getRecycler().setEnabled(false);
      }else{
          selectExitMode.setSelected(false);
          selectExitMode.setImageDrawable(getContext().getResources().getDrawable(R.drawable.icon_collect_selected_menu,null));
          selectExitMode.setSelected(true);
          selected_3.setEnabled(true);
      }
        selectIconAction3(selected_3);
        slideSel_3(true);
        slideSel_4(true);
    }

    @Override
    protected void invisibleMenu() {
        if(getIndexAdapter()==ROOT_ADAPTER){
            getFoldAdapt().setModeSelected(false);
            slideExit(false);
            slideSel_1(false);
            slideSel_2(false);
            getRecycler().setEnabled(true);
        }else {
            /*implement animation exit*/
            getImgAdapt().setModeSelected(false);
            selectExitMode.setSelected(false);

        }
        slideSel_3(false);
        slideSel_4(false);
    }


    protected void selectIconAction3(ImageView view){
        if(getIndexAdapter()==ROOT_ADAPTER){
            view.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_selected_copy,null));

        }else {
            view.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_share,null));
        }
    }



    protected void slideExit(boolean s){
        if(s){
            selectExitMode.animate().translationY(0).setDuration(300).start();
        }else {
            selectExitMode.animate().translationY(-slide).setDuration(300).start();
        }
    }

    protected void slideSel_1(boolean s){
        if(s){
            selected_1.animate().translationY(0).setDuration(300).start();
        }else {
            selected_1.animate().translationY(-slide).setDuration(300).start();
        }
    }

    protected void slideSel_2(boolean s){
        if(s){
            selected_2.animate().translationY(0).setDuration(300).start();
        }else {
            selected_2.animate().translationY(-slide).setDuration(300).start();
        }
    }

    protected void slideSel_3(boolean s){
        if(s){
            selected_3.animate().translationY(0).setDuration(300).start();
        }else {
            selected_3.animate().translationY(-slide).setDuration(300).start();
        }
    }


protected void slideSel_4(boolean s){
        if(s){
            selected_4.animate().translationY(0).setDuration(300).start();
        }else {
            selected_4.animate().translationY(-slide).setDuration(300).start();
        }
    }

    protected ImageView getSelected_3(){
        return selected_3;
    }

    protected ArrayList<String>getNamesDirs(){
        return dirs;
    }


    private boolean modeSel(){
        if(getIndexAdapter()!=ROOT_ADAPTER){
            return getImgAdapt().isModeSelected();
        }else {
            return getFoldAdapt().isModeSelected();
        }
    }

    protected void hideMenuInAction(){
        selected_1.animate().translationY(-slide).setDuration(300).start();
        selected_2.animate().translationY(-slide).setDuration(300).start();
        selected_3.animate().translationY(-slide).setDuration(300).start();
        selected_4.animate().translationY(-slide).setDuration(300).start();
        if(getIndexAdapter()!=ROOT_ADAPTER) {
            selectExitMode.animate().translationY(-slide).setDuration(300).start();
        }else {
            selectExitMode.setSelected(false);
        }
    }
    protected void hideMenu(){
        selectExitMode.animate().translationY(-slide).setDuration(0).start();
        selected_1.animate().translationY(-slide).setDuration(0).start();
        selected_2.animate().translationY(-slide).setDuration(0).start();
        selected_3.animate().translationY(-slide).setDuration(0).start();
        selected_4.animate().translationY(-slide).setDuration(0).start();

    }



}
