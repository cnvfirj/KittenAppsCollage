package com.example.kittenappscollage.helpers;


import android.content.Context;
import android.util.Log;

public class Massages {
    private static final String TAG = "MASSAGE APP";

    public static void ERROR(String text, Class c){
        Log.e(TAG,"class name - "+getName(c)+" error - "+text);
    }

    public static void MASSAGE(String text, Class c){
        Log.d(TAG,"class name - "+getName(c)+"(MASSAGE) - "+text);
    }

    private static String getName(Class c){
        if (c!=null) return c.getClass().getName();
        else return "anon class";
    }
}
