package com.paula.android.bechef.data.entity;

public class DiscoverItem extends BaseItem {
    private String mId;
    private String mPublishedAt;
    private String mDuration;
    private String mViewCount;

    public DiscoverItem() {
        super();
        mId = "";
        mPublishedAt = "";
        mDuration = "";
        mViewCount = "";
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getPublishedAt() {
        return mPublishedAt;
    }

    public void setPublishedAt(String publishdAt) {
        mPublishedAt = publishdAt;
    }

    public String getDuration() {
        return mDuration;
    }

    public void setDuration(String duration) {
        mDuration = duration;
    }

    public String getViewCount() {
        return mViewCount;
    }

    public void setViewCount(String viewCount) {
        mViewCount = viewCount;
    }
}
