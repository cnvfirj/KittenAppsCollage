package com.example.kittenappscollage.collect.fragment;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.webkit.MimeTypeMap;
import androidx.core.content.FileProvider;

import com.example.kittenappscollage.helpers.Massages;
import com.example.kittenappscollage.helpers.RequestFolder;
import com.example.kittenappscollage.helpers.db.aller.ActionsContentPerms;
import com.example.kittenappscollage.helpers.db.aller.ContentPermis;

import java.io.File;
import java.util.ArrayList;

import static com.example.kittenappscollage.collect.adapters.ListenLoadFoldAdapter.ROOT_ADAPTER;

public class FragmentGalleryActionFile extends FragmentGalleryAction {

    @Override
    protected void renameFoldFileDevise(String name){
        super.renameFoldFileDevise(name);
        final String nameFold = getListFolds().get(getKey());//имя папки
        final String excludeNameFold = getKey().split(nameFold)[0];
        final String newFold = excludeNameFold + name;//новое имя папки
        File oldfile = new File(getKey());
        File newfile = new File(newFold);
        if (getListFolds().keySet().contains(newFold)) {
            Massages.SHOW_MASSAGE(getContext(), "Выбери другое имя папке");
        } else {
            File[] old = oldfile.listFiles();
            if (oldfile.renameTo(newfile)) {

                ActionsContentPerms.create(getContext()).deleteItemDB(getKey());
                ActionsContentPerms.create(getContext()).queryItemDB(newfile.getAbsolutePath(),
                        ActionsContentPerms.GRAND,ActionsContentPerms.ZHOPA,ActionsContentPerms.SYS_FILE,
                        ActionsContentPerms.NON_LOC_STOR, View.VISIBLE);

                File[] young = newfile.listFiles();
                ArrayList<String>renamed = new ArrayList<>();
                for (int i = 0; i < young.length; i++) {
                    renamed.add(i,young[i].getAbsolutePath());
                    getContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(old[i])));
                    getContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(young[i])));

                }

                getListFolds().put(newfile.getAbsolutePath(),name);
                getListPerms().put(newfile.getAbsolutePath(),ActionsContentPerms.GRAND);
                getIndexesStorage().put(newfile.getAbsolutePath(),getIndexesStorage().get(getKey()));
                getListImagesInFolders().put(newfile.getAbsolutePath(),renamed);

                clearLists(getKey());

                setListImagesInFolders(getListImagesInFolders());

            } else {
                Massages.SHOW_MASSAGE(getContext(), "Не удалось переименовать папку");
            }
        }
    }

    @Override
    protected void applyDeleteSelectedFiles(){
        super.applyDeleteSelectedFiles();
        ArrayList<String> imgs = getListImagesInFolders().get(getKey());
        int all = imgs.size();
        if(getSelectFiles().size()==all){
            String fold = getKey();
            setIndexAdapter(ROOT_ADAPTER);
            getGridLayoutManager().setSpanCount(2);
            getRecycler().setAdapter(getFoldAdapt());
            deletedFoldFile(fold);
        }else {
            for (String i:getSelectFiles()){
                File f = new File(i);
                if(f.exists())f.delete();
                getListImagesInFolders().get(getKey()).remove(f.getAbsolutePath());
                setListImagesInFolders(getListImagesInFolders());
                getContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(f)));
            }
            getSelectFiles().clear();
            getImgAdapt().setIndexKey(0);
        }
    }

    @Override
    protected void deletedFoldFile(String fold){
        super.deletedFoldFile(fold);
        File file = new File(fold);
        File[]files = file.listFiles();
        for (File f:files){
            if(f.exists()) {
                if(f.delete()){
                    if(!fold.equals(RequestFolder.getFolderCollages(getContext())))
                        ActionsContentPerms.create(getContext()).deleteItemDB(fold);
                }
            }
            getListImagesInFolders().get(fold).remove(f.getAbsolutePath());
            setListImagesInFolders(getListImagesInFolders());
            getContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(f)));
        }
        if(file.exists()){
            if(file.delete())ActionsContentPerms.create(getContext()).deleteItemDB(fold);

        }

        getSelectFiles().clear();
        clearLists(fold);
        setListImagesInFolders(getListImagesInFolders());
    }

    @Override
    protected void shareSelImagesFile(){
        super.shareSelImagesFile();

        ArrayList<Uri>selected = new ArrayList<>();
        for (String s:getSelectFiles()){
            File file = new File(s);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                selected.add(FileProvider.getUriForFile(getContext(), "com.example.kittenappscollage.fileprovider", file));
            }else {
                selected.add(Uri.fromFile(file));
            }
        }

        if(selected.size()>0){
            if(selected.size()==1){
                shareSingleImg(selected.get(0), getSelectFiles().get(0));
            }else {
                shareSelectedImgs(selected);
            }
        }
        getSelectFiles().clear();

    }

    private void shareSingleImg(Uri uri, String path){
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        String ext =path.substring(path.lastIndexOf(".") + 1);
        String type = mime.getMimeTypeFromExtension(ext);
        try {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.putExtra(Intent.EXTRA_STREAM, uri);
                intent.setType(type);
            } else {
                intent.setDataAndType(uri, type);
            }
            startActivity(Intent.createChooser(intent, null));
        } catch (ActivityNotFoundException anfe) {
            Massages.LYTE("FragmentGalleryActionFile что то с активити "+anfe.getMessage());
        }
    }

    private void shareSelectedImgs(ArrayList<Uri>uris){
        try {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND_MULTIPLE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
                intent.setType("image/*");
            }else {
                intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
                intent.setType("image/*");
            }

            startActivity(Intent.createChooser(intent, null));
        }catch (ActivityNotFoundException anfe){
            Massages.LYTE("FragmentGalleryActionFile что то с активити "+anfe.getMessage());
        }

    }

    protected void clearLists(String key){
        getListPerms().remove(key);
        getListImagesInFolders().remove(key);
        getListFolds().remove(key);
        getIndexesStorage().remove(key);


    }
}
