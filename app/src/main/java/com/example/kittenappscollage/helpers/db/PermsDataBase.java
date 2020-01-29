package com.example.kittenappscollage.helpers.db;


import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {PermStorage.class}, version = 1, exportSchema = false)
public abstract class PermsDataBase extends RoomDatabase {
   public abstract WorkWithPerms workPerms();

}
