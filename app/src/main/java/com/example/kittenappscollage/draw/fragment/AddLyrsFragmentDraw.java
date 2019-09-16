package com.example.kittenappscollage.draw.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.kittenappscollage.R;
import com.example.kittenappscollage.draw.addLyrs.FrameDialogAdd;
import com.example.kittenappscollage.draw.RepDraw;

import static com.example.kittenappscollage.helpers.Massages.SHOW_MASSAGE;

/*обрабатываем добавление слоя или начало коллажа*/
public class AddLyrsFragmentDraw extends SuperFragmentDraw implements RepDraw.Adding{


    public final static String DIALOG = "dialog";

    private FrameDialogAdd aDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RepDraw.get().listenerAdd(this);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void slideTools() {

        if(!RepDraw.get().isImg()&&!isSlideTools())SHOW_MASSAGE(getContext(),getContext().getResources().getString(R.string.condition_apply_tools));
            super.slideTools();

    }

    @Override
    protected void addCreated(ImageView v) {
        super.addCreated(v);
        aDialog = FrameDialogAdd.instance(FrameDialogAdd.ADD_NEW);
        aDialog.show(getChildFragmentManager(),DIALOG);

    }

    @Override
    protected void addLink(ImageView v) {
        super.addLink(v);
        aDialog = FrameDialogAdd.instance(FrameDialogAdd.ADD_NET);
        aDialog.show(getChildFragmentManager(),DIALOG);
    }

    @Override
    protected void addCam(ImageView v) {
        super.addCam(v);
        aDialog = FrameDialogAdd.instance(FrameDialogAdd.ADD_CAM);
        aDialog.show(getChildFragmentManager(),DIALOG);
    }

    @Override
    protected void addColl(ImageView v) {
        super.addColl(v);
        aDialog = FrameDialogAdd.instance(FrameDialogAdd.ADD_COLL);
        aDialog.show(getChildFragmentManager(),DIALOG);
    }

    @Override
    public void readinessImg(boolean is) {
        if(is)dViewDraw.invalidate();
        if(!isSlideTools())slideTools();
    }

    @Override
    public void readinessLyr(boolean is) {
        if(is)dViewDraw.invalidate();

    }

    @Override
    public void readinessAll(boolean is) {
        if(is)dViewDraw.invalidate();
        if(!isSlideTools())slideTools();
    }


}
