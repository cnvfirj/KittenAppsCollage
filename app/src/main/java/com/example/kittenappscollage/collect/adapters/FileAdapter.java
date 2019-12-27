package com.example.kittenappscollage.collect.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.example.kittenappscollage.helpers.RequestFolder;
import com.example.kittenappscollage.helpers.rx.ThreadTransformers;

import java.io.File;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;


public class FileAdapter extends SuperAdapter {

    public static final int SOURCE_DOWNLOAD = 222;
    public static final int SOURCE_PROJECT = 111;
    public static final int SOURCE_PHOTO = 333;

    private Context context;

    private String dir;

    private File fileDir;

    private boolean modeSelected;


    private File[] arrFiles;
    private boolean[]arrChecks;

    public FileAdapter(Context c, int source) {
        modeSelected = false;
        context = c;
        if(source==SOURCE_DOWNLOAD){
            dir = RequestFolder.getFolderDown();
        }else if(source==SOURCE_PROJECT){
            dir = RequestFolder.getFolderImages();
        }else if(source==SOURCE_PHOTO){
            dir = RequestFolder.getFolderPhotos();
        }

        fileDir = new File(dir);
        requestList();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        if(arrChecks==null)return 0;
        return arrChecks.length;
    }

    @Override
    protected void click(ImageView image, ImageView check, int pos) {
         if(modeSelected){
             arrChecks[pos] = true;
             check.setVisibility(View.VISIBLE);
         }
    }

    @Override
    protected void clickLong(ImageView image, ImageView check, int pos) {
         arrChecks[pos] = true;
         check.setVisibility(View.VISIBLE);
         modeSelected = true;
    }

    @SuppressLint("CheckResult")
    public void requestList(){
        Observable.create(new ObservableOnSubscribe<File[]>() {
            @Override
            public void subscribe(ObservableEmitter<File[]> emitter) throws Exception {

                if(testFolder(fileDir))emitter.onNext(scanDir(fileDir));
                emitter.onComplete();
            }
        }).compose(new ThreadTransformers.InputOutput<>())
                .subscribe(new Consumer<File[]>() {
                    @Override
                    public void accept(File[] files) throws Exception {
                        arrFiles = files;
                        arrChecks = new boolean[arrFiles.length];
                        notifyDataSetChanged();
                    }
                });
    }

    public void setModeSelected(boolean mode){
        modeSelected = mode;
    }

    private File[] scanDir(File dir){
        if(dir.isDirectory()) {
            return dir.listFiles();
        }else {
            return new File[1];
        }
    }

    public static boolean testFolder(File file){
        boolean success = true;
        if(!file.exists()){
            success = file.mkdirs();
        }
        return success;
    }


}
