package com.example.kittenappscollage.helpers;

import android.content.Context;
import android.os.Build;

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

    public static boolean checkVersion(){
        return Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q;
    }
}
