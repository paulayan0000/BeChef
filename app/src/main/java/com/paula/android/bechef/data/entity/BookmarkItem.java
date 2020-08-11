package com.paula.android.bechef.data.entity;

import com.paula.android.bechef.data.SearchItem;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(tableName = "bookmark_item")
public class BookmarkItem extends BaseItem {
    @ColumnInfo(name = "video_id")
    private String mVideoId;

    @ColumnInfo(name = "description")
    private String mDescription;

    @ColumnInfo(name = "published_time")
    private String mPublishedTime;

    public BookmarkItem(int uid, String videoId, String description, String publishedTime, int tabUid, String title,
                        String imageUrl, double rating, String tags, int inTodayId, int itemIndex) {
        super(uid, tabUid, title, imageUrl, rating, tags, inTodayId, itemIndex);
        mVideoId = videoId;
        mDescription = description;
        mPublishedTime = publishedTime;
    }

    public BookmarkItem(SearchItem searchItem) {
        super(searchItem);
        mVideoId = searchItem.getId();
        mDescription = searchItem.getDescription();
        mPublishedTime = searchItem.getPublishedAt();
    }

    public String getVideoId() {
        return mVideoId;
    }

    public void setVideoId(String videoId) {
        mVideoId = videoId;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getPublishedTime() {
        return mPublishedTime;
    }

    public void setPublishedTime(String publishedTime) {
        mPublishedTime = publishedTime;
    }
}
