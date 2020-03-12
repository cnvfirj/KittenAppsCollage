package com.example.kittenappscollage.draw.textProp.fonts;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "searchFonts")
public class ItemFont {

    @PrimaryKey
    @NonNull
    public String path;

    public long id;


}
