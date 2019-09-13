package com.example.kittenappscollage.helpers;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Admin on 07.02.2018.
 */

public class PropertiesImage {

    public static String MIME_TYPE_IMAGE = "image/png";

    public static String NAME_IMAGE(){

        return date()+".png";
    }

    public static String NAME_PROJECT(){
       return date();
    }

    private static String date(){
        Calendar calendar = new GregorianCalendar();

        int year = calendar.get(Calendar.YEAR);
        int day = calendar.get(Calendar.DAY_OF_YEAR);
        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);

        int second_of_day = ((hour*60)+minute)*60+second;

        return year+"_"+day+"_"+second_of_day;
    }
}
