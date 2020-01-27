package com.example.kittenappscollage.draw.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.documentfile.provider.DocumentFile;

import com.example.kittenappscollage.helpers.AllPermissions;
import com.example.kittenappscollage.helpers.Massages;
import com.example.kittenappscollage.helpers.SaveImageToFile;
import com.example.kittenappscollage.draw.repozitoryDraw.RepDraw;
import com.example.kittenappscollage.helpers.rx.ThreadTransformers;

import java.io.IOException;
import java.io.OutputStream;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.functions.Consumer;

import static com.example.kittenappscollage.helpers.Massages.LYTE;

/*здесь обработаем нажатие кнопок сохранения*/

public class SavedKollagesFragmentDraw extends AddLyrsFragmentDraw {


    private final String MIME_PNG = "image/png";

    public static final int REQUEST_SAVED = 91;

    private final int REQUEST_PERM_SAVE = 901;

    private final int REQUEST_FOLDER = 902;

    private final String KEY_PERM_SAVE = "perm save";

    private final String ZHOPA = "(_!_)";

    public void reSave(){
        saved();
    }

    @Override
    protected void saveNet(ImageView v) {
        super.saveNet(v);
        if (AllPermissions.create().activity(getActivity()).reqSingle(AllPermissions.STORAGE).isStorage()) {
            requestFold();
        } else {
            AllPermissions.create().activity(getActivity()).callDialog(AllPermissions.STORAGE,REQUEST_SAVED);
        }
    }

    @Override
    protected void saveTel(ImageView v) {
        super.saveTel(v);
        if (AllPermissions.create().activity(getActivity()).reqSingle(AllPermissions.STORAGE).isStorage()) {
            saved();
        } else {
            AllPermissions.create().activity(getActivity()).callDialog(AllPermissions.STORAGE,REQUEST_SAVED);
        }
    }

    @Override
    protected void slideSave(ImageView v) {
        if (version()&&v!=null){
            v.setSelected(!v.isSelected());
            saved();
        }
        else super.slideSave(v);
    }

    private void saved(){
        if (version())saveAPI21();
        else saveAPI29();
    }

    private boolean version(){
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.Q;
    }

    private void saveAPI29(){
       String data = getPreferences().getString(KEY_PERM_SAVE,ZHOPA);
       if(data.equals(ZHOPA)){
           requestFold();
       }else {
           requestSaveFile(Uri.parse(data))
           .subscribe(new Consumer<Boolean>() {
               @Override
               public void accept(Boolean aBoolean) throws Exception {
                   if(aBoolean) Massages.SHOW_MASSAGE(getContext(), "Изображение сохранено");
                   else {
                       Massages.SHOW_MASSAGE(getContext(), "Изображение не сохранено. Проверь память устройства");
                   }
               }
           });
       }
    }

    private void saveAPI21(){
        if(RepDraw.get().isImg())SaveImageToFile.saveImage(getContext(),RepDraw.get().getImg());
    }

    private boolean saved(Uri uri){
        try {
            DocumentFile pickedDir = DocumentFile.fromTreeUri(getContext(),uri);
            DocumentFile img = pickedDir.createFile(MIME_PNG, RepDraw.PropertiesImage.NAME_IMAGE());
            OutputStream out = getContext().getContentResolver().openOutputStream(img.getUri());
            boolean save = RepDraw.get().getImg().compress(Bitmap.CompressFormat.PNG, 100, out);
            out.close();
            return save;

        } catch (IOException e) {
            return false;
        }

    }

    private void requestFold(){
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

    private boolean createFolder(Uri uri){
        int takeFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION;
        getContext().getContentResolver().takePersistableUriPermission(uri, takeFlags);
        DocumentFile pickedDir = DocumentFile.fromTreeUri(getContext(), uri);
        boolean exists = pickedDir.exists();
        if(exists) {
            getEditor().putString(KEY_PERM_SAVE, uri.toString());
            getEditor().apply();
        }
        return exists;
    }

    private Observable<Boolean> requestSaveFile(Uri perm){
        return Observable.create((ObservableOnSubscribe<Boolean>) emitter -> {
            emitter.onNext(saved(perm));
            emitter.onComplete();
        }).compose(new ThreadTransformers.InputOutput<>())
                .onErrorResumeNext(new Observable<Boolean>() {
                    @Override
                    protected void subscribeActual(Observer<? super Boolean> observer) {
                        LYTE("Error - "+perm.toString());
                        getEditor().putString(KEY_PERM_SAVE, ZHOPA);
                        getEditor().apply();
                        saveAPI29();
                    }
                });
    }
}
