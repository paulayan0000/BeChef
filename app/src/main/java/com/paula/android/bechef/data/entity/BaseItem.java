package com.paula.android.bechef.data.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

public class BaseItem {
    @PrimaryKey
    @NonNull
    private int uid = -1;

    @ColumnInfo(name = "tab_uid")
    private int mTabUid = -1;

    @ColumnInfo(name = "title")
    private String mTitle;

    @ColumnInfo(name = "image_url")
    private String mImageUrl;

    @ColumnInfo(name = "rating")
    private double mRating = 0.0;

    @ColumnInfo(name = "tags")
    private String mTags; // Split by space*1

    @ColumnInfo(name = "in_today_id")
    private int mInTodayId = -1;

    @ColumnInfo(name = "item_index")
    private int mItemIndex = -1;

    @ColumnInfo(name = "description")
    private String mDescription;

    @ColumnInfo(name = "video_id")
    private String mVideoId;

    public BaseItem(int uid, int tabUid, String title, String imageUrl, double rating, String tags,
                    int inTodayId, int itemIndex, String description, String videoId) {
        this.uid = uid;
        mTabUid = tabUid;
        mTitle = title;
        mImageUrl = imageUrl;
        mRating = rating;
        mTags = tags;
        mInTodayId = inTodayId;
        mItemIndex = itemIndex;
        mDescription = description;
        mVideoId = videoId;
    }

    @Ignore
    public BaseItem() {
        mTitle = "";
        mImageUrl = "";
        mTags = "";
        mDescription = "";
        mVideoId = "";
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getTabUid() {
        return mTabUid;
    }

    public void setTabUid(int tabUid) {
        mTabUid = tabUid;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }

    public double getRating() {
        return mRating;
    }

    public void setRating(double rating) {
        mRating = rating;
    }

    public String getTags() {
        return mTags;
    }

    public void setTags(String tags) {
        mTags = tags;
    }

    public int getInTodayId() {
        return mInTodayId;
    }

    public void setInTodayId(int inTodayId) {
        mInTodayId = inTodayId;
    }

    public int getItemIndex() {
        return mItemIndex;
    }

    public void setItemIndex(int itemIndex) {
        mItemIndex = itemIndex;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getVideoId() {
        return mVideoId;
    }

    public void setVideoId(String videoId) {
        mVideoId = videoId;
    }
}
