package com.paula.android.bechef.data;

public class SearchItem {
    private String mId;
    private String mPublishedAt;
    private String mTitle;
    private String mDescription;
    private String mThumbnailMediumUrl;
    private String mTags;  // Split by space*1
    private String mDuration;
    private String mViewCount;

    public SearchItem() {
        mId = "";
        mPublishedAt = "";
        mTitle = "";
        mDescription = "";
        mThumbnailMediumUrl = "";
        mTags = "";
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

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getThumbnailMediumUrl() {
        return mThumbnailMediumUrl;
    }

    public void setThumbnailMediumUrl(String thumbnailMediumUrl) {
        mThumbnailMediumUrl = thumbnailMediumUrl;
    }

    public String getTags() {
        return mTags;
    }

    public void setTags(String tags) {
        mTags = tags;
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
