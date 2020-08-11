package com.paula.android.bechef.data.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(tableName = "discover_tab")
public class DiscoverTab extends BaseTab {
    @ColumnInfo(name = "channel_id")
    private String mChannelId;

    public DiscoverTab(int uid, String channelId, String tabName) {
        super(uid, tabName);
        mChannelId = channelId;
    }

    public String getChannelId() {
        return mChannelId;
    }

    public void setChannelId(String channelId) {
        mChannelId = channelId;
    }
}
