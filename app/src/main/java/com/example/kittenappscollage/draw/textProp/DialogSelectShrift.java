package com.example.kittenappscollage.draw.textProp;

import android.graphics.Typeface;

import com.example.kittenappscollage.draw.repozitoryDraw.RepDraw;

import java.util.ArrayList;

import static com.example.kittenappscollage.helpers.Massages.LYTE;

public class DialogSelectShrift extends DialogSelecledTextFragment {

    private ArrayList<String>pathFonts;

    public DialogSelectShrift() {
        pathFonts = new ArrayList<>();
    }

    public static DialogSelectShrift get(){
        DialogSelectShrift d = new DialogSelectShrift();
        return d;
    }

    @Override
    public void onResume() {
        super.onResume();
        pathFonts.clear();
        pathFonts.add("fonts/font_1.otf");
        pathFonts.add("fonts/font_2.otf");
        pathFonts.add("fonts/font_3.ttf");
        pathFonts.add("fonts/font_4.ttf");
    }
}
