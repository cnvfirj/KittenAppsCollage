package com.example.kittenappscollage.collect;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ExLayoutManager extends GridLayoutManager {

    public ExLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public ExLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
    }

    public ExLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        try {
            super.onLayoutChildren(recycler, state);
        } catch (IndexOutOfBoundsException e) {
            Log.e("TAG ExLayoutManager", "meet a IOOBE in RecyclerView");
        }
    }

    @Override
    public boolean supportsPredictiveItemAnimations() {
        return false;
    }
}
