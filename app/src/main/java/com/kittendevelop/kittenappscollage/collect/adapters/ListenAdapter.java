package com.kittendevelop.kittenappscollage.collect.adapters;

import android.view.View;
import android.widget.ImageView;

public interface ListenAdapter {
     void click(int adapter,ImageView img, ImageView check, int pos);
     void longClick(int adapter,ImageView img, ImageView check, int pos);
     void createHolder(int adapter,View holder, int pos);
     void createContentHolder(int adapter,View[]content, int pos);
     void exit(int adapter);
}
