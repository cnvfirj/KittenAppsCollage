package com.kittendevelop.kittenappscollage.collect.reviewImage;

import android.graphics.Bitmap;
import android.net.Uri;

public interface SelectorOperationReview {

    void edit(Bitmap bitmap);
    void share(Uri img);
    void exit();
}
