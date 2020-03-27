package com.example.kittenappscollage.collect.fragment;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import androidx.documentfile.provider.DocumentFile;

import com.example.kittenappscollage.R;
import com.example.kittenappscollage.helpers.Massages;
import com.example.kittenappscollage.helpers.dbPerms.WorkDBPerms;
import com.example.kittenappscollage.helpers.rx.ThreadTransformers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.CollationKey;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

import static com.example.kittenappscollage.collect.adapters.ListenLoadFoldAdapter.ROOT_ADAPTER;
import static com.example.kittenappscollage.helpers.Massages.LYTE;

public class FragmentGalleryActionStorage extends FragmentGalleryShareImages {

    private final int REQUEST_FOLDER = 903;

    @Override
    protected void applyDeleteSelectedStorage() {
        super.applyDeleteSelectedStorage();
         threadDelImages(getKey());

    }

    @Override
    protected void deletedFoldStorage(String fold) {
        super.deletedFoldStorage(fold);
        threadDelFold(fold);
    }


    @SuppressLint("CheckResult")
    private void threadDelFold(String fold){
     Observable.create((ObservableOnSubscribe<HashMap<String, ArrayList<String>>>) emitter ->
             deleteImagesAndFold(fold,emitter))
             .compose(new ThreadTransformers.InputOutput<>())
             .doOnComplete(() -> {

                 Massages.SHOW_MASSAGE(getContext(),getContext().getResources().getString(R.string.FOLDER_DELETE));
             }).subscribe(stringArrayListHashMap -> setListImagesInFolders(stringArrayListHashMap));
    }

    @SuppressLint("CheckResult")
    private void threadDelImages(String key){
        final int select = getSelectFiles().size();
        final int all = getListImagesInFolders().get(key).size();
        if(select==all){
            getImgAdapt().setModeSelected(false);
            setIndexAdapter(ROOT_ADAPTER);
            getGridLayoutManager().setSpanCount(2);
            getImgAdapt().activate(false);
            getRecycler().setAdapter(getFoldAdapt().activate(true));

            threadDelFold(key);
        }else {
            Observable.create((ObservableOnSubscribe<HashMap<String, ArrayList<String>>>) emitter ->
                    deleteImages(key, emitter))
                    .compose(new ThreadTransformers.InputOutput<>())
                    .doOnComplete(() -> {
                        Massages.SHOW_MASSAGE(getContext(), getContext().getResources().getString(R.string.SELECTED_IMAGES_DELETED));
                        /*запустит сервис физического удаления*/
                    }).subscribe(stringArrayListHashMap -> setListImagesInFolders(stringArrayListHashMap));
        }
    }

//    @SuppressLint("CheckResult")
//    private void threadCopy(Uri uri){
//        Observable.create((ObservableOnSubscribe<HashMap<String, ArrayList<String>>>) emitter ->
//                copyImages(uri,getListImagesInFolders().get(getKey()),emitter))
//                .compose(new ThreadTransformers.InputOutput<>())
//                .doOnComplete(() -> Massages.SHOW_MASSAGE(getContext(), "Изображения скопированы"))
//                .subscribe(stringArrayListHashMap -> setListImagesInFolders(stringArrayListHashMap));
//    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        if(requestCode==REQUEST_FOLDER){
//            if(resultCode== Activity.RESULT_OK){
//                threadCopy(data.getData());
//            }
//        }else super.onActivityResult(requestCode, resultCode, data);
//    }

//    private void requestFoldToCopy(){
//        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
//        startActivityForResult(intent, REQUEST_FOLDER);
//    }

//    private void copyImages(Uri uri, ArrayList<String>imgs, ObservableEmitter<HashMap<String, ArrayList<String>>> emitter){
//        int takeFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION ;
//        getContext().getContentResolver().takePersistableUriPermission(uri, takeFlags);
//        DocumentFile fold = DocumentFile.fromTreeUri(getContext(), uri);
//        WorkDBPerms.get(getContext()).setAction(WorkDBPerms.INSERT,fold.getUri().toString(), fold.getName());
//        try {
//            for (String img:imgs) {
//                Uri u = Uri.parse(img);
//                    saveImg(u,fold);
//            }
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//
//
//    }

