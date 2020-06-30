package com.kittendevelop.kittenappscollage.draw.textProp;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dynamikseekbar.DynamicSeekBar;
import com.kittendevelop.kittenappscollage.R;
import com.kittendevelop.kittenappscollage.draw.repozitoryDraw.RepDraw;
import com.kittendevelop.kittenappscollage.view.PresentPaint;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.io.IOException;

public class DialogSelecledTextFragment extends DialogFragment implements View.OnClickListener, DynamicSeekBar.OnSeekBarChangeListener {

    public final static String DIALOG_SEL_TXT = "sel txt";

    private EditText editText;

    private PresentPaint presentPaint;

    private ExpandableLayout expandable;

    private RecyclerView listShrift;

    private ImageView action_1, action_2, action_3;

    private ImageView downFonts;

    private DynamicSeekBar angleText;

    private ImageView resetAngleText, fillText;

    private WorkAdapterShrift adapterShrift;

    private int sizeBaseFonts;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(getContext().getResources().getColor(R.color.colorPrimaryTransparent)));
        return inflater.inflate(R.layout.dialog_select_text,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        presentPaint = view.findViewById(R.id.item_present_draw);
        presentPaint.setText("Your Font");
        if(RepDraw.get().getShrift()!=null)presentPaint.setShrift(RepDraw.get().getShrift());
        presentPaint.setType(PresentPaint.TEXT);
        presentPaint.setOnClickListener(this);
        editText = view.findViewById(R.id.dialog_edit_text);
        editText.setText(RepDraw.get().getText());

        expandable = view.findViewById(R.id.item_present_draw_expadable);
        expandable.setOnExpansionUpdateListener(new ExpandableLayout.OnExpansionUpdateListener() {
            @Override
            public void onExpansionUpdate(float expansionFraction, int state) {
                if(expansionFraction>=1)slideListShrift(expandable.isExpanded());
//                LYTE("expansionFraction "+expansionFraction);
//                LYTE("state "+state);

            }
        });
        initListShrift(view);
        angleText = view.findViewById(R.id.dialog_edit_text_angle);
        angleText.setOnSeekBarChangeListener(this);

        initButtons(view);

        paramView(view);

    }

    @Override
    public void onClick(View view) {
       switch (view.getId()){
           case R.id.item_present_draw:
               if(!expandable.isExpanded()){
                   openList();
               } else {
                   closeList();
               }
               break;
           case R.id.action_1:
               clickAction_1((ImageView)view);
               break;
           case R.id.action_2:
               clickAction_2((ImageView)view);
               break;
           case R.id.action_3:
               clickAction_3((ImageView)view);
               break;
            case R.id.dialog_edit_text_reset_angle:
                enableAngle((ImageView)view);
               break;
           case R.id.dialog_edit_text_fill_text:
               fillText((ImageView)view);
               break;
           case R.id.dialog_edit_text_down_fonts:
               searchFonts((ImageView)view);
               break;
       }
    }

    @Override
    public void onProgressChanged(DynamicSeekBar seekBar, int progress, boolean isTouch) {

    }

    @Override
    public void onStartTrackingTouch(DynamicSeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(DynamicSeekBar seekBar) {

    }

    protected void enableAngle(ImageView view){

    }

    protected void fillText(ImageView view){

    }

    protected void searchFonts(ImageView view){

    }

    protected void slideListShrift(boolean open){

    }

    protected void paramView(final View view){
        ViewTreeObserver viewTreeObserver = view.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    int width = view.getWidth();
                    float step = getContext().getResources().getDimension(R.dimen.margin_save)*6;
                    step += getContext().getResources().getDimension(R.dimen.param_save)*2;
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)angleText.getLayoutParams();
                    params.width =width- (int) step;
                    angleText.setLayoutParams(params);

                }
            });
        }
    }

    protected void clickAction_1(ImageView view){

    }

    protected void clickAction_2(ImageView view){

    }

    protected void clickAction_3(ImageView view){

    }

    protected void openList(){
        expandable.expand();
    }

    protected void closeList(){
        expandable.collapse();
    }

    protected ExpandableLayout getExpand(){
        return expandable;
    }

    protected RecyclerView getListShrift(){
        return listShrift;
    }

    protected PresentPaint getPresent(){
        return presentPaint;
    }

    protected EditText getEnterText(){
        return editText;
    }

    protected WorkAdapterShrift getAdapter(){
        return adapterShrift;
    }

    protected int getSizeBaseFonts(){
        return sizeBaseFonts;
    }

    protected DynamicSeekBar getBarAngle(){
        return angleText;
    }

    protected ImageView getActFill(){
        return fillText;
    }

    protected ImageView getActItal(){
        return resetAngleText;
    }

    protected ImageView getVisibleInstruct(){
        return downFonts;
    }

    protected void initListShrift(View view){
        listShrift = view.findViewById(R.id.item_present_draw_list_shrift);
        listShrift.setHasFixedSize(true);
        listShrift.setLayoutManager(new GridLayoutManager(getContext(),1));
        String[]fonts = null;
        try {
            fonts = getContext().getAssets().list("fonts");
            sizeBaseFonts = fonts.length;
        } catch (IOException e) {
            e.printStackTrace();
        }
        adapterShrift = new WorkAdapterShrift(getContext(),fonts);
        listShrift.setAdapter(adapterShrift);
    }

    private void initButtons(View view){
        action_1 = view.findViewById(R.id.action_1);
        action_1.setOnClickListener(this);
        action_1.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_done,null));
        action_2 = view.findViewById(R.id.action_2);
        action_2.setOnClickListener(this);
        action_2.setImageDrawable(getContext().getResources().getDrawable(R.drawable.icon_clear_link,null));
        action_3 = view.findViewById(R.id.action_3);
        action_3.setOnClickListener(this);
        action_3.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_close,null));
        resetAngleText = view.findViewById(R.id.dialog_edit_text_reset_angle);
        resetAngleText.setOnClickListener(this);
        fillText = view.findViewById(R.id.dialog_edit_text_fill_text);
        fillText.setOnClickListener(this);
        downFonts = view.findViewById(R.id.dialog_edit_text_down_fonts);
        downFonts.setOnClickListener(this);
    }
}
