package com.example.kittenappscollage.draw.fragment;

import android.widget.ImageView;

import com.example.kittenappscollage.helpers.SaveImageToFile;
import com.example.kittenappscollage.packProj.RepDraw;

/*здесь обработаем нажатие кнопок сохранения*/

public class SavedKollagesFragmentDraw extends AddLyrsFragmentDraw {


    @Override
    protected void saveNet(ImageView v) {
        super.saveNet(v);
    }

    @Override
    protected void saveTel(ImageView v) {
        super.saveTel(v);
        if(RepDraw.get().isImg()) SaveImageToFile.saveImage(getContext(),RepDraw.get().getImg());
    }
}
