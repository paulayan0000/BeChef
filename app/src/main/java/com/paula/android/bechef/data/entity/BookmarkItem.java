package com.paula.android.bechef.data.entity;

import androidx.room.Entity;
import androidx.room.Ignore;

@Entity(tableName = "bookmark_item")
public class BookmarkItem extends BaseItem {
    public BookmarkItem(long tabUid, String title, String imageUrl, double rating,
                        String tags, int inTodayId, String createdTime, String description, String videoId) {
        super(tabUid, title, imageUrl, rating, tags, inTodayId, createdTime, description, videoId);
    }

    @Ignore
    public BookmarkItem(DiscoverItem discoverItem) {
        super(discoverItem);
    }

    @Ignore
    public BookmarkItem(BaseItem baseItem) {
        super(baseItem);
    }

    @Ignore
    public BookmarkItem() {
        super();
    }
}
