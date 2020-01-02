package com.example.kittenappscollage.collect.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.kittenappscollage.R;
import com.example.kittenappscollage.collect.adapters.PresentAdapter;

import java.io.File;
import java.util.HashMap;

public class FragmentModeSelected extends FragmentCollect implements View.OnClickListener, PresentAdapter.ModeSelected {

    private ImageView selectedClose, selectedShare, selectedDelete;

    private float slide;

    private HashMap<Integer, File>selectedImages;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        slide = getContext().getResources().getDimension(R.dimen.param_save)+getContext().getResources().getDimension(R.dimen.margin_save);
        selectedImages = new HashMap<>();
        getSelector().setListener(this);
        init(view);
        slideMenu(false,0);
    }

    private void init(View v){
        selectedClose = v.findViewById(R.id.selected_collect_exit);
        selectedClose.setOnClickListener(this);
        selectedDelete = v.findViewById(R.id.selected_collect_delete);
        selectedDelete.setOnClickListener(this);
        selectedShare = v.findViewById(R.id.selected_collect_share);
        selectedShare.setOnClickListener(this);
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.selected_collect_delete:
                selectedDelete((ImageView) view);
                break;
            case R.id.selected_collect_share:
                selectedShare((ImageView) view);
                break;
            case R.id.selected_collect_exit:
                selectedExit((ImageView) view);
                break;
        }
    }

    @Override
    public void selected(boolean mode) {
        if(mode){
            slideMenu(true,500);
           getSelector().adapter(getIndexAdapter()).setMap(selectedImages);
        }else {
            selectedImages.clear();
            slideMenu(false,500);
        }
    }

    @Override
    public void click(File file) {
        /*open dialog fragment*/
    }

    @Override
    public void adapter(PresentAdapter adapter) {

    }

    protected void slideMenu(boolean s, int time){
        if(s){
            selectedClose.animate().translationY(0).setDuration(time).start();
            selectedDelete.animate().translationY(0).setDuration(time).start();
            selectedShare.animate().translationY(0).setDuration(time).start();
        }else {
          selectedClose.animate().translationY(-slide).setDuration(time).start();
          selectedDelete.animate().translationY(-slide).setDuration(time).start();
          selectedShare.animate().translationY(-slide).setDuration(time).start();
        }
    }

    protected void selectedExit(ImageView view){
          slideMenu(false,500);
          selectedImages.clear();
          getSelector().adapter(getIndexAdapter()).resetChecks();
    }

    protected void selectedShare(ImageView view){
        slideMenu(false,500);
        selectedImages.clear();
    }

    protected void selectedDelete(ImageView view){
        slideMenu(false,500);
        selectedImages.clear();
    }

    private boolean delete(File file){
        if (file.exists()) {
            return file.delete();
        }
        return false;
    }


}
