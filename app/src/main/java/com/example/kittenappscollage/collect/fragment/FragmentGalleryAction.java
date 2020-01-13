package com.example.kittenappscollage.collect.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.kittenappscollage.collect.dialogActions.DialogAction;
import com.example.kittenappscollage.collect.dialogActions.ListenActions;

import java.io.File;
import java.util.ArrayList;

import static com.example.kittenappscollage.collect.adapters.ListenLoadFoldAdapter.ROOT_ADAPTER;
import static com.example.kittenappscollage.collect.dialogActions.DialogAction.ACTION_DELETE;
import static com.example.kittenappscollage.helpers.Massages.LYTE;

public class FragmentGalleryAction extends FragmentSelectedGallery implements ListenActions {

    private final String TAG_DIALOG = "dialog act";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void clickSel_1(ImageView v) {
        super.clickSel_1(v);
        /*скріть папку*/
        /*візов диалога*/
    }

    @Override
    protected void clickSel_2(ImageView v) {
        super.clickSel_2(v);
        /*переименовать*/
        /*візов диалога*/
        DialogAction.inst(DialogAction.ACTION_RENAME, getIndexAdapter(),this)
                .show(getFragmentManager().beginTransaction(),TAG_DIALOG);
    }

    @Override
    protected void clickSel_3(ImageView v) {
        super.clickSel_3(v);
        if(getIndexAdapter()== ROOT_ADAPTER){
            /*переместить на карту*/
            /*візов диалога*/
        }else {
            /*поделиться вібранное*/
            /*візов диалога*/
        }
    }

    @Override
    protected void clickSel_4(ImageView v) {
        super.clickSel_4(v);
        if(getIndexAdapter()== ROOT_ADAPTER){
            /*удалить папку*/
            /*візов диалога*/

        }else {
            /*удалить вібраное*/
            /*візов диалога*/

        }
        DialogAction.inst(ACTION_DELETE, getIndexAdapter(),this)
                .show(getFragmentManager().beginTransaction(),TAG_DIALOG);
    }

    @Override
    public void result(boolean done, int action, int indexAdapter) {
        invisibleMenu();

    }

    /*это применимо только в корневом адаптере*/
    /*применить другое сканирование почему то старую папку не удаляет*/
    @Override
    public void result(boolean done, String name) {
        invisibleMenu();
        if(done&&!name.isEmpty()) {
            String oldFold = getSelectFiles().get(0);
            String excludeNameFold = oldFold.split(getKey())[0];
            String newFold = excludeNameFold+name;

            File oldfile = new File(oldFold);
            File newfile = new File(newFold);
            if(oldfile.renameTo(newfile)){
                ArrayList<String>imgs = getListImagesInFolders().get(getKey());
                ArrayList<String >newImgs = new ArrayList<>();
                for (String path:imgs){
                    String[]split = path.split(getKey());
                    String p = split[0]+name+split[1];
                    newImgs.add(p);
                    getContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(p))));
                    getContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(path))));

                }
                getListImagesInFolders().remove(getKey());
                getListImagesInFolders().put(name,newImgs);
                getListFolds().remove(getKey());
                getListFolds().put(name, newFold);

//                getContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(newfile)));
//                getContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(oldfile)));

            }
                 getFoldAdapt().setAll(getListImagesInFolders());
                 getImgAdapt().setAll(getListImagesInFolders());

        }
    }
}
