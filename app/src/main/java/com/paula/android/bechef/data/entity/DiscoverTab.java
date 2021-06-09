package com.paula.android.bechef.data.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;

@Entity(tableName = "discover_tab")
public class DiscoverTab extends BaseTab {
    @ColumnInfo(name = "channel_id")
    private String mChannelId;

    public DiscoverTab(String channelId, String tabName) {
        super(tabName);
        mChannelId = channelId;
    }

    @Ignore
    public DiscoverTab(DiscoverTab baseTab) {
        super(baseTab);
        mChannelId = baseTab.mChannelId;
    }

    public String getChannelId() {
        return mChannelId;
    }

    public void setChannelId(String channelId) {
        mChannelId = channelId;
    }
}