package com.example.kittenappscollage.helpers;


import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class Massages {
    private static final String TAG = "_____MASSAGE";

    public static void ERROR(String text, Class c){
        Log.e(TAG,"class name - "+getName(c)+" |error - "+text);
    }

    public static void MASSAGE(String text, Class c){
        Log.d(TAG,"class name - "+getName(c)+" || "+text);
    }

    private static String getName(Class c){
        if (c!=null) return c.getClass().getSimpleName();
        else return "anon class";
    }

    public static void SHOW_MASSAGE(Context context, String text){
        Toast.makeText(context,text,Toast.LENGTH_SHORT).show();
    }
}
