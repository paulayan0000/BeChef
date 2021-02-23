package com.paula.android.bechef.data.dao;

import com.paula.android.bechef.data.entity.BaseTab;
import com.paula.android.bechef.data.entity.DiscoverTab;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface DiscoverTabDao extends BaseDao<DiscoverTab> {
    @Query("SELECT channel_id FROM discover_tab ORDER BY uid ASC")
    List<String> getAllChannelIds();

    @Query("SELECT tab_name FROM discover_tab ORDER BY uid ASC")
    List<String> getAllTabTitles();

    @Query("SELECT * FROM discover_tab ORDER BY uid ASC")
    LiveData<List<DiscoverTab>> getAllLive();

    @Query("SELECT * FROM discover_tab ORDER BY uid ASC")
    List<DiscoverTab> getAll();

    @Query("DELETE FROM discover_tab WHERE uid = :tabUid")
    void deleteItemWithTabUid(long tabUid);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateBaseTab(DiscoverTab discoverTab);

    @Query("SELECT * FROM discover_tab WHERE channel_id = :channelId")
    DiscoverTab getTabWithChannelId(String channelId);
}
