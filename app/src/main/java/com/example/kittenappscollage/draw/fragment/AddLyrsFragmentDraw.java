package com.example.kittenappscollage.draw.fragment;

import android.graphics.Point;
import android.graphics.PointF;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.kittenappscollage.R;
import com.example.kittenappscollage.draw.addLyrs.FrameDialogAdd;
import com.example.kittenappscollage.draw.RepDraw;
import com.example.kittenappscollage.helpers.AllPermissions;
import com.example.kittenappscollage.helpers.SaveImageToFile;

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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        enabledOperLyr(false);
        enabledGrouping(false);
        enableDraw(false);
        enabledDeleteAll(false);
        enabledInfo(false);
        enableUndo(false);
        enableRedo(false);
        waitingReadinessView(dViewDraw);
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
        if (AllPermissions.create().activity(getActivity()).reqSingle(AllPermissions.CAMERA).isCamera()) {
            aDialog = FrameDialogAdd.instance(FrameDialogAdd.ADD_CAM);
            aDialog.show(getChildFragmentManager(),DIALOG);
        } else {
            AllPermissions.create().activity(getActivity()).callDialog(AllPermissions.CAMERA);
        }

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
        if(!isSlideTools()&&is)slideTools();
            enabledDeleteAll(is);
            enableDraw(is);
            enabledInfo(is);
    }

    @Override
    public void readinessLyr(boolean is) {
        if(is)dViewDraw.invalidate();
            enabledGrouping(is);
            enabledOperLyr(is);
    }

    @Override
    public void readinessAll(boolean is) {
        if(!isSlideTools())slideTools();
        if(RepDraw.get().isImg())readinessImg(is);
        if(RepDraw.get().isLyr())readinessLyr(is);
    }

    protected void waitingReadinessView(final View view){
        ViewTreeObserver viewTreeObserver = view.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    RepDraw.get().viewDraw(new PointF(view.getWidth(),view.getHeight()));
                }
            });
        }
    }


}
