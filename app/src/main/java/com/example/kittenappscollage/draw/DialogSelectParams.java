package com.example.kittenappscollage.draw;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.dynamikseekbar.DynamicSeekBar;
import com.example.kittenappscollage.CustomDialog;
import com.example.kittenappscollage.R;
import com.example.kittenappscollage.draw.repozitoryDraw.RepDraw;
import com.example.kittenappscollage.view.PresentPaint;
import com.example.targetviewnote.TargetView;
import com.madrapps.pikolo.HSLColorPicker;
import com.madrapps.pikolo.listeners.OnColorSelectionListener;

import java.util.Objects;

import static com.example.kittenappscollage.draw.repozitoryDraw.RepParams.KEY_SAVE_ALPHA;
import static com.example.kittenappscollage.draw.repozitoryDraw.RepParams.KEY_SAVE_COLOR;
import static com.example.kittenappscollage.draw.repozitoryDraw.RepParams.KEY_SAVE_WIDTH;
import static com.example.kittenappscollage.helpers.Massages.LYTE;

public class DialogSelectParams extends DialogFragment implements DynamicSeekBar.OnSeekBarChangeListener,
        View.OnClickListener,
         TargetView.OnClickTargetViewNoleListener{

    private TargetView targetView;

    private HSLColorPicker selectColor;

    private PresentPaint presentPaint, presentErase, presentText;

    private DynamicSeekBar barPaintWidth, barPaintAlpha, barEraseAlpha;

    private ImageView done, close;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(getContext().getResources().getColor(R.color.colorPrimaryTransparent)));
        return inflater.inflate(R.layout.dialog_select_params,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        selectColor = view.findViewById(R.id.color_pick);
        presentPaint = view.findViewById(R.id.present_paint);
        presentErase = view.findViewById(R.id.present_erase);
        presentErase.setType(PresentPaint.ERASER);
        presentText = view.findViewById(R.id.present_text);
        presentText.setType(PresentPaint.TEXT);
        presentText.setShrift(RepDraw.get().getShrift());
        barPaintAlpha = view.findViewById(R.id.prop_paint_alpha);
        barPaintWidth = view.findViewById(R.id.prop_paint_width);
        barEraseAlpha = view.findViewById(R.id.prop_erase_alpha);
        done = view.findViewById(R.id.prop_done);
        close = view.findViewById(R.id.prop_back);
        selectColor.setColorSelectionListener(new OnColorSelectionListener() {
            @Override
            public void onColorSelected(int i) {
               presentPaint.setColor(i);
               presentErase.setColor(i);
               presentText.setColor(i);
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
        barEraseAlpha.setOnSeekBarChangeListener(this);
        done.setOnClickListener(this);
        close.setOnClickListener(this);

        targetView = TargetView
                .build(this)
                .colorBackground(getContext().getResources().getColor(R.color.colorAccentTransparent))
                .colorBackgroundContent(getContext().getResources().getColor(R.color.colorPrimaryAlpha))
                .sizeTitle(50)
                .iconTitle(R.drawable.icon_collect)
                .textTitle("Main title")
                .target(barPaintAlpha);
        targetView.show();
    }

    @Override
    public void onClickTarget(int i) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.prop_done:
                RepDraw.get()
                        .setAlpha(Color.alpha(presentErase.getColor()))
                        .setColor(presentPaint.getColor())
                        .setWidth(presentPaint.getWidthPaint());
                SharedPreferences p = getActivity().getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor e = p.edit();
                e.putInt(KEY_SAVE_COLOR, RepDraw.get().getColor());
                e.putInt(KEY_SAVE_ALPHA,RepDraw.get().getAlpha());
                e.putFloat(KEY_SAVE_WIDTH, RepDraw.get().getWidth());
                e.apply();
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
                presentText.setAlpha(progress);
                break;
            case R.id.prop_paint_width:
                presentPaint.setWidthPaint(progress);
                presentText.setWidthPaint(progress);
                presentErase.setWidthPaint(progress);
                break;
            case R.id.prop_erase_alpha:
                presentErase.setAlpha(progress);
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
        Resources res = getContext().getResources();
        float height = res.getDimension(R.dimen.param_color_pick)+res.getDimension(R.dimen.param_save)*4+res.getDimension(R.dimen.param_save_2);
        height+=res.getDimension(R.dimen.margin_save)*6;
        float width = res.getDimension(R.dimen.param_color_pick)+res.getDimension(R.dimen.margin_save)*2;
        window.setLayout((int) (width), (int)(height));
        window.setGravity(Gravity.CENTER);
        addParams();
    }

    private void addParams(){
        selectColor.setColor(RepDraw.get().getColor());
        presentPaint.setWidthPaint((int) RepDraw.get().getWidth());
        presentPaint.setColor(RepDraw.get().getColor());
        presentErase.setWidthPaint((int) RepDraw.get().getWidth());
        presentErase.setColor(RepDraw.get().getColor());
        presentText.setWidthPaint((int) RepDraw.get().getWidth());
        presentText.setColor(RepDraw.get().getColor());
        presentText.setText(RepDraw.get().getText());
        barPaintWidth.setProgress((int)RepDraw.get().getWidth());
        barPaintAlpha.setProgress(Color.alpha(RepDraw.get().getColor()));
        barEraseAlpha.setProgress(RepDraw.get().getAlpha());
    }

    protected void paramView(final View view){
        ViewTreeObserver viewTreeObserver = view.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    view.getViewTreeObserver().removeOnGlobalLayoutListener(this);


                }
            });
        }
    }




}
