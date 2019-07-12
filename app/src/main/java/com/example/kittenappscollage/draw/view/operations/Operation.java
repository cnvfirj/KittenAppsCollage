package com.example.kittenappscollage.draw.view.operations;

public abstract class Operation {

    public enum Command{
        DRAW,
        DRAW_LINE,
        DRAW_SPOT,
        DRAW_LINE_SHADOW,
        DRAW_LINE_SHADOW_SIDE,
        ELAST,
        ELAST_SHADOW,
        ELAST_SHADOW_SIDE,
        FILL,
        FILL_TO_BORDER,
        FILL_TO_COLOR,
        CUT,
        CUT_RECT,
        CUT_ARB
    }
}
