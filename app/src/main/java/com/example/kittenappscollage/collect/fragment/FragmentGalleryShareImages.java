package com.example.kittenappscollage.collect.fragment;


import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.documentfile.provider.DocumentFile;

import com.example.kittenappscollage.helpers.Massages;
import com.example.kittenappscollage.helpers.rx.ThreadTransformers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

import static android.provider.MediaStore.VOLUME_EXTERNAL;
import static com.example.kittenappscollage.collect.adapters.ListenLoadFoldAdapter.ROOT_ADAPTER;
import static com.example.kittenappscollage.helpers.Massages.LYTE;

public class FragmentGalleryShareImages extends FragmentGalleryAction {

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

    private void shareSingleImg(Uri uri, String path){
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        String ext =path.substring(path.lastIndexOf(".") + 1);
        String type = mime.getMimeTypeFromExtension(ext);
        try {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.putExtra(Intent.EXTRA_STREAM, uri);
                intent.setType(type);
            } else {
                intent.setDataAndType(uri, type);
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
            File file = new File(s);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

                selected.add(FileProvider.getUriForFile(getContext(), "com.example.kittenappscollage.fileprovider", file));
            }else {
                selected.add(Uri.fromFile(file));
            }
        }

        if(selected.size()>0){
            if(selected.size()==1){
                shareSingleImg(selected.get(0), getSelectFiles().get(0));
            }else {
                shareSelectedImgs(selected);
            }
        }
        getSelectFiles().clear();

    }

}
