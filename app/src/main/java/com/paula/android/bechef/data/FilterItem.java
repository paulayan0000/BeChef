package com.paula.android.bechef.data;

import com.paula.android.bechef.data.entity.BaseTab;

import java.util.ArrayList;

public class FilterItem {
    private String mFilterType = "";
    private ArrayList<BaseTab> mFilterContents = new ArrayList<>();

    public String getFilterType() {
        return mFilterType;
    }

    public void setFilterType(String filterType) {
        mFilterType = filterType;
    }

    public ArrayList<BaseTab> getFilterContents() {
        return mFilterContents;
    }

    public void setFilterContents(ArrayList<BaseTab> filterContents) {
        mFilterContents = filterContents;
    }
}
