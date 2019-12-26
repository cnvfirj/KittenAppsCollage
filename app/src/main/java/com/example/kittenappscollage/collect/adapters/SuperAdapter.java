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
import androidx.recyclerview.widget.RecyclerView;

import com.example.kittenappscollage.R;
import com.example.kittenappscollage.collect.TouchViewListener;
import com.example.kittenappscollage.helpers.RequestFolder;
import com.example.kittenappscollage.helpers.rx.ThreadTransformers;

import java.io.File;
import java.io.Serializable;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;

public abstract class SuperAdapter
        extends RecyclerView.Adapter<SuperAdapter.MyViewHolder>
        implements Serializable {


    public static final int SOURCE_DOWNLOAD = 222;
    public static final int SOURCE_PROJECT = 111;
    public static final int SOURCE_PHOTO = 333;

    private String dir;
    private boolean checkable;
    private boolean scroll;


    private Context context;
    private View view;
    private File[] listImg;
    private boolean[]listChecked;
    private TouchViewListener listener;

    public SuperAdapter(Context c, String d) {
        dir = d;
        context = c;
        requestList();
    }

    public SuperAdapter(Context c, int source){
        context = c;
        if(source==SOURCE_DOWNLOAD){
            dir = RequestFolder.getFolderDown();
        }else if(source==SOURCE_PROJECT){
            dir = RequestFolder.getFolderImages();
        }else if(source==SOURCE_PHOTO){
            dir = RequestFolder.getFolderPhotos();
        }
        requestList();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public int getItemCount() {
        return listImg.length;
    }

    public void checkable(boolean check){
        checkable = check;
    }

    @SuppressLint("CheckResult")
    public void requestList(){
        Observable.create(new ObservableOnSubscribe<File[]>() {
            @Override
            public void subscribe(ObservableEmitter<File[]> emitter) throws Exception {
                emitter.onNext(scanDir(new File(dir)));
            }
        }).compose(new ThreadTransformers.InputOutput<>())
                .subscribe(new Consumer<File[]>() {
                    @Override
                    public void accept(File[] files) throws Exception {
                        listImg = files;
                        listChecked = new boolean[listImg.length];
                        notifyDataSetChanged();
                    }
                });
    }

    private File[] scanDir(File dir){
        if(dir.isDirectory()) {
            return dir.listFiles();
        }else {
            return new File[1];
        }
    }

    protected File[] getListImg(){
        return listImg;
    }

    protected Context getContext(){
        return context;
    }

    public  class MyViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener
    , View.OnTouchListener {

        private ImageView image;
        private ImageView check;

        public MyViewHolder(View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.collect_item_gallery_image);
            image.setScaleType(ImageView.ScaleType.CENTER_CROP);
            image.setOnClickListener(this);
            check = itemView.findViewById(R.id.collect_item_select);


        }

        @Override
        public void onClick(View view) {
//            listener.clickItemGallery(view);
            if(checkable){

            }else{

            }
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                checkable = true;
            }
            return true;
        }

        public ImageView getImage() {
            return image;
        }


    }




}
