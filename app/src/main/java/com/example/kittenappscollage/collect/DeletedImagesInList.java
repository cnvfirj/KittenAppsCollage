package com.example.kittenappscollage.collect;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.provider.DocumentsContract;
import android.util.Pair;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.documentfile.provider.DocumentFile;

import com.example.kittenappscollage.R;
import com.example.kittenappscollage.helpers.Massages;
import com.example.kittenappscollage.helpers.rx.ThreadTransformers;

import java.io.FileNotFoundException;
import java.util.Arrays;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

import static com.example.kittenappscollage.helpers.Massages.LYTE;

public class DeletedImagesInList extends Service {


    public static final String KEY_NAMES = "key names";
//    public static final String KEY_MUT = "key mut";


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final Uri data = intent.getData();
        final String[]names = intent.getStringArrayExtra(KEY_NAMES);
        startForegroundService();
        thread(data,names);
        return super.onStartCommand(intent, flags, startId);
    }

    private void startForegroundService() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel("my_service_delete", "Delete files in device");
        }
        startForeground(2, buildNotification("my_service_delete"));
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
                        .setSmallIcon(R.drawable.icon_delete_all)
                        .setContentTitle("Удаление")
                        .setContentText("Файлы удаляются с физического носителя")
                        .setShowWhen(true)
                        .setOngoing(true)
                        .setProgress(100, 0, true)
                        .setPriority(NotificationCompat.PRIORITY_MAX);

        return builder.build();

    }

    @SuppressLint("CheckResult")
    private void thread(Uri data, String[]names){
        Observable.create((ObservableOnSubscribe<Pair<String, Boolean>>) emitter -> {
            clearDeletedList(data,names,emitter);
            emitter.onComplete();
        }).compose(new ThreadTransformers.InputOutput<>())
          .doOnComplete(() -> {
                    stopForeground(true);
                    stopSelf();
                })
          .subscribe(pair -> {
                if(!pair.second)
                Massages.SHOW_MASSAGE(getApplicationContext(),
                        getApplicationContext().getResources().getString(R.string.ERROR_DELETE_FILE)+pair.first);
//                else LYTE("delete - "+pair.first);
        });
    }

    private void clearDeletedList(Uri uri, String[]names,ObservableEmitter<Pair<String,Boolean>> emitter){
        DocumentFile fold = DocumentFile.fromTreeUri(getApplicationContext(),uri);
        DocumentFile[] files = fold.listFiles();
        if(files.length>1) {
            Arrays.sort(files, (f1, f2) -> {
                final Long l1 = f1.lastModified();
                final Long l2 = f2.lastModified();
                return l1.compareTo(l2);
            });
        }
        int index = 0;
        for (String name:names){
            index = bruteForceOption(index,files,name,emitter);
        }
    }

    private int bruteForceOption(int index, DocumentFile[]files, String name,ObservableEmitter<Pair<String,Boolean>> emitter){
        for (int i=index;i<files.length;i++){
            DocumentFile f = files[i];
            if(f.isFile()) {
                String type = f.getType();
                if (type.equals("image/png") || type.equals("image/jpeg") || type.equals("image/jpg")) {
                    final String n = f.getName();
                    if (n != null && n.equals(name)) {
                        index = i;
                        emitter.onNext(new Pair<>(name, deleteFile(files[i])));
                        break;
                    }
                }
            }
        }
        return index;
    }

    /*проверить на реальном устройстве*/
    private boolean deleteFile(DocumentFile df){
        if(df.exists()){
            try {
               return DocumentsContract.deleteDocument(getApplicationContext().getContentResolver(),df.getUri());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return false;
    }


}
