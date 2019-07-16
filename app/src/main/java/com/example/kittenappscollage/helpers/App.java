package com.example.kittenappscollage.helpers;

import android.content.Context;

public class App {
    private static Context main;

    private static Context child;

    public static Context getMain() {
        return main;
    }

    public static void setMain(Context context) {
        App.main = context;
    }

    public static void  setChild(Context context){
        App.child = context;
    }
}
