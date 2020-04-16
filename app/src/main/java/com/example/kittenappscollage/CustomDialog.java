package com.example.kittenappscollage;

import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class CustomDialog extends DialogFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(getContext().getResources().getColor(R.color.colorAccentTransparent)));
        return inflater.inflate(R.layout.dialog_custom,null);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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

    }
}
