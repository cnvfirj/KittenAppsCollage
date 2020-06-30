package com.kittendevelop.kittenappscollage.draw.textProp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.documentfile.provider.DocumentFile;

import com.example.dynamikseekbar.DynamicSeekBar;
import com.kittendevelop.kittenappscollage.R;
import com.kittendevelop.kittenappscollage.draw.fragment.ApplyDrawToolsFragmentDraw;
import com.kittendevelop.kittenappscollage.draw.repozitoryDraw.RepDraw;
import com.kittendevelop.kittenappscollage.helpers.rx.ThreadTransformers;
import com.kittendevelop.kittenappscollage.helpers.Massages;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;


public class DialogSelectShrift extends DialogSelecledTextFragment {

    private final int REQUEST_ADD_FONT = 656;

    private int sourceFont;

    private String pathFont;

    private InputMethodManager imm;

    public static DialogSelectShrift get(){
        DialogSelectShrift d = new DialogSelectShrift();
        return d;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        enableBarAngle(RepDraw.get().isTextItalic());
        getBarAngle().setProgress(computeProgressBar());
        getPresent().selFill(RepDraw.get().isTextFill());
        getPresent().setItalic(RepDraw.get().isTextItalic(),RepDraw.get().getItalicText());
        getActItal().setSelected(RepDraw.get().isTextItalic());
        getActFill().setSelected(RepDraw.get().isTextFill());
        threadScanFontsCreatedDialog();
    }

    @Override
    public void onProgressChanged(DynamicSeekBar seekBar, int progress, boolean isTouch) {
        super.onProgressChanged(seekBar, progress, isTouch);
        float angle = -(progress-100f)/100f;
        if(getActItal().isSelected())getPresent().setItalic(getActItal().isSelected(),angle);
    }

    @Override
    protected void initListShrift(View view) {
        super.initListShrift(view);
        getAdapter().setListen(new ListenAdapterShrift() {
            @Override
            public void item(int position, String path) {
              sourceFont = position<getSizeBaseFonts()? ApplyDrawToolsFragmentDraw.S_ASSETS:ApplyDrawToolsFragmentDraw.S_STORAGE;
              pathFont = path;
            }

            @Override
            public void font(Typeface t) {
                getPresent().setShrift(t);
            }
        });
    }

    @Override
    protected void enableAngle(ImageView view) {
        super.enableAngle(view);
        view.setSelected(!view.isSelected());
        enableBarAngle(view.isSelected());
        getPresent().setItalic(view.isSelected(),computeAngleBar());
    }

    @Override
    protected void fillText(ImageView view) {
        super.fillText(view);
        view.setSelected(!view.isSelected());
        getPresent().selFill(view.isSelected());
    }

