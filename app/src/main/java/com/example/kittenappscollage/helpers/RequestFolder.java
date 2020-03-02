package com.example.kittenappscollage.helpers;

import android.content.Context;
import android.os.Environment;

import java.io.File;

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
