package com.paula.android.bechef.data.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class BaseItem {
    @Ignore
    private Boolean mIsSelected = false;

    @PrimaryKey(autoGenerate = true)
    private long uid = 0;

    @ColumnInfo(name = "tab_uid")
    private long mTabUid = 0;

    @NonNull
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

    @ColumnInfo(name = "created_time")
    private String mCreatedTime;

    @ColumnInfo(name = "description")
    private String mDescription;

    @ColumnInfo(name = "video_id")
    private String mVideoId;

    public BaseItem(long tabUid, @NonNull String title, String imageUrl, double rating, String tags,
                    int inTodayId, String createdTime, String description, String videoId) {
        mTabUid = tabUid;
        mTitle = title;
        mImageUrl = imageUrl;
        mRating = rating;
        mTags = tags;
        mInTodayId = inTodayId;
        mCreatedTime = createdTime;
        mDescription = description;
        mVideoId = videoId;
    }

    @Ignore
    public BaseItem() {
        mTitle = "";
        mImageUrl = "";
        mTags = "";
        mCreatedTime = "";
        mDescription = "";
        mVideoId = "";
    }

    @Ignore
    public BaseItem(BaseItem baseItem) {
        uid = baseItem.getUid();
        mTabUid = baseItem.getTabUid();
        mTitle = baseItem.getTitle();
        mImageUrl = baseItem.getImageUrl();
        mRating = baseItem.getRating();
        mTags = baseItem.getTags();
        mInTodayId = baseItem.getInTodayId();
        mCreatedTime = baseItem.getCreatedTime();
        mDescription = baseItem.getDescription();
        mVideoId = baseItem.getVideoId();
    }

    public void setSelected(Boolean selected) {
        mIsSelected = selected;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public long getTabUid() {
        return mTabUid;
    }

    public void setTabUid(long tabUid) {
        mTabUid = tabUid;
    }

    @NonNull
    public String getTitle() {
        return mTitle;
    }

    public void setTitle(@NonNull String title) {
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

    public String getCreatedTime() {
        return mCreatedTime;
    }

    public void setCreatedTime(String createdTime) {
        mCreatedTime = createdTime;
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
