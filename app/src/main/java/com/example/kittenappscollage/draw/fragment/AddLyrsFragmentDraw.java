package com.example.kittenappscollage.draw.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.kittenappscollage.R;
import com.example.kittenappscollage.draw.addLyrs.AddLyr;
import com.example.kittenappscollage.draw.addLyrs.FrameDialogAdd;

import java.sql.Time;
import java.util.concurrent.TimeUnit;

import static com.example.kittenappscollage.helpers.Massages.MASSAGE;

public class AddLyrsFragmentDraw extends SuperFragmentDraw {


    public final static String DIALOG = "dialog";

    private FrameDialogAdd aDialog;

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
}
