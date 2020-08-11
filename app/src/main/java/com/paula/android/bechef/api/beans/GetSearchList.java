package com.paula.android.bechef.api.beans;

import com.paula.android.bechef.data.SearchItem;
import java.util.ArrayList;

public class GetSearchList {
    private String mNextPageToken;
    private String mPrevPageToken;
    private ArrayList<SearchItem> mSearchItems;

    public GetSearchList() {
        mNextPageToken = "";
        mPrevPageToken = "";
        mSearchItems = new ArrayList<>();
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

    public ArrayList<SearchItem> getSearchItems() {
        return mSearchItems;
    }

    public void setSearchItems(ArrayList<SearchItem> searchItems) {
        mSearchItems = searchItems;
    }
}
