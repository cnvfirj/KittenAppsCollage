package com.example.kittenappscollage.draw.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
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

    public static final int INDEX_NAME_FOLD = 6;

    public static final int INDEX_TYPE_SYSTEM = 7;

    public static final int REQUEST_SAVED = 91;

    private final int REQUEST_PERM_SAVE = 901;

    private final int REQUEST_FOLDER = 902;

    private final String KEY_PERM_SAVE = "perm save";

    private final String ZHOPA = "(_!_)";

    private final String NOT_DIR = "not dir";

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
                   if(str.equals(NOT_DIR)){
                       requestFold();
                   }else if(str.equals(ZHOPA)){
                       Massages.SHOW_MASSAGE(getContext(), "Изображение не сохранено. Проверь память устройства");
                   } else {
                       Massages.SHOW_MASSAGE(getContext(), "Изображение сохранено");
                       reportSave(str);
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

    private String saved(Uri uri){
        try {
            final String nameImg = RepDraw.PropertiesImage.NAME_IMAGE();
            DocumentFile dir = DocumentFile.fromTreeUri(getContext(),uri);
            if(!dir.exists())return NOT_DIR;
            DocumentFile img = dir.createFile(MIME_PNG, nameImg);
            OutputStream out = getContext().getContentResolver().openOutputStream(img.getUri());
            boolean save = RepDraw.get().getImg().compress(Bitmap.CompressFormat.PNG, 100, out);
            out.close();

            final String sDir = getRealPath(dir.getUri().getLastPathSegment());
            final String sImg = getRealPath(img.getUri().getLastPathSegment());
            final String system = ZHOPA;
            final String sNameDir = dir.getName();

            if(save){
                String report =
                        sImg+                   REPORT_DELIMITER+ //адрез изображения
                        sDir+                   REPORT_DELIMITER+ //адрес папки
                        uri.toString()+         REPORT_DELIMITER+ //разрешение на редакт папки
                        img.getUri().toString()+REPORT_DELIMITER+ //юри дф изображения
                        dir.getUri().toString()+REPORT_DELIMITER+ //юри дф папки
                        nameImg+                REPORT_DELIMITER+ //имя изображения
                        sNameDir+                 REPORT_DELIMITER+//файловая система в папке, если она до этого
                                                                  //не дф, то надо пересканировать всю папку
                        system;
                WorkDBPerms.get(getContext()).addParams(dir.getUri().toString(),report,REPORT_DELIMITER);
                return report;
            }
            else return ZHOPA;
        } catch (IOException e) {
            return ZHOPA;
        }
    }

    protected Uri question(){
        Uri uri = null;
        if(App.checkVersion())uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        else uri = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL);
        return uri;
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
            WorkDBPerms.get(getContext()).setAction(WorkDBPerms.INSERT,fold.getUri().toString(), fold.getName());

            getEditor().putString(KEY_PERM_SAVE, fold.getUri().toString());
            getEditor().apply();
        }

        return exists;
    }

    private String getRealPath(String lastSegment) {
        String[]split = lastSegment.split("[:]");
        String[]sub = lastSegment.split(split[0]+":");
        String[]storage = getContext().getExternalFilesDir(null).getAbsolutePath().split("[/]");
        String p = "/"+storage[1]+"/"+split[0]+"/"+sub[1];
        if(p.startsWith("/storage/primary/")){
            String[]correct = p.split("/storage/primary/");
            return "/storage/emulated/0/"+correct[1];
        } else return p;
    }

    /**/
    private Observable<String> requestSaveFile(Uri perm){
        return Observable.create((ObservableOnSubscribe<String>) emitter -> {
            emitter.onNext(saved(perm));
            emitter.onComplete();
        }).compose(new ThreadTransformers.InputOutput<>());

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
