package com.example.kittenappscollage.draw.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.documentfile.provider.DocumentFile;

import com.example.kittenappscollage.helpers.AllPermissions;
import com.example.kittenappscollage.helpers.App;
import com.example.kittenappscollage.helpers.Massages;
import com.example.kittenappscollage.helpers.RequestFolder;
import com.example.kittenappscollage.draw.repozitoryDraw.RepDraw;
import com.example.kittenappscollage.helpers.dbPerms.WorkDBPerms;
import com.example.kittenappscollage.helpers.rx.ThreadTransformers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.functions.Consumer;

import static com.example.kittenappscollage.helpers.Massages.LYTE;
import static com.example.kittenappscollage.helpers.Massages.SHOW_MASSAGE;

public class SavedKollagesFragmentDraw extends AddLyrsFragmentDraw {

    private final String MIME_PNG = "image/png";

    private final String REPORT_DELIMITER = "_%%_Saved_%%_Kollages_%%_Fragment_&&_Draw_%%_";

    public static final int INDEX_PATH_IMG = 0;

    public static final int INDEX_PATH_FOLD = 1;

    public static final int INDEX_URI_PERM_FOLD = 2;

    public static final int INDEX_URI_DF_IMG = 3;

    public static final int INDEX_URI_DF_FOLD = 4;

    public static final int INDEX_NAME_IMG = 5;

    public static final int INDEX_TYPE_SYSTEM = 6;

    public static final int REQUEST_SAVED = 91;

    private final int REQUEST_PERM_SAVE = 901;

    private final int REQUEST_FOLDER = 902;

    private final String KEY_PERM_SAVE = "perm save";

    private final String ZHOPA = "(_!_)";

    private ActionSave report;

    public void reSave(){
        saveAPI29();
    }

    @Override
    protected void saveIs(ImageView v) {
        super.saveIs(v);

//        if(ContextCompat.getExternalFilesDirs(getContext(), null).length>1) {
            if (RepDraw.get().isImg()) {
                if (AllPermissions.create().activity(getActivity()).reqSingle(AllPermissions.STORAGE).isStorage()) {
                    requestFold();
                } else {
                    AllPermissions.create().activity(getActivity()).callDialog(AllPermissions.STORAGE, REQUEST_SAVED);
                }
            } else Massages.SHOW_MASSAGE(getContext(), "Создай холст");
//        }else Massages.SHOW_MASSAGE(getContext(),"Подключи SD card или накопитель и перезапусти приложение");
    }

    @Override
    protected void saveNet(ImageView v) {
        super.saveNet(v);
        /*расшарить изображение*/
        if(RepDraw.get().isImg()) share();
        else Massages.SHOW_MASSAGE(getContext(), "Создай холст");
    }

    @Override
    protected void saveTel(ImageView v) {
        super.saveTel(v);
        if(RepDraw.get().isImg()) {
            if (AllPermissions.create().activity(getActivity()).reqSingle(AllPermissions.STORAGE).isStorage()) {
                saveAPI29();
            } else {
                AllPermissions.create().activity(getActivity()).callDialog(AllPermissions.STORAGE, REQUEST_SAVED);
            }
        }else Massages.SHOW_MASSAGE(getContext(), "Создай холст");
    }

    @SuppressLint("CheckResult")
    private void saveAPI29(){
       String data = getPreferences().getString(KEY_PERM_SAVE,ZHOPA);
       if(data.equals(ZHOPA)){
           requestFold();
       }else {
           requestSaveFile(Uri.parse(data))
           .subscribe(new Consumer<String>() {
               @Override
               public void accept(String str) throws Exception {
                   if(!str.equals(ZHOPA)){
                       Massages.SHOW_MASSAGE(getContext(), "Изображение сохранено");
                       reportSave(str);
                   } else {
                       Massages.SHOW_MASSAGE(getContext(), "Изображение не сохранено. Проверь память устройства");
                   }
               }
           });
       }
    }

    /*получаем доклад о сохранении
    * [REPORT_DELIMITER] - разделитель строки на сегменты
    *
    * */
    private void reportSave(String path){
        report = (ActionSave)getContext();
        if(report!=null){
            report.request(false);
            report.savedStorage(true,path,REPORT_DELIMITER);
        }

    }


    private void saveAPI21(){
        if(RepDraw.get().isImg())saveImage(RepDraw.get().getImg());
    }

    private String saved(Uri uri){
        try {
            final String nameImg = RepDraw.PropertiesImage.NAME_IMAGE();
            DocumentFile dir = DocumentFile.fromTreeUri(getContext(),uri);
            DocumentFile img = dir.createFile(MIME_PNG, nameImg);
            OutputStream out = getContext().getContentResolver().openOutputStream(img.getUri());
            boolean save = RepDraw.get().getImg().compress(Bitmap.CompressFormat.PNG, 100, out);
            out.close();

            String sDir = getRealPath(dir.getUri().getLastPathSegment());
            String sImg = getRealPath(img.getUri().getLastPathSegment());
            String system = ZHOPA;

            if(save){
                String report =
                        sImg+                   REPORT_DELIMITER+ //адрез изображения
                        sDir+                   REPORT_DELIMITER+ //адрес папки
                        uri.toString()+         REPORT_DELIMITER+ //разрешение на редакт папки
                        img.getUri().toString()+REPORT_DELIMITER+ //юри дф изображения
                        dir.getUri().toString()+REPORT_DELIMITER+ //юри дф папки
                        nameImg+                REPORT_DELIMITER+ //имя изображения
                        system;                                   //файловая система в папке, если она до этого
                                                                  //не дф, то надо пересканировать всю папку
                return report;
            }
            else return ZHOPA;
        } catch (IOException e) {
            return ZHOPA;
        }
    }

