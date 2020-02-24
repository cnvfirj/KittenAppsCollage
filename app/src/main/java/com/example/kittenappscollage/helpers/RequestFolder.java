package com.example.kittenappscollage.helpers;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.example.kittenappscollage.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static com.example.kittenappscollage.helpers.Massages.LYTE;

/**
 * Created by Admin on 11.02.2018.
 */

public class RequestFolder {
    /*внешнее хранилище*/
    public final static String GENERAL_FOLDER = "/Kitten Paint Folder";

    public final static String FOLDER_IMAGES = "/My Collages";
    public final static String FOLDER_SHRIFT = "/fonts";
    /*доступен только из приложения*/
    /*внутреннее хранилище*/
    public final static String FOLDER_CASH = "/cash";
    /*общий адрес для видимых из других приложения папок*/
    public final static String GENERAL = Environment.getExternalStorageDirectory().getPath();


    /*получаем директорию доступную только из приложения*/
    public static String getPersonalFolder(Context context){
        return context.getFilesDir().getPath();
    }

    public static String getCacheFolder(Context context){
        return getPersonalFolder(context)+FOLDER_CASH;

    }

    public static String getFolderCollages(Context context){
        String fold = context.getExternalFilesDir(null).getAbsolutePath().split("Android")[0]+GENERAL_FOLDER+FOLDER_IMAGES;
        return fold;
    }
    public static String getMainFolder(){
        return GENERAL+GENERAL_FOLDER;
    }

    /*проверяем наличие папки, если ее нет создаем
    *в зависимости от успеха возвращаем да/нет*/
    public static boolean testFolder(File file){
        boolean success = true;
        if(!file.exists()){
            success = file.mkdirs();
        }
        return success;
    }

}
