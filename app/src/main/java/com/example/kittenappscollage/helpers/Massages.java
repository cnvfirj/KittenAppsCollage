package com.example.kittenappscollage.helpers;


import android.content.Context;
import android.util.Log;

public class Massages {
    private static final String TAG = "MASSAGE APP";

    public static void ERROR(String text, Context c){
        Log.e(TAG,"class name - "+getName(c)+" error - "+text);
    }

    public static void MASSAGE(String text, Context c){
        Log.d(TAG,"class name - "+getName(c)+" massage - "+text);
    }

    private static String getName(Context c){
        return c.getClass().getName();
    }
}
