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

public class ReviewFragment extends Fragment {

    private static final String IMG = "img";

    private ViewReview viewReview;

    private Bitmap bitmap;

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
            Uri uri = Uri.parse(b.getString(IMG));
            try {
                bitmap = MediaStore.Images.Media.getBitmap (getContext().getContentResolver (), uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return inflater.inflate(R.layout.fragment_review,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewReview = view.findViewById(R.id.fragment_review_view);
        viewReview.setBitmap(bitmap);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(bitmap!=null&&!bitmap.isRecycled())bitmap.recycle();
    }
}
