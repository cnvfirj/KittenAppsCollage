package com.example.kittenappscollage.helpers.db.aller;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "contentPerms")
public class ContentPermis {

    @PrimaryKey
    @NonNull
    public String keyPath;//адрес файла

    public String uriPerm;//разрешение или запрет на редактирование

    public String uriDocFile;//зашифрованый документ. Нужен для работы с картой памяти

    /*принимает параметр VISIBLE or INVISIBLE*/
    public int visible;//не видимая в этом приложении папка

    public int storage;//индекс зранилища. 0 - устройство

    public String system;//тип доступа к файлу. Например File или DocumentFile

    public ContentPermis(@NonNull String keyPath) {
        this.keyPath = keyPath;
    }
}
