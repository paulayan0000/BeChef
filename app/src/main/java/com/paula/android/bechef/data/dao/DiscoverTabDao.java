package com.paula.android.bechef.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.paula.android.bechef.data.entity.DiscoverTab;

import java.util.List;

@Dao
public interface DiscoverTabDao extends BaseDao<DiscoverTab> {
    @Query("SELECT * FROM discover_tab ORDER BY uid ASC")
    LiveData<List<DiscoverTab>> getAllLive();

    @Query("DELETE FROM discover_tab WHERE uid = :tabUid")
    void deleteItemWithTabUid(long tabUid);

    @Query("SELECT * FROM discover_tab WHERE channel_id = :channelId")
    DiscoverTab getTabWithChannelId(String channelId);
}