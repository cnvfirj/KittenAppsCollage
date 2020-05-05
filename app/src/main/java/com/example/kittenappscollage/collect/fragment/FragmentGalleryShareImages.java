package com.example.kittenappscollage.collect.fragment;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import com.example.kittenappscollage.helpers.Massages;
import java.util.ArrayList;

public abstract class FragmentGalleryShareImages extends FragmentGalleryAction {

    private void shareSelectedImgs(ArrayList<Uri>uris){
        try {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND_MULTIPLE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
                intent.setType("image/*");
            }else {
                intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
                intent.setType("image/*");
            }

            startActivity(Intent.createChooser(intent, null));
        }catch (ActivityNotFoundException anfe){
            Massages.LYTE("FragmentGalleryActionFile что то с активити "+anfe.getMessage());
        }

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


    @Override
    protected void shareSelImages(){
        super.shareSelImages();
        ArrayList<Uri>selected = new ArrayList<>();
        for (String s:getSelectFiles()){
                selected.add(Uri.parse(s));
        }

        if(selected.size()>0){
            if(selected.size()==1){
                shareSingleImg(selected.get(0));
            }else {
                shareSelectedImgs(selected);
            }
        }
        getSelectFiles().clear();

    }

    @Override
    protected void shareFolder() {
        super.shareFolder();
        ArrayList<Uri>selected = new ArrayList<>();
        for (String s:getListImagesInFolders().get(getKey())){
            selected.add(Uri.parse(s));
        }

        if(selected.size()>0){
            if(selected.size()==1){
                shareSingleImg(selected.get(0));
            }else {
                shareSelectedImgs(selected);
            }
        }
        getSelectFiles().clear();
    }
}
