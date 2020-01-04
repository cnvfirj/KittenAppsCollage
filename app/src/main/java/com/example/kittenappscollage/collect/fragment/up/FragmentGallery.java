package com.example.kittenappscollage.collect.fragment.up;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kittenappscollage.R;
import com.example.kittenappscollage.collect.adapters.up.ListenAdapter;
import com.example.kittenappscollage.collect.adapters.up.LoadFoldAdapt;
import com.example.kittenappscollage.collect.adapters.up.LoadImgAdapt;

import static com.example.kittenappscollage.helpers.Massages.LYTE;

public class FragmentGallery extends FragmentScanAllImages implements ListenAdapter{

    private RecyclerView recycler;

    private LoadFoldAdapt foldAdapt;

    private LoadImgAdapt imgAdapt;

    private GridLayoutManager gridLayoutManager;

    private int indexAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        foldAdapt = new LoadFoldAdapt(getContext());
        imgAdapt = new LoadImgAdapt(getContext());
        foldAdapt.setParams(getContext().getResources().getDisplayMetrics().widthPixels);
        imgAdapt.setParams(getContext().getResources().getDisplayMetrics().widthPixels);
        scanDevice();
        foldAdapt.setAll(getListImagesInFolders()).setListen(this);
        imgAdapt.setAll(getListImagesInFolders()).setListen(this);
        return inflater.inflate(R.layout.fragment_gallery,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
    }

    private void init(View v){
        gridLayoutManager = new GridLayoutManager(getContext(),2);
        recycler = v.findViewById(R.id.gallery_list);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(gridLayoutManager);
//        recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
//
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                if(newState==0){
//                    /*передаем в адаптер стоп отображение*/
//
//                }
//                if(newState==1){
//                    /*передаем в адаптнр старт отображение*/
//                }
//
//            }
//        });
        recycler.setAdapter(foldAdapt);


    }


    @Override
    public void click(int adapter, ImageView img, ImageView check, int pos) {
        if(adapter==LoadFoldAdapt.ROOT_ADAPTER){
            imgAdapt.setIndexKey(pos);
            gridLayoutManager.setSpanCount(3);
            recycler.setAdapter(imgAdapt);
            setIndexAdapter(pos);
        }
    }

    @Override
    public void longClick(int adapter, ImageView img, ImageView check, int pos) {

    }

    @Override
    public void createHolder(int adapter, View holder, int pos) {

    }

    @Override
    public void createContentHolder(int adapter, View[] content, int pos) {

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
