package com.example.kittenappscollage.collect.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

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

import static com.example.kittenappscollage.helpers.Massages.LYTE;


public class FileAdapter extends SuperAdapter {

    public static final int SOURCE_DOWNLOAD = 222;
    public static final int SOURCE_PROJECT = 111;
    public static final int SOURCE_PHOTO = 333;

    private final String PNG = "png";
    private final String JPEG = "jpeg";
    private final String JPG = "jpg";

    private Context context;

    private String dir;

    private File fileDir;

//    private boolean modeSelected;


    private ArrayList<File> arrFiles;
    private boolean[] arrChecks;

    public FileAdapter(Context c, int source) {
//        modeSelected = false;
        arrFiles = new ArrayList();
        context = c;
        if(source==SOURCE_DOWNLOAD){
            dir = RequestFolder.getFolderDown();
        }else if(source==SOURCE_PROJECT){
            dir = RequestFolder.getFolderImages();
        }else if(source==SOURCE_PHOTO){
            dir = RequestFolder.getFolderPhotos();
            File[] dirs = ContextCompat.getExternalFilesDirs(getContext(), null);
            if(dirs.length>0&&dirs.length<2){
                dir = RequestFolder.getFolderPhotos();
            }else if(dirs.length>1){
                dir = RequestFolder.getSDFolderPhotos(dirs[1].getAbsolutePath());
            }else return;

        }
        fileDir = new File(dir);
        requestList();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SuperAdapter.MyViewHolder holder = new SuperAdapter.MyViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.collect_item,parent,false));
        return holder;
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

//    @Override
//    protected void click(ImageView image, ImageView check, int pos) {
//         if(modeSelected){
//             arrChecks[pos] = true;
//             check.setVisibility(View.VISIBLE);
//         }
//    }
//
//    @Override
//    protected void clickLong(ImageView image, ImageView check, int pos) {
//         arrChecks[pos] = true;
//         check.setVisibility(View.VISIBLE);
//         modeSelected = true;
//    }

    @SuppressLint("CheckResult")
    public void requestList(){
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
                        arrChecks = new boolean[arrFiles.size()];
                        notifyDataSetChanged();
                    }
                });
    }

//    public void setModeSelected(boolean mode){
//        modeSelected = mode;
//    }

    private ArrayList<File>sort(File[]files){
        ArrayList<File>arr = new ArrayList<>();

        for (File f:files){
            String[] name = f.getName().split("[.]");
            if(name[name.length-1].equals(PNG)||
                    name[name.length-1].equals(JPEG)||
                    name[name.length-1].equals(JPG))arr.add(f);
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


}
