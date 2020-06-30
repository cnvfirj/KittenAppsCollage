package com.kittendevelop.kittenappscollage.collect.reviewImage;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.kittendevelop.kittenappscollage.R;


public class ReviewFragment extends Fragment implements ViewReview.LoadImage{

    private static final String IMG = "img";

    private ViewReview viewReview;

    private ProgressBar progress;

    private Uri  img;

    public static ReviewFragment inst(String img){
        ReviewFragment f = new ReviewFragment();
        Bundle b = new Bundle();
        b.putString(IMG,img);
        f.setArguments(b);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle b = getArguments();
        if(b!=null){
            img = Uri.parse(b.getString(IMG));
        }
        return inflater.inflate(R.layout.fragment_review,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progress = view.findViewById(R.id.fragment_review_progress);
        viewReview = view.findViewById(R.id.fragment_review_view);
        viewReview.setListenLoadImage(this);
        viewReview.setUriBitmap(img);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        viewReview.resetImg();
    }

    @Override
    public void done(boolean d) {
//        getView().findViewById(R.id.fragment_review_progress).setVisibility(View.INVISIBLE);
        progress.setVisibility(View.INVISIBLE);
        if(!d){
            getView().findViewById(R.id.fragment_review_error).setVisibility(View.VISIBLE);
        }
    }

    public Bitmap getImage(){
        return viewReview.getBitmap();
    }

    public Uri getImg(){
        return img;
    }
}
