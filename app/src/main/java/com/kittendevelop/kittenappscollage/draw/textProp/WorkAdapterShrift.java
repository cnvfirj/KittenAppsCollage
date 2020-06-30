package com.kittendevelop.kittenappscollage.draw.textProp;

import android.content.Context;

import androidx.annotation.NonNull;

import com.kittendevelop.kittenappscollage.view.PresentPaint;

import java.util.ArrayList;

public class WorkAdapterShrift extends AdapterShrift {

    private ListenAdapterShrift listen;

    public WorkAdapterShrift(Context context, @NonNull String[] base) {
        super(context, base);
    }

    public WorkAdapterShrift(Context context, @NonNull ArrayList<String> base) {
        super(context, base);
    }

    public WorkAdapterShrift setListen(ListenAdapterShrift listen){
        this.listen = listen;
        return this;
    }

    @Override
    protected void clickItem(PresentPaint view, int pos) {
        super.clickItem(view, pos);
        if(listen!=null){
            listen.item(pos, getFonts().get(pos));
            listen.font(view.getShrift());
        }
    }


}
