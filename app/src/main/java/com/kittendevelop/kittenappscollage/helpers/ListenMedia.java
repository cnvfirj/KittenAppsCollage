package com.kittendevelop.kittenappscollage.helpers;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;

import com.kittendevelop.kittenappscollage.collect.fragment.FragmentScanAllImages;

import static com.kittendevelop.kittenappscollage.helpers.Massages.LYTE;

public class ListenMedia extends ContentObserver {

    public static final int FILE_SYS = 0;

    public static final int STOR_SYS = 1;

    private int sys;

    private FragmentScanAllImages fragment;

    private Context context;

    private String lostPath;

    public ListenMedia(Handler handler) {
        super(handler);
        lostPath = "(_!_)";
    }

    public ListenMedia setFragment(FragmentScanAllImages f){
        fragment = f;
        return this;
    }

    public ListenMedia setContext(Context c){
        context = c;
        return this;
    }

    public ListenMedia setSys(int index){
        sys = index;
        return this;
    }



    @Override
    public void onChange(boolean selfChange, Uri uri) {
        super.onChange(selfChange, uri);
        LYTE("ListenMedia report  - "+uri.toString() );
        Cursor cursor = context.getContentResolver().query(uri,new String[]{MediaStore.Images.Media.BUCKET_ID,MediaStore.Images.Media.DATA},null,null,null);
        cursor.moveToFirst();

        String name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
//        long id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_ID));
        LYTE("ListenMedia report "+name);
        cursor.close();
        fragment.setSavingInMedia(uri);

    }

    private void contentEmpty(Uri uri){
        LYTE("ListenMedia content save - null");
    }
}
