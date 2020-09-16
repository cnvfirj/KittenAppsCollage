package com.kittendevelop.kittenappscollage.collect.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
//import android.database.Cursor;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.documentfile.provider.DocumentFile;

import com.kittendevelop.kittenappscollage.R;
import com.kittendevelop.kittenappscollage.draw.fragment.SavedKollagesFragmentDraw;
import com.kittendevelop.kittenappscollage.helpers.Massages;
import com.kittendevelop.kittenappscollage.helpers.dbPerms.WorkDBPerms;
import com.kittendevelop.kittenappscollage.helpers.rx.ThreadTransformers;
import com.kittendevelop.kittenappscollage.menu.DialogMainMenu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

public abstract class FragmentGalleryAddFolder extends FragmentGalleryReviewImages {

    private String addingFold;

    private final String TAG_DIALOG_MENU = "FragmentGalleryAddFolder dialog main menu";

    @Override
    protected void clickAddFolder(ImageView v) {
        super.clickAddFolder(v);
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        startActivityForResult(intent, 49);
    }

    @Override
    protected void clickMenu(ImageView v) {
        super.clickMenu(v);
        DialogMainMenu.get().show(getFragmentManager(),TAG_DIALOG_MENU);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==49){
            if(resultCode== Activity.RESULT_OK){
                startScan(data.getData(),49);
            }
        }else super.onActivityResult(requestCode, resultCode, data);

    }

//    @Override
    public void setSavingInStorageCollage(Uri uri, String report, String delimiter, long date) {
//        super.setSavingInStorageCollage(uri, report, delimiter, date);
//        startScan(Uri.parse(report.split(delimiter)[SavedKollagesFragmentDraw.INDEX_URI_DF_FOLD]),499);
        reportSave(report,delimiter,date);
    }

    @SuppressLint("CheckResult")
    private void startScan(Uri uri,int request){
        getAddFolders().setEnabled(false);
        final int[] iterator = new int[]{0};
        addingFold = "name";
        Observable.create((ObservableOnSubscribe<HashMap<String, ArrayList<String>>>) emitter -> {
            scanFold(uri,emitter);
            emitter.onComplete();
        }).compose(new ThreadTransformers.InputOutput<>())
                .doOnComplete(() -> {
                    getAddFolders().setEnabled(true);
                    if(iterator[0]==0&&request==49) Massages.SHOW_MASSAGE(getContext(),getContext().getResources().getString(R.string.IS_SELECTED_FOLDER_IMAGES_NOT));
                })
                .subscribe(stringArrayListHashMap -> {
                    iterator[0]++;
                    setListImagesInFolders(stringArrayListHashMap);
                    Massages.SHOW_MASSAGE(getContext(),getContext().getResources().getString(R.string.IN_GALLERY_ADD_FOLDER)+" "+addingFold);
                });
    }

    @SuppressLint("CheckResult")
    private void reportSave(String report, String delimiter,long date){
        final int[] iterator = new int[]{0};
        addingFold = "name";
        Observable.create(new ObservableOnSubscribe<HashMap<String, ArrayList<String>>>() {
            @Override
            public void subscribe(ObservableEmitter<HashMap<String, ArrayList<String>>> emitter) throws Exception {
                scanFold(report,delimiter,date,emitter);
                emitter.onComplete();
            }
        }).compose(new ThreadTransformers.InputOutput<>())
                .doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {
                        if(iterator[0]==0){
                            startScan(Uri.parse(report.split(delimiter)[SavedKollagesFragmentDraw.INDEX_URI_DF_FOLD]),499);
                        }else {
                            setListImagesInFolders(getListImagesInFolders());
                        }
                    }
                })
        .subscribe(new Consumer<HashMap<String, ArrayList<String>>>() {
            @Override
            public void accept(HashMap<String, ArrayList<String>> stringArrayListHashMap) throws Exception {
                iterator[0]++;
            }
        });
    }

