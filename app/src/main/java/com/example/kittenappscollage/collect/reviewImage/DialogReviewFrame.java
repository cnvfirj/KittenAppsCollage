package com.example.kittenappscollage.collect.reviewImage;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.kittenappscollage.MainSwitching;
import com.example.kittenappscollage.R;
import com.example.kittenappscollage.draw.addLyrs.SelectorFrameFragments;
import com.example.kittenappscollage.draw.repozitoryDraw.RepDraw;
import com.example.kittenappscollage.helpers.Massages;

import java.util.ArrayList;


public class DialogReviewFrame extends DialogFragment implements SelectorOperationReview ,SelectorFrameFragments{

    public static final String KEY_ARR = "key arr";

    public static final String KEY_POS = "key pos";

    public static final int DIALOG_ACTION = -112;

    private FragmentManager manager;

    private DialogReview review;

    private DialogReviewEdit edit;

    private int position;

    public static DialogReviewFrame inst(ArrayList<String> imgs, int position, Fragment target){
        DialogReviewFrame d = new DialogReviewFrame();
        Bundle b = new Bundle();
        b.putStringArrayList(KEY_ARR,imgs);
        b.putInt(KEY_POS,position);
        d.setArguments(b);
        d.setTargetFragment(target,DIALOG_ACTION);
        return d;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_add_frame,null);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle b = getArguments();
        if(b!=null) {
            edit = new DialogReviewEdit();
           review = new DialogReview();
           review.setArguments(b);
           manager = getChildFragmentManager();
           manager.beginTransaction().add(R.id.dialog_add_frame,review).commit();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        Window window = getDialog().getWindow();
        Rect rect = new Rect();
        getActivity().getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        window.setLayout((int) (rect.right*0.9), (int)(rect.bottom*0.7));
        window.setGravity(Gravity.CENTER);
    }

    @Override
    public void edit(Bitmap bitmap) {
        position = review.currentPosition();
        edit.setBitmap(bitmap);
        manager.beginTransaction().replace(R.id.dialog_add_frame,edit).commit();

    }

    @Override
    public void share(Uri img) {
        shareSingleImg(img);
    }

    @Override
    public void exit() {
        dismiss();
    }


    @Override
    public void backInAddLyr(View v, Object way) {

    }

    @Override
    public void backInSelectedLyr() {
        Bundle b = getArguments();
        b.putInt(KEY_POS,position);
           review.setArguments(b);
            manager.beginTransaction().replace(R.id.dialog_add_frame,review).commit();
    }

    @Override
    public void exitAll() {
       dismiss();
    }

    @Override
    public void doneLyr(Bitmap b) {
        RepDraw.get().setImg(b,null,true);
        MainSwitching ms = (MainSwitching)getContext();
        ms.stepToEdit();
        dismiss();
    }

    private void shareSingleImg(Uri uri){
        try {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.putExtra(Intent.EXTRA_STREAM, uri);
                intent.setType("image/*");
            } else {
                intent.setDataAndType(uri, "image/*");
            }
            startActivity(Intent.createChooser(intent, null));
        } catch (ActivityNotFoundException anfe) {
            Massages.LYTE("FragmentGalleryActionFile что то с активити "+anfe.getMessage());
        }
    }
}
