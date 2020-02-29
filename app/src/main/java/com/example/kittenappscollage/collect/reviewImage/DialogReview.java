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
import androidx.viewpager.widget.ViewPager;

import com.example.kittenappscollage.R;
import com.example.kittenappscollage.mainTabs.SelectSweepViewPager;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class DialogReview extends DialogFragment {

    public static final int DIALOG_ACTION = -112;

    public static final String KEY_ARR = "key arr";

    public static final String KEY_POS = "key pos";


    private SelectSweepViewPager viewPager;

    private ReviewAdapter adapter;

    private TabLayout tabs;

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
        viewPager = view.findViewById(R.id.dialog_review_view_pager);
        tabs = view.findViewById(R.id.dialog_review_tabs);
        tabs.setupWithViewPager(viewPager);
        Bundle b = getArguments();
        if(b!=null) {
            adapter = new ReviewAdapter(getChildFragmentManager(), b.getStringArrayList(KEY_ARR));
            viewPager.setAdapter(adapter);
            viewPager.setCurrentItem(b.getInt(KEY_POS));
            viewPager.setSweep(true);
        }
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
