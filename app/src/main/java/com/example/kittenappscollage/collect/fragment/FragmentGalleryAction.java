package com.example.kittenappscollage.collect.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.kittenappscollage.collect.dialogActions.DialogAction;
import com.example.kittenappscollage.collect.dialogActions.ListenActions;

import java.io.File;

import static com.example.kittenappscollage.collect.adapters.ListenLoadFoldAdapter.ROOT_ADAPTER;
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
        for (String s:getSelectFiles()) {
            LYTE("invis fold "+s);
        }
    }

    @Override
    protected void clickSel_2(ImageView v) {
        super.clickSel_2(v);
        /*переименовать*/
        /*візов диалога*/
        for (String s:getSelectFiles()) {
            LYTE("rename fold "+s);
        }
    }

    @Override
    protected void clickSel_3(ImageView v) {
        super.clickSel_3(v);
        if(getIndexAdapter()== ROOT_ADAPTER){
            /*переместить на карту*/
            /*візов диалога*/
            for (String s:getSelectFiles()) {
                LYTE("exp in sd fold "+s);
            }
        }else {
            /*поделиться вібранное*/
            /*візов диалога*/
            LYTE("share sel");
        }
    }

    @Override
    protected void clickSel_4(ImageView v) {
        super.clickSel_4(v);
        if(getIndexAdapter()== ROOT_ADAPTER){
            /*удалить папку*/
            /*візов диалога*/
            for (String s:getSelectFiles()) {
                LYTE("del fold "+s);
            }
        }else {
            /*удалить вібраное*/
            /*візов диалога*/
            for (String s:getSelectFiles()) {
                LYTE("del sel "+s);
            }
        }
        DialogAction.inst(DialogAction.ACTION_DELETE, getIndexAdapter(),this).show(getFragmentManager().beginTransaction(),TAG_DIALOG);
    }

    @Override
    public void result(boolean done, int action) {
        LYTE("result "+done);
    }

    @Override
    public void result(boolean done, String name) {

    }
}
