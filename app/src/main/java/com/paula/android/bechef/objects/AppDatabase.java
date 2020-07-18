package com.paula.android.bechef.objects;

import com.paula.android.bechef.objects.dao.DiscoverTableDao;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {DiscoverItem.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract DiscoverTableDao userDao();
}
