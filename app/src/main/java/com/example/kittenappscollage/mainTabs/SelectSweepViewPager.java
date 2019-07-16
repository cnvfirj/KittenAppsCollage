package com.example.kittenappscollage.mainTabs;

import android.content.Context;

import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

public class SelectSweepViewPager extends ViewPager {

    private boolean sweep;

    public SelectSweepViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (this.sweep) {
            return super.onTouchEvent(event);
        }

        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (this.sweep) {
            return super.onInterceptTouchEvent(event);
        }

        return false;
    }

    public void setSweep(boolean sweep) {
        this.sweep = sweep;
    }
}
