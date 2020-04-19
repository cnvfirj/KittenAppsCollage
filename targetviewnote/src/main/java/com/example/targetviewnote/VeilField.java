package com.example.targetviewnote;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.targetviewnote.veil.DrawMidiVeil;
import com.example.targetviewnote.veil.DrawVeil;


public class VeilField extends DialogFragment {

    private DrawMidiVeil veil;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.field_veil,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        veil = view.findViewById(R.id.veil);
        Bundle bundle = getArguments();
//
        if(bundle!=null){
            veil.setColorBackground(bundle.getInt(TargetView.KEY_COLOR_BACK,Color.BLUE));
            veil.setTarget(bundle.getIntArray(TargetView.KEY_TARGET_VIEW));
            veil.setFrame(bundle.getInt(TargetView.KEY_TARGET_FRAME,0));
        }


    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();

        Window window = getDialog().getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams windowParams = window.getAttributes();
        windowParams.dimAmount = 0f;
        windowParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        window.setGravity(Gravity.CENTER);
        window.setAttributes(windowParams);


    }




}
