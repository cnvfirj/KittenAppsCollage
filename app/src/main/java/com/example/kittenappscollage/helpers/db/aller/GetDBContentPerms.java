package com.example.kittenappscollage.helpers.db.aller;


import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;

@Database(entities = {ContentPermis.class}, version = 1, exportSchema = false)
public abstract class GetDBContentPerms extends RoomDatabase {
    public abstract MethodsDBContentPerms work();
}
