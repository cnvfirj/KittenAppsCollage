package com.example.kittenappscollage.draw.saveSteps;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.kittenappscollage.R;
import com.example.kittenappscollage.helpers.rx.ThreadTransformers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.Objects;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;

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
        return START_STICKY;
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
            startForegroundService();
            requestDlelteSelectInFold(state)
                    .doOnComplete(() -> {
                        stopForeground(true);
                        stopSelf();
                    })
                    .subscribe(aBoolean -> stopSelf());
        }
    }


    private void startForegroundService() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel("my_service", "clear temp files");
        }
            startForeground(1, buildNotification("my_service"));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel(String channelId, String channelName){

        NotificationChannel chan = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);
    }

    private Notification buildNotification(String channelId) {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.icon_clear_link)
//                        .setContentTitle("Киттен колаж трудится")
//                        .setContentText("Временные файлы удаляются")
                        .setShowWhen(true)
                        .setOngoing(true)
                        .setProgress(100, 0, true)
                        .setPriority(NotificationCompat.PRIORITY_MAX);

//        NotificationManager notificationManager =
//                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        notificationManager.notify(1, notification);
        return builder.build();

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
//                LYTE("clear catch - "+file.getName());
                file.delete();
            }
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
