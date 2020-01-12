package com.example.kittenappscollage.collect.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

import static com.example.kittenappscollage.collect.adapters.ListenLoadFoldAdapter.ROOT_ADAPTER;
import static com.example.kittenappscollage.helpers.Massages.LYTE;

public class FragmentSelectedGallery extends FragmentSlideGallery {


    private ArrayList<String> selectFiles;

    private ArrayList<String>under;

    private boolean modeSel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       selectFiles = new ArrayList<>();
       modeSel = false;
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void click(int adapter, ImageView img, ImageView check, int pos) {
        super.click(adapter, img, check, pos);
        if(modeSel) {
            if (getIndexAdapter() == ROOT_ADAPTER) {
                    if (!under.get(0).equals(getListImagesInFolders().get(getFoldAdapt().getKeys()[pos]).get(0))) {
                        setKey(getFoldAdapt().getKeys()[pos]);
                        selectFiles.clear();
                        under = getListImagesInFolders().get(getFoldAdapt().getKeys()[pos]);
                        addPathFolder(pos);
                    } else {
                        invisibleMenu();
                        getFoldAdapt().setModeSelected(false);
                    }
                    checkFolder();
            } else {
                    if (selectFiles.contains(getListImagesInFolders().get(getFoldAdapt().getKeys()[getIndexAdapter()]).get(pos))) {
                        selectFiles.remove(getListImagesInFolders().get(getFoldAdapt().getKeys()[getIndexAdapter()]).get(pos));
                        if (selectFiles.size() == 0) {
                            invisibleMenu();
                            getImgAdapt().setModeSelected(false);
                        }
                    } else {
                        selectFiles.add(getListImagesInFolders().get(getFoldAdapt().getKeys()[getIndexAdapter()]).get(pos));
                    }
            }
        }
    }

    @Override
    public void longClick(int adapter, ImageView img, ImageView check, int pos) {

        if(!modeSel){
            if(getImgAdapt().isModeSelected()||getFoldAdapt().isModeSelected()){
                modeSel = true;
                selectFiles.clear();
                if(getIndexAdapter()== ROOT_ADAPTER){
                  under = getListImagesInFolders().get(getFoldAdapt().getKeys()[pos]);
                  addPathFolder(pos);
                }else {
                  selectFiles.add(getListImagesInFolders().get(getFoldAdapt().getKeys()[getIndexAdapter()]).get(pos));
                }
                super.longClick(adapter, img, check, pos);
            }
        }
//        super.longClick(adapter, img, check, pos);
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
                }else {
                    setIndexAdapter(ROOT_ADAPTER);
                    getGridLayoutManager().setSpanCount(2);
                    getRecycler().setAdapter(getFoldAdapt());
                    return true;
                }
            }
        }

        return false;

    }

    private void addPathFolder(int pos){
        if(under.size()>0){
            setKey(getFoldAdapt().getKeys()[pos]);
            String[]split = under.get(0).split(getFoldAdapt().getKeys()[pos]);
            String file = split[0]+getFoldAdapt().getKeys()[pos];
            selectFiles.clear();
            selectFiles.add(file);
        }
    }



    @Override
    protected void selectIconAction3(ImageView view) {
        super.selectIconAction3(view);
        if(getIndexAdapter()==ROOT_ADAPTER) {
            view.setEnabled(false);
            checkFolder();
        }else {
            view.setEnabled(true);
        }
    }

    @Override
    protected void invisibleMenu() {
        super.invisibleMenu();
        modeSel = false;
    }

    protected ArrayList<String >getSelectFiles(){
        return selectFiles;
    }

    protected ArrayList<String>getUnder(){
        return under;
    }

    private void checkFolder() {
        if (getNamesDirs().size() > 1) {
            getSelected_3().setEnabled(true);
            if (selectFiles.get(0).contains(getNamesDirs().get(0))) {
                getSelected_3().setSelected(true);
            } else {
                getSelected_3().setSelected(false);
            }
        }
    }


}
