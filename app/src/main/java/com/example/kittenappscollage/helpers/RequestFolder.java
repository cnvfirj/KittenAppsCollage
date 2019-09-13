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
    public final static String FOLDER_IMAGES = "/images";
    public final static String FOLDER_PROJECTS = "/projects";
    public final static String FOLDER_PHOTO = "/photo";
    /*префиксы в папке проекта*/
    public final static String NAME_PROP = "prop.proj";
    public final static String NAME_IMAGE = "img.png";
    public final static String NAME_LAYER = "lyr.png";
    /*доступен только из приложения*/
    /*внутреннее хранилище*/
    public final static String FOLDER_CASH = "/cash";

    public final static String FOLDER_ICONS = "/icons";
    /*общий адрес для видимых из других приложения папок*/
    public final static String GENERAL = Environment.getExternalStorageDirectory().getPath();

    public final static String DCIM = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString();


    /*получаем директорию доступную только из приложения*/
    public static String getPersonalFolder(Context context){
        return context.getFilesDir().getPath();
    }

    public static String getCameraDefaultFolder(){
        return DCIM+"/Camera";
    }
    public static String getCacheFolder(Context context){
//            return getPersonalFolder(context) + FOLDER_CASH ;
//        return GENERAL+GENERAL_FOLDER+FOLDER_CASH;
        return getPersonalFolder(context)+FOLDER_CASH;

    }
    public static String getPathCacheImg(Context context, String key){
        return getCacheFolder(context)+"/"+key+".png";
    }
    public static String getMainFolder(){
        return GENERAL+GENERAL_FOLDER;
    }

    public static String getFolderImages(){
        return getMainFolder()+FOLDER_IMAGES;
    }

    public static String getFolderPhotos(){
        return DCIM;
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
