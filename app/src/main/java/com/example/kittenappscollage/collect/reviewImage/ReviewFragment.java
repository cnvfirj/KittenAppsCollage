package com.example.kittenappscollage.collect.reviewImage;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.kittenappscollage.R;

import java.io.IOException;

import static com.example.kittenappscollage.helpers.Massages.LYTE;

public class ReviewFragment extends Fragment {

    private static final String IMG = "img";

    private ViewReview viewReview;

    private Bitmap bitmap;

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
        viewReview = view.findViewById(R.id.fragment_review_view);
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

    public Bitmap getImage(){
        return viewReview.getBitmap();
    }

    public Uri getImg(){
        return img;
    }
}
