package com.example.kittenappscollage.draw.addLyrs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.kittenappscollage.R;

public class AddLyrInColl extends SelectedFragment {


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_gallery,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected void readinessView(View v) {

    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public Integer[] targetsEx() {
        return new Integer[0];
    }

    @Override
    public int[] sizesWin() {
        return new int[0];
    }

    @Override
    public String[] getTitles() {
        return new String[0];
    }

    @Override
    public String[] getNotes() {
        return new String[0];
    }

    @Override
    public int[] icTitles() {
        return new int[0];
    }

    @Override
    public int[] icSoftKey() {
        return new int[0];
    }
}
