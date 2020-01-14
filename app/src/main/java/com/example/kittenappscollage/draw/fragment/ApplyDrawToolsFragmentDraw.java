package com.example.kittenappscollage.draw.fragment;

import android.graphics.Color;
import android.graphics.Typeface;
import android.widget.ImageView;


import com.example.kittenappscollage.draw.textProp.DialogSelecledTextFragment;
import com.example.kittenappscollage.draw.DialogSelectParams;
import com.example.kittenappscollage.draw.repozitoryDraw.RepDraw;
import com.example.kittenappscollage.draw.operations.Operation;
import com.example.kittenappscollage.draw.textProp.DialogSelectShrift;

import static com.example.kittenappscollage.draw.repozitoryDraw.RepParams.KEY_SAVE_ALPHA;
import static com.example.kittenappscollage.draw.repozitoryDraw.RepParams.KEY_SAVE_COLOR;
import static com.example.kittenappscollage.draw.repozitoryDraw.RepParams.KEY_SAVE_WIDTH;

/*применяем инструменты обработки изображения*/
public class ApplyDrawToolsFragmentDraw extends ApplyCommonToolsFragmentDraw {

    private final String KEY_EVENT = "key event ApplyDrawToolsFragmentDraw";

    private final String SOURCE_FONT = "source font ApplyDrawToolsFragmentDraw";

    private final String PATH_FONT = "path font ApplyDrawToolsFragmentDraw";

    private final String KEY_ITALIC = "key italic ApplyDrawToolsFragmentDraw";

    private final String KEY_FILL = "key fill ApplyDrawToolsFragmentDraw";

    private final String KEY_ANGLE_TEXT = "key angle text ApplyDrawToolsFragmentDraw";

    private final String KEY_TEXT = "key text ApplyDrawToolsFragmentDraw";

    public static final int S_STORAGE = 77;

    public static final int S_ASSETS = 88;

    @Override
    protected void toolPaint(ImageView v) {
        super.toolPaint(v);
        int e = Operation.Event.LAYERS_LINE_1.ordinal();
        if(dIndexPaint%10==1)e = dViewDraw.setEvent(Operation.Event.LAYERS_LINE_1);
        else if(dIndexPaint%10==2)e = dViewDraw.setEvent(Operation.Event.LAYERS_LINE_2);
        else if(dIndexPaint%10==3)e = dViewDraw.setEvent(Operation.Event.LAYERS_LINE_3);
        else if(dIndexPaint%10==4)e = dViewDraw.setEvent(Operation.Event.DRAW_SPOT);
        getEditor().putInt(KEY_EVENT,e).apply();
    }

    @Override
    protected void toolErase(ImageView v) {
        super.toolErase(v);
        int e = Operation.Event.LAYERS_ELASTIC_1.ordinal();
        if(dIndexErase%10==1)e = dViewDraw.setEvent(Operation.Event.LAYERS_ELASTIC_1);
        else if(dIndexErase%10==2)e = dViewDraw.setEvent(Operation.Event.LAYERS_ELASTIC_2);
        else if(dIndexErase%10==3)e = dViewDraw.setEvent(Operation.Event.LAYERS_ELASTIC_3);
        else if(dIndexErase%10==4)e = dViewDraw.setEvent(Operation.Event.LAYERS_ELASTIC_4);
        getEditor().putInt(KEY_EVENT,e).apply();
    }

    @Override
    protected void toolFill(ImageView v) {
        super.toolFill(v);
        int e = Operation.Event.LAYERS_FILL_TO_COLOR.ordinal();
        if(dIndexFill%10==1) e = dViewDraw.setEvent(Operation.Event.LAYERS_FILL_TO_COLOR);
        else if(dIndexFill%10==2)e = dViewDraw.setEvent(Operation.Event.LAYERS_FILL_TO_BORDER);
        getEditor().putInt(KEY_EVENT,e).apply();
    }

    @Override
    protected void toolText(ImageView v) {
        super.toolText(v);
        int e = dViewDraw.setEvent(Operation.Event.DRAW_TEXT);
        getEditor().putInt(KEY_EVENT,e).apply();
    }

    @Override
    protected void toolCut(ImageView v) {
        super.toolCut(v);
        int e = dViewDraw.setEvent(Operation.Event.LAYERS_CUT);
        getEditor().putInt(KEY_EVENT,e).apply();
    }

