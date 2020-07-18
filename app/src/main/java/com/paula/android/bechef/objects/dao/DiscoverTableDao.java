package com.paula.android.bechef.objects.dao;

import com.paula.android.bechef.objects.DiscoverItem;

import java.util.ArrayList;
import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface DiscoverTableDao {

    @Query("SELECT discover_channel_id FROM discover_table WHERE uid = (:index)")
    String getChannelAt(int index);

    @Insert (onConflict = OnConflictStrategy.IGNORE)
    void insert(ArrayList<DiscoverItem> discoverItems);

    @Delete
    void delete(DiscoverItem... discoverItems);

    @Query("SELECT discover_channel_name FROM discover_table ORDER BY uid ASC")
    List<String> getAllTabTitles();
}
