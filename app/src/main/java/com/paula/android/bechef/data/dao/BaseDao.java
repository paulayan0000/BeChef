package com.paula.android.bechef.data.dao;

import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Update;

import java.util.ArrayList;

public interface BaseDao<T> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long[] insert(ArrayList<T> items);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insert(T item);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateItem(T item);
}
