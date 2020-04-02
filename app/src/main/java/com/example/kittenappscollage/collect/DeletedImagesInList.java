package com.example.kittenappscollage.collect;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;

public class DeletedImagesInList extends Service {


    public static final String KEY_NAMES = "key names";
    public static final String KEY_MUT = "key mut";

    private Uri data;
    private HashMap<Long,String>delete;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        data = intent.getData();
        final String[]names = intent.getStringArrayExtra(KEY_NAMES);
        final long[]mut = intent.getLongArrayExtra(KEY_MUT);
        delete = new HashMap<>();
        if(names!=null&&mut!=null){
            for (int i=0;i<names.length;i++){
                delete.put(mut[i],names[i]);
            }
        }
        Arrays.sort(mut);
        return super.onStartCommand(intent, flags, startId);
    }


}
