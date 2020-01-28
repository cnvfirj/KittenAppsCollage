package com.example.kittenappscollage.helpers.db;


import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "perms")
public class PermStorage {

    public PermStorage(@NonNull String path) {
        this.path = path;
    }

    /*адрес папки (ключи в мапе)*/
    @PrimaryKey
    @NonNull
    public String path;

    /*значение разрешения*/
    public String uri;

    /*разрешение редактирования*/
    public boolean redact;

    /*видимость папки*/
    public boolean visible;

    @NonNull
    public String getPath() {
        return path;
    }

    public String getUri() {
        return uri;
    }

    public boolean isRedact() {
        return redact;
    }

    public boolean isVisible() {
        return visible;
    }


    public PermStorage setUri(String uri){
        this.uri = uri;
        return this;
    }
    public PermStorage setVis(boolean vis){
        this.visible = vis;
        return this;
    }
    public PermStorage setRed(boolean red){
        this.redact = red;
        return this;
    }

}
