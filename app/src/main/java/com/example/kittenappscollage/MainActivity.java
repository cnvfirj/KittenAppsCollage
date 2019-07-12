package com.example.kittenappscollage;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.kittenappscollage.collect.FragmentCollect;
import com.example.kittenappscollage.draw.FragmentDraw;

public class MainActivity extends AppCompatActivity {

    private FragmentDraw mFragDraw;

    private FragmentCollect mFragColl;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    private void initVar(){
        mFragDraw = new FragmentDraw();
        mFragColl = new FragmentCollect();
    }

}
