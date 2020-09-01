package com.paula.android.bechef.data.entity;

import androidx.room.Entity;

@Entity(tableName = "bookmark_item")
public class BookmarkItem extends BaseItem {
    public BookmarkItem(int tabUid, String title, String imageUrl, double rating,
                        String tags, int inTodayId, String createdTime, String description, String videoId) {
        super(tabUid, title, imageUrl, rating, tags, inTodayId, createdTime, description, videoId);
    }
}
