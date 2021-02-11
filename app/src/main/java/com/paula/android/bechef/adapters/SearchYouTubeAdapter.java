package com.paula.android.bechef.adapters;

import android.content.Context;
import android.view.ViewGroup;

import com.paula.android.bechef.api.beans.GetSearchList;
import com.paula.android.bechef.data.entity.DiscoverItem;
import com.paula.android.bechef.search.SearchPresenter;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SearchYouTubeAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private SearchPresenter mSearchPresenter;
    private ArrayList<DiscoverItem> mDiscoverItems;
    private String mNextPageToken;
    private boolean mIsLoading;

    public SearchYouTubeAdapter(GetSearchList bean, SearchPresenter presenter) {
        mDiscoverItems = bean.getDiscoverItems();
        mNextPageToken = bean.getNextPageToken();
        mSearchPresenter = presenter;
        mIsLoading = false;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
