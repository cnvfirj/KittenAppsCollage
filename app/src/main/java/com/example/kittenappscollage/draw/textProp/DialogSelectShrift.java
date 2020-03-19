package com.example.kittenappscollage.draw.textProp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.documentfile.provider.DocumentFile;

import com.example.dynamikseekbar.DynamicSeekBar;
import com.example.kittenappscollage.R;
import com.example.kittenappscollage.draw.fragment.ApplyDrawToolsFragmentDraw;
import com.example.kittenappscollage.draw.repozitoryDraw.RepDraw;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static com.example.kittenappscollage.helpers.Massages.LYTE;
import static com.example.kittenappscollage.helpers.Massages.SHOW_MASSAGE;


public class DialogSelectShrift extends DialogSelecledTextFragment {


    private int sourceFont;

    private String pathFont;

    private InputMethodManager imm;

    private TextView instruct;


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
        instruct = view.findViewById(R.id.dialog_edit_text_instruct);
        instruct.setText("Скачай на устройство шрифт и найди его здесь");
        slideTextInstruct(true,0);
        enableBarAngle(RepDraw.get().isTextItalic());
        getBarAngle().setProgress(computeProgressBar());
        getPresent().selFill(RepDraw.get().isTextFill());
        getPresent().setItalic(RepDraw.get().isTextItalic(),RepDraw.get().getItalicText());
        getActItal().setSelected(RepDraw.get().isTextItalic());
        getActFill().setSelected(RepDraw.get().isTextFill());
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
//        view.setActivated(!view.isActivated());
//        slideTextInstruct(!view.isActivated(),500);
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/octet-stream");
        startActivityForResult(intent, 656);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==656){
            if(resultCode== Activity.RESULT_OK){
                final Uri uri = data.getData();
                DocumentFile font = DocumentFile.fromSingleUri(getContext(),uri);
                final String name = font.getName();
                if(name.endsWith(".ttf")||name.endsWith(".otf")){
                    try {
                        writeInRootFoldFont(font,uri);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }else {
                    Toast.makeText(getContext(),"Файл должен быть скачан на устройство и иметь расширение ttf или otf",Toast.LENGTH_LONG).show();
                }
            }else {
                Toast.makeText(getContext(),"Файл должен быть скачан на устройство и иметь расширение ttf или otf",Toast.LENGTH_LONG).show();
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
            SHOW_MASSAGE(getContext(),"Некорректный текст");
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

    private void writeInRootFoldFont(DocumentFile df, Uri u) throws FileNotFoundException {
        String root = ContextCompat.getExternalFilesDirs(getContext(), null)[0].getAbsolutePath();
        DocumentFile fontsFold = DocumentFile.fromFile(new File(root+"/fonts"));
        if(!fontsFold.exists()){
            LYTE("create fold");
            DocumentFile rootFold = DocumentFile.fromFile(new File(root));
            rootFold.createDirectory("fonts");
        }
        if(fontsFold.exists()&&fontsFold.isDirectory()) {
            InputStream is = getContext().getContentResolver().openInputStream(u);
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

            for (DocumentFile f : fontsFold.listFiles()) {
                LYTE("writing " + f.getName() + "|" + f.getType());
            }
            if(writeFont.exists())SHOW_MASSAGE(getContext(),"Выбранный шрифт добавлен");
        }

    }

    private void slideTextInstruct(boolean invisible, int tyme){
        getVisibleInstruct().setActivated(invisible);
        if(invisible){
            instruct.animate().alpha(0).scaleY(0).setDuration(tyme).start();
            getVisibleInstruct().animate().alpha(1).setDuration(tyme).start();
        }else {
            instruct.animate().alpha(1).scaleY(1).setDuration(tyme).start();
            getVisibleInstruct().animate().alpha(0.5f).setDuration(tyme).start();
        }
    }

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
