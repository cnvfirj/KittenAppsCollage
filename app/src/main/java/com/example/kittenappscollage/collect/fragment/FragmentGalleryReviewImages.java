package com.example.kittenappscollage.collect.fragment;


import com.example.kittenappscollage.collect.reviewImage.DialogReviewFrame;
import com.example.kittenappscollage.helpers.dbPerms.WorkDBPerms;

public class FragmentGalleryReviewImages extends FragmentGalleryActionStorage {

    private final String TAG_DIALOG = "FragmentGalleryReviewImages dialog act";

    @Override
    protected void clickItem(int adapter, int position) {
        super.clickItem(adapter, position);
        DialogReviewFrame.inst(getImgAdapt().getOperationList(),position,this)
                .show(getFragmentManager().beginTransaction(),TAG_DIALOG);
    }

    @Override
    protected void invisFolder() {
        super.invisFolder();
        WorkDBPerms.get(getContext()).setAction(WorkDBPerms.DELETE,getKey());
        clearLists(getKey());
        setListImagesInFolders(getListImagesInFolders());
    }
}