    @Override
    protected void toolDeformRotate(ImageView v) {
        super.toolDeformRotate(v);
        int e = Operation.Event.MATRIX_R.ordinal();
        if(dIndexDefRot%10==1)e = dViewDraw.setEvent(Operation.Event.MATRIX_R);
        else if(dIndexDefRot%10==2)e = dViewDraw.setEvent(Operation.Event.MATRIX_D);
        else if(dIndexDefRot%10==3)e = dViewDraw.setEvent(Operation.Event.MATRIX_RESET_DR);
        getEditor().putInt(KEY_EVENT,e).apply();
    }

    @Override
    protected void toolTranslate(ImageView v) {
        super.toolTranslate(v);
        int e = dViewDraw.setEvent(Operation.Event.MATRIX_T);
        getEditor().putInt(KEY_EVENT,e).apply();
    }

    @Override
    protected void toolScale(ImageView v) {
        super.toolScale(v);
        int e = Operation.Event.MATRIX_S_P.ordinal();
        if(dIndexScale%10==1)e = dViewDraw.setEvent(Operation.Event.MATRIX_S_P);
        else if(dIndexScale%10==2)e = dViewDraw.setEvent(Operation.Event.MATRIX_S);
        else if(dIndexScale%10==3)e = dViewDraw.setEvent(Operation.Event.MATRIX_S_M);
        getEditor().putInt(KEY_EVENT,e).apply();
    }

    @Override
    protected void toolProperties(ImageView v) {
        super.toolProperties(v);
        DialogSelectParams d = new DialogSelectParams();
        d.show(getChildFragmentManager(),d.getClass().getName());
    }

    @Override
    protected void doneCut() {
        super.doneCut();
        dViewDraw.doneCut();
    }

    @Override
    protected void enterText() {
        super.enterText();
        DialogSelectShrift.get().show(getChildFragmentManager(),DialogSelecledTextFragment.DIALOG_SEL_TXT);
    }

    @Override
    protected void selectorButtons(int index) {
        super.selectorButtons(index);
        if(index!=TOOL_CUT) RepDraw.get().zeroingRepers();
        dViewDraw.invalidate();
    }

    @Override
    public void onPause() {
        super.onPause();
        getEditor().putInt(SOURCE_FONT, RepDraw.get().getSourceFont());
        getEditor().putString(PATH_FONT,RepDraw.get().getPathFont());
        getEditor().putBoolean(KEY_ITALIC,RepDraw.get().isTextItalic());
        getEditor().putBoolean(KEY_FILL,RepDraw.get().isTextFill());
        getEditor().putFloat(KEY_ANGLE_TEXT,RepDraw.get().getItalicText());
        getEditor().putString(KEY_TEXT,RepDraw.get().getText());
        getEditor().apply();
    }

    @Override
    public void onResume() {
        super.onResume();
        Operation.Event e = Operation.Event.values()[getPreferences().getInt(KEY_EVENT, Operation.Event.MATRIX_T.ordinal())];
        dViewDraw.setEvent(e);

        RepDraw.get().setWidth(getPreferences().getFloat(KEY_SAVE_WIDTH,50))
                     .setText(getPreferences().getString(KEY_TEXT,"Your Text"))
                     .textFill(getPreferences().getBoolean(KEY_FILL,true))
                     .textItalic(getPreferences().getBoolean(KEY_ITALIC,false))
                     .setItalicText(getPreferences().getFloat(KEY_ANGLE_TEXT,0))
                     .setAlpha(getPreferences().getInt(KEY_SAVE_ALPHA,0))
                     .setColor(getPreferences().getInt(KEY_SAVE_COLOR, Color.BLACK));

        int s = getPreferences().getInt(SOURCE_FONT,-1);
        String p = getPreferences().getString(PATH_FONT,"");
        if(s==S_ASSETS){
            try {
                RepDraw.get().setShrift(Typeface.createFromAsset(getContext().getAssets(), "fonts/" + p));
            }catch (RuntimeException e1){
                RepDraw.get().setShrift(Typeface.createFromAsset(getContext().getAssets(),"fonts/font_1.otf"));
            }
        }
        else if(s==S_STORAGE){
            try {
                RepDraw.get().setShrift(Typeface.createFromFile(p));
            }catch (RuntimeException e1){
                RepDraw.get().setShrift(Typeface.createFromAsset(getContext().getAssets(),"fonts/font_1.otf"));
            }

        }
        else {
            RepDraw.get().setShrift(Typeface.createFromAsset(getContext().getAssets(),"fonts/font_1.otf"));
        }
    }
}
