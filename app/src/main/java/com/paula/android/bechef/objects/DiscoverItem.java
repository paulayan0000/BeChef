package com.paula.android.bechef.objects;

import com.paula.android.bechef.utils.Constants;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "discover_table")
public class DiscoverItem {
    @PrimaryKey
    @NonNull
    private int uid;

    @ColumnInfo(name = "discover_channel_id")
    private String mChannelId;

    @ColumnInfo(name = "discover_channel_name")
    private String mChannelName;

    public DiscoverItem(int uid, String channelId, String channelName) {
        this.uid = uid;
        mChannelId = channelId;
        mChannelName = channelName;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getChannelId() {
        return mChannelId;
    }

    public void setChannelId(String channelId) {
        mChannelId = channelId;
    }

    public String getChannelName() {
        return mChannelName;
    }

    public void setChannelName(String channelName) {
        mChannelName = channelName;
    }
}
