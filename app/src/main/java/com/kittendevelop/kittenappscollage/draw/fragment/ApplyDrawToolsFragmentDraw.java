package com.kittendevelop.kittenappscollage.draw.fragment;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.kittendevelop.kittenappscollage.draw.params.TutorialDialogSelParams;
import com.kittendevelop.kittenappscollage.draw.textProp.DialogSelecledTextFragment;
import com.kittendevelop.kittenappscollage.draw.repozitoryDraw.RepDraw;
import com.kittendevelop.kittenappscollage.draw.operations.Operation;
import com.kittendevelop.kittenappscollage.draw.textProp.TutorialSelectShrift;
import com.kittendevelop.kittenappscollage.draw.repozitoryDraw.RepParams;

import java.util.ArrayList;

/*применяем инструменты обработки изображения*/
public abstract class ApplyDrawToolsFragmentDraw extends ApplyCommonToolsFragmentDraw {

    private final String KEY_EVENT = "key event ApplyDrawToolsFragmentDraw";

    private final String SOURCE_FONT = "source font ApplyDrawToolsFragmentDraw";

    private final String PATH_FONT = "path font ApplyDrawToolsFragmentDraw";

    private final String KEY_ITALIC = "key italic ApplyDrawToolsFragmentDraw";

    private final String KEY_FILL = "key fill ApplyDrawToolsFragmentDraw";

    private final String KEY_ANGLE_TEXT = "key angle text ApplyDrawToolsFragmentDraw";

    private final String KEY_TEXT = "key text ApplyDrawToolsFragmentDraw";

    private ArrayList<String>fonts;

    public static final int S_STORAGE = 77;

    public static final int S_ASSETS = 88;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dViewDraw.setListPipette(new Pipette() {
            @Override
            public void listen(boolean l) {
                toolColor(null);
            }
        });
    }



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
    protected void toolColor(ImageView v) {
        super.toolColor(v);
        if(v!=null)dViewDraw.applyPipette(v.isActivated());
        else dViewDraw.applyPipette(false);
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
        TutorialDialogSelParams d = new TutorialDialogSelParams();
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
        TutorialSelectShrift.get().show(getChildFragmentManager(), DialogSelecledTextFragment.DIALOG_SEL_TXT);
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

        RepDraw.get().setWidth(getPreferences().getFloat(RepParams.KEY_SAVE_WIDTH,50))
                     .setText(getPreferences().getString(KEY_TEXT,"Your Text"))
                     .textFill(getPreferences().getBoolean(KEY_FILL,true))
                     .textItalic(getPreferences().getBoolean(KEY_ITALIC,false))
                     .setItalicText(getPreferences().getFloat(KEY_ANGLE_TEXT,0))
                     .setAlpha(getPreferences().getInt(RepParams.KEY_SAVE_ALPHA,0))
                     .setColor(getPreferences().getInt(RepParams.KEY_SAVE_COLOR, Color.BLACK));

        int s = getPreferences().getInt(SOURCE_FONT,-1);
        String p = getPreferences().getString(PATH_FONT,"");
        if(s==S_ASSETS){
            try {
                RepDraw.get().setShrift(Typeface.createFromAsset(getContext().getAssets(), "fonts/" + p));
            }catch (RuntimeException e1){
                RepDraw.get().setShrift(Typeface.create(Typeface.DEFAULT,Typeface.NORMAL));
            }
        }
        else if(s==S_STORAGE){
            try {
                RepDraw.get().setShrift(Typeface.createFromFile(p));
            }catch (RuntimeException e1){
                RepDraw.get().setShrift(Typeface.create(Typeface.DEFAULT,Typeface.NORMAL));
            }

        }
        else {
            RepDraw.get().setShrift(Typeface.create(Typeface.DEFAULT,Typeface.NORMAL));
        }
    }

//    @SuppressLint("CheckResult")
//    private void scanFonts(){
//        Observable.create((ObservableOnSubscribe<ArrayList<String>>) emitter ->
//                scaner(emitter)).compose(new ThreadTransformers.InputOutput<>())
//                .subscribe(strings -> {
//                    fonts = strings;
//                    for (String font:fonts){
//                        LYTE("font "+font);
//                    }
//                });
//    }

//    private void scaner(ObservableEmitter<ArrayList<String>> emitter){
//        String[]pr = {MediaStore.Files.FileColumns._ID,MediaStore.Files.FileColumns.TITLE,MediaStore.Files.FileColumns.DATA};
//        String sel = MediaStore.Files.FileColumns.BUCKET_DISPLAY_NAME + " = ?";
//        String select = MediaStore.Files.FileColumns.MIME_TYPE+"=?";
//        String[]args = new String[]{"application/x-font-ttf"};
////        String[]args = new String[]{"Download"};
//        Uri quest = MediaStore.Files.getContentUri("external");
//        Cursor c = getContext().getContentResolver().query(quest,pr,sel,args,null);
//        ArrayList<String> files = new ArrayList();
//        while (c.moveToNext()){
//            long id = c.getLong(c.getColumnIndexOrThrow(MediaStore.Files.FileColumns._ID));
////            final String file = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id).toString();
//            String file = c.getString(c.getColumnIndexOrThrow(MediaStore.Files.FileColumns.TITLE));
//            String type = c.getString(c.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA));
//
//            files.add(file+"|||||"+type);
//        }
//        emitter.onNext(files);
//        emitter.onComplete();
//    }

    public interface Pipette{
        void listen(boolean l);
    }
}
