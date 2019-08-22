package com.example.kittenappscollage.draw.addLyrs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.kittenappscollage.R;

import static com.example.kittenappscollage.helpers.Massages.MASSAGE;

public class AddLyr extends Fragment implements View.OnClickListener {


    private ImageView aBack, aDone, aClose, aMirror;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_add_lyr,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
    }

    private void initViews(View v){
        aBack = v.findViewById(R.id.dialog_back);
        aBack.setOnClickListener(this);
        aDone = v.findViewById(R.id.dialog_apply);
        aDone.setOnClickListener(this);
        aClose = v.findViewById(R.id.dialog_close);
        aClose.setOnClickListener(this);
        aMirror = v.findViewById(R.id.dialog_mirror);
        aMirror.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.dialog_back:
                pressBack((ImageView) view);
                break;
            case R.id.dialog_apply:
                pressDone((ImageView) view);
                break;
            case R.id.dialog_close:
                pressClose((ImageView) view);
                break;
            case R.id.dialog_mirror:
                pressMirror((ImageView) view);
                break;

        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private void pressMirror(ImageView view){
        view.setSelected(!view.isSelected());
    }

    private void pressDone(ImageView view){
        view.setSelected(!view.isSelected());
    }

    private void pressClose(ImageView view){
        view.setSelected(!view.isSelected());
    }

    private void pressBack(ImageView view){
        view.setSelected(!view.isSelected());
    }





}
