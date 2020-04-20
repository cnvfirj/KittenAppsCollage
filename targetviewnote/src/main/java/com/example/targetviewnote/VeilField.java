package com.example.targetviewnote;

import android.graphics.Color;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.targetviewnote.veil.DrawMidiVeil;
import com.example.targetviewnote.veil.DrawVeil;


public class VeilField extends DialogFragment implements DrawMidiVeil.InternalListener {

    private DrawMidiVeil veil;

    private TargetView.OnClickTargetViewNoleListener clickListener;

    private int colorContent;

    private int actionExit;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        colorContent = Color.BLUE;
        return inflater.inflate(R.layout.field_veil,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        veil = view.findViewById(R.id.veil);
        Bundle bundle = getArguments();
        if(bundle!=null){
            veil.setColorBackground(bundle.getInt(TargetView.KEY_COLOR_BACK,Color.BLUE));
            veil.setTarget(bundle.getIntArray(TargetView.KEY_TARGET_VIEW));
            veil.setFrame(bundle.getInt(TargetView.KEY_TARGET_FRAME,0));
            setColorContent(bundle.getInt(TargetView.KEY_COLOR_BACKGROUND_CONTENT,Color.BLUE));
            setClickListener(bundle.getInt(TargetView.KEY_SOURCE_TARGET));
            setActionExit(bundle.getInt(TargetView.KEY_ACTION_EXIT,TargetView.TOUCH_UOT));
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
        FrameLayout l = getView().findViewById(R.id.content);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) l.getLayoutParams();
        params.leftMargin = (int)r.left;
        params.topMargin = (int)r.top;
        params.width = (int)r.width();
        params.height = (int)r.height();
        l.setLayoutParams(params);
        l.setBackgroundColor(colorContent);
    }

    @Override
    public void click(int i) {
        if(clickListener!=null)clickListener.onClickTarget(i);
        if(actionExit==i&&isVisible())dismiss();
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

    private void setColorContent(int color){
        colorContent = color;
    }

    private void setActionExit(int i){
        actionExit = i;
    }
}
