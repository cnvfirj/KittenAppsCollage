package com.example.kittenappscollage.collect.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.kittenappscollage.R;

import static com.example.kittenappscollage.collect.adapters.ListenLoadFoldAdapter.ROOT_ADAPTER;

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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
          /*implement animation exit*/
          selectExitMode.setSelected(false);
          selectExitMode.setImageDrawable(getContext().getResources().getDrawable(R.drawable.icon_collect_selected_menu,null));
          selectExitMode.setSelected(true);
      }
        selectIconAction3(selected_3);
        slideSel_3(true);
        slideSel_4(true);

    }

    @Override
    protected void invisibleMenu() {
        if(getIndexAdapter()==ROOT_ADAPTER){
            slideExit(false);
            slideSel_1(false);
            slideSel_2(false);
            getFoldAdapt().setModeSelected(false);
            getRecycler().setEnabled(true);
        }else {
            /*implement animation exit*/
            selectExitMode.setImageDrawable(getContext().getResources().getDrawable(R.drawable.icon_collect_selected_menu,null));
            selectExitMode.setSelected(false);
            getImgAdapt().setModeSelected(false);
        }
        slideSel_3(false);
        slideSel_4(false);
    }

    protected void selectIconAction3(ImageView view){
        if(getIndexAdapter()==ROOT_ADAPTER){
            view.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_trans_device_to_sd,null));
            /*если нет карты памяти сделать неактивной и назначить иконку с у-ва на карту*/
            /*здесь сделать автоматом выбор переносом на карту памяти*/
            /*если выбранная папка на карте памяти то перенос на у-во*/
        }else {
            view.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_share,null));
        }
    }

    protected void slideExit(boolean s){
        if(s){
            selectExitMode.animate().translationY(0).setDuration(500).start();
        }else {
            selectExitMode.animate().translationY(-slide).setDuration(500).start();
        }
    }

    protected void slideSel_1(boolean s){
        if(s){
            selected_1.animate().translationY(0).setDuration(500).start();
        }else {
            selected_1.animate().translationY(-slide).setDuration(500).start();
        }
    }

    protected void slideSel_2(boolean s){
        if(s){
            selected_2.animate().translationY(0).setDuration(500).start();
        }else {
            selected_2.animate().translationY(-slide).setDuration(500).start();
        }
    }

    protected void slideSel_3(boolean s){
        if(s){
            selected_3.animate().translationY(0).setDuration(500).start();
        }else {
            selected_3.animate().translationY(-slide).setDuration(500).start();
        }
    }


protected void slideSel_4(boolean s){
        if(s){
            selected_4.animate().translationY(0).setDuration(500).start();
        }else {
            selected_4.animate().translationY(-slide).setDuration(500).start();
        }
    }



    private boolean modeSel(){
        if(getIndexAdapter()!=ROOT_ADAPTER){
            return getImgAdapt().isModeSelected();
        }else {
            return getFoldAdapt().isModeSelected();
        }
    }

    private void hideMenu(){
        selectExitMode.animate().translationY(-slide).setDuration(0).start();
        selected_1.animate().translationY(-slide).setDuration(0).start();
        selected_2.animate().translationY(-slide).setDuration(0).start();
        selected_3.animate().translationY(-slide).setDuration(0).start();
        selected_4.animate().translationY(-slide).setDuration(0).start();

    }



}
