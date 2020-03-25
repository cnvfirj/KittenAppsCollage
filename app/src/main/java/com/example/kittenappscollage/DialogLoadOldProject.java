package com.example.kittenappscollage;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.kittenappscollage.draw.repozitoryDraw.RepDraw;
import com.example.kittenappscollage.draw.saveSteps.BackNextStep;
import com.example.kittenappscollage.draw.saveSteps.State;
import com.example.kittenappscollage.helpers.RequestFolder;
import com.example.kittenappscollage.helpers.rx.ThreadTransformers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.Objects;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;

public class DialogLoadOldProject extends DialogFragment {
    public static final String TAG = "DIALOG_LOAD_OLD_PR";

    public static final int REQUEST = 1259;

    private ResultQuery query;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        query = (ResultQuery)getContext();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder
                .setTitle(R.string.REQUEST_LOAD_OLD_PROJECT)
                .setMessage(R.string.OPEN_OLD_PROJECT)
                .setPositiveButton(R.string.YES, (dialogInterface, i) -> {
                    query.result(true);
                })
                .setNegativeButton(R.string.NO, (dialogInterface, i) -> {
                    query.result(false);
                })
                .setCancelable(false);
        return builder.create();
    }

    @SuppressLint("CheckResult")
    public static State requestData(String foldData){


        State state = null;
        if(RequestFolder.testFolder(new File(foldData))) {
            File[] data = Objects.requireNonNull((new File(foldData)).listFiles());

            if (data.length == 0) return state;
            try {
                InputStream fis = new FileInputStream(data[0]);
                ObjectInputStream ois = new ObjectInputStream(fis);

                state = (State) ois.readObject();

                ois.close();
            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
            }
        }
        return state;

    }

    public interface ResultQuery{
        public void result(boolean r);
    }
}
