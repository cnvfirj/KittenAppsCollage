package com.example.kittenappscollage.collect.dialogActions;

import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.kittenappscollage.R;

import static com.example.kittenappscollage.collect.adapters.ListenLoadFoldAdapter.ROOT_ADAPTER;

public class DialogAction extends DialogFragment implements View.OnClickListener {

    public static final int DIALOG_ACTION = -12;

    public static final String KEY_ACTION = "key action";

    public static final String KEY_ADAPTER = "key adapter";

    public static final int ACTION_DELETE = 1;

    public static final int ACTION_SHARE = 2;

    public static final int ACTION_TRANSFER = 3;

    public static final int ACTION_RENAME = 4;

    public static final int ACTION_INVIS = 5;

    private int indexAction;

    private int indexAdapter;

    private ImageView done, close, clear;

    private EditText text;

    private ListenActions listen;

    public static DialogAction inst(int index, int adapter, Fragment target){
        DialogAction d = new DialogAction();

        Bundle b = new Bundle();
        b.putInt(KEY_ACTION, index);
        b.putInt(KEY_ADAPTER, adapter);
        d.setArguments(b);
        d.setTargetFragment(target,DIALOG_ACTION);
        return d;
    }

    public static DialogAction inst(int index, int adapter){
        DialogAction d = new DialogAction();

        Bundle b = new Bundle();
        b.putInt(KEY_ACTION, index);
        b.putInt(KEY_ADAPTER, adapter);
        d.setArguments(b);
        return d;
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(getContext().getResources().getColor(R.color.colorPrimaryTransparent)));
        indexAction = 1;
        Bundle b = getArguments();
        if(b!=null){
            indexAction = b.getInt(KEY_ACTION);
            indexAdapter = b.getInt(KEY_ADAPTER, ROOT_ADAPTER);
        }
        return inflater.inflate(R.layout.dialog_action_gallery,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        done = view.findViewById(R.id.dialog_action_gallery_done);
        done.setOnClickListener(this);
        close = view.findViewById(R.id.dialog_action_gallery_close);
        close.setOnClickListener(this);
        clear = view.findViewById(R.id.dialog_action_gallery_clear);
        clear.setOnClickListener(this);
        text = view.findViewById(R.id.dialog_action_gallery_enter_name);
        selectText();

    }

    @Override
    public void onResume() {
        super.onResume();
        Window window = getDialog().getWindow();
        Rect rect = new Rect();
        getActivity().getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        float h = getContext().getResources().getDimension(R.dimen.param_save)*2;
        h+=getContext().getResources().getDimension(R.dimen.margin_save)*4;
        window.setLayout((int) (rect.right*0.8), (int)h);
        window.setGravity(Gravity.CENTER);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.dialog_action_gallery_done:
                listen = (ListenActions)getTargetFragment();
                listen.result(true, indexAction);
                if(indexAction==ACTION_RENAME){
                    listen.result(true,text.getText().toString());
                }
                break;
            case R.id.dialog_action_gallery_close:
                listen = (ListenActions)getTargetFragment();
                listen.result(false, indexAction);
                break;
            case R.id.dialog_action_gallery_clear:
                text.setText("");
                break;

        }
    }

    private void selectText(){
        switch (indexAction){
            case ACTION_DELETE:
                delete();
                break;
            case ACTION_SHARE:
                break;
            case ACTION_INVIS:
                break;
            case ACTION_RENAME:
                rename();
                break;
            case ACTION_TRANSFER:
                break;

        }
        paramView(text);
    }

    private void rename(){
        implementPropEdit(true);
        text.setText("Имя папки");

    }

    private void delete(){
        implementPropEdit(false);
        String text = "Выбранные объекты будут удалены";
        if(indexAdapter==ROOT_ADAPTER)text = "Выбранная папка будет удалена со всем содержимым";
        this.text.setText(text);

    }

    private void implementPropEdit(boolean v){
        text.setFocusable(v);
        text.setClickable(v);
        text.setCursorVisible(v);
        text.setEnabled(v);
        clear.setVisibility(v?View.VISIBLE:View.INVISIBLE);
    }
    protected void paramView(final View view){
        ViewTreeObserver viewTreeObserver = view.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    Window window = getDialog().getWindow();
                    Rect rect = new Rect();
                    getActivity().getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
                    float h = getContext().getResources().getDimension(R.dimen.param_save);
                    h+=view.getHeight();
                    h+=getContext().getResources().getDimension(R.dimen.margin_save)*4;
                    window.setLayout((int) (rect.right*0.8), (int)h);
                    window.setGravity(Gravity.CENTER);

                }
            });
        }
    }
}
