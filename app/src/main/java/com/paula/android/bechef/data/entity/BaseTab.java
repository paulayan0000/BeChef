package com.paula.android.bechef.data.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

public class BaseTab {
    @PrimaryKey(autoGenerate = true)
    private long uid = 0;

    @ColumnInfo(name = "tab_name")
    @NonNull
    private String mTabName;

    public BaseTab(@NonNull String tabName) {
        mTabName = tabName;
    }

    @Ignore
    public BaseTab(@NonNull BaseTab baseTab) {
        uid = baseTab.getUid();
        mTabName = baseTab.getTabName();
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    @NonNull
    public String getTabName() {
        return mTabName;
    }

    public void setTabName(@NonNull String tabName) {
        mTabName = tabName;
    }

    @NonNull
    @Override
    public String toString() {
        return mTabName;
    }
}