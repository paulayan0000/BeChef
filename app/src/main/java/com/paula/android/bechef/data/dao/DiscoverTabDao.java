package com.paula.android.bechef.data.dao;

import com.paula.android.bechef.data.entity.DiscoverTab;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Query;

@Dao
public interface DiscoverTabDao extends BaseDao<DiscoverTab> {
    @Query("SELECT channel_id FROM discover_tab ORDER BY uid ASC")
    List<String> getAllChannelIds();

    @Query("SELECT tab_name FROM discover_tab ORDER BY uid ASC")
    List<String> getAllTabTitles();

    @Query("SELECT * FROM discover_tab ORDER BY uid ASC")
    List<DiscoverTab> getAll();
}
