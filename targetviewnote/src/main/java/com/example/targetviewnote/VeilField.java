package com.example.targetviewnote;

import android.graphics.Color;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.targetviewnote.veil.DrawMidiVeil;


public class VeilField extends DialogFragment implements DrawMidiVeil.InternalListener{

    private DrawMidiVeil veil;

    private TargetView.OnClickTargetViewNoleListener clickListener;

//    private int colorContent;

    private int sizeTitle, sizeNote;

    private Drawable dTitle, dSoftKey;

    private int actionExit;

    private FrameLayout content;

    private TextView title;

    private ImageView iconTitle, iconSoftKey;

    private EditText note;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        colorContent = Color.BLUE;
        return inflater.inflate(R.layout.field_veil,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        veil = view.findViewById(R.id.veil);
        initViews(view);

    }


    @Override
    public void setArguments(@Nullable Bundle args) {
        super.setArguments(args);
        setParams(getArguments());
    }

    @Override
    public void onStart() {
        super.onStart();
        setParams(getArguments());
    }

    @Override
    public void onResume() {
        super.onResume();
        Window window = getDialog().getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams windowParams = window.getAttributes();
        windowParams.dimAmount = 0f;
        windowParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        window.setGravity(Gravity.CENTER);
        window.setAttributes(windowParams);
    }

