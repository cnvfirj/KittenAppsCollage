package com.example.kittenappscollage.draw;

import android.app.Dialog;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.kittenappscollage.R;
import com.example.kittenappscollage.draw.repozitoryDraw.RepDraw;
import com.example.kittenappscollage.view.PresentPaint;

import net.cachapa.expandablelayout.ExpandableLayout;

import static com.example.kittenappscollage.helpers.Massages.LYTE;

public class DialogSelecledTextFragment extends DialogFragment implements View.OnClickListener {

    public final static String DIALOG_SEL_TXT = "sel txt";

    private EditText editText;

    private PresentPaint presentPaint;

    private ExpandableLayout expandable;

    private ListView listShrift;

    public static DialogSelecledTextFragment get(){
        DialogSelecledTextFragment d = new DialogSelecledTextFragment();
        return d;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(getContext().getResources().getColor(R.color.colorPrimaryTransparent)));
//        RepDraw.get().setShrift(Typeface.createFromAsset(getContext().getAssets(),"fonts/lt_ki_Ouroboros_Regular.ttf"));
        return inflater.inflate(R.layout.dialog_select_text,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        presentPaint = view.findViewById(R.id.item_present_draw);
        presentPaint.setText("Shrift");
        presentPaint.setType(PresentPaint.TEXT);
        presentPaint.setWidthPaint((int) (getContext().getResources().getDimension(R.dimen.param_save)/1.4f));
        presentPaint.setOnClickListener(this);
        editText = view.findViewById(R.id.dialog_edit_text);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        expandable = view.findViewById(R.id.item_present_draw_expadable);
        listShrift = view.findViewById(R.id.item_present_draw_list_shrift);
    }

    @Override
    public void onResume() {
        super.onResume();
        Window window = getDialog().getWindow();
        Resources res = getContext().getResources();
        float height = res.getDimension(R.dimen.param_save)*3;
        height+=res.getDimension(R.dimen.margin_save)*6;
        float width = res.getDimension(R.dimen.param_color_pick)+res.getDimension(R.dimen.margin_save)*2;
        window.setLayout((int) (width), (int)(height));
        window.setGravity(Gravity.CENTER);
    }

    @Override
    public void onClick(View view) {
       switch (view.getId()){
           case R.id.item_present_draw:
               LYTE("click present");
               if(!expandable.isExpanded())expandable.expand();
               else expandable.collapse();
           break;
       }
    }

    private void type(){
       Typeface.create(Typeface.SERIF, Typeface.NORMAL);
    }
}
