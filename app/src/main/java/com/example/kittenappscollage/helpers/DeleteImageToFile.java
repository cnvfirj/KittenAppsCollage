package com.example.kittenappscollage.helpers;

import android.content.Context;

import java.io.File;


/**
 * Created by Admin on 11.02.2018.
 */

public class DeleteImageToFile {


    public static void delete(Context context, String path) {
        File file = new File(path); //Создаем файловую переменную
        if (file.exists()) { //Если файл или директория существует
          if(file.delete()){
//              SHOW_MASSAGE(context, "file deleted");


          }
        }


    }
}
