package com.example.kittenappscollage.collect.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.provider.MediaStore;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.kittenappscollage.helpers.ListenMedia;
import com.example.kittenappscollage.helpers.RequestFolder;
import com.example.kittenappscollage.helpers.db.ActionsDataBasePerms;
import com.example.kittenappscollage.helpers.db.PermStorage;
import com.example.kittenappscollage.helpers.db.PermsDataBase;
import com.example.kittenappscollage.helpers.rx.ThreadTransformers;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;

import static com.example.kittenappscollage.helpers.Massages.LYTE;

public class FragmentScanAllImages extends Fragment {

    private HashMap<String,ArrayList<String>>listImagesToFolder;

    private HashMap<String,String>listFolds;

    private HashMap<String, Integer>listStorages;

    private HashMap<String,String>listPerms;

    private ArrayList<String> storage;

    private Handler handler;

    private ListenMedia observer;

    public void scanDevice(){
         check();
    }

    @Override
    public void onResume() {
        super.onResume();
        handler = new Handler();
        observer = new ListenMedia(handler).setFragment(this).setContext(getContext());
        getContext().getContentResolver().registerContentObserver(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,true,observer);
    }

    @Override
    public void onPause() {
        super.onPause();
        getContext().getContentResolver().unregisterContentObserver(observer);
    }

    @SuppressLint("CheckResult")
    private void check(){
        Observable.create((ObservableOnSubscribe<HashMap<String, ArrayList<String>>>) emitter -> {
          emitter.onNext(scan(getListImagesInFolders(), getListFolds()));
          emitter.onComplete();
        }).compose(new ThreadTransformers.NewThread<>())
          .subscribe(stringArrayListHashMap -> setListImagesInFolders(stringArrayListHashMap));
    }

    @SuppressLint("Recycle")
    private HashMap<String,ArrayList<String>> scan(HashMap<String,ArrayList<String>>list,HashMap<String,String>folds){
         definitionStorage();
         if(getListImagesInFolders()==null)initListImagesInFolders();
         else clearListImagesInFolders();
        String[] projection = {
                MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME};

        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        Cursor cursor = getContext().getContentResolver().query(uri, projection, null,
                null, null);

        int col_path, col_fold,col_date;
        col_path = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        col_fold = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
        cursor.moveToFirst();
        if(list==null)initListImagesInFolders();
        clearListImagesInFolders();
        while (cursor.moveToNext()) {
            String path = cursor.getString(col_path).toLowerCase();
            String key = cursor.getString(col_path).split(cursor.getString(col_fold))[0]+cursor.getString(col_fold);


            boolean pik = path.endsWith(".png")||path.endsWith(".jpeg")||path.endsWith("jpg");
            if(list.containsKey(key)){
                if(pik) {
                    list.get(key).add(cursor.getString(col_path));
                }
            } else {
                if(pik&&addPerm(key)) {
                    ArrayList<String> imgs = new ArrayList<>();
                    imgs.add(cursor.getString(col_path));
                    list.put(key, imgs);

                    folds.put(key,cursor.getString(col_fold));
                        for(int i=0;i<getNamesStorage().size();i++) {
                            if(key.contains(getNamesStorage().get(i))){
                                getIndexesStorage().put(key, getNamesStorage().indexOf(getNamesStorage().get(i)));
                            }
                        }
                }
            }
        }
        return list;
    }

    private boolean addPerm(String fold){
        boolean visible = true;
        if(getPermsDataBase().workPerms().getPerm(fold)==null){
            getPermsDataBase().workPerms().insert(new PermStorage(fold)
            .setVis(true)
            .setUri(ActionsDataBasePerms.NON_PERM));
        }else {
            String uri = getPermsDataBase().workPerms().getPerm(fold).uri;
            if(uri!=null) {
                visible = getPermsDataBase().workPerms().getPerm(fold).isVisible();
                    getListPerms().put(fold, uri);
            }else {
                visible = getPermsDataBase().workPerms().getPerm(fold).isVisible();
                getListPerms().put(fold, ActionsDataBasePerms.NON_PERM);
            }
        }
        return visible;
    }
    /*определяем тома в устройстве*/
    private void definitionStorage(){

        File[] files = ContextCompat.getExternalFilesDirs(getContext(), null);
        storage = new ArrayList<>();
        for (int i=0;i<files.length;i++){
            storage.add(files[i].getAbsolutePath().split("Android")[0]);
        }
    }

    public void setSavingCollage(String path,String key){
        if(getListImagesInFolders().containsKey(key)){
            getListImagesInFolders().get(key).add(path);
        } else {
            correctAdapter();
            ArrayList<String> imgs = new ArrayList<>();
            imgs.add(path);
            getListImagesInFolders().put(key, imgs);

            String[]split = key.split("[/]");

            getListFolds().put(key,split[split.length-1]);

            for(int i=0;i<getNamesStorage().size();i++) {
                if(key.contains(getNamesStorage().get(i))){
                    getIndexesStorage().put(key, getNamesStorage().indexOf(getNamesStorage().get(i)));
                }
            }
        }
        setListImagesInFolders(getListImagesInFolders());
    }

    protected void correctAdapter(){
        /*если в момент сохранения окрыт подкаталог галереи,
        * то при создании директории индекс открыто не изменяется
        * и отображается предыдущая по порядку*/
    }
    protected void setListImagesInFolders(HashMap<String,ArrayList<String>> list){
         listImagesToFolder = list;
    }

    protected ArrayList<String>getNamesStorage(){
        return storage;
    }

    protected HashMap<String,ArrayList<String>> getListImagesInFolders(){
        return listImagesToFolder;
    }

    protected HashMap<String,String>getListFolds(){
        return listFolds;
    }

    /*индекс хранилища из getNamesStorage()*/
    protected HashMap<String,Integer>getIndexesStorage(){
        return listStorages;
    }

    protected HashMap<String,String>getListPerms(){
        return listPerms;
    }


    protected PermsDataBase getPermsDataBase(){
        return ActionsDataBasePerms.create(getContext()).getPermsDataBase();
    }
    protected void clearListImagesInFolders(){
        listImagesToFolder.clear();
        listFolds.clear();
        listStorages.clear();
        listPerms.clear();
    }

    protected void initListImagesInFolders(){
        listImagesToFolder = new HashMap<>();
        listFolds = new HashMap<>();
        listStorages = new HashMap<>();
        listPerms = new HashMap<>();

    }

}
