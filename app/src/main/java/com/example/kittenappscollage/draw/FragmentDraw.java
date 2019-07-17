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

public class FragmentDraw extends Fragment {

    private ViewDraw dViewDraw;

    private DrawTools dDrawTools;

    private boolean dVisibleTools;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        dVisibleTools = false;
        dDrawTools = new DrawTools();
        return inflater.inflate(R.layout.fragment_draw,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dViewDraw = view.findViewById(R.id.view_draw);

        initViews(view);
    }

    private void  initViews(View view){
//        ImageView i = (ImageView)view.findViewById(R.id.slide_tools);
//        i.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                MASSAGE("slide",null);
//
//                slideTools((LinearLayout)view.findViewById(R.id.layout_tools));
//            }
//        });
        addListener();
    }

    private void slideTools(ViewGroup container){
//        if(container==null)MASSAGE("cont null ",null);
//        if(!dVisibleTools){
//            assert container != null;
//            container.animate().translationY(-50).setDuration(1000).start();
//        }else {
//
//        }
    }

    private void addListener(){

    }
}
