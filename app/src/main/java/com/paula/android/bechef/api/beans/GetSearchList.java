package com.paula.android.bechef.api.beans;

import com.paula.android.bechef.objects.SearchItem;
import java.util.ArrayList;

public class GetSearchList {
    private int mViewType;
    private String mTableName;
    private String mNextPageToken;
    private String mPrevPageToken;
    private ArrayList<SearchItem> mSearchItems;

    public GetSearchList() {
        mViewType = -1;
        mTableName = "";
        mNextPageToken = "";
        mPrevPageToken = "";
        mSearchItems = new ArrayList<>();
    }

    public int getViewType() {
        return mViewType;
    }

    public void setViewType(int viewType) {
        mViewType = viewType;
    }

    public String getTableName() {
        return mTableName;
    }

    public void setTableName(String tableName) {
        mTableName = tableName;
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
