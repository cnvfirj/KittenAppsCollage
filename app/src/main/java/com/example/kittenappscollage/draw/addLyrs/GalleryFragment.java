package com.example.kittenappscollage.draw.addLyrs;

import android.animation.Animator;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.kittenappscollage.R;
import com.example.kittenappscollage.collect.fragment.FragmentGalleryReviewImages;

public class GalleryFragment extends FragmentGalleryReviewImages {

    private SelectorFrameFragments selector;

    private int width;

    private boolean blockParams;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setRetainInstance(true);
        blockParams = false;
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        awaitingView(view);
        super.onViewCreated(view, savedInstanceState);
        getAddFolders().setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_close,null));
    }


    @Override
    protected void clickItem(int adapter, int position) {
        String way = getImgAdapt().getOperationList().get(position);
        selector = (SelectorFrameFragments) getParentFragment();
        selector.backInAddLyr(null,way);

    }

    @Override
    protected void visibleMenu() {
        /*нам не надо меню*/
    }

    @Override
    protected void slideAddFold(boolean s) {
        /*эта кнопка нужна видимой*/
    }

    @Override
    public void longClick(int adapter, ImageView img, ImageView check, int pos) {
        /*выделение не нужно*/
    }

    @Override
    protected void setParamsAdapter(int params) {
        if(blockParams)super.setParamsAdapter(params);
    }

    @Override
    protected void clickAddFolder(ImageView v) {
        selector = (SelectorFrameFragments) getParentFragment();
        selector.exitAll();
    }

    private void awaitingView(final View view){
        ViewTreeObserver viewTreeObserver = view.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    width = view.getWidth();
                    blockParams = true;
                    setParamsAdapter(width);

                }
            });
        }
    }

    @Override
    public void onAnimationStart(Animator animation) {

    }

    @Override
    public void onAnimationEnd(Animator animation) {

    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }
}
