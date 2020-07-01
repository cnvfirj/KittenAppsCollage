package com.kittendevelop.kittenappscollage.draw.addLyrs;

import android.animation.Animator;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.kittendevelop.kittenappscollage.R;
import com.kittendevelop.kittenappscollage.collect.fragment.FragmentGalleryReviewImages;
import com.kittendevelop.kittenappscollage.draw.tutorial.ExcursInTutorial;
import com.example.targetviewnote.TargetView;

import static com.kittendevelop.kittenappscollage.collect.adapters.ListenLoadFoldAdapter.ROOT_ADAPTER;


public class GalleryFragment extends FragmentGalleryReviewImages implements TargetView.OnClickTargetViewNoleListener {

    private final String KEY_STEP_TUTORIAL = "GalleryFragment key step tutorial_2";

    private SelectorFrameFragments selector;

    private int width;

    private boolean blockParams;

    private ExcursInTutorial excurs;

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
        (view.findViewById(R.id.gallery_main_menu)).setVisibility(View.INVISIBLE);
        if(getContext()!=null)excurs(pref().getInt(KEY_STEP_TUTORIAL,0));
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
        if(getIndexAdapter()==ROOT_ADAPTER){
            getFoldAdapt().setModeSelected(false);
        }else {
            getImgAdapt().setModeSelected(false);
        }
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

    private void excurs(int step){
        if(step<999){
            initExcurs();
            if (!excurs.getOngoing()) {
                excurs
                        .targets(new Integer[]{getAddFolders().getId()})
                        .titles(getContext().getResources().getStringArray(R.array.addlyr_collect_title))
                        .notes(getContext().getResources().getStringArray(R.array.addlyr_collect_note))
                        .sizeWin(new int[]{TargetView.MIDI_VEIL})
                        .iconsTitle(new int[]{R.drawable.icon_collect})
                        .iconsSoftKey(new int[]{R.drawable.ic_icon_exit})
                        .setStep(step)
                        .ongoing(true);
                excurs.start();
            }
        }
    }

    private SharedPreferences pref(){
        return getActivity().getPreferences(Context.MODE_PRIVATE);
    }

    private SharedPreferences.Editor edit(){
        return pref().edit();
    }

    private void initExcurs(){
        if(getContext()!=null) {
            TargetView t = TargetView.build(this)
                    .touchExit(TargetView.NON_TOUCH)
                    .dimmingBackground(getContext().getResources().getColor(R.color.colorDimenPrimaryDarkTransparent));
            excurs = new ExcursInTutorial(t);

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

    @Override
    public void onClickTarget(int i) {
//        if(i==TargetView.TOUCH_SOFT_KEY||i==TargetView.TOUCH_TARGET){
            if(excurs.next()) {
                edit().putInt(KEY_STEP_TUTORIAL, excurs.getStep()).apply();
            }else {
                edit().putInt(KEY_STEP_TUTORIAL, 999).apply();
            }
//        }
    }
}
