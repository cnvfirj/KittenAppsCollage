package com.kittendevelop.kittenappscollage.collect.reviewImage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.kittendevelop.kittenappscollage.R;
import com.kittendevelop.kittenappscollage.draw.tutorial.ExcursInTutorial;
import com.kittendevelop.kittenappscollage.helpers.rx.ThreadTransformers;
import com.kittendevelop.kittenappscollage.mainTabs.SelectSweepViewPager;
import com.example.targetviewnote.TargetView;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;

public class DialogReview extends Fragment implements View.OnClickListener, TargetView.OnClickTargetViewNoleListener{

    private SelectSweepViewPager viewPager;

    private ImageView back, edit, share;

    private SelectorOperationReview selector;

    private ExcursInTutorial excurs;

    private SharedPreferences preferences;

    private final String KEY_STEP_TUTORIAL = "DialogReview key step tutorial";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.dialog_review_images,null);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle b = getArguments();
        if(b!=null) {
            viewPager = view.findViewById(R.id.dialog_review_view_pager);
            initAdapter(getChildFragmentManager(),b.getStringArrayList(DialogReviewFrame.KEY_ARR),b.getInt(DialogReviewFrame.KEY_POS));
        }
        initButtons(view);

    }

    @Override
    public void onResume() {
        super.onResume();
        preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        excurs(preferences.getInt(KEY_STEP_TUTORIAL,0));
    }

    @SuppressLint("CheckResult")
    private void initAdapter(FragmentManager fm, ArrayList<String>imgs, int pos){
        Observable.create((ObservableOnSubscribe<ReviewAdapter>) emitter -> {
            emitter.onNext(new ReviewAdapter(fm,imgs));
            emitter.onComplete();
        }).compose(new ThreadTransformers.OnlySingle<>())
                .subscribe(reviewAdapter -> {
                    viewPager.setAdapter(reviewAdapter);
                    viewPager.setCurrentItem(pos);
                    viewPager.setSweep(true);

                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.dialog_review_exit:
                pressBack();
                break;
            case R.id.dialog_review_edit:
                pressEdit();
                break;
            case R.id.dialog_review_share:
                pressShare();
                break;

        }
    }

    @Override
    public void onClickTarget(int i) {
//        if(i==TargetView.TOUCH_SOFT_KEY||i==TargetView.TOUCH_TARGET){
            if(excurs.next()) {
                getEditor().putInt(KEY_STEP_TUTORIAL, excurs.getStep()).apply();
            }else {
                getEditor().putInt(KEY_STEP_TUTORIAL, 999).apply();
            }
//        }
    }

    public int currentPosition(){
        return viewPager.getCurrentItem();
    }

    private SharedPreferences.Editor getEditor(){
        return preferences.edit();
    }

    private void excurs(int step){
        if(step<999){
            initExcurs();
            excurs
                    .targets(new Integer[]{R.id.dialog_review_share,R.id.dialog_review_edit,R.id.dialog_review_exit})
                    .titles(getContext().getResources().getStringArray(R.array.review_title))
                    .notes(getContext().getResources().getStringArray(R.array.review_note))
                    .sizeWin(new int[]{TargetView.MIDI_VEIL,TargetView.MIDI_VEIL,TargetView.MINI_VEIL})
                    .setStep(step);
            excurs.start();
        }
    }

    private void initExcurs(){
        if(getContext()!=null) {
            TargetView t = TargetView.build(this)
                    .touchExit(TargetView.NON_TOUCH)
                    .dimmingBackground(getContext().getResources().getColor(R.color.colorDimenPrimaryDarkTransparent));
            excurs = new ExcursInTutorial(t);
        }
    }

    private void pressBack(){
        selector = (SelectorOperationReview) getParentFragment();
        selector.exit();
    }

    private void pressEdit(){
        ReviewFragment f = (ReviewFragment)viewPager.getAdapter().instantiateItem(viewPager,viewPager.getCurrentItem());
        if(f.getImage()!=null&&!f.getImage().isRecycled()) {
            selector = (SelectorOperationReview) getParentFragment();
            selector.edit(f.getImage().copy(Bitmap.Config.ARGB_8888, true));
        }

    }

    private void pressShare(){
        ReviewFragment f = (ReviewFragment)viewPager.getAdapter().instantiateItem(viewPager,viewPager.getCurrentItem());
        selector = (SelectorOperationReview) getParentFragment();
       selector.share(f.getImg());
    }

    private void initButtons(View view){
        back = view.findViewById(R.id.dialog_review_exit);
        back.setOnClickListener(this);
        edit = view.findViewById(R.id.dialog_review_edit);
        edit.setOnClickListener(this);
        share = view.findViewById(R.id.dialog_review_share);
        share.setOnClickListener(this);
    }
}
