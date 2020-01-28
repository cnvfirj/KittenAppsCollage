package com.example.kittenappscollage.helpers.db;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface WorkWithPerms {

    @Query("SELECT * FROM perms")
    List<PermStorage>loadAll();

    @Query("SELECT * FROM perms WHERE path = :path")
    PermStorage getPerm(String path);

    @Insert
    void insert(PermStorage perm);

    @Update
    void update(PermStorage perm);

    @Delete
    void delete(PermStorage perm);
}
