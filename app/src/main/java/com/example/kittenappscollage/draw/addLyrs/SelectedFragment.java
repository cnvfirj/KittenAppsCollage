package com.example.kittenappscollage.draw.addLyrs;

import android.view.View;
import android.view.ViewTreeObserver;

import androidx.fragment.app.Fragment;

public abstract class SelectedFragment extends Fragment implements View.OnClickListener {

    protected SelectorFrameFragments selector;





    protected void paramView(final View view){
        ViewTreeObserver viewTreeObserver = view.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    readinessView(view);

                }
            });
        }
    }

    protected abstract void readinessView(View v);



}
