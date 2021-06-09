package com.paula.android.bechef.discover;

import android.app.Activity;

import androidx.lifecycle.Observer;

import com.paula.android.bechef.BaseContract;
import com.paula.android.bechef.data.database.TabDatabase;
import com.paula.android.bechef.data.entity.BaseTab;
import com.paula.android.bechef.data.entity.DiscoverTab;

import java.util.ArrayList;
import java.util.List;

import static com.google.android.gms.common.internal.Preconditions.checkNotNull;

public class DiscoverPresenter implements BaseContract.MainPresenter {
    private final DiscoverFragment mDiscoverView;
    private final ArrayList<BaseTab> mDiscoverTabs = new ArrayList<>();

    public DiscoverPresenter(DiscoverFragment discoverView) {
        mDiscoverView = checkNotNull(discoverView, "discoverView cannot be null!");
        mDiscoverView.setCustomMainPresenter(this);
    }

    @Override
    public void start() {
        loadDiscoverTabs();
    }

    @Override
    public Activity getActivity() {
        return mDiscoverView.getActivity();
    }

    private void loadDiscoverTabs() {
        TabDatabase.getTabInstance().discoverDao().getAllLive()
                .observe(mDiscoverView, new Observer<List<DiscoverTab>>() {
                    @Override
                    public void onChanged(List<DiscoverTab> discoverTabs) {
                        mDiscoverTabs.clear();
                        mDiscoverTabs.addAll(discoverTabs);
                        mDiscoverView.showDefaultUi(mDiscoverTabs);
                    }
                });
    }

    @Override
    public ArrayList<BaseTab> getTabs() {
        return mDiscoverTabs;
    }
}