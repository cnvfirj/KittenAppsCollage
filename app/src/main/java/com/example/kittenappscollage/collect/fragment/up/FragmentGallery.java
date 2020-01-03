package com.example.kittenappscollage.collect.fragment.up;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kittenappscollage.R;
import com.example.kittenappscollage.collect.adapters.up.LoadFoldAdapt;
import com.example.kittenappscollage.collect.adapters.up.LoadImgAdapt;

public class FragmentGallery extends FragmentScanAllImages {

    private RecyclerView recycler;

    private LoadFoldAdapt foldAdapt;

    private LoadImgAdapt imgAdapt;

    private final String RUT = "-1";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        foldAdapt = new LoadFoldAdapt(getContext());
        imgAdapt = new LoadImgAdapt(getContext());
        foldAdapt.setParams(getContext().getResources().getDisplayMetrics().widthPixels);
        imgAdapt.setParams(getContext().getResources().getDisplayMetrics().widthPixels);
        scanDevice();
        foldAdapt.setAll(getListImagesInFolders());
        imgAdapt.setAll(getListImagesInFolders());
        return inflater.inflate(R.layout.fragment_gallery,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
    }

    private void init(View view){
        recycler = view.findViewById(R.id.gallery_list);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new GridLayoutManager(getContext(),2));
        recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState==0){
                    /*передаем в адаптер стоп отображение*/

                }
                if(newState==1){
                    /*передаем в адаптнр старт отображение*/
                }

            }
        });
        recycler.setAdapter(foldAdapt);


    }


}
