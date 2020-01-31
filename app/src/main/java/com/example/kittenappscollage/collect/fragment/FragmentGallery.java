package com.example.kittenappscollage.collect.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kittenappscollage.R;
import com.example.kittenappscollage.collect.adapters.ListenAdapter;
import com.example.kittenappscollage.collect.adapters.LoadFoldAdapt;
import com.example.kittenappscollage.collect.adapters.LoadImgAdapt;
import com.example.kittenappscollage.helpers.AllPermissions;

import java.util.ArrayList;
import java.util.HashMap;

import static android.app.Activity.RESULT_OK;
import static com.example.kittenappscollage.collect.adapters.ListenLoadFoldAdapter.ROOT_ADAPTER;

public class FragmentGallery extends FragmentScanAllImages implements ListenAdapter{

    public static final int REQUEST_READ_STORAGE = 92;

    public static final int REQUEST_WRITE_ROOT = 94;



    private RecyclerView recycler;

    private LoadFoldAdapt foldAdapt;

    private LoadImgAdapt imgAdapt;

    private GridLayoutManager gridLayoutManager;

    private int indexAdapter;

    private String key;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        indexAdapter = ROOT_ADAPTER;
        foldAdapt = new LoadFoldAdapt(getContext());
        imgAdapt = new LoadImgAdapt(getContext());
        foldAdapt.setParams(getContext().getResources().getDisplayMetrics().widthPixels);
        imgAdapt.setParams(getContext().getResources().getDisplayMetrics().widthPixels);
        return inflater.inflate(R.layout.fragment_gallery,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onResume() {
        super.onResume();
        initListImagesInFolders();
        stateReadStorage(AllPermissions.create()
                .activity(getActivity())
                .reqSingle(AllPermissions.STORAGE).isStorage());
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void stateReadStorage(boolean state){
        if(state) {
            scanDevice();
        }else {
            AllPermissions.create()
                    .activity(getActivity())
                    .callDialog(AllPermissions.STORAGE,REQUEST_READ_STORAGE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_WRITE_ROOT){
            if(resultCode==RESULT_OK){
                if(AllPermissions.create()
                        .activity(getActivity())
                        .reqSingle(AllPermissions.STORAGE).isStorage()) {
                    scanDevice();
                }else {
                    AllPermissions.create()
                            .activity(getActivity())
                            .callDialog(AllPermissions.STORAGE,REQUEST_READ_STORAGE);
                }
            }
        }
    }

    private void init(View v){
        gridLayoutManager = new GridLayoutManager(getContext(),2);
        recycler = v.findViewById(R.id.gallery_list);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(gridLayoutManager);
        recycler.setAdapter(foldAdapt);


    }

    @Override
    protected void setListImagesInFolders(HashMap<String, ArrayList<String>> list) {
        super.setListImagesInFolders(list);
        invisibleMenu();
        foldAdapt.setAll(getListImagesInFolders()).setListen(this);
        imgAdapt.setAll(getListImagesInFolders()).setListen(this);
    }

    @Override
    public void click(int adapter, ImageView img, ImageView check, int pos) {
        if(adapter==ROOT_ADAPTER){
            if(!foldAdapt.isModeSelected()) {
                imgAdapt.setIndexKey(pos);
                gridLayoutManager.setSpanCount(3);
                recycler.setAdapter(imgAdapt);
                setIndexAdapter(pos);
                key = getFoldAdapt().getKeys()[pos];
            }
        }
    }

    @Override
    public void longClick(int adapter, ImageView img, ImageView check, int pos) {
            if(imgAdapt.isModeSelected()||foldAdapt.isModeSelected()){
                visibleMenu();
            }
    }

    @Override
    protected void correctAdapter() {
        super.correctAdapter();
        if(getIndexAdapter()!=ROOT_ADAPTER) {
            indexAdapter++;
            imgAdapt.setIndexKey(indexAdapter);
        }

    }

    @Override
    public void createHolder(int adapter, View holder, int pos) {

    }

    @Override
    public void createContentHolder(int adapter, View[] content, int pos) {

    }



    protected void visibleMenu(){

    }

    protected void invisibleMenu(){

    }


    protected String getKey(){
        return key;
    }

    protected void setKey(String k){
        key = k;
    }

    protected void setIndexAdapter(int i){
        indexAdapter = i;
    }

    protected RecyclerView getRecycler(){
        return recycler;
    }

    protected GridLayoutManager getGridLayoutManager(){
        return gridLayoutManager;
    }

    protected LoadFoldAdapt getFoldAdapt(){
        return foldAdapt;
    }

    protected LoadImgAdapt getImgAdapt(){
        return imgAdapt;
    }

    protected int getIndexAdapter(){
        return indexAdapter;
    }


}
