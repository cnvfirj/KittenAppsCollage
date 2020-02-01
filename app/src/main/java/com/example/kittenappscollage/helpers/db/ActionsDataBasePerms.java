package com.example.kittenappscollage.helpers.db;

import android.content.Context;
import android.net.Uri;
import android.os.Build;

import androidx.room.Room;

import com.example.kittenappscollage.helpers.App;
import com.example.kittenappscollage.helpers.RequestFolder;
import com.example.kittenappscollage.helpers.rx.ThreadTransformers;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

import static com.example.kittenappscollage.helpers.Massages.LYTE;

public class ActionsDataBasePerms {

    public static final String GRAND = "(_GRAND_)";

    public static final String NON_PERM = "(_NON_)";

    private PermsDataBase permsDataBase;

    private static ActionsDataBasePerms singleton;

    private ActionsDataBasePerms(Context context){
        permsDataBase = Room.databaseBuilder(context,PermsDataBase.class,"perms.db").build();

    }

    public static ActionsDataBasePerms create(Context context){
        if(singleton==null){
            synchronized (ActionsDataBasePerms.class){
                singleton = new ActionsDataBasePerms(context);
            }
        }
       return singleton;
    }

    public PermsDataBase getPermsDataBase(){
        return permsDataBase;
    }

    public void initInThread(String key, String uri){
        Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> emitter) throws Exception {
                emitter.onNext(init(key,uri));
                emitter.onComplete();
            }
        }).compose(new ThreadTransformers.InputOutput<>()).subscribe();
    }

    private boolean init(String key, String uri){
        PermStorage ps = new PermStorage(key);
         ps.setUri(uri).setVis(true);
         permsDataBase.workPerms().insert(ps);
         return true;
    }

    public void deleteInThread(String key){
        Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> emitter) throws Exception {
                emitter.onNext(delete(key));
                emitter.onComplete();
            }
        }).compose(new ThreadTransformers.InputOutput<>()).subscribe();
    }

    private boolean delete(String key){
        LYTE("ActionsDataBasePerms delete  perm in "+key);
        PermStorage p = permsDataBase.workPerms().getPerm(key);
        if(p!=null) permsDataBase.workPerms().delete(p);
        return true;
    }

    private boolean version(){
        return App.checkVersion();
    }

}
