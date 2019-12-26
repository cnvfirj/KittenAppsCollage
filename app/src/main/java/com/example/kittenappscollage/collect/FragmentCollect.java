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

import com.bumptech.glide.Glide;
import com.example.kittenappscollage.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import static com.example.kittenappscollage.helpers.Massages.LYTE;

public class FragmentCollect extends Fragment {

    private BottomNavigationViewEx navigation;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_collect,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);
    }

    private void init(View view){
        navigation = view.findViewById(R.id.gallery_navigation_ex);
        navigation.inflateMenu(R.menu.collect_navigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                if(menuItem.getItemId()==R.id.m_navigation_gallery){
                    LYTE("item g");
                }else if(menuItem.getItemId()==R.id.m_navigation_photo){
                    LYTE("item p");
                }else if(menuItem.getItemId()==R.id.m_navigation_down){
                    LYTE("item d");
                }
                return false;
            }
        });
    }
}