//    private void changeLocale(Locale locale) {
////        this.locale = locale.getCountry();
//        String l = getContext().getResources().getConfiguration().locale.getCountry();
//        if(l.equals("EN"))changeLocale(new Locale("RU"));
//        else changeLocale(new Locale("EN"));
//
//
//        Locale.setDefault(locale);
//        Configuration configuration = new Configuration();
//        configuration.setLocale(locale);
//        getContext().getResources()
//                .updateConfiguration(configuration,
//                        getContext()
//                                .getResources()
//                                .getDisplayMetrics());
//    }


    private void scanFold(String report, String delimiter, long date,ObservableEmitter<HashMap<String, ArrayList<String>>> emitter){
        String[]split = report.split(delimiter);
        if(!getListImagesInFolders().containsKey(split[SavedKollagesFragmentDraw.INDEX_URI_DF_FOLD])){
            emitter.onComplete();
        }else {
           addImage(report,delimiter,date);
           emitter.onNext(getListImagesInFolders());
        }
    }

    private void addImage(String report, String delimiter,long date){
        String[]split = report.split(delimiter);//        /*здесь выясняем айди папки и потом закидываем его в бд*/
        String nameImg = split[SavedKollagesFragmentDraw.INDEX_NAME_IMG];
        String key = split[SavedKollagesFragmentDraw.INDEX_URI_PERM_FOLD];
        Cursor c = null;
        try {
            c = getContext().getContentResolver().query(
                    question(),
                    new String[]{MediaStore.Images.Media.BUCKET_ID, MediaStore.Images.Media._ID},
                    MediaStore.Images.Media.DISPLAY_NAME + " = ?",
                    new String[]{nameImg},
                    null);

            c.moveToFirst();
            WorkDBPerms.get(getContext()).addId(key, c.getLong(c.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_ID)));
            final String img = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, c.getLong(c.getColumnIndexOrThrow(MediaStore.Images.Media._ID))).toString();
            addImgCollect(
                    split[SavedKollagesFragmentDraw.INDEX_URI_DF_FOLD],
                    img,
                    split[SavedKollagesFragmentDraw.INDEX_NAME_FOLD],
                    split[SavedKollagesFragmentDraw.INDEX_URI_DF_FOLD],
                    date);
//            setListImagesInFolders(getListImagesInFolders());
        }finally {
            if(c!=null)c.close();
        }
    }

    private void scanFold(Uri uri, ObservableEmitter<HashMap<String, ArrayList<String>>> emitter){
//        int takeFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION;
//        getContext().getContentResolver().takePersistableUriPermission(uri, takeFlags);
        DocumentFile folder = DocumentFile.fromTreeUri(getContext(),uri);

        steps(folder,emitter);
        /*сканируем подпапки
        * убрал из-за неразберизи с разрешениями*/
//        for (DocumentFile f:folder.listFiles()){
//            if(f.isDirectory())steps(f,emitter);
//        }
    }

    private void steps(DocumentFile folder,ObservableEmitter<HashMap<String, ArrayList<String>>> emitter){
        DocumentFile[]files = folder.listFiles();
        if(files.length>0) {
            for (DocumentFile f : files) {
                if (f.isDirectory()) steps(f, emitter);
                else {
                    Cursor c = null;
                    try {
                        c = getContext().getContentResolver().query(
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
                    }finally {
                        if(c!=null)c.close();
                    }

                    break;
                }
            }
        }
    }

    private void searchInId(long id, String keyAndPerm,String name,ObservableEmitter<HashMap<String, ArrayList<String>>> emitter){
        WorkDBPerms.get(getContext()).createItem(keyAndPerm,name,id);
        Cursor cursor = null;
        try {
        cursor = getContext().getContentResolver().query(
                question(),
                new String[]{MediaStore.Images.Media._ID, MediaStore.Images.Media.DATE_MODIFIED, MediaStore.Images.Media.MIME_TYPE,MediaStore.Images.Media.BUCKET_ID},
                MediaStore.Images.Media.BUCKET_ID + " = ?",
                new String[]{Long.toString(id)},
                MediaStore.Images.Media.DATE_MODIFIED);
        if(cursor.getCount()>0) {
            int iterator = 0;
            final int col_id = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
            final int col_mime = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.MIME_TYPE);
            final int col_date = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_MODIFIED);
            while (cursor.moveToNext()) {
                final String type = cursor.getString(col_mime);
                if (type.equals("image/png") || type.equals("image/jpeg") || type.equals("image/jpg")) {
                    final String img = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cursor.getLong(col_id)).toString();
                    final long date = cursor.getLong(col_date);
                    addInScan(keyAndPerm, img, name, keyAndPerm, date);
                    iterator++;
                }
            }
            if(iterator>0) {
                emitter.onNext(getListImagesInFolders());
                addingFold = name;
            }
        }
        }finally {
          if(cursor!=null)cursor.close();
        }
    }
}