    @Override
    public void rect(RectF r) {
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) content.getLayoutParams();
        params.leftMargin = (int)r.left;
        params.topMargin = (int)r.top;
        params.width = (int)r.width();
        params.height = (int)r.height();
        content.setLayoutParams(params);
        appSizeNote();
    }

    @Override
    public void click(int i) {
        if(clickListener!=null)clickListener.onClickTarget(i);
        if(actionExit==i&&isVisible())dismiss();
    }

    private void initViews(View view){
        title = view.findViewById(R.id.text_title);
        iconTitle = view.findViewById(R.id.icon_title);
        note = view.findViewById(R.id.text_note);
        iconSoftKey = view.findViewById(R.id.icon_soft_key);
        content = view.findViewById(R.id.content);
        iconSoftKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if(clickListener!=null)clickListener.onClickTarget(TargetView.TOUCH_SOFT_KEY);

            }
        });
    }

    private void setParams(Bundle bundle){

        if(bundle!=null&&(veil!=null)){
            setColorBackgroundContent(bundle.getInt(TargetView.KEY_COLOR_BACKGROUND_CONTENT,Color.BLUE));
            setClickListener(bundle.getInt(TargetView.KEY_SOURCE_TARGET));
            setActionExit(bundle.getInt(TargetView.KEY_ACTION_EXIT,TargetView.TOUCH_UOT));
            setTextTitle(bundle.getString(TargetView.KEY_TEXT_TITLE,""));
            setSizeTitle(bundle.getFloat(TargetView.KEY_SIZE_TITLE,50));
            setIconTitle(bundle.getInt(TargetView.KEY_ICON_TITLE,0));
            setColorTitle(bundle.getInt(TargetView.KEY_COLOR_TEXT_TITLE,Color.WHITE));
            setTextNote(bundle.getString(TargetView.KEY_TEXT_NOTE,""));
            setSizeNote(bundle.getFloat(TargetView.KEY_SIZE_NOTE,25));
            setColorNote(bundle.getInt(TargetView.KEY_COLOR_TEXT_NOTE,Color.WHITE));
            setIconSoftKey(bundle.getInt(TargetView.KEY_ICON_SOFT_KEY,0));
            veil.setColorVeil(bundle.getInt(TargetView.KEY_COLOR_BACK,Color.BLUE));
            veil.setTarget(bundle.getIntArray(TargetView.KEY_TARGET_VIEW));
            veil.setFrame(bundle.getInt(TargetView.KEY_TARGET_FRAME,0));
            veil.setContentVeil(bundle.getInt(TargetView.KEY_SIZE_CONTENT_WINDOW,TargetView.MINI_VEIL));
            veil.setColorDimming(bundle.getInt(TargetView.KEY_DIMMING_BACKGROUND,0));
            veil.setListener(this);
            veil.invalidate();
        }
     }
    private void setClickListener(int source){

        try {
            if (source == TargetView.SOURCE_ACTIVITY) {
                if(getContext()!=null)clickListener = (TargetView.OnClickTargetViewNoleListener) getContext();
            } else if (source == TargetView.SOURCE_FRAGMENT) {
                if(getTargetFragment()!=null)clickListener = (TargetView.OnClickTargetViewNoleListener) getTargetFragment();
            }
        }catch (ClassCastException e){
            Log.e("TargetView", "implement the interface: OnClickTargetViewNoleListener in the called activity or fragment ");
        }
    }

    private void setIconSoftKey(int id){
        if(id!=0){
            if(getContext()!=null)dSoftKey = getContext().getResources().getDrawable(id,null);
        }else {
            dSoftKey = null;
            if(iconSoftKey!=null)iconSoftKey.setImageDrawable(null);
        }
    }

    private void setTextNote(String text){
            if(note!=null)note.setText(text);
    }

    private void setColorNote(int color){
        if(note!=null)note.setTextColor(color);
    }

    private void setSizeNote(float size){
        sizeNote = (int)size;
        size/=getContext().getApplicationContext().getResources().getDisplayMetrics().density;
        if(note!=null)note.setTextSize(size);
    }

    private void setTextTitle(String text){
//        if(!text.equals("")){
            if(title!=null)title.setText(text);
//        }
    }

    private void setIconTitle(int id){
        if(id!=0){
            if(getContext()!=null)dTitle = getContext().getResources().getDrawable(id,null);
            appSizeTitle();
        }else {
            dTitle = null;
            if(iconTitle!=null)iconTitle.setImageDrawable(null);
        }
    }

    private void setColorTitle(int color){
        if(title!=null)title.setTextColor(color);
    }

    private void setSizeTitle(float size){
        sizeTitle = (int)size;
        appSizeTitle();
        size/=getContext().getApplicationContext().getResources().getDisplayMetrics().density;
        if(title!=null)title.setTextSize(size);

    }

    private void setColorBackgroundContent(int color){
        if(content!=null)content.setBackgroundColor(color);
    }

    private void setActionExit(int i){
        actionExit = i;
    }

    private void appSizeTitle(){
        if(dTitle!=null&&iconTitle!=null){
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)iconTitle.getLayoutParams();
            params.height = sizeTitle;
            params.width = sizeTitle;
            iconTitle.setLayoutParams(params);
            iconTitle.setImageDrawable(dTitle);
        }
    }

    private void appSizeNote(){
        if(dSoftKey!=null){
            paramView(note);
        }
    }

    private void paramView(final EditText view){
        ViewTreeObserver viewTreeObserver = view.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) iconSoftKey.getLayoutParams();
                    if(view.getText().length()>0) {
                        Layout layout = view.getLayout();
                        int offset = view.getText().length() - 1;
                        int lineOfText = layout.getLineForOffset(offset);
                        int xCoordinate = (int) layout.getSecondaryHorizontal(offset);
                        int yCoordinate = layout.getLineTop(lineOfText);
                        params.height = sizeNote;
                        params.width = sizeNote;
                        params.topMargin = (view.getTop() + yCoordinate) ;
                        params.leftMargin = (int) (xCoordinate + view.getTextSize());
                        if (content.getWidth() < params.leftMargin + sizeNote) {
                            params.leftMargin = 0;
                            params.topMargin = (int) ((view.getTop() + yCoordinate) +params.height*1.1f);
                        }
                    }else {
                        params.topMargin = 0;
                        params.leftMargin = 0;
                    }
                    iconSoftKey.setLayoutParams(params);
                    iconSoftKey.setImageDrawable(dSoftKey);
                }
            });
        }
    }
}
