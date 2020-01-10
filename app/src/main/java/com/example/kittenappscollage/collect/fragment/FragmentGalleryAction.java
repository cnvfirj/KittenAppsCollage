package com.example.kittenappscollage.collect.fragment;

import android.widget.ImageView;

import static com.example.kittenappscollage.collect.adapters.ListenLoadFoldAdapter.ROOT_ADAPTER;
import static com.example.kittenappscollage.helpers.Massages.LYTE;

public class FragmentGalleryAction extends FragmentSelectedGallery{

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
            LYTE("del sel");
        }
    }
}
