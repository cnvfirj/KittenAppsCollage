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
             if(selectFiles.contains(getListImagesInFolders().get(getFoldAdapt().getKeys()[getIndexAdapter()]).get(pos))){
                 selectFiles.remove(getListImagesInFolders().get(getFoldAdapt().getKeys()[getIndexAdapter()]).get(pos));
                 if(selectFiles.size()==0){
                     invisibleMenu();
                     getImgAdapt().setModeSelected(false);
                 }
             }else {
                 selectFiles.add(getListImagesInFolders().get(getFoldAdapt().getKeys()[getIndexAdapter()]).get(pos));

             }
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

    public boolean onBackPressed(int index){
        if(index==1) {
            if (getIndexAdapter() == ROOT_ADAPTER) {
                if (getFoldAdapt().isModeSelected()) {
                    invisibleMenu();
                    getFoldAdapt().setModeSelected(false);
                    return true;
                }
            } else {
                if (getImgAdapt().isModeSelected()) {
                    invisibleMenu();
                    getImgAdapt().setModeSelected(false);
                    return true;
                }
            }
        }

        return false;

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

    protected HashSet<String >getSelectFiles(){
        return selectFiles;
    }


}
