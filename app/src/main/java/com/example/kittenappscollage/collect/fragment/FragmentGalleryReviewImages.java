package com.example.kittenappscollage.collect.fragment;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.documentfile.provider.DocumentFile;

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
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        startActivityForResult(intent, 49);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==49){
            if(resultCode== Activity.RESULT_OK){
                scanFold(data.getData());
            }
        }else super.onActivityResult(requestCode, resultCode, data);

    }

    private void scanFold(Uri uri){
        int takeFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION;
        getContext().getContentResolver().takePersistableUriPermission(uri, takeFlags);
        DocumentFile f = DocumentFile.fromTreeUri(getContext(),uri);
        Cursor cursor = getActivity().getContentResolver()
                .query(f.getUri(),
                        new String[]{MediaStore.Images.Media.DISPLAY_NAME,MediaStore.Images.Media._ID},
                        null, null, null);
        cursor.moveToFirst();
        LYTE("name - "+cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)));
        LYTE("id - "+cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)));
    }
}
