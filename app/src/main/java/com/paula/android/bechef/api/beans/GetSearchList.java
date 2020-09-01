package com.paula.android.bechef.api.beans;

import com.paula.android.bechef.data.entity.DiscoverItem;

import java.util.ArrayList;

public class GetSearchList {
    private String mNextPageToken;
    private String mPrevPageToken;
    private ArrayList<DiscoverItem> mDiscoverItems;

    public GetSearchList() {
        mNextPageToken = "";
        mPrevPageToken = "";
        mDiscoverItems = new ArrayList<>();
    }

    public String getNextPageToken() {
        return mNextPageToken;
    }

    public void setNextPageToken(String nextPageToken) {
        mNextPageToken = nextPageToken;
    }

    public String getPrevPageToken() {
        return mPrevPageToken;
    }

    public void setPrevPageToken(String prevPageToken) {
        mPrevPageToken = prevPageToken;
    }

    public ArrayList<DiscoverItem> getDiscoverItems() {
        return mDiscoverItems;
    }

    public void setDiscoverItems(ArrayList<DiscoverItem> discoverItems) {
        mDiscoverItems = discoverItems;
    }
}
