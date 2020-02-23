package com.example.kittenappscollage.helpers;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;

import com.example.kittenappscollage.collect.fragment.FragmentScanAllImages;

import static com.example.kittenappscollage.helpers.Massages.LYTE;

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
        LYTE("ListenMedia report save path - "+uri.toString() );

        if(uri.getPathSegments().size()<=1){
            contentEmpty(uri);
            return;
        }

        if(App.checkVersion()) {
            if (fragment != null && context != null) {
                String[] projection = {
                        MediaStore.Images.Media._ID,
                        MediaStore.MediaColumns.DATA,
                        MediaStore.Images.Media.BUCKET_DISPLAY_NAME};
                Cursor cursor = context.getContentResolver().query(uri, projection, null,
                        null, null);

                int col_path, col_fold, col_id, col_doc_id;

                assert cursor != null;
                col_path = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                col_fold = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
                col_id = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);

                cursor.moveToLast();

                String path = cursor.getString(col_path).toLowerCase();
                String key = cursor.getString(col_path).split(cursor.getString(col_fold))[0] + cursor.getString(col_fold);
                String id = ""+cursor.getLong(col_id);
                LYTE("ListenMedia id- "+id);
                boolean pik = path.endsWith(".png") || path.endsWith(".jpeg") || path.endsWith("jpg");
                if (pik) {
                    /*вызывается несколько раз
                     *  по этому проверяем последний адрес*/
                    if (!lostPath.equals(path)) {
                        lostPath = path;
//                        if(lostPath.startsWith("/storage/emulated/0/"))
                        LYTE("ListenMedia report save path - " + path);
//                        context.getContentResolver().delete(uri,null,null);
//                        if(sys==FILE_SYS)fragment.setSavingInFileCollage(path, key);
//                        else if(sys==STOR_SYS)fragment.setSavingInStorageCollage(path, key);
                    }
                }
            }
        }else {
            if (!lostPath.equals(uri.toString())) {
                lostPath = uri.toString();
                LYTE("ListenMedia report save uri - " + uri.toString());
//                fragment.setSavingInStorageCollage(uri);
            }
        }

    }

    private void contentEmpty(Uri uri){
        LYTE("ListenMedia content save - null");
    }
}
