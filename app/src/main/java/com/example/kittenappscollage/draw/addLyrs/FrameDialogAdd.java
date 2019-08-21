package com.example.kittenappscollage.draw.addLyrs;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.kittenappscollage.R;

public class FrameDialogAdd extends DialogFragment {

    public static final int ADD_CAM = 1;
    public static final int ADD_NEW = 2;
    public static final int ADD_NET = 3;
    public static final int ADD_COLL = 4;

    private FragmentManager dManager;

    private AddLyr dFragmentAdd;

    private SelectedFragment dSelectedFragment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        dFragmentAdd = new AddLyr();
        dManager = getChildFragmentManager();
        return inflater.inflate(R.layout.dialog_add_frame,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dManager.beginTransaction().add(R.id.dialog_add_frame,dFragmentAdd).commit();

    }

    @Override
    public void onResume() {
        super.onResume();
        Window window = getDialog().getWindow();

        Rect rect = new Rect();
        getActivity().getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        window.setLayout((int) (rect.right*0.9), (int)(rect.bottom*0.7));
        window.setGravity(Gravity.CENTER);
    }
}
