package com.kittendevelop.kittenappscollage.helpers.dbPerms;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Permis.class}, version = 5, exportSchema = false)
public abstract class GetPermsFolds  extends RoomDatabase {

    public abstract MethodsDBPerm work();

}
