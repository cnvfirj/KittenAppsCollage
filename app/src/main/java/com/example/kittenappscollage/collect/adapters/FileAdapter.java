package com.example.kittenappscollage.collect.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.kittenappscollage.R;
import com.example.kittenappscollage.helpers.RequestFolder;
import com.example.kittenappscollage.helpers.rx.ThreadTransformers;

import java.io.File;
import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;


public class FileAdapter extends SuperAdapter {

    public static final int SOURCE_DOWNLOAD = 222;
    public static final int SOURCE_PROJECT = 111;
    public static final int SOURCE_PHOTO = 333;

    private Context context;

    private String dir;

    private int source;

    private File fileDir;

    private ArrayList<File> arrFiles;

    private boolean[] arrChecks;

    public FileAdapter(Context c, int source) {
        this.source = source;
        arrFiles = new ArrayList();
        context = c;
        if(source==SOURCE_DOWNLOAD){
            dir = RequestFolder.getFolderDown();
        }else if(source==SOURCE_PROJECT){
            dir = RequestFolder.getFolderImages();
        }else if(source==SOURCE_PHOTO){
            dir = RequestFolder.getFolderPhotos();
            File[] dirs = ContextCompat.getExternalFilesDirs(getContext(), null);
            if(dirs.length == 1){
                dir = RequestFolder.getFolderPhotos();
            }else if(dirs.length>1){
                dir = RequestFolder.getSDFolderPhotos(dirs[1].getAbsolutePath());
            }else return;

        }
        assert dir != null;
        fileDir = new File(dir);
        requestList();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.collect_img_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Glide.with(getContext())
                .load(arrFiles.get(position))
                .into(holder.getImage());
    }

    @Override
    public int getItemCount() {
        if(arrChecks==null)return 0;
        return arrChecks.length;
    }

    @SuppressLint("CheckResult")
    public FileAdapter requestList(){
        Observable.create(new ObservableOnSubscribe<ArrayList<File>>() {
            @Override
            public void subscribe(ObservableEmitter<ArrayList<File>> emitter) throws Exception {

                if(testFolder(fileDir))emitter.onNext(sort(scanDir(fileDir)));
                emitter.onComplete();
            }
        }).compose(new ThreadTransformers.InputOutput<>())
                .subscribe(new Consumer<ArrayList<File>>() {
                    @Override
                    public void accept(ArrayList<File> files) throws Exception {
                        arrFiles = files;

//                        arrChecks = new boolean[arrFiles.size()];
                        notifyDataSetChanged();
                    }
                });
        return this;
    }

    private ArrayList<File>sort(File[]files){
        ArrayList<File>arr = new ArrayList<>();
        for (File f:files){
            String[] name = f.getName().split("[.]");
            if(name[name.length-1].equals("png")||
                    name[name.length-1].equals("jpeg")||
                    name[name.length-1].equals("jpg"))arr.add(f);
        }
        return arr;
    }

    private File[] scanDir(File dir){
        if(dir.isDirectory()) {
            return dir.listFiles();
        }else {
            return new File[1];
        }
    }

    public static boolean testFolder(File file){
        boolean success = true;
        if(!file.exists()){
            success = file.mkdirs();
        }
         return success;
    }

    protected Context getContext(){
        return context;
    }

    protected int getSource(){
        return source;
    }

    protected boolean[]getArrChecks(){
        return arrChecks;
    }

    protected ArrayList<File>getArrFiles(){
        return arrFiles;
    }

    protected void resetArrChecks(){
        arrChecks = new boolean[arrFiles.size()];
    }


}