    @Override
    protected void searchFonts(ImageView view) {
        super.searchFonts(view);
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/octet-stream");
        startActivityForResult(intent, REQUEST_ADD_FONT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==REQUEST_ADD_FONT){
            if(resultCode== Activity.RESULT_OK){
                threadAddFont(data);
            }else {
                Toast.makeText(getContext(), getContext().getResources().getString(R.string.FIND_FILE_FONT),Toast.LENGTH_LONG).show();
            }
        }
        else super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void clickAction_1(ImageView view) {
        super.clickAction_1(view);
        RepDraw.get().setShrift(getPresent().getShrift()).setPathFont(pathFont).setSourceFont(sourceFont);
        if(!getEnterText().getText().toString().isEmpty()){
            imm.hideSoftInputFromWindow(getEnterText().getWindowToken(), 0);
            RepDraw.get()
                    .setText(getEnterText().getText().toString())
                    .textFill(getPresent().isTextFill())
                    .textItalic(getPresent().isTextItalic())
                    .setItalicText(getPresent().getAngleItalic());
            dismiss();
        }else {
            Massages.SHOW_MASSAGE(getContext(),getContext().getResources().getString(R.string.INCORRECT_TEXT));
        }

    }

    @Override
    protected void clickAction_2(ImageView view) {
        super.clickAction_2(view);
        getEnterText().setText("");
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        getEnterText().requestFocus();
    }

    @Override
    protected void clickAction_3(ImageView view) {
        super.clickAction_3(view);
        sourceFont = -1;
        pathFont = "";
        imm.hideSoftInputFromWindow(getEnterText().getWindowToken(), 0);
        dismiss();
    }

    @Override
    public void onPause() {
        super.onPause();
        imm.hideSoftInputFromWindow(getEnterText().getWindowToken(), 0);
    }

    @SuppressLint("CheckResult")
    private void threadAddFont(Intent data){
        DocumentFile font = DocumentFile.fromSingleUri(getContext(),data.getData());
        final String name = font.getName();
        if(name.endsWith(".ttf")||name.endsWith(".otf")) {
            Observable.create((ObservableOnSubscribe<Boolean>) emitter ->
                    handlingAddFont(font, emitter))
                    .compose(new ThreadTransformers.OnlySingle<>())
                    .subscribe(aBoolean -> {
                        if (aBoolean) {
                           threadScanFontsPostAdd();
                        } else {
                            Toast.makeText(getContext(), getContext().getResources().getString(R.string.NOT_ADD_FONT_IF_NAME_CLONE), Toast.LENGTH_SHORT).show();
                        }
                    });
        }else {
            Toast.makeText(getContext(), getContext().getResources().getString(R.string.FILE_FONT),Toast.LENGTH_LONG).show();
        }
    }

    @SuppressLint("CheckResult")
    private void threadScanFontsPostAdd(){
        Observable.create((ObservableOnSubscribe<ArrayList<String>>) emitter -> {
            emitter.onNext(scanFonts());
            emitter.onComplete();
        })
                .compose(new ThreadTransformers.OnlySingle<>())
                .subscribe(list -> {
                    if(list.size()>0){
                        ((AdapterShrift)getListShrift().getAdapter()).setFonts(list);
                        Toast.makeText(getContext(), getContext().getResources().getString(R.string.FONT_ADD_FIND_IN_LIST),Toast.LENGTH_SHORT).show();
                    }

                });
    }

    @SuppressLint("CheckResult")
    private void threadScanFontsCreatedDialog(){
        Observable.create((ObservableOnSubscribe<ArrayList<String>>) emitter -> {
            emitter.onNext(scanFonts());
            emitter.onComplete();
        })
                .compose(new ThreadTransformers.OnlySingle<>())
                .subscribe(list -> {
                    if(list.size()>0){
                        ((AdapterShrift)getListShrift().getAdapter()).setFonts(list);
                        Toast.makeText(getContext(), getContext().getResources().getString(R.string.ADD_FONTS_FIND),Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(getContext(), getContext().getResources().getString(R.string.ADD_FONTS_NOT_FIND),Toast.LENGTH_LONG).show();
                    }
                });
    }

    private ArrayList<String> scanFonts(){
//        String fold = ContextCompat.getExternalFilesDirs(getContext(), null)[0].getAbsolutePath()+"/fonts";
        File fold = new File(ContextCompat.getExternalFilesDirs(getContext(), null)[0].getAbsolutePath()+"/fonts");
        boolean success = true;
        if(!fold.exists()){
            success = fold.mkdirs();
        }
        if(success) {
            File[] files = fold.listFiles();
            if (files.length > 0) {
                Arrays.sort(files, new Comparator<File>() {
                    @Override
                    public int compare(File o1, File o2) {
                        Long m1 = o1.lastModified();
                        Long m2 = o2.lastModified();
                        return m1.compareTo(m2);
                    }
                });
                ArrayList<String> fonts = new ArrayList<>();
                for (File f : files) {
                    fonts.add(f.getAbsolutePath());
                }
                ArrayList<String> adapt = ((AdapterShrift) getListShrift().getAdapter()).getFonts();
                boolean contain = false;
                for (int i = fonts.size() - 1; i >= 0; i++) {
                    if (!adapt.contains(fonts.get(i))) {
                        contain = true;
                        break;
                    }
                }
                if (contain) return fonts;
            }
        }
        return new ArrayList<String>();
    }

    private void handlingAddFont(DocumentFile font,ObservableEmitter<Boolean> emitter){
            try {
                emitter.onNext(writeInRootFoldFont(font));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
               emitter.onComplete();
    }

    private boolean writeInRootFoldFont(DocumentFile df) throws FileNotFoundException {
        String root = ContextCompat.getExternalFilesDirs(getContext(), null)[0].getAbsolutePath();
        DocumentFile fontsFold = DocumentFile.fromFile(new File(root+"/fonts"));
        if(!fontsFold.exists()){
            DocumentFile rootFold = DocumentFile.fromFile(new File(root));
            rootFold.createDirectory("fonts");
        }

        if(fontsFold.exists()&&fontsFold.isDirectory()) {
            boolean isClone = false;
            for (DocumentFile f:fontsFold.listFiles()){
                if(f.getName().equals(df.getName()))isClone = true;
            }
            if(!isClone) {
                InputStream is = getContext().getContentResolver().openInputStream(df.getUri());
                DocumentFile writeFont = fontsFold.createFile(df.getType(), df.getName());
                OutputStream out = getContext().getContentResolver().openOutputStream(writeFont.getUri());
                byte[] buffer = new byte[8 * 1024];
                int bytesRead;
                try {
                    while ((bytesRead = is.read(buffer)) != -1) {
                        out.write(buffer, 0, bytesRead);
                    }
                    is.close();
                    out.close();
                } catch (IOException e) {

                }
                return writeFont.exists();
            }
        }
        return false;

    }

//    private void slideTextInstruct(boolean invisible, int tyme){
//        getVisibleInstruct().setActivated(invisible);
//        if(invisible){
//            instruct.animate().alpha(0).scaleY(0).setDuration(tyme).start();
//            getVisibleInstruct().animate().alpha(1).setDuration(tyme).start();
//        }else {
//            instruct.animate().alpha(1).scaleY(1).setDuration(tyme).start();
//            getVisibleInstruct().animate().alpha(0.5f).setDuration(tyme).start();
//        }
//    }

    private void enableBarAngle(boolean enable){
        getBarAngle().setEnabled(enable);
        getBarAngle().setColorWay(enable?getResources().getColor(R.color.colorPrimary):Color.GRAY);
        getBarAngle().setColorMark(enable?getResources().getColor(R.color.colorPrimary):Color.GRAY);
        getBarAngle().setColorProgress(enable?getResources().getColor(R.color.colorAccent):Color.GRAY);
    }

    private float computeAngleBar(){
        return -(getBarAngle().getProgress()-100f)/100f;
    }
    private int computeProgressBar(){
        return ((int)(-RepDraw.get().getItalicText()*100)+100);
    }
}
