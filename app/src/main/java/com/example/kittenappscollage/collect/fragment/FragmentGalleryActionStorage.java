package com.example.kittenappscollage.collect.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;
import androidx.documentfile.provider.DocumentFile;

import com.example.kittenappscollage.helpers.Massages;
import com.example.kittenappscollage.helpers.rx.ThreadTransformers;

import java.io.File;
import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableTransformer;
import io.reactivex.Observer;
import io.reactivex.functions.Consumer;

import static com.example.kittenappscollage.collect.adapters.ListenLoadFoldAdapter.ROOT_ADAPTER;
import static com.example.kittenappscollage.helpers.Massages.LYTE;

public class FragmentGalleryActionStorage extends FragmentGalleryActionFile {

    private final String TYPE_PNG = "image/png";

    private final String TYPE_JPEG = "image/jpeg";

    private final String TYPE_JPG = "image/jpg";

    private boolean editAdapter;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onResume() {
        super.onResume();
//        if(editAdapter)getImgAdapt().setIndexKey(0);
    }

    @Override
    protected void clickExit(ImageView v) {
        super.clickExit(v);
//        if(getIndexAdapter()==ROOT_ADAPTER)editAdapter = false;
    }

    @Override
    public boolean onBackPressed(int index) {
//        if(getIndexAdapter()!=ROOT_ADAPTER) {
//            if (!getImgAdapt().isModeSelected())editAdapter = false;
//        }
        return super.onBackPressed(index);
    }

    @Override
    protected void renameFoldStorage(String name) {
        super.renameFoldStorage(name);
        Uri treeUri = Uri.parse(getListPerms().get(getKey()));
        int takeFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION;
        getContext().getContentResolver().takePersistableUriPermission(treeUri, takeFlags);

        DocumentFile folder = DocumentFile.fromTreeUri(getContext(),treeUri);

    }

    @Override
    protected void applyDeleteSelectedStorage() {
        super.applyDeleteSelectedStorage();

        ArrayList<String> imgs = getListImagesInFolders().get(getKey());
        int all = imgs.size();

        if(getSelectFiles().size()<all){
            delSelectSteps(getKey(),(ArrayList<String>)getSelectFiles().clone());

        }else if(getSelectFiles().size()==all){
            deleteFoldStorage(getKey());

            setIndexAdapter(ROOT_ADAPTER);
            getGridLayoutManager().setSpanCount(2);
            getRecycler().setAdapter(getFoldAdapt());
        }
        getSelectFiles().clear();







    }

    @Override
    protected void deleteFoldStorage(String fold) {
        super.deleteFoldStorage(fold);
        threadDelFold(fold);
        clearLists(fold);
        setListImagesInFolders(getListImagesInFolders());
    }

    @SuppressLint("CheckResult")
    private void threadDelFold(String fold){
        Uri treeUri = Uri.parse(getListPerms().get(fold));
        DocumentFile folder = DocumentFile.fromTreeUri(getContext(),treeUri);
        Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> emitter) throws Exception {
                emitter.onNext(delFold(folder));
                emitter.onComplete();
            }
        }).compose(new ThreadTransformers.InputOutput<>())
                .onErrorResumeNext(new Observable<Boolean>() {
                    @Override
                    protected void subscribeActual(Observer<? super Boolean> observer) {
                        Massages.SHOW_MASSAGE(getContext(), "Не удалось удалить папку");
                    }
                })
                .subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                if(aBoolean) {
                    if (folder.delete()) {
                        Massages.SHOW_MASSAGE(getContext(), "Эта папка была удалена");
                    } else Massages.SHOW_MASSAGE(getContext(), "Не удалось удалить папку");
                }else {
                    Massages.SHOW_MASSAGE(getContext(), "Поддерживаемые изображения из этой папки удалены");
                }
            }
        });
    }


    @SuppressLint("CheckResult")
    private void delSelectSteps(String key, ArrayList<String>names){
        Uri treeUri = Uri.parse(getListPerms().get(key));
        DocumentFile folder = DocumentFile.fromTreeUri(getContext(),treeUri);
        DocumentFile[] content = folder.listFiles();
        final int[] index = {names.size()};
        Observable.create(new ObservableOnSubscribe<String>() {

            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                for (String n:names){
                    File f = new File(n);
                    for (DocumentFile df:content){
                        if(df.isFile()) {
                            if (df.getName().equals(f.getName())) {
                                    if(df.delete())emitter.onNext(n);
                                    break;
                            }
                        }
                    }
                }
                emitter.onComplete();
            }
        }).compose(new ThreadTransformers.InputOutput<>())
                .doOnNext(new Consumer<String>() {
                    @Override
                    public void accept(String aBoolean) throws Exception {
                        getListImagesInFolders().get(getKey()).remove(aBoolean);
                        setListImagesInFolders(getListImagesInFolders());
                        getImgAdapt().setIndexKey(0);
                    }
                })
                .onErrorResumeNext(new Observable<String>() {
                    @Override
                    protected void subscribeActual(Observer<? super String> observer) {
                        Massages.SHOW_MASSAGE(getContext(), "Не удалось удалить выбранное");
                    }
                })
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String aBoolean) throws Exception {
                        index[0]--;
                        if(index[0]==0)Massages.SHOW_MASSAGE(getContext(), "Выбранные изображения удалены ");
                    }
                });
    }

    private boolean delFold(DocumentFile folder){
        DocumentFile[]content = folder.listFiles();
        boolean allDelete = true;
        for (DocumentFile df:content){
            if(df.isDirectory()){
                allDelete = false;
                continue;
            }
            final String type = df.getType();
            if(type.equals(TYPE_PNG)||type.equals(TYPE_JPEG)||type.equals(TYPE_JPG)){
                try {
                    df.delete();
                }catch (Exception e){

                }
            }else allDelete = false;

        }
        return allDelete;
    }
}
