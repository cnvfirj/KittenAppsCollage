package com.kittendevelop.kittenappscollage.collect.fragment;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kittendevelop.kittenappscollage.R;
import com.kittendevelop.kittenappscollage.collect.ExLayoutManager;
import com.kittendevelop.kittenappscollage.collect.adapters.ListenAdapter;
import com.kittendevelop.kittenappscollage.collect.adapters.LoadImgAdapt;
import com.kittendevelop.kittenappscollage.collect.adapters.LockFoldAdapter;
import com.kittendevelop.kittenappscollage.draw.fragment.SavedKollagesFragmentDraw;
import com.kittendevelop.kittenappscollage.helpers.AllPermissions;
import com.kittendevelop.kittenappscollage.helpers.dbPerms.WorkDBPerms;

import java.util.ArrayList;
import java.util.HashMap;

import static android.app.Activity.RESULT_OK;
import static com.kittendevelop.kittenappscollage.collect.adapters.ListenLoadFoldAdapter.ROOT_ADAPTER;

public class FragmentGallery extends FragmentScanAllImages implements ListenAdapter{

    public static final int REQUEST_READ_STORAGE = 92;

    public static final int REQUEST_WRITE_ROOT = 94;

    private RecyclerView recycler;

    private LockFoldAdapter foldAdapt;

    private LoadImgAdapt imgAdapt;

    private ExLayoutManager gridLayoutManager;

    private int indexClickAdapter;

    private int indexLongClickAdapter;

    private String key;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initListImagesInFolders();
        indexClickAdapter = ROOT_ADAPTER;
        foldAdapt = new LockFoldAdapter(getContext());
        imgAdapt = new LoadImgAdapt(getContext());
        setParamsAdapter(getContext().getResources().getDisplayMetrics().widthPixels);
        return inflater.inflate(R.layout.fragment_gallery,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
    }



    @Override
    public void onResume() {
        super.onResume();
        readinessScan();
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void stateReadStorage(boolean state){
        if(state) {
            scanDevice();
        }else {
            AllPermissions.create()
                    .activity(getActivity())
                    .callDialog(AllPermissions.STORAGE,REQUEST_READ_STORAGE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_WRITE_ROOT){
            if(resultCode==RESULT_OK){
                if(AllPermissions.create()
                        .activity(getActivity())
                        .reqSingle(AllPermissions.STORAGE).isStorage()) {
                    scanDevice();
                }else {
                    AllPermissions.create()
                            .activity(getActivity())
                            .callDialog(AllPermissions.STORAGE,REQUEST_READ_STORAGE);
                }
            }
        }
    }

    private void init(View v){
        gridLayoutManager = new ExLayoutManager(getContext(),2);
        recycler = v.findViewById(R.id.gallery_list);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(gridLayoutManager);
        recycler.setAdapter(foldAdapt.activate(true));
    }

    @Override
    protected void setListImagesInFolders(HashMap<String, ArrayList<String>> list) {
        super.setListImagesInFolders(list);
        invisibleMenu();
        foldAdapt.setAll(list, getListFolds()).setListen(this);
        imgAdapt.setAll(list,foldAdapt.getItems(),key).setListen(this);
    }

    @Override
    public void click(int adapter, ImageView img, ImageView check, int pos) {
        if(pos>-1) {
            if (adapter == ROOT_ADAPTER) {
                if (!foldAdapt.isModeSelected()) {
                    imgAdapt.setIndexKey(pos);
                    gridLayoutManager.setSpanCount(3);
                    foldAdapt.activate(false);
                    recycler.setAdapter(imgAdapt.activate(true));
                    setIndexAdapter(pos);
                    key = getFoldAdapt().getItems()[pos].getKey();
                }
            } else {
                if (!imgAdapt.isModeSelected()) clickItem(adapter, pos);
            }
        }
    }

    @Override
    public void longClick(int adapter, ImageView img, ImageView check, int pos) {
            if(imgAdapt.isModeSelected()||foldAdapt.isModeSelected()){
                visibleMenu();
            }
    }


//    @Override
//    public void setSavingInStorageCollage(Uri uri, String report, String delimiter,long date) {
//        super.setSavingInStorageCollage(uri, report, delimiter,date);
//        String[]split = report.split(delimiter);//        /*здесь выясняем айди папки и потом закидываем его в бд*/
//    String nameImg = split[SavedKollagesFragmentDraw.INDEX_NAME_IMG];
//    String key = split[SavedKollagesFragmentDraw.INDEX_URI_PERM_FOLD];
//    Cursor c = null;
//        try {
//        c = getContext().getContentResolver().query(
//                question(),
//                new String[]{MediaStore.Images.Media.BUCKET_ID, MediaStore.Images.Media._ID},
//                MediaStore.Images.Media.DISPLAY_NAME + " = ?",
//                new String[]{nameImg},
//                null);
//
//        c.moveToFirst();
//        WorkDBPerms.get(getContext()).addId(key, c.getLong(c.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_ID)));
//        final String img = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, c.getLong(c.getColumnIndexOrThrow(MediaStore.Images.Media._ID))).toString();
//        addImgCollect(
//                split[SavedKollagesFragmentDraw.INDEX_URI_DF_FOLD],
//                img,
//                split[SavedKollagesFragmentDraw.INDEX_NAME_FOLD],
//                split[SavedKollagesFragmentDraw.INDEX_URI_DF_FOLD],
//                date);
//        setListImagesInFolders(getListImagesInFolders());
//    }finally {
//        if(c!=null)c.close();
//    }
//    }

    @Override
    public void createHolder(int adapter, View holder, int pos) {

    }

    @Override
    public void createContentHolder(int adapter, View[] content, int pos) {

    }

    @Override
    public void exit(int adapter) {

    }


    protected void readinessScan(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            stateReadStorage(AllPermissions.create()
                    .activity(getActivity())
                    .reqSingle(AllPermissions.STORAGE).isStorage());
        }else scanDevice();
    }

    protected void setParamsAdapter(int params){
        foldAdapt.setParams(params);
        imgAdapt.setParams(params);
    }


    protected void clickItem(int adapter, int position){

    }

    protected void visibleMenu(){

    }

    protected void invisibleMenu(){

    }

    protected String getKey(){
        return key;
    }

    protected void setKey(String k){
        key = k;
    }

    protected void setIndexAdapter(int i){
        indexClickAdapter = i;
    }

    protected void setSelectItemRootAdapter(int i){
        indexLongClickAdapter = i;
    }

    protected RecyclerView getRecycler(){
        return recycler;
    }

    protected GridLayoutManager getGridLayoutManager(){
        return gridLayoutManager;
    }

    protected LockFoldAdapter getFoldAdapt(){
        return foldAdapt;
    }

    protected LoadImgAdapt getImgAdapt(){
        return imgAdapt;
    }

    protected int getIndexAdapter(){
        return indexClickAdapter;
    }

}
