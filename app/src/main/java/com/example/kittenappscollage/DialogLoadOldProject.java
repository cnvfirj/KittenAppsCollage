package com.example.kittenappscollage;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.kittenappscollage.draw.saveSteps.State;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.Objects;

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
                .setTitle("У вас остался не завершенный проект")
                .setMessage("Открыть его? В противном случае он будет уничтожен.")
                .setPositiveButton("да", (dialogInterface, i) -> {
                    query.result(true);
                })
                .setNegativeButton("нет", (dialogInterface, i) -> {
                    query.result(false);
                })
                .setCancelable(false);
        return builder.create();
    }

    @SuppressLint("CheckResult")
    public static State requestData(String foldData){

        File[] data = Objects.requireNonNull((new File(foldData)).listFiles());

        State state = null;
        if(data.length==0)return state;
        try {
            InputStream fis = new FileInputStream(data[0]);
            ObjectInputStream ois = new ObjectInputStream(fis);

            state = (State) ois.readObject();

            ois.close();
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }

        return state;

    }

    public interface ResultQuery{
        public void result(boolean r);
    }
}
