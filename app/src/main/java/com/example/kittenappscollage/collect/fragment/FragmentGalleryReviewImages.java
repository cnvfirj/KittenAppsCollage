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
import com.example.kittenappscollage.helpers.rx.ThreadTransformers;

import java.util.ArrayList;
import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;

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
                startScan(data.getData());
            }
        }else super.onActivityResult(requestCode, resultCode, data);

    }

    @SuppressLint("CheckResult")
    private void startScan(Uri uri){
        Observable.create((ObservableOnSubscribe<HashMap<String, ArrayList<String>>>) emitter -> {
            scanFold(uri,emitter);
            emitter.onComplete();
        }).compose(new ThreadTransformers.InputOutput<>())
                .subscribe(stringArrayListHashMap -> {
                    setListImagesInFolders(stringArrayListHashMap);

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
        if(files.length>0){
            Cursor c = getContext().getContentResolver().query(files[0].getUri(),new String[]{MediaStore.Images.Media.BUCKET_ID},null,null,null);
            c.moveToFirst();
            final String id = ""+c.getLong(c.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_ID));
            searchInId(id,folder.getUri().toString(),folder.getName(),emitter);
        }
        else notImages();
    }

    private void searchInId(String id, String keyAndPerm,String name,ObservableEmitter<HashMap<String, ArrayList<String>>> emitter){
        Cursor cursor = getContext().getContentResolver().query(
                question(),
                new String[]{MediaStore.Images.Media._ID, MediaStore.Images.Media.DATE_MODIFIED, MediaStore.Images.Media.MIME_TYPE},
                MediaStore.Images.Media.BUCKET_ID + " = ?",
                new String[]{id},
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
            if(iterator==0)notImages();
            else {
                emitter.onNext(getListImagesInFolders());
                addFold(name);
            }
        }
        else notImages();
    }


    @SuppressLint("CheckResult")
    private void notImages(){
        Observable.create(emitter -> emitter.onComplete()).compose(new ThreadTransformers.InputOutput<>())
                .subscribe(o -> Massages.SHOW_MASSAGE(getContext(),"В выбранной папке поддерживаемых изображений не обнаружено"));

    }

    @SuppressLint("CheckResult")
    private void addFold(String name){
        Observable.create(emitter -> emitter.onComplete()).compose(new ThreadTransformers.InputOutput<>())
                .subscribe(o -> Massages.SHOW_MASSAGE(getContext(),"В галерею добавлена папка - "+name));

    }


}
