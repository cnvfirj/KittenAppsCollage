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


public class FrameDialogAdd extends DialogFragment implements SelectedFragment.SelectorAddLyrFragments {

    public static final int ADD_CAM = 1;
    public static final int ADD_NEW = 2;
    public static final int ADD_NET = 3;
    public static final int ADD_COLL = 4;

    public static final String KEY_SOURCE_ADD = "key source";

    private FragmentManager dManager;

    private AddLyr dFragmentAdd;

    private SelectedFragment dSelectedFragment;

    public static FrameDialogAdd instance(int ind){
        FrameDialogAdd dialog = new FrameDialogAdd();
        Bundle b = new Bundle();
        b.putInt(KEY_SOURCE_ADD,ind);
        dialog.setArguments(b);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if(bundle!=null){
            int index = bundle.getInt(KEY_SOURCE_ADD,ADD_NEW);
            dSelectedFragment = select(index);
        }

        dFragmentAdd = new AddLyr();
        dManager = getChildFragmentManager();
        return inflater.inflate(R.layout.dialog_add_frame,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dManager.beginTransaction().add(R.id.dialog_add_frame,dSelectedFragment).commit();

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

    private SelectedFragment select(int index){
        if(index==ADD_CAM)return new AddLyrInCam();
        else if(index==ADD_COLL)return new AddLyrInColl();
        else if(index==ADD_NET)return new AddLyrInNet();
        else if(index==ADD_NEW)return new AddLyrInCreator();
        return null;
    }

    @Override
    public void done() {

    }

    @Override
    public void back() {

    }


}
