package com.example.kittenappscollage.helpers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;

import com.example.kittenappscollage.helpers.rx.ThreadTransformers;
import com.example.kittenappscollage.draw.repozitoryDraw.RepDraw;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;

import static com.example.kittenappscollage.helpers.Massages.LYTE;
import static com.example.kittenappscollage.helpers.Massages.SHOW_MASSAGE;
import static com.example.kittenappscollage.helpers.RequestFolder.testFolder;


public class SaveImageToFile {


    private static byte quality = 100;

    private static ActionSave report;

    public static void setReport(ActionSave r){
        report = r;
    }

    /*сохраняем в рисунок*/
    @SuppressLint("CheckResult")
    public static void saveImage(Context context, Bitmap bitmap) {

        if(bitmap==null||bitmap.isRecycled())return;

        quality = 100;

        final File folder = new File(RequestFolder.getFolderImages());
        final String name = "/"+ RepDraw.PropertiesImage.NAME_IMAGE();
        if(RequestFolder.testFolder(folder)) {
            requestSaveFile(folder.getAbsolutePath() + name, bitmap)
                    .subscribe(aBoolean -> {
                        if(report!=null)report.saved(aBoolean,folder.getAbsolutePath() + name);
                        if (aBoolean) {
                            SHOW_MASSAGE(context, "изображение сохранено");
                        }
                        else SHOW_MASSAGE(context, "ошибка сохранения");
                    });
        }else {
            SHOW_MASSAGE(context, "проверь память устройства");
        }

    }
@SuppressLint("CheckResult")
    public static void saveImage(Context context, Bitmap bitmap, String name) {

        if(bitmap==null||bitmap.isRecycled())return;

        quality = 100;
        final File folder = new File(RequestFolder.getFolderImages());
//        final String name = "/"+ RepDraw.PropertiesImage.NAME_IMAGE();
        if(RequestFolder.testFolder(folder)) {
            requestSaveFile(folder.getAbsolutePath() + name, bitmap)
                    .subscribe(aBoolean -> {
                        if(report!=null)report.saved(aBoolean);
                        if (aBoolean) SHOW_MASSAGE(context, "image saved");
                        else SHOW_MASSAGE(context, "error saved");
//                        EventBus.getDefault().post(new FragmentCollections.SaveFile(aBoolean));
                    });
        }else {
            SHOW_MASSAGE(context, "check device memory");
        }

    }

     @SuppressLint("CheckResult")
    public static void saveImage(Bitmap bitmap, ActionSave act) {
        if(bitmap==null||bitmap.isRecycled())return;
        quality = 100;
        final File folder = new File(RequestFolder.getFolderImages());
        final String name = "/"+ RepDraw.PropertiesImage.NAME_IMAGE();
        if(testFolder(folder)) {
            requestSaveFile(folder.getAbsolutePath() + name, bitmap)
                    .subscribe(aBoolean -> {
                        act.saved(aBoolean);

                    });
        }

    }

      @SuppressLint("CheckResult")
    public static void saveImage(ActionSave act, Bitmap bitmap, String name) {
        if(bitmap==null||bitmap.isRecycled())return;
        quality = 100;
        final File folder = new File(RequestFolder.getFolderImages());
//        final String name = "/"+ RepDraw.PropertiesImage.NAME_IMAGE();
        if(testFolder(folder)) {
            requestSaveFile(folder.getAbsolutePath() + name, bitmap)
                    .subscribe(aBoolean -> {
                        act.saved(aBoolean);

                    });
        }

    }





    private static Observable<Boolean> requestSaveFile(String path, Bitmap bitmap){
        return Observable.create((ObservableOnSubscribe<Boolean>) emitter -> {
            emitter.onNext(save(path, bitmap));
            emitter.onComplete();


        }).compose(new ThreadTransformers.InputOutput<>());
    }



    private static boolean save(String path, Bitmap bitmap){
        final File file = new File(path);

        OutputStream os = null;
        try {
            os = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, quality, os);
            os.flush();
            os.close();
        } catch (IOException e) {

        }
        return file.exists();
    }

    public interface ActionSave{
        public void saved(boolean saved);
        public void saved(boolean saved, String path);
    }




}
