package com.example.kittenappscollage.draw.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.kittenappscollage.helpers.AllPermissions;
import com.example.kittenappscollage.helpers.SaveImageToFile;
import com.example.kittenappscollage.draw.RepDraw;

import static com.example.kittenappscollage.helpers.Massages.LYTE;

/*здесь обработаем нажатие кнопок сохранения*/

public class SavedKollagesFragmentDraw extends AddLyrsFragmentDraw {


    @Override
    protected void saveNet(ImageView v) {
        super.saveNet(v);
    }

    @Override
    protected void saveTel(ImageView v) {
        super.saveTel(v);
        if (AllPermissions.create().activity(getActivity()).reqSingle(AllPermissions.STORAGE).isStorage()) {
            if(RepDraw.get().isImg())SaveImageToFile.saveImage(getContext(),RepDraw.get().getImg());
        } else {
            AllPermissions.create().activity(getActivity()).callDialog(AllPermissions.STORAGE);
        }

    }


}
