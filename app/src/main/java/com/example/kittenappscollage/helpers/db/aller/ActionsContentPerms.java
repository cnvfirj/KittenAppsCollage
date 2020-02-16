package com.example.kittenappscollage.helpers.db.aller;

import android.content.Context;
import android.util.Log;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.room.Room;
import com.example.kittenappscollage.helpers.rx.ThreadTransformers;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

import static com.example.kittenappscollage.helpers.Massages.LYTE;

public class ActionsContentPerms {

    public static final String GRAND = "(_GRAND_)";

    public static final String NON_PERM = "(_NON_)";

    public static final String ZHOPA = "(_!_)";

    public static final String SYS_FILE = "(_FILE_)";

    public static final String SYS_DF = "(_DF_)";

    public static final int NON_LOC_STOR = -5;

    public ArrayList<String>listStor;

    private HashMap<String, String>perms;

    private GetDBContentPerms db;

    private static ActionsContentPerms singleton;

    private ActionsContentPerms(Context context) {
        db = Room.databaseBuilder(context, GetDBContentPerms.class,"permsContent.db").build();
        perms = new HashMap<>();
        initPermsDB();
        exampleStorage(context);
    }

    public static ActionsContentPerms create(Context context){
        if(singleton==null){
            synchronized (ActionsContentPerms.class){
                singleton = new ActionsContentPerms(context);
            }
        }
        return singleton;
    }

    public GetDBContentPerms getDB(){
        return db;
    }

    public ContentPermis createBlank(String key){
        return new ContentPermis(key);
    }

    public String getPerm(String key){
        return perms.get(key);
    }

    public void deleteItemDB(String key){
        Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> emitter) throws Exception {
                emitter.onNext(delete(key));
                emitter.onComplete();
            }
        }).compose(new ThreadTransformers.InputOutput<>()).subscribe();
    }

    private void initPermsDB(){
        Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> emitter) throws Exception {
                emitter.onNext(init());
                emitter.onComplete();
            }
        }).compose(new ThreadTransformers.InputOutput<>()).subscribe();
    }
    private boolean init(){
        List<ContentPermis>list = db.work().loadAll();
        for (ContentPermis cp:list){
            perms.put(cp.keyPath,cp.uriPerm);
        }
        return true;
    }

    private boolean delete(String key){
        ContentPermis cp = db.work().getPerm(key);
        if(perms.containsKey(key))perms.remove(key);
        if(cp!=null){
            db.work().delete(cp);
        }
        return true;
    }

    public void queryItemDB(String key, String uriPerm, String uriDF, String system, int storage, int visible){
        Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> emitter) throws Exception {
                emitter.onNext(query(key,uriPerm,uriDF,system,storage,visible));
                emitter.onComplete();
            }
        }).compose(new ThreadTransformers.InputOutput<>()).subscribe();
    }

    public void insertCP(ContentPermis cp){
        perms.put(cp.keyPath,cp.uriPerm);
        db.work().insert(cp);
    }

    private boolean query(String key, String uriPerm, String uriDF, String system, int storage, int visible){
        boolean insert = false;
        ContentPermis cp = db.work().getPerm(key);
        perms.put(key,uriPerm);
        if(cp==null){
            cp = createBlank(key);
            insert = true;
        }

        if(uriPerm!=null)cp.uriPerm = uriPerm;
        if(uriDF!=null)cp.uriDocFile = uriDF;
        if(system!=null)cp.system = system;
        cp.storage = storage;
        if(visible== View.VISIBLE||visible==View.INVISIBLE)cp.visible = visible;

        if(insert)db.work().insert(cp);
        else db.work().update(cp);
        return true;
    }

    public ContentPermis getItem(String key){
        return db.work().getPerm(key);
    }

    public boolean check(String key){
      return getItem(key)!=null;
    }

    private void exampleStorage(Context c){
        File[] files = ContextCompat.getExternalFilesDirs(c, null);
        listStor = new ArrayList<>();
        for (int i=0;i<files.length;i++){
            listStor.add(files[i].getAbsolutePath().split("Android")[0]);
        }
    }

    private void absentDB(){
        Log.e("ActionsContentPerms: ", "айтем ContentPermis не найден");
    }

}
