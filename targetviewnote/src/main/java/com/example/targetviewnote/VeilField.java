package com.example.targetviewnote;

import android.graphics.Color;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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
import com.example.targetviewnote.veil.DrawVeil;


public class VeilField extends DialogFragment implements DrawMidiVeil.InternalListener {

    private DrawMidiVeil veil;

    private TargetView.OnClickTargetViewNoleListener clickListener;

//    private int colorContent;

    private int actionExit;

    private FrameLayout content;

    private TextView title;

    private ImageView iconTitle;

    private EditText note;

    private ImageView softKey;

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
        Bundle bundle = getArguments();
        if(bundle!=null){
            veil.setColorVeil(bundle.getInt(TargetView.KEY_COLOR_BACK,Color.BLUE));
            veil.setTarget(bundle.getIntArray(TargetView.KEY_TARGET_VIEW));
            veil.setFrame(bundle.getInt(TargetView.KEY_TARGET_FRAME,0));
            setColorBackgroundContent(bundle.getInt(TargetView.KEY_COLOR_BACKGROUND_CONTENT,Color.BLUE));
            setClickListener(bundle.getInt(TargetView.KEY_SOURCE_TARGET));
            setActionExit(bundle.getInt(TargetView.KEY_ACTION_EXIT,TargetView.TOUCH_UOT));
            setTextTitle(bundle.getString(TargetView.KEY_TEXT_TITLE,""));
            setSizeTitle(bundle.getFloat(TargetView.KEY_SIZE_TITLE,100));
            setIconTitle(bundle.getInt(TargetView.KEY_ICON_TITLE,0));
            setColorTitle(bundle.getInt(TargetView.KEY_COLOR_TEXT_TITLE,Color.WHITE));
        }
           veil.setListener(this);

    }



    @Override
    public void onStart() {
        super.onStart();
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
        /*готовность области контента*/
//        FrameLayout l = getView().findViewById(R.id.content);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) content.getLayoutParams();
        params.leftMargin = (int)r.left;
        params.topMargin = (int)r.top;
        params.width = (int)r.width();
        params.height = (int)r.height();
        content.setLayoutParams(params);
//        l.setBackgroundColor(colorContent);
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
        softKey = view.findViewById(R.id.icon_soft_key);
        content = view.findViewById(R.id.content);
    }
    private void setClickListener(int source){
        try {
            if (source == TargetView.SOURCE_ACTIVITY) {
                clickListener = (TargetView.OnClickTargetViewNoleListener) getContext();
            } else if (source == TargetView.SOURCE_FRAGMENT) {
                clickListener = (TargetView.OnClickTargetViewNoleListener) getTargetFragment();
            }
        }catch (ClassCastException e){
            Log.e("TargetView", "implement the interface: OnClickTargetViewNoleListener in the called activity or fragment ");
        }
    }

    private void setTextTitle(String text){
        if(!text.equals("")){
            title.setText(text);
        }
    }

    private void setIconTitle(int id){
        if(id!=0){
            Drawable d = getContext().getResources().getDrawable(id,null);
            iconTitle.setImageDrawable(d);
        }
    }

    private void setColorTitle(int color){
        title.setTextColor(color);
    }

    private void setSizeTitle(float size){
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)iconTitle.getLayoutParams();
        params.height = (int) size;
        params.width = (int) size;
        iconTitle.setLayoutParams(params);
        size/=getContext().getApplicationContext().getResources().getDisplayMetrics().density;
        title.setTextSize(size);

    }

    private void setColorBackgroundContent(int color){
        content.setBackgroundColor(color);
    }

    private void setActionExit(int i){
        actionExit = i;
    }
}
