package com.example.kittenappscollage.collect.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
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
import com.example.kittenappscollage.helpers.dbPerms.WorkDBPerms;
import com.example.kittenappscollage.helpers.rx.ThreadTransformers;

import java.util.ArrayList;
import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

import static com.example.kittenappscollage.helpers.Massages.LYTE;
import static com.example.kittenappscollage.helpers.Massages.MASSAGE;

public class FragmentGalleryReviewImages extends FragmentGalleryActionStorage {

    private final String TAG_DIALOG = "FragmentGalleryReviewImages dialog act";

    private String addingFold;

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
                startScan(data.getData());
            }
        }else super.onActivityResult(requestCode, resultCode, data);

    }

    @SuppressLint("CheckResult")
    private void startScan(Uri uri){
        final int[] iterator = new int[]{0};
        addingFold = "name";
        Observable.create((ObservableOnSubscribe<HashMap<String, ArrayList<String>>>) emitter -> {
            scanFold(uri,emitter);
            emitter.onComplete();
        }).compose(new ThreadTransformers.InputOutput<>())
                .doOnComplete(() -> {
                    if(iterator[0]==0)Massages.SHOW_MASSAGE(getContext(),"В выбранной паке новых изображений не найдено");
                })
                .subscribe(stringArrayListHashMap -> {
                    iterator[0]++;
                    setListImagesInFolders(stringArrayListHashMap);
                    Massages.SHOW_MASSAGE(getContext(),"В галерею добавлена папка "+addingFold);
                });
    }

    private void scanFold(Uri uri,ObservableEmitter<HashMap<String, ArrayList<String>>> emitter){
        int takeFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION;
        getContext().getContentResolver().takePersistableUriPermission(uri, takeFlags);
        DocumentFile folder = DocumentFile.fromTreeUri(getContext(),uri);
        steps(folder,emitter);
        for (DocumentFile f:folder.listFiles()){
            if(f.isDirectory())steps(f,emitter);
        }
    }

    private void steps(DocumentFile folder,ObservableEmitter<HashMap<String, ArrayList<String>>> emitter){
        DocumentFile[]files = folder.listFiles();
        if(files.length>0) {
            for (DocumentFile f : files) {
                if (f.isDirectory()) steps(f, emitter);
//                else if(f.getType().equals("image/png") ||
//                        f.getType().equals("image/jpeg") ||
//                        f.getType().equals("image/jpg")){
//                    final String name = f.getName();
//                    final String parent = folder.getName();

                else {
                    Cursor c = getContext().getContentResolver().query(
                            question(),
                            new String[]{MediaStore.Images.Media.BUCKET_ID},
                            MediaStore.Images.Media.BUCKET_DISPLAY_NAME + " = ?",
                            new String[]{folder.getName()},
                            MediaStore.Images.Media.BUCKET_ID);

                    final int col_b_id = c.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_ID);
                    while (c.moveToNext()) {
                        final long b_id = c.getLong(col_b_id);
                        if (WorkDBPerms.get(getContext()).queryToId(b_id)) {
                            continue;
                        } else {
                            searchInId(b_id, folder.getUri().toString(), folder.getName(), emitter);
                        }
                    }
                    /*выходим из перебора файлов*/
                    break;
                }
            }
        }
    }

    private void searchInId(long id, String keyAndPerm,String name,ObservableEmitter<HashMap<String, ArrayList<String>>> emitter){
        WorkDBPerms.get(getContext()).createItem(keyAndPerm,name,id);
        Cursor cursor = getContext().getContentResolver().query(
                question(),
                new String[]{MediaStore.Images.Media._ID, MediaStore.Images.Media.DATE_MODIFIED, MediaStore.Images.Media.MIME_TYPE,MediaStore.Images.Media.BUCKET_ID},
                MediaStore.Images.Media.BUCKET_ID + " = ?",
                new String[]{Long.toString(id)},
                MediaStore.Images.Media.DATE_MODIFIED);
        if(cursor.getCount()>0){
            int iterator = 0;
            final int col_id = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
            final int col_mime = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.MIME_TYPE);
            final int col_date = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_MODIFIED);
            while (cursor.moveToNext()){
                final String type = cursor.getString(col_mime);
                if (type.equals("image/png") || type.equals("image/jpeg") || type.equals("image/jpg")) {
                    final String img = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cursor.getLong(col_id)).toString();
                    final long date = cursor.getLong(col_date);
                    addInScan(keyAndPerm,img,name,keyAndPerm,date);
                    iterator++;
                }
            }
            if(iterator>0) {
                emitter.onNext(getListImagesInFolders());
                addingFold = name;
            }
        }
    }




}
