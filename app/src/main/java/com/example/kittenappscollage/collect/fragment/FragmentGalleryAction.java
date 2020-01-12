package com.example.kittenappscollage.collect.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.FileUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.kittenappscollage.collect.dialogActions.DialogAction;
import com.example.kittenappscollage.collect.dialogActions.ListenActions;

import java.io.File;

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
        LYTE("d "+done+"|"+getKey());
        for (String f:getSelectFiles()){
//            if(action==ACTION_DELETE){
//                File d = new File(f);
//                LYTE("delete "+d.delete());
//                getContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(d)));
//            }
        }

    }

    /*это применимо только в корневом адаптере*/
    @Override
    public void result(boolean done, String name) {
        if(done&&!name.isEmpty()) {
//            getListFolds();
//            getListImagesInFolders();
//            getUnder();
            String oldFold = getSelectFiles().get(0);
            String[] splitFold = oldFold.split("[/]");
            String oldName = splitFold[splitFold.length-1];
            String excludeNameFold = oldFold.split(oldName)[0];
            String newFold = excludeNameFold+name;
            LYTE("old fold in rename "+oldFold);
            LYTE("old name "+oldName);
            LYTE("key name "+getKey());
            LYTE("ex fold in rename "+excludeNameFold);
            LYTE("new fold in rename "+newFold);
            File oldfile = new File(oldFold);
            File newfile = new File(newFold);
//            if(oldfile.renameTo(newfile)){
//                getContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(newfile)));
//                getContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(oldfile)));
//
//            }



//            String[] splitOldName = getUnder().get(0).split("[/]");
//            String newName = splitOldName[splitOldName.length-1];
//            String[] split = getSelectFiles().get(0).split("[/]");
//            String newPath = getSelectFiles().get(0).split(split[split.length - 1])[0] + name;
//            LYTE(getUnder().get(0));
//            File oldfile = new File(getUnder().get(0));
//            File newfile = new File(newPath+"/"+newName);
//            boolean b = oldfile.renameTo(newfile);
//            LYTE("rename "+b);


        }
    }
}
