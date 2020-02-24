package com.example.kittenappscollage.helpers.dbPerms;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.room.Room;

import com.example.kittenappscollage.helpers.rx.ThreadTransformers;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;

import static com.example.kittenappscollage.helpers.Massages.LYTE;

public class WorkDBPerms {
    public static int INSERT = 0;

    public static int DELETE = 1;

    private static WorkDBPerms singleton;

    private GetPermsFolds db;

    public WorkDBPerms(Context context) {
        db = Room.databaseBuilder(context,GetPermsFolds.class,"permsfold.db").build();
    }

    public static WorkDBPerms get(Context context){
        if(singleton==null){
            synchronized (WorkDBPerms.class){
                singleton = new WorkDBPerms(context);
            }
        }
        return singleton;
    }

    public static WorkDBPerms get(){
        return singleton;
    }

    public void setAction(int operation, String uri){
        Observable.create(emitter -> {
            if(operation==INSERT)setItem(uri);
            else if (operation==DELETE)delItem(uri);
            emitter.onComplete();
        }).compose(new ThreadTransformers.InputOutput<>())
                .doOnError(throwable -> LYTE("WorkDBPerms error operation - "+operation))
                .subscribe();

    }

    @SuppressLint("CheckResult")
    public void queryList(QueryWorkDBPerms listener){
        Observable.create((ObservableOnSubscribe<List<Permis>>) emitter -> {
            emitter.onNext(allItems());
            emitter.onComplete();
        }).compose(new ThreadTransformers.InputOutput<>())
                .doOnError(throwable -> LYTE("WorkDBPerms error query list"))
                .subscribe(permis -> listener.list(permis));
    }


    public void setItem(String uri){
       db.work().insert(new Permis(uri));
    }

    public void delItem(String uri){
        db.work().delete(db.work().getPerm(uri));
    }

    public List<Permis>allItems(){
        return db.work().list();
    }

    public interface QueryWorkDBPerms{
        void list(List<Permis> perms);
    }
}