    private void requestFold(){
        report = (ActionSave)getContext();
        if(report!=null)report.request(true);
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        startActivityForResult(intent, REQUEST_FOLDER);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode== Activity.RESULT_OK){
        if(requestCode==REQUEST_PERM_SAVE) {

        }else if(requestCode==REQUEST_FOLDER){
            if(createFolder(data.getData()))saveAPI29();
            else Massages.SHOW_MASSAGE(getContext(), "Изображение не сохранено. Проверь папку сохранения");
        }
        }super.onActivityResult(requestCode, resultCode, data);
    }

    /*метод не создает папку а находит. Создание происходит в пользовательском
    * интерфейсе САФ*/
    private boolean createFolder(Uri uri){
        int takeFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION ;
        getContext().getContentResolver().takePersistableUriPermission(uri, takeFlags);
        DocumentFile fold = DocumentFile.fromTreeUri(getContext(), uri);
        boolean exists = fold.exists();
        if(exists) {
            WorkDBPerms.get(getContext()).setAction(WorkDBPerms.INSERT,fold.getUri().toString());
            getEditor().putString(KEY_PERM_SAVE, fold.getUri().toString());
            getEditor().apply();
        }

        return exists;
    }

    private String getRealPath(String lastSegment) {
        String[]split = lastSegment.split("[:]");
        String[]sub = lastSegment.split(split[0]+":");
        String[]storage = getContext().getExternalFilesDir(null).getAbsolutePath().split("[/]");
        return "/"+storage[1]+"/"+split[0]+"/"+sub[1];
    }

    /**/
    private Observable<String> requestSaveFile(Uri perm){
        return Observable.create((ObservableOnSubscribe<String>) emitter -> {
            emitter.onNext(saved(perm));
            emitter.onComplete();
        }).compose(new ThreadTransformers.InputOutput<>());
//                .doOnError(new Consumer<Throwable>() {
//                    @Override
//                    public void accept(Throwable throwable) throws Exception {
//                        LYTE("SavedKollagesFragmentDraw Error save - "+perm.toString());
//                        getEditor().putString(KEY_PERM_SAVE, ZHOPA);
//                        getEditor().apply();
//                        saveAPI29();
//                    }
//                });
//                .onErrorResumeNext(new Observable<String>() {
//                    @Override
//                    protected void subscribeActual(Observer<? super String> observer) {
//                        LYTE("SavedKollagesFragmentDraw Error - "+perm.toString());
//                        getEditor().putString(KEY_PERM_SAVE, ZHOPA);
//                        getEditor().apply();
//                        saveAPI29();
//                    }
//                });
    }

    private void share(){
        final String folder = RequestFolder.getPersonalFolder(getContext());
        final String name = folder+"/collage share.png";
        final File file = new File(name);
        OutputStream os = null;
        try {
            os = new FileOutputStream(file);
            RepDraw.get().getImg().compress(Bitmap.CompressFormat.PNG, 100, os);
            os.flush();
            os.close();
        } catch (IOException e) { }
        if(file.exists()) {
            try {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Uri uri = FileProvider.getUriForFile(getContext(), "com.example.kittenappscollage.fileprovider", file);
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.putExtra(Intent.EXTRA_STREAM, uri);
                    intent.setType("image/png");
                } else {
                    intent.setDataAndType(Uri.fromFile(file), "image/png");
                }
                startActivity(Intent.createChooser(intent, null));
            } catch (ActivityNotFoundException anfe) { }
        }
    }

    /*сохраняем в рисунок*/
    @SuppressLint("CheckResult")
    private  void saveImage(Bitmap bitmap) {

//        if(bitmap==null||bitmap.isRecycled())return;
//
//        final File folder = new File(RequestFolder.getFolderCollages(getContext()));
//        final String name = RepDraw.PropertiesImage.NAME_IMAGE();
//        if(RequestFolder.testFolder(folder)) {
//            final File image = new File(folder.getAbsolutePath() + "/"+ name);
//            requestSaveFile(image, bitmap)
//                    .subscribe(aBoolean -> {
//                        report = (ActionSave)getContext();
//                        if(report!=null){
//                            report.savedFile(aBoolean,
//                                    folder.getAbsolutePath(),
//                                    image.getAbsolutePath(),
//                                    name);
//                        }else SHOW_MASSAGE(getContext(), "перезапусти приложение");
//                        if (aBoolean) {
//                            SHOW_MASSAGE(getContext(), "изображение сохранено");
//                        }
//                        else SHOW_MASSAGE(getContext(), "ошибка сохранения");
//                    });
//        }else {
//            SHOW_MASSAGE(getContext(), "проверь память устройства");
//        }

    }


    private Observable<Boolean> requestSaveFile(File file, Bitmap bitmap){
        return Observable.create((ObservableOnSubscribe<Boolean>) emitter -> {
            emitter.onNext(save(file, bitmap));
            emitter.onComplete();


        }).compose(new ThreadTransformers.InputOutput<>());
    }



    private boolean save(File file, Bitmap bitmap){

        OutputStream os = null;
        try {
            os = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
            os.flush();
            os.close();
        } catch (IOException e) {

        }
        return file.exists();
    }

    public interface ActionSave{
        public void request(boolean block);
        public void savedFile(boolean saved, String fold, String img, String name);
        public void savedStorage(boolean saved, String report, String delimiter);
    }
}
