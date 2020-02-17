package com.example.kittenappscollage.collect.reviewImage;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.kittenappscollage.R;

import java.util.ArrayList;

public class DialogReview extends DialogFragment {

    public static final int DIALOG_ACTION = -112;

    public static final String KEY_ARR = "key arr";

    public static final String KEY_POS = "key pos";


    public static DialogReview inst(ArrayList<String>imgs, int position, Fragment target){
        DialogReview d = new DialogReview();
        Bundle b = new Bundle();
        b.putStringArrayList(KEY_ARR,imgs);
        b.putInt(KEY_POS,position);
        d.setArguments(b);
        d.setTargetFragment(target,DIALOG_ACTION);
        return d;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_review_images,null);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        Window window = getDialog().getWindow();
        Rect rect = new Rect();
        getActivity().getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        window.setLayout((int) (rect.right*0.9), (int)(rect.bottom*0.7));
        window.setGravity(Gravity.CENTER);
    }
}
