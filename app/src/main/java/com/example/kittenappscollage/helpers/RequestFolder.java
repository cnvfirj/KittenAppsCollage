package com.example.kittenappscollage.helpers;

import android.content.Context;
import android.os.Environment;

import com.example.kittenappscollage.R;

import java.io.File;

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
    public static String getMainFolder(){
        return GENERAL+GENERAL_FOLDER;
    }

    public static String getFolderImages(){
        return getMainFolder()+FOLDER_IMAGES;
    }

    public static String getNameFoldCollages(){
        return FOLDER_IMAGES.substring(1);
    }

    public static String getFolderShrift(){
        return getMainFolder()+FOLDER_SHRIFT;
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
