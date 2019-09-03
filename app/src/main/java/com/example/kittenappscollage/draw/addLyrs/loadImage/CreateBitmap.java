package com.example.kittenappscollage.draw.addLyrs.loadImage;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;

import com.example.kittenappscollage.helpers.rx.ThreadTransformers;

import java.net.MalformedURLException;
import java.net.URL;

import io.reactivex.Observable;

/**
 * Created by Admin on 20.01.2018.
 */

public class CreateBitmap {

    public static Bitmap createNew(Point size){
        return Bitmap.createBitmap(size.x,size.y, Bitmap.Config.RGB_565);
    }

    public static Bitmap writeImageToFile(String path){
        return BitmapFactory.decodeFile(path);
    }

    public static Observable createRequestInArr(byte[]data){
        return Observable.create(e->{
            try {
                e.onNext(BitmapFactory.decodeByteArray(data,0,data.length));

            }catch (Exception ex){
                e.onNext(createNew(new Point(1,1)));
            }
        }).compose(new ThreadTransformers.Processor<>());
    }


    public static Observable createRequestInFile(String path){
        return Observable.create(e -> {
            try {
                Bitmap b = BitmapFactory.decodeFile(path);
                if(b==null){
                    e.onNext(createNew(new Point(1,1)));
                }else {
                    e.onNext(b);
                }
            }catch (Exception ex){
                /*если получаем ошибку то возвращаем битмап из одного пикселя*/
                e.onNext(createNew(new Point(1,1)));
            }
            e.onComplete();
        }).compose(new ThreadTransformers.Processor<>());
    }

    public static Observable createBitmapRequestInNet(URL url){
        return Observable.create(e -> {
            try {
                Bitmap b = BitmapFactory.decodeStream(url.openConnection().getInputStream());

                if(b==null){
                    e.onNext(createNew(new Point(1,1)));
                }else {
                    e.onNext(b);
                }
            }catch (Exception ex){
                /*если получаем ошибку то возвращаем битмап из одного пикселя*/
                e.onNext(createNew(new Point(1,1)));
            }
            e.onComplete();
        }).compose(new ThreadTransformers.InputOutput<>());
    }

//    private static Bitmap mutable(Bitmap bitmap){
//       return bitmap.copy(Bitmap.Config.ARGB_8888,true);
//    }





    public static URL createURL(String url){
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }




}

