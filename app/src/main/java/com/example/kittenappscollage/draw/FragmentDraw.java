package com.example.kittenappscollage.draw;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.kittenappscollage.R;
import com.example.kittenappscollage.draw.view.ViewDraw;
import com.example.kittenappscollage.helpers.Massages;
import com.example.kittenappscollage.packProj.RepDraw;

import static com.example.kittenappscollage.helpers.Massages.MASSAGE;

public class FragmentDraw extends Fragment implements View.OnClickListener {

    private ViewDraw dViewDraw;

    private boolean dVisibleTools;

    private LinearLayout dToolsLayout;

    private ImageView dSlideTools;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        dVisibleTools = false;
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
        }
    }

    private void  initViews(View view){
        dToolsLayout =  view.findViewById(R.id.layout_tools);
        dSlideTools = view.findViewById(R.id.slide_tools);

        addListener();
    }



    /*анимация выдвижения панели инструментов для рисования*/
    private void slideTools(){

        if(!dVisibleTools) {
            dToolsLayout.animate().translationY(-getSlideTools()).setDuration(500).start();
            dSlideTools.animate().rotation(180).setDuration(500).start();
            dVisibleTools = true;
        }else {
            dToolsLayout.animate().translationY(0).setDuration(500).start();
            dSlideTools.animate().rotation(0).setDuration(500).start();
            dVisibleTools = false;
        }
    }

    private float getSlideTools(){
        return getResources().getDimension(R.dimen.SLIDE_TOOLS);
    }


    private void addListener(){
         dSlideTools.setOnClickListener(this);
    }
}
