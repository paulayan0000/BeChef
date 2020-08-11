package com.paula.android.bechef.data.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;

public class BaseTab {
    @PrimaryKey
    @NonNull
    private int uid;

    @ColumnInfo(name = "tab_name")
    private String mTabName;

    public BaseTab(int uid, String tabName) {
        this.uid = uid;
        mTabName = tabName;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getTabName() {
        return mTabName;
    }

    public void setTabName(String tabName) {
        mTabName = tabName;
    }
}
