package com.paula.android.bechef.data.entity;

import androidx.room.Entity;

@Entity(tableName = "bookmark_tab")
public class BookmarkTab extends BaseTab {
    public BookmarkTab(String tabName) {
        super(tabName);
    }

    public BookmarkTab(BookmarkTab bookmarkTab) {
        super(bookmarkTab);
    }
}
