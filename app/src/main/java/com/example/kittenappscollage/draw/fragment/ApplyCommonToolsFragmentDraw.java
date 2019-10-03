package com.example.kittenappscollage.draw.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.kittenappscollage.draw.RepDraw;


/*применяем действия стандартной панети инструментов*/
public class ApplyCommonToolsFragmentDraw extends SavedKollagesFragmentDraw implements RepDraw.Appling {


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RepDraw.get().listenerApp(this);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void toolUndo(ImageView v) {
        super.toolUndo(v);
    }

    @Override
    protected void toolRedo(ImageView v) {
        super.toolRedo(v);
    }

    @Override
    protected void toolInfo(ImageView v) {
        super.toolInfo(v);
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
    }

    @Override
    protected void toolDelAll(ImageView v) {
        super.toolDelAll(v);
        RepDraw.get().delAll();

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
