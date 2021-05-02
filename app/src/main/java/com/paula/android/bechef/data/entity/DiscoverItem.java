package com.paula.android.bechef.data.entity;

public class DiscoverItem extends BaseItem {
    private String mPublishedAt;
    private String mDuration;
    private String mViewCount;
    private String mSubscribeCount;
    private String mChannelId;
    private boolean mIsChannelInBeChef;

    public DiscoverItem() {
        super();
        mPublishedAt = "";
        mDuration = "";
        mViewCount = "";
        mSubscribeCount = "";
        mChannelId = "";
        mIsChannelInBeChef = true;
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

    public String getSubscribeCount() {
        return mSubscribeCount;
    }

    public void setSubscribeCount(String subscribeCount) {
        mSubscribeCount = subscribeCount;
    }

    public String getChannelId() {
        return mChannelId;
    }

    public void setChannelId(String channelId) {
        mChannelId = channelId;
    }

    public boolean isChannelInBeChef() {
        return mIsChannelInBeChef;
    }

    public void setChannelInBeChef(boolean channelInBeChef) {
        mIsChannelInBeChef = channelInBeChef;
    }
}
