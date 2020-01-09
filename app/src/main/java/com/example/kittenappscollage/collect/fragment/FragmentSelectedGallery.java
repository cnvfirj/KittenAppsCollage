package com.example.kittenappscollage.collect.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashSet;

import static com.example.kittenappscollage.collect.adapters.ListenLoadFoldAdapter.ROOT_ADAPTER;
import static com.example.kittenappscollage.helpers.Massages.LYTE;

public class FragmentSelectedGallery extends FragmentSlideGallery {


    private HashSet<String> selectFiles;

    private ArrayList<String>under;

    private boolean modeSel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       selectFiles = new HashSet<>();
       modeSel = false;
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void click(int adapter, ImageView img, ImageView check, int pos) {
        super.click(adapter, img, check, pos);
        if(getIndexAdapter()== ROOT_ADAPTER){
            if(!under.get(0).equals(getListImagesInFolders().get(getFoldAdapt().getKeys()[pos]).get(0))) {
                selectFiles.clear();
                under = getListImagesInFolders().get(getFoldAdapt().getKeys()[pos]);
                addPathFolder();
            }else {
                invisibleMenu();
                getFoldAdapt().setModeSelected(false);
            }
        }else {

        }
    }

    @Override
    public void longClick(int adapter, ImageView img, ImageView check, int pos) {
        super.longClick(adapter, img, check, pos);
        if(!modeSel){
            if(getImgAdapt().isModeSelected()||getFoldAdapt().isModeSelected()){
                modeSel = true;
                selectFiles.clear();
                if(getIndexAdapter()== ROOT_ADAPTER){
                  under = getListImagesInFolders().get(getFoldAdapt().getKeys()[pos]);
                  addPathFolder();
                }else {
                  selectFiles.add(getListImagesInFolders().get(getFoldAdapt().getKeys()[getIndexAdapter()]).get(pos));
                }
            }
        }
    }

    private void addPathFolder(){
        if(under.size()>0){
            String[]split = under.get(0).split("[/]");
            String file = "";
            for(int i=0;i<split.length-1;i++){
                file+=split[i]+"/";
            }
            selectFiles.clear();
            selectFiles.add(file);
        }
    }

    @Override
    protected void invisibleMenu() {
        super.invisibleMenu();
        modeSel = false;
    }

    @Override
    protected void clickSel_1(ImageView v) {
        super.clickSel_1(v);
        /*скріть папку*/
        /*візов диалога*/
        for (String s:selectFiles) {
            LYTE("invis fold "+s);
        }
    }

    @Override
    protected void clickSel_2(ImageView v) {
        super.clickSel_2(v);
        /*переименовать*/
        /*візов диалога*/
        for (String s:selectFiles) {
            LYTE("rename fold "+s);
        }
    }

    @Override
    protected void clickSel_3(ImageView v) {
        super.clickSel_3(v);
        if(getIndexAdapter()== ROOT_ADAPTER){
            /*переместить на карту*/
            /*візов диалога*/
            for (String s:selectFiles) {
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
            for (String s:selectFiles) {
                LYTE("del fold "+s);
            }
        }else {
            /*удалить вібраное*/
            /*візов диалога*/
            LYTE("del sel");
        }
    }
}
