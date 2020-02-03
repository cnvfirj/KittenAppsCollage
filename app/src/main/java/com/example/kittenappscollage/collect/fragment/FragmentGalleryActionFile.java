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

        boolean[]checks = getImgAdapt().getArrChecks();
        ArrayList<String> imgs = getListImagesInFolders().get(getKey());
        int sum = 0;
        int all = imgs.size();
        for (int i=0;i<checks.length;i++){
            if(checks[i]){
                sum++;
                /*перебираем файлы в обратном порядке*/
                File f = new File(imgs.get(all-(i+1)));
                if(f.exists())f.delete();
                getListImagesInFolders().get(getKey()).remove(f.getAbsolutePath());
                setListImagesInFolders(getListImagesInFolders());
                getContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(f)));
            }
        }

        if(sum>0&&sum<all){
            /*так как изменение произошло в текущей директрии
             * то в адаптере эта папка получит индекс 0.
             * Это связано с сортировкой по времени изменения*/
            getImgAdapt().setIndexKey(0);
        }

        if(sum==all){
            File f = new File(getKey());
            if(f.exists()){
                if(f.delete()){
                    if(!getKey().equals(RequestFolder.getFolderCollages(getContext()))){
                        ActionsContentPerms.create(getContext()).deleteItemDB(getKey());
                    }
                }
            }
            clearLists(getKey());
            if (getImgAdapt().isModeSelected()) {
                invisibleMenu();
                getImgAdapt().setModeSelected(false);
            }

            setIndexAdapter(ROOT_ADAPTER);
            getGridLayoutManager().setSpanCount(2);
            getRecycler().setAdapter(getFoldAdapt());

            setListImagesInFolders(getListImagesInFolders());
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

        clearLists(fold);
        setListImagesInFolders(getListImagesInFolders());
    }

    @Override
    protected void shareSelImagesFile(){
        super.shareSelImagesFile();

        boolean[]checks = getImgAdapt().getArrChecks();
        ArrayList<String>imgs = getListImagesInFolders().get(getKey());
        ArrayList<Uri>selected = new ArrayList<>();
        int all = imgs.size();
        String lostPath = "";
        for (int i=0;i<checks.length;i++){
            if(checks[i]){
                /*перебираем файлы в обратном порядке*/
                lostPath = imgs.get(all-(i+1));
                File file = new File(lostPath);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    selected.add(FileProvider.getUriForFile(getContext(), "com.example.kittenappscollage.fileprovider", file));
                }else {
                    selected.add(Uri.fromFile(file));
                }
            }
        }
        if(selected.size()>0){
            if(selected.size()==1){
                shareSingleImg(selected.get(0), lostPath);
            }else {
                shareSelectedImgs(selected);
            }
        }
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
