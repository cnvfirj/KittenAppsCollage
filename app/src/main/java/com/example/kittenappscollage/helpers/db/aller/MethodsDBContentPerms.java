package com.example.kittenappscollage.helpers.db.aller;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;


import java.util.List;


@Dao
public interface MethodsDBContentPerms {

    @Query("SELECT * FROM contentPerms")
    List<ContentPermis> loadAll();

    @Query("SELECT * FROM contentPerms WHERE visible = :visible")
    List<ContentPermis> loadVis(int visible);

    @Query("SELECT * FROM contentPerms WHERE storage = :storage")
    List<ContentPermis> loadStor(int storage);


    @Query("SELECT * FROM contentPerms WHERE system = :system")
    List<ContentPermis> loadSys(String system);

    @Query("SELECT * FROM contentPerms WHERE keyPath = :keyPath")
    ContentPermis getPerm(String keyPath);

    @Insert
    void insert(ContentPermis perm);

    @Update
    void update(ContentPermis perm);

    @Delete
    void delete(ContentPermis perm);
}
