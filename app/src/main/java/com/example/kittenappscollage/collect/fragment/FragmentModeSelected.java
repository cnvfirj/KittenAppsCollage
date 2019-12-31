package com.example.kittenappscollage.collect.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.kittenappscollage.R;

public class FragmentModeSelected extends FragmentCollect implements View.OnClickListener {

    private ImageView selectedClose, selectedShare, selectedDelete;

    private float slide;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        slide = getContext().getResources().getDimension(R.dimen.param_save)+getContext().getResources().getDimension(R.dimen.margin_save);
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
    }

    protected void selectedShare(ImageView view){
        slideMenu(false,500);
    }

    protected void selectedDelete(ImageView view){
        slideMenu(false,500);
    }


}
