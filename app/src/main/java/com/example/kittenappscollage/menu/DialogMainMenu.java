package com.example.kittenappscollage.menu;

import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.kittenappscollage.R;
import com.example.kittenappscollage.draw.addLyrs.FrameDialogAdd;

import static com.example.kittenappscollage.helpers.Massages.LYTE;

public class DialogMainMenu extends DialogFragment implements MenuSwitching{


    public static DialogMainMenu get(){
        return new DialogMainMenu();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(getContext().getResources().getColor(R.color.colorPrimaryTransparent)));
        return inflater.inflate(R.layout.dialog_add_frame,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getChildFragmentManager().beginTransaction().add(R.id.dialog_add_frame,ListOffers.get(this,0)).commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        Window window = getDialog().getWindow();
        Rect rect = new Rect();
        getActivity().getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        window.setLayout((int) (rect.right*0.8), (int)(rect.bottom*0.65));
        window.setGravity(Gravity.CENTER);
    }

    @Override
    public void language() {
      LYTE("lang");
      ListLanguages l = new ListLanguages();
      getChildFragmentManager().beginTransaction().replace(R.id.dialog_add_frame,l).commit();
    }

    @Override
    public void color() {
        LYTE("color");
    }
}
