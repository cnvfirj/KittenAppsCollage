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


    FrameDialogAdd dialog;
    @Override
    protected void addCreated(ImageView v) {
        super.addCreated(v);
        dialog = new FrameDialogAdd();
        dialog.show(getFragmentManager(),"dialog");

    }

    @Override
    protected void addLink(ImageView v) {
        super.addLink(v);
    }

    @Override
    protected void addCam(ImageView v) {
        super.addCam(v);
    }

    @Override
    protected void addColl(ImageView v) {
        super.addColl(v);
    }
}
