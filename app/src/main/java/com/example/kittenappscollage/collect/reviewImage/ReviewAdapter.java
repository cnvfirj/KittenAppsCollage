package com.example.kittenappscollage.collect.reviewImage;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

import static com.example.kittenappscollage.helpers.Massages.LYTE;

public class ReviewAdapter extends FragmentStatePagerAdapter {

    private ArrayList<String>imgs;

    @SuppressLint("WrongConstant")
    public ReviewAdapter(@NonNull FragmentManager fm,ArrayList<String>imgs) {
        super(fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.imgs = imgs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return ReviewFragment.inst(imgs.get(position));
    }

    @Override
    public int getCount() {
        return imgs.size();
    }
}
