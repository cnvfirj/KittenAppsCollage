package com.example.kittenappscollage.collect.fragment;

import com.example.kittenappscollage.collect.reviewImage.DialogReview;
import com.example.kittenappscollage.helpers.App;
import com.example.kittenappscollage.helpers.Massages;

import java.util.ArrayList;

import static com.example.kittenappscollage.helpers.Massages.LYTE;

public class FragmentGalleryReviewImages extends FragmentGalleryActionStorage {

    private final String TAG_DIALOG = "FragmentGalleryReviewImages dialog act";

    @Override
    protected void clickItem(int adapter, int position) {
        super.clickItem(adapter, position);
        LYTE("FragmentGalleryReviewImages click item - "+position+", adapter "+getListFolds().get((key(adapter))));

        DialogReview.inst(getImgAdapt().getOperationList(),position,this)
                .show(getFragmentManager().beginTransaction(),TAG_DIALOG);

    }
}
