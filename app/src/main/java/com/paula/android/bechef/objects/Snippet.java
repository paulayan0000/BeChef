package com.paula.android.bechef.objects;

public class Snippet {
    private String mPublishedAt;
    private String mTitle;
    private String mDescription;
    private String mThumbnailMediumUrl;

    public Snippet() {
        mPublishedAt = "";
        mTitle = "";
        mDescription = "";
        mThumbnailMediumUrl = "";
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
}