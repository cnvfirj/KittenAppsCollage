package com.kittendevelop.kittenappscollage.mainTabs;


import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPageAdapter extends FragmentPagerAdapter {

    /*списки для хранения фрагментов и имен вкладок*/
    private final List<Fragment> mFragmentList = new ArrayList<>();

    @SuppressLint("WrongConstant")
    public ViewPageAdapter(FragmentManager manager) {
        super(manager, FragmentStatePagerAdapter.POSITION_UNCHANGED);
    }

//    public ViewPageAdapter(@NonNull FragmentManager fm, int behavior) {
//        super(fm, behavior);
//    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    /*в списки добавляем фрагмент с именем вкладки*/
    public void addFragment(Fragment fragment) {
        mFragmentList.add(fragment);
        notifyDataSetChanged();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return null;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }


    public void replaceFragment(Fragment newF, Fragment oldF){
        int index = mFragmentList.indexOf(oldF);
        mFragmentList.set(index,newF);
        notifyDataSetChanged();
    }

    public void clear(){
        mFragmentList.clear();
        notifyDataSetChanged();
    }

    public int getPosition(Fragment f){
        return mFragmentList.indexOf(f);
    }



}
