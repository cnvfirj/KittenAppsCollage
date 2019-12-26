package com.example.kittenappscollage.collect;

import android.annotation.SuppressLint;
import android.content.Context;

import com.example.kittenappscollage.helpers.RequestFolder;
import com.example.kittenappscollage.helpers.rx.ThreadTransformers;

import java.io.File;
import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;


public class RequestImagesInStorage {

    private Context context;
    private ResultListImagesListener listener;

    private static RequestImagesInStorage singleton = null;

    public static RequestImagesInStorage requestList(Context context, ResultListImagesListener listener){
        if(singleton==null){
            synchronized (RequestImagesInStorage.class){
                if(singleton==null){
                    singleton = new RequestImagesInStorage(context,listener);
                }
            }
        }

        return singleton;
    }



    public static RequestImagesInStorage get(Context context, ResultListImagesListener listener){
        return new RequestImagesInStorage(context,listener);
    }

    private RequestImagesInStorage(Context context, ResultListImagesListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void loadImages(String path){
        if(RequestFolder.testFolder(new File(path))){
            loadList(path);
        }
    }

    @SuppressLint("CheckResult")
    private void loadList(String path){
        Observable.create((ObservableOnSubscribe<ArrayList<String>>) emitter -> {
            ArrayList<String> list = new ArrayList<>();
            File dir = new File(path);
            if(RequestFolder.testFolder(dir)){

                checkDir(dir,list);
                emitter.onNext(list);
            }
            emitter.onComplete();

        }).compose(new ThreadTransformers.InputOutput<>())
          .subscribe(strings -> {
              listener.result(strings);
          });
    }

    private void checkDir(File dir, ArrayList<String> list){
        if(dir.isDirectory()) {
            File[] files = dir.listFiles();
            if(files!=null) {
                for (File f : files) {
                    checkDir(f, list);
                }
            }
        }else {
            list.add(dir.getAbsolutePath());
        }

    }
}
