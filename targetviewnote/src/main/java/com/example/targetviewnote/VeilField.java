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

import com.example.targetviewnote.veil.DrawVeil;


public class VeilField extends DialogFragment {

    private int idTarget;

    private DrawVeil veil;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setStyle(DialogFragment.STYLE_NORMAL,
//                android.R.style.Theme_Light_NoTitleBar_Fullscreen);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.field_veil,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = getArguments();
        int color = Color.BLUE;
        if(bundle!=null){
            color = bundle.getInt(TargetView.KEY_COLOR_BACK,Color.BLUE);
            int source = bundle.getInt(TargetView.KEY_SOURCE_TARGET);
            int id = bundle.getInt(TargetView.KEY_TARGET_VIEW);
            View target = null;
            if(source==TargetView.SOURCE_ACTIVITY)target = getActivity().findViewById(id);
            else if(source==TargetView.SOURCE_FRAGMENT)target = getTargetFragment().getView().findViewById(id);
//            paramView(target);
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

//    protected void paramView(final View view){
//        ViewTreeObserver viewTreeObserver = view.getViewTreeObserver();
//        if (viewTreeObserver.isAlive()) {
//            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//                @Override
//                public void onGlobalLayout() {
//                    view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//                    Log.d("TAGTAG ", ""+view.getWidth());
//                }
//            });
//        }
//    }


}
