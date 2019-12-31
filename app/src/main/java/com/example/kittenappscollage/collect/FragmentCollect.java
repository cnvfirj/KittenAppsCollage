package com.example.kittenappscollage.collect;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.kittenappscollage.R;
import com.example.kittenappscollage.collect.adapters.FileAdapter;
import com.example.kittenappscollage.collect.adapters.SelectorAdapter;
import com.example.kittenappscollage.collect.adapters.SuperAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import static com.example.kittenappscollage.helpers.Massages.LYTE;

public class FragmentCollect extends Fragment {

    private BottomNavigationViewEx navigation;

    private RecyclerView recycler;

//    private FileAdapter adapter;

    private int indexAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        indexAdapter = FileAdapter.SOURCE_PROJECT;
        return inflater.inflate(R.layout.fragment_collect,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        adapter = SelectorAdapter.get(getContext()).adapter(FileAdapter.SOURCE_PROJECT);
        init(view);
    }

    private void init(View view){
        SelectorAdapter.get(getContext()).setParams(getContext().getResources().getDisplayMetrics().widthPixels);
        recycler = view.findViewById(R.id.gallery_list);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new GridLayoutManager(getContext(),3));
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
        recycler.setAdapter(SelectorAdapter.get(getContext()).adapter(indexAdapter));
        navigation = view.findViewById(R.id.gallery_navigation_ex);
        navigation.inflateMenu(R.menu.collect_navigation);
//        navigation.setTextVisibility(false);
        navigation.enableAnimation(false);
        navigation.enableShiftingMode(true);
        navigation.setTextSize(5);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if(menuItem.getItemId()==R.id.m_navigation_gallery){
                    indexAdapter = FileAdapter.SOURCE_PROJECT;
                }else if(menuItem.getItemId()==R.id.m_navigation_photo){
                    indexAdapter = FileAdapter.SOURCE_PHOTO;
                }else if(menuItem.getItemId()==R.id.m_navigation_down){
                    indexAdapter = FileAdapter.SOURCE_DOWNLOAD;
                }
                recycler.setAdapter(SelectorAdapter.get(getContext()).adapter(indexAdapter));
                return true;
            }
        });
    }


}
