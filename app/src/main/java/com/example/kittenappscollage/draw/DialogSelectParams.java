package com.example.kittenappscollage.draw;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.dynamikseekbar.DynamicSeekBar;
import com.example.kittenappscollage.R;
import com.example.kittenappscollage.draw.repozitoryDraw.RepDraw;
import com.example.kittenappscollage.view.CustomFon;
import com.example.kittenappscollage.view.PresentPaint;
import com.madrapps.pikolo.HSLColorPicker;
import com.madrapps.pikolo.listeners.OnColorSelectionListener;

public class DialogSelectParams extends DialogFragment implements DynamicSeekBar.OnSeekBarChangeListener,
        View.OnClickListener {

    private HSLColorPicker selectColor;

    private PresentPaint presentPaint;

    private DynamicSeekBar barPaintWidth, barPaintAlpha;

    private ImageView done, close;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_select_params,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        selectColor = view.findViewById(R.id.color_pick);
        presentPaint = view.findViewById(R.id.present_paint);
        barPaintAlpha = view.findViewById(R.id.prop_paint_alpha);
        barPaintWidth = view.findViewById(R.id.prop_paint_width);
        done = view.findViewById(R.id.prop_done);
        close = view.findViewById(R.id.prop_back);
        selectColor.setColorSelectionListener(new OnColorSelectionListener() {
            @Override
            public void onColorSelected(int i) {
               presentPaint.setColor(i);
            }

            @Override
            public void onColorSelectionStart(int i) {

            }

            @Override
            public void onColorSelectionEnd(int i) {

            }
        });
        barPaintAlpha.setOnSeekBarChangeListener(this);
        barPaintWidth.setOnSeekBarChangeListener(this);
        done.setOnClickListener(this);
        close.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.prop_done:
                RepDraw.get().allParams(
                        presentPaint.getWidthPaint(),
                        presentPaint.getColor(),
                        0,
                        "TEXT"
                );
                dismiss();
                break;
            case R.id.prop_back:
                dismiss();
                break;
        }
    }

    @Override
    public void onProgressChanged(DynamicSeekBar seekBar, int progress, boolean isTouch) {
        switch (seekBar.getId()){
            case R.id.prop_paint_alpha:
                presentPaint.setAlpha(progress);
                break;
            case R.id.prop_paint_width:
                presentPaint.setWidthPaint(progress);
                break;
        }
    }

    @Override
    public void onStartTrackingTouch(DynamicSeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(DynamicSeekBar seekBar) {

    }

    @Override
    public void onResume() {
        super.onResume();
        Window window = getDialog().getWindow();
        Rect rect = new Rect();
        getActivity().getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        window.setLayout((int) (rect.right*0.9), (int)(rect.bottom*0.9));
        window.setGravity(Gravity.CENTER);

        addParams();
    }

    private void addParams(){
        selectColor.setColor(RepDraw.get().getColor());
        presentPaint.setWidthPaint((int) RepDraw.get().getWidth());
        presentPaint.setColor(RepDraw.get().getColor());
        barPaintWidth.setProgress((int)RepDraw.get().getWidth());
        barPaintAlpha.setProgress(Color.alpha(RepDraw.get().getColor()));
    }


}
