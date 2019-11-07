package com.example.kittenappscollage.draw.saveSteps;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.example.kittenappscollage.helpers.rx.ThreadTransformers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.Objects;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

import static com.example.kittenappscollage.helpers.Massages.LYTE;

public class ClearCatch extends Service {

    public static final String KEY_FOLD = "key fold";


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String fold = intent.getStringExtra(KEY_FOLD);
        clearSelect(fold);
        return super.onStartCommand(intent, flags, startId);
    }

    @SuppressLint("CheckResult")
    private void clearSelect(String foldData){

        File[] data = Objects.requireNonNull((new File(foldData)).listFiles());
        State state = null;
        if(data.length==0)return;
        try {
            InputStream fis = new FileInputStream(data[0]);
            ObjectInputStream ois = new ObjectInputStream(fis);

           state = (State) ois.readObject();

            ois.close();
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }

        if(state!=null){
            requestDlelteSelectInFold(state).subscribe(aBoolean -> stopSelf());
        }
    }

    private Observable<Boolean> requestDlelteSelectInFold(State state){
        return Observable.create((ObservableOnSubscribe<Boolean>) emitter -> {
            emitter.onNext(deleteSelectInFold(state));
            emitter.onComplete();
        }).compose(new ThreadTransformers.InputOutput<>());
    }

    private boolean deleteSelectInFold(State state){
        File[]files = (new File(state.getPathFoldImg())).listFiles();
        for (File file:files){
            if(!file.getAbsolutePath().equals(state.getPathImg())&&
            !file.getAbsolutePath().equals(state.getPathLyr())){
                file.delete();
            }
            else LYTE("non del "+file.getAbsolutePath());
        }
        return false;
    }



    @SuppressLint("CheckResult")
    public static void clearAll(String path){
        requestDleteAllInFold(path).subscribe();
    }

    private static Observable<Boolean> requestDleteAllInFold(String path){
        return Observable.create((ObservableOnSubscribe<Boolean>) emitter -> {
            emitter.onNext(clearAllInFold(path));
            emitter.onComplete();
        }).compose(new ThreadTransformers.InputOutput<>());
    }

    private static boolean clearAllInFold(String fold){
        File[] files = (new File(fold)).listFiles();
        if(files!=null) {
            for (File file : files) {
                file.delete();
            }
            return true;
        }
        return false;
    }
}