    private void saveImg(Uri uri, DocumentFile fold) throws FileNotFoundException {
        InputStream is = getContext().getContentResolver().openInputStream(uri);
        DocumentFile old = DocumentFile.fromSingleUri(getContext(),uri);
        DocumentFile img = fold.createFile(old.getType(), old.getName());
        OutputStream out = getContext().getContentResolver().openOutputStream(img.getUri());
        byte[] buffer = new byte[8 * 1024];
        int bytesRead;
            try {
                while ((bytesRead = is.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
                is.close();
                out.close();
            }catch (IOException e){

            }

    }

    private void deleteImages(String key,ObservableEmitter<HashMap<String, ArrayList<String>>> emitter){
          DocumentFile[]folder = DocumentFile.fromTreeUri(getContext(),Uri.parse(key)).listFiles();
          String[] args = new String[getSelectFiles().size()];
          for (int i=0;i<args.length;i++){
              args[i] = Uri.parse(getSelectFiles().get(i)).getLastPathSegment();
              LYTE("last seg "+args[i]);
          }
          String select = "";
          for (int i=0;i<args.length;i++){
              if(i>0)select = select+" AND ";
              select = select+ MediaStore.Images.Media._ID+" = ?";
          }
        Arrays.sort(folder, (o1, o2) -> {
            final Long l1 = o1.lastModified();
            final Long l2 = o2.lastModified();
            return l1.compareTo(l2);
        });
          int index = 0;
          Cursor c = null;
          try {
              c = getContext().getContentResolver().query(question(),
                      new String[]{MediaStore.Images.Media.DISPLAY_NAME,MediaStore.Images.Media._ID},
                      select,
                      args,
                      MediaStore.Images.Media.DATE_MODIFIED);
              LYTE("count "+c.getCount());
              final int col_name = c.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);
              final int col_id = c.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
              while (c.moveToNext()){
//                  index = delImg(index,c.getString(col_name),folder);
                  LYTE("del "+c.getLong(col_id)+"|"+c.getString(col_name));
//                  if(index>-1){
//                      final String img = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, c.getLong(col_id)).toString();
//                      getListImagesInFolders().get(key).remove(img);
//                      emitter.onNext(getListImagesInFolders());
//                  }
              }
          }finally {
              if(c!=null)c.close();
          }

//        for (String img:getSelectFiles()){
//            if(delFile(Uri.parse(img))>0) {
//                getListImagesInFolders().get(key).remove(img);
//                emitter.onNext(getListImagesInFolders());
//            }
//        }
        emitter.onComplete();
    }

    private void deleteImagesAndFold(String key,ObservableEmitter<HashMap<String, ArrayList<String>>> emitter){
        ArrayList<String>images = (ArrayList<String>)getListImagesInFolders().get(key).clone();
        for (String img:images){
            if(delFile(Uri.parse(img))>0) {
                getListImagesInFolders().get(key).remove(img);
                emitter.onNext(getListImagesInFolders());
            }
        }
        if(getListImagesInFolders().get(key).size()==0){
            clearLists(key);
            WorkDBPerms.get(getContext()).delItem(key);
            DocumentFile fold = DocumentFile.fromTreeUri(getContext(),Uri.parse(key));
            if(fold.listFiles().length==0){
                delDocFile(fold.getUri());
            }
        }
        emitter.onNext(getListImagesInFolders());
        emitter.onComplete();
    }

    private int delImg(int index, String name, DocumentFile[] list){
        int d = -1;
            for (int i=index;i<list.length;i++){
                if(name.equals(list[i].getName())){
                    if(delDocFile(list[i].getUri())){
                        d = i+1;
                    } else d = -1;
                }
            }
        return d;
    }

    private int delFile(Uri uri){
        return getContext().getContentResolver().delete(uri,null,null);
    }

    private boolean delDocFile(Uri uri){

        boolean b = false;
        try {
            b = DocumentsContract.deleteDocument(getContext().getContentResolver(),uri);
        }catch(FileNotFoundException e) {
            LYTE("FragmentGalleryActionStorage FileNotFoundException del "+e.toString());
        }catch(SecurityException s){
            LYTE("FragmentGalleryActionStorage SecurityException del "+s.toString());
        }catch (IllegalArgumentException i){
            LYTE("FragmentGalleryActionStorage IllegalArgumentException del "+i.toString());
            b = true;
        }
        return b;
    }

    protected void clearLists(String key){
        if(getListImagesInFolders().containsKey(key)) {
            getListImagesInFolders().remove(key);
            getListFolds().remove(key);
            getListMutable().remove(key);
        }
    }

    private String getRealPath(String lastSegment) {
        String[]split = lastSegment.split("[:]");
        String[]sub = lastSegment.split(split[0]+":");
        String[]storage = getContext().getExternalFilesDir(null).getAbsolutePath().split("[/]");
        String p = "/"+storage[1]+"/"+split[0]+"/"+sub[1];
        if(p.startsWith("/storage/primary/")){
            String[]correct = p.split("/storage/primary/");
            return "/storage/emulated/0/"+correct[1];
        } else return p;
    }

}
