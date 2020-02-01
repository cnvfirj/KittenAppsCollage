package com.example.kittenappscollage.helpers;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.MediaStore;

import com.example.kittenappscollage.collect.fragment.FragmentScanAllImages;

import static com.example.kittenappscollage.helpers.Massages.LYTE;

public class ListenMedia extends ContentObserver {

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

    @Override
    public void onChange(boolean selfChange, Uri uri) {
        super.onChange(selfChange, uri);

        if(uri.getPathSegments().size()<=1){
            contentEmpty(uri);
            return;
        }

        if(fragment!=null&&context!=null){
            String[] projection = {

                    MediaStore.MediaColumns.DATA,
                    MediaStore.Images.Media.BUCKET_DISPLAY_NAME};
            Cursor cursor = context.getContentResolver().query(uri, projection, null,
                    null, null);

            int col_path, col_fold;

            assert cursor != null;
            col_path = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            col_fold = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);

            cursor.moveToLast();

            String path = cursor.getString(col_path).toLowerCase();
            String key = cursor.getString(col_path).split(cursor.getString(col_fold))[0]+cursor.getString(col_fold);

            LYTE("ListenMedia path save - "+path);

            boolean pik = path.endsWith(".png")||path.endsWith(".jpeg")||path.endsWith("jpg");
            if(pik){
                /*вызывается несколько раз
                *  по этому проверяем последний адрес*/
                if(!lostPath.equals(path)){
                    lostPath = path;
                    LYTE("ListenMedia report save - "+path);
                    fragment.setSavingCollage(path,key);
                }
            }
        }

    }

    private void contentEmpty(Uri uri){

    }
}
