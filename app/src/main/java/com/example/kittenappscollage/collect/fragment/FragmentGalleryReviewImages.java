package com.example.kittenappscollage.collect.fragment;

import android.content.Intent;
import android.widget.ImageView;

import com.example.kittenappscollage.collect.reviewImage.DialogReview;
import com.example.kittenappscollage.collect.reviewImage.DialogReviewFrame;
import com.example.kittenappscollage.helpers.App;
import com.example.kittenappscollage.helpers.Massages;

import java.util.ArrayList;

import static com.example.kittenappscollage.helpers.Massages.LYTE;

public class FragmentGalleryReviewImages extends FragmentGalleryActionStorage {

    private final String TAG_DIALOG = "FragmentGalleryReviewImages dialog act";

    @Override
    protected void clickItem(int adapter, int position) {
        super.clickItem(adapter, position);
        DialogReviewFrame.inst(getImgAdapt().getOperationList(),position,this)
                .show(getFragmentManager().beginTransaction(),TAG_DIALOG);
    }

    @Override
    protected void clickAddFolder(ImageView v) {
        super.clickAddFolder(v);
//        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);

        intent.addCategory(Intent.CATEGORY_OPENABLE);

        intent.setType("image/*");

        startActivityForResult(intent, 12);
    }
}
