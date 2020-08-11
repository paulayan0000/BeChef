package com.paula.android.bechef.data.entity;

import com.paula.android.bechef.data.SearchItem;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;

public class BaseItem {
    @PrimaryKey
    @NonNull
    private int uid;

    @ColumnInfo(name = "tab_uid")
    private int mTabUid = -1;

    @ColumnInfo(name = "title")
    private String mTitle;

    @ColumnInfo(name = "image_url")
    private String mImageUrl;

    @ColumnInfo(name = "rating")
    private double mRating = -1;

    @ColumnInfo(name = "tags")
    private String mTags; // Split by space*1

    @ColumnInfo(name = "in_today_id")
    private int mInTodayId = -1;

    @ColumnInfo(name = "item_index")
    private int mItemIndex = -1;

    public BaseItem(int uid, int tabUid, String title, String imageUrl, double rating, String tags,
                    int inTodayId, int itemIndex) {
        this.uid = uid;
        mTabUid = tabUid;
        mTitle = title;
        mImageUrl = imageUrl;
        mRating = rating;
        mTags = tags;
        mInTodayId = inTodayId;
        mItemIndex = itemIndex;
    }

    public BaseItem(SearchItem searchItem) {
        mTitle = searchItem.getTitle();
        mImageUrl = searchItem.getThumbnailMediumUrl();
        mTags = searchItem.getTags();
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
}
