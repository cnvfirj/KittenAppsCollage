package com.example.kittenappscollage.draw.operations.canvas;

import android.graphics.Paint;

import com.example.kittenappscollage.draw.repozitoryDraw.RepDraw;
import com.example.kittenappscollage.draw.repozitoryDraw.RepParams;

public class DrawText extends DrawArbitLine implements RepParams.ListParams {

    private HelperDrawText dText;

    public DrawText() {
        dText = new HelperDrawText();
        RepDraw.get().listParams(this);
    }

    @Override
    public void command(Command command) {
        super.command(command);
        fill(dCommand);
    }

    private void fill(Command c){
        if(c.equals(Command.ARBIT_LINE_1)||
                c.equals(Command.ARBIT_LINE_2)||
                c.equals(Command.ARBIT_LINE_3)) dText.fill(Paint.Style.STROKE);
        else if(c.equals(Command.ARBIT_SPOT))dText.fill(Paint.Style.FILL_AND_STROKE);
    }
    @Override
    public void color(int c) {
        dText.color(c);
    }

    @Override
    public void alpha(int a) {

    }

    @Override
    public void width(float w) {
        dText.size(w);
    }

    @Override
    public void text(String t) {

    }
}
