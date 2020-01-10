package com.example.kittenappscollage.collect.dialogActions;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.kittenappscollage.R;

public class DialogAction extends DialogFragment {

    public static final int ACTION_DELETE = 1;

    public static final int ACTION_SHARE = 2;

    public static final int ACTION_TRANSFER = 3;

    public static final int ACTION_RENAME = 4;

    public static final int ACTION_INVIS = 5;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_action_gallery,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
