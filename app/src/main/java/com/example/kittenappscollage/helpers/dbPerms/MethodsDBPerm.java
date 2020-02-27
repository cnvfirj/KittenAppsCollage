package com.example.kittenappscollage.helpers.dbPerms;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MethodsDBPerm {

    @Query("SELECT * FROM grandPerm")
    List<Permis> list();

    @Query("SELECT * FROM grandPerm WHERE uriPerm = :uriPerm")
    Permis getPerm(String uriPerm);

    @Query("SELECT * FROM grandPerm WHERE id = :id")
    Permis getPermInId(long id);

    @Insert
    void insert(Permis perm);

    @Update
    void update(Permis perm);

    @Delete
    void delete(Permis perm);
}
