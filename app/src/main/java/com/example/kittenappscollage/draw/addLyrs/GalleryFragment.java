package com.example.kittenappscollage.draw.addLyrs;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
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

import static com.example.kittenappscollage.collect.adapters.ListenLoadFoldAdapter.ROOT_ADAPTER;

public class GalleryFragment extends FragmentGalleryReviewImages {

    private final String KEY_ADAPTER = "GalleryFragment key adapter";

    private final String KEY_POSITION = "GalleryFragment key position";

    private final String KEY_KEY = "GalleryFragment key key";

    private SelectorFrameFragments selector;

    private int width;

    private SharedPreferences pref;

    private SharedPreferences.Editor edit;

    private boolean block;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        block = false;
        pref = getActivity().getPreferences(Context.MODE_PRIVATE);
        edit = pref.edit();
//        setBlock(true);
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
        edit.putInt(KEY_ADAPTER,adapter);
        edit.putInt(KEY_POSITION,position);
        edit.putString(KEY_KEY, getKey());
        edit.apply();
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
        if(block)super.setParamsAdapter(params);
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
                    block = true;
                    setParamsAdapter(width);

                      String key = pref.getString(KEY_KEY,null);
                      int adapter = pref.getInt(KEY_ADAPTER, -1);
                      int position = pref.getInt(KEY_POSITION,0);
                      if(adapter==ROOT_ADAPTER){

                       }else {
//                          getImgAdapt().setIndexKey(adapter);
//                          getGridLayoutManager().setSpanCount(3);
//                          getFoldAdapt().activate(false);
//                          getRecycler().setAdapter(getImgAdapt().activate(true));
//                          setIndexAdapter(adapter);
//                          setKey(getFoldAdapt().getItems()[adapter].getKey());
//                          getRecycler().scrollToPosition(position);
                       }

                }
            });
        }
    }

    @Override
    public void onStop() {
        super.onStop();

    }
}
