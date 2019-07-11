package com.example.kittenappscollage.draw;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.kittenappscollage.R;
import com.example.kittenappscollage.packProj.RepDraw;

public class FragmentDraw extends Fragment {

    private ViewDraw dViewDraw;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_draw,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dViewDraw = view.findViewById(R.id.view_draw);
        RepDraw.get().list(new RepDraw.Adding() {
            @Override
            public void readinessImg(boolean is) {
                dViewDraw.applyMutable();
            }

            @Override
            public void readinessLyr(boolean is) {
                dViewDraw.applyMutable();
            }

            @Override
            public void readinessAll(boolean is) {
                dViewDraw.applyMutable();
            }
        });
    }
}
