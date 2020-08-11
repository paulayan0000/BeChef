package com.paula.android.bechef.data.dao;

import com.paula.android.bechef.data.entity.DiscoverTab;
import java.util.ArrayList;
import java.util.List;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface DiscoverTabDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(ArrayList<DiscoverTab> discoverTabs);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(DiscoverTab discoverTab);

    @Delete
    void deleteItem(DiscoverTab discoverTab);

    @Delete
    void deleteItems(DiscoverTab... discoverTabs);

    @Query("SELECT channel_id FROM discover_tab ORDER BY uid ASC")
    List<String> getAllChannelIds();

    @Query("SELECT tab_name FROM discover_tab ORDER BY uid ASC")
    List<String> getAllTabTitles();
}
