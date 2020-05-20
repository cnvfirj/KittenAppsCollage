package com.example.kittenappscollage.menu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.kittenappscollage.R;
import com.example.kittenappscollage.helpers.Massages;

public class ListOffers extends Fragment implements View.OnClickListener {

    private TextView language;

    private TextView color;

    private MenuSwitching listener;

    public static ListOffers get(DialogMainMenu target,int request){
        ListOffers d = new ListOffers();
//        d.setTargetFragment(target,request);
        d.setListener(target);
        return d;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.menu_offers,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.menu_language).setOnClickListener(this);
        view.findViewById(R.id.menu_color).setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        if (listener!=null) {
            switch (v.getId()) {
                case R.id.menu_language:
                    listener.language();
                    break;
                case R.id.menu_color:
                    listener.color();
                    break;
            }
        }else Massages.SHOW_MASSAGE(getContext(), getString(R.string.restart_window));
    }

    private void setListener(MenuSwitching switching){
        listener = switching;
    }
}
