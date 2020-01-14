package com.example.kittenappscollage.draw.textProp;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.dynamikseekbar.DynamicSeekBar;
import com.example.kittenappscollage.R;
import com.example.kittenappscollage.draw.fragment.ApplyDrawToolsFragmentDraw;
import com.example.kittenappscollage.draw.repozitoryDraw.RepDraw;

import static com.example.kittenappscollage.helpers.Massages.LYTE;
import static com.example.kittenappscollage.helpers.Massages.SHOW_MASSAGE;


public class DialogSelectShrift extends DialogSelecledTextFragment {


    private int sourceFont;

    private String pathFont;

    private InputMethodManager imm;


    public static DialogSelectShrift get(){
        DialogSelectShrift d = new DialogSelectShrift();
        return d;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        enableBarAngle(RepDraw.get().isTextItalic());
        getBarAngle().setProgress(computeProgressBar());
        getPresent().selFill(RepDraw.get().isTextFill());
        getPresent().setItalic(RepDraw.get().isTextItalic(),RepDraw.get().getItalicText());
        getActItal().setSelected(RepDraw.get().isTextItalic());
        getActFill().setSelected(RepDraw.get().isTextFill());
    }

    @Override
    public void onProgressChanged(DynamicSeekBar seekBar, int progress, boolean isTouch) {
        super.onProgressChanged(seekBar, progress, isTouch);
        float angle = (progress-100f)/100f;
        if(getActItal().isSelected())getPresent().setItalic(getActItal().isSelected(),angle);
    }

    @Override
    protected void initListShrift(View view) {
        super.initListShrift(view);
        getAdapter().setListen(new ListenAdapterShrift() {
            @Override
            public void item(int position, String path) {
              sourceFont = position<getSizeBaseFonts()? ApplyDrawToolsFragmentDraw.S_ASSETS:ApplyDrawToolsFragmentDraw.S_STORAGE;
              pathFont = path;
            }

            @Override
            public void font(Typeface t) {
                getPresent().setShrift(t);
            }
        });
    }

    @Override
    protected void enableAngle(ImageView view) {
        super.enableAngle(view);
        view.setSelected(!view.isSelected());
        enableBarAngle(view.isSelected());
        getPresent().setItalic(view.isSelected(),computeAngleBar());
    }

    @Override
    protected void fillText(ImageView view) {
        super.fillText(view);
        view.setSelected(!view.isSelected());
        getPresent().selFill(view.isSelected());
    }

    @Override
    protected void clickAction_1(ImageView view) {
        super.clickAction_1(view);
        RepDraw.get().setShrift(getPresent().getShrift()).setPathFont(pathFont).setSourceFont(sourceFont);
        if(!getEnterText().getText().toString().isEmpty()){
            imm.hideSoftInputFromWindow(getEnterText().getWindowToken(), 0);
            RepDraw.get()
                    .setText(getEnterText().getText().toString())
                    .textFill(getPresent().isTextFill())
                    .textItalic(getPresent().isTextItalic())
                    .setItalicText(getPresent().getAngleItalic());
            dismiss();
        }else {
            SHOW_MASSAGE(getContext(),"Некорректный текст");
        }

    }

    @Override
    protected void clickAction_2(ImageView view) {
        super.clickAction_2(view);
        getEnterText().setText("");
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        getEnterText().requestFocus();
    }

    @Override
    protected void clickAction_3(ImageView view) {
        super.clickAction_3(view);
        sourceFont = -1;
        pathFont = "";
        imm.hideSoftInputFromWindow(getEnterText().getWindowToken(), 0);
        dismiss();
    }

    @Override
    public void onPause() {
        super.onPause();
        imm.hideSoftInputFromWindow(getEnterText().getWindowToken(), 0);
    }

    private void enableBarAngle(boolean enable){
        getBarAngle().setEnabled(enable);
        getBarAngle().setColorWay(enable?getResources().getColor(R.color.colorPrimary):Color.GRAY);
        getBarAngle().setColorMark(enable?getResources().getColor(R.color.colorPrimary):Color.GRAY);
        getBarAngle().setColorProgress(enable?getResources().getColor(R.color.colorAccent):Color.GRAY);
    }

    private int computeAngleBar(){
        return (getBarAngle().getProgress()-100)/100;
    }
    private int computeProgressBar(){
        return ((int)(RepDraw.get().getItalicText()*100)+100);
    }
}
