package com.example.kittenappscollage.draw.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.kittenappscollage.draw.RepDraw;
import com.example.kittenappscollage.draw.saveSteps.BackNextStep;

import java.util.Objects;


/*применяем действия стандартной панети инструментов*/
public class ApplyCommonToolsFragmentDraw extends SavedKollagesFragmentDraw implements RepDraw.Appling {

    private final String KEY_INFO = "info";

    private final String KEY_GROUP = "group";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RepDraw.get().listenerApp(this);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    protected void toolUndo(ImageView v) {
        super.toolUndo(v);
        BackNextStep.get().inBackToNext();
    }

    @Override
    protected void toolRedo(ImageView v) {
        super.toolRedo(v);
        BackNextStep.get().inNextToBack();
    }

    @Override
    protected void toolInfo(ImageView v) {
        super.toolInfo(v);
        dViewDraw.changeInfo(v.isActivated());
    }

    @Override
    protected void toolAllLyrs(ImageView v) {
        super.toolAllLyrs(v);
        dViewDraw.groupLyrs(v.isActivated());
    }

    @Override
    protected void toolUnion(ImageView v) {
        super.toolUnion(v);
        RepDraw.get().union();
        readinessLyr(false);
    }

    @Override
    protected void toolChange(ImageView v) {
        super.toolChange(v);
        RepDraw.get().change();
    }

    @Override
    protected void toolDelLyr(ImageView v) {
        super.toolDelLyr(v);
        RepDraw.get().delLyr();
        readinessLyr(false);
    }

    @Override
    protected void toolDelAll(ImageView v) {
        super.toolDelAll(v);
        RepDraw.get().delAll();

        readinessLyr(false);
        readinessImg(false);
    }

    /*интерфейс применения изменений*/
    @Override
    public void delLyr(boolean is) {
        if(is)dViewDraw.invalidate();
    }

    @Override
    public void delAll(boolean is) {
        if(is)dViewDraw.invalidate();
        if(isSlideTools())slideTools();
    }

    @Override
    public void change(boolean is) {
        if(is)dViewDraw.invalidate();
    }



}
