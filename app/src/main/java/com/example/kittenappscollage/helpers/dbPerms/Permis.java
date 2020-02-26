package com.example.kittenappscollage.helpers.dbPerms;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "grandPerm")
public class Permis {

    @PrimaryKey
    @NonNull
    public String uriPerm;

    public String report;

    public String delimiter;

    public Permis(@NonNull String uriPerm) {
        this.uriPerm = uriPerm;
    }

    public String getPerm(){
        return uriPerm;
    }

    public String getReport() {
        return report;
    }

    public String getDelimiter() {
        return delimiter;
    }


}
