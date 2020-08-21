package com.paula.android.bechef.data.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(tableName = "bookmark_item")
public class BookmarkItem extends BaseItem {

    @ColumnInfo(name = "published_time")
    private String mPublishedTime;

    public BookmarkItem(int uid, String publishedTime, int tabUid, String title, String imageUrl, double rating,
                        String tags, int inTodayId, int itemIndex, String description, String videoId) {
        super(uid, tabUid, title, imageUrl, rating, tags, inTodayId, itemIndex, description, videoId);
        mPublishedTime = publishedTime;
    }

    public String getPublishedTime() {
        return mPublishedTime;
    }

    public void setPublishedTime(String publishedTime) {
        mPublishedTime = publishedTime;
    }
}
