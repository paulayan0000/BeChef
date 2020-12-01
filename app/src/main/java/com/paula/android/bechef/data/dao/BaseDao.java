package com.paula.android.bechef.data.dao;

import java.util.ArrayList;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Update;

public interface BaseDao<T> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long[] insert(ArrayList<T> items);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insert(T item);

    @Delete
    void deleteItem(T items);

    @Delete
    void deleteItems(ArrayList<T> items);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateItem(T item);
}
