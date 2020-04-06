package com.example.kittenappscollage.collect;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import androidx.annotation.Nullable;
import java.util.Arrays;
import java.util.HashMap;

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
        LYTE("data "+data.toString());
        for (String n:names){
            LYTE("del name "+n);
        }
        stopSelf();
        return super.onStartCommand(intent, flags, startId);
    }


}
