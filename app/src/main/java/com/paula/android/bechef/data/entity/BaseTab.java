package com.paula.android.bechef.data.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;

public class BaseTab {
    @PrimaryKey(autoGenerate = true)
    private int uid = 0;

    @ColumnInfo(name = "tab_name")
    @NonNull
    private String mTabName;

    public BaseTab(@NonNull String tabName) {
        mTabName = tabName;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    @NonNull
    public String getTabName() {
        return mTabName;
    }

    public void setTabName(String tabName) {
        mTabName = tabName;
    }
}
