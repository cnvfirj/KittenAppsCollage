package com.example.kittenappscollage.collect.reviewImage;

import android.annotation.SuppressLint;
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

import com.example.kittenappscollage.R;
import com.example.kittenappscollage.helpers.rx.ThreadTransformers;
import com.example.kittenappscollage.mainTabs.SelectSweepViewPager;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;

import static com.example.kittenappscollage.collect.reviewImage.DialogReviewFrame.KEY_ARR;
import static com.example.kittenappscollage.collect.reviewImage.DialogReviewFrame.KEY_POS;

public class DialogReview extends Fragment implements View.OnClickListener {

    private SelectSweepViewPager viewPager;

    private ImageView back, edit, share;

    private SelectorOperationReview selector;

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
            initAdapter(getChildFragmentManager(),b.getStringArrayList(KEY_ARR),b.getInt(KEY_POS));
        }
        initButtons(view);

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

    public int currentPosition(){
        return viewPager.getCurrentItem();
    }

    private void pressBack(){
        selector = (SelectorOperationReview) getParentFragment();
        selector.exit();
    }

    private void pressEdit(){
        ReviewFragment f = (ReviewFragment)viewPager.getAdapter().instantiateItem(viewPager,viewPager.getCurrentItem());
        selector = (SelectorOperationReview) getParentFragment();
       selector.edit(f.getImage().copy(Bitmap.Config.ARGB_8888,true));

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
