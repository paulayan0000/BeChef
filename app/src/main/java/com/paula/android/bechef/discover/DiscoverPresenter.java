package com.paula.android.bechef.discover;

import com.paula.android.bechef.BaseContract;
import com.paula.android.bechef.data.database.TabDatabase;
import com.paula.android.bechef.data.entity.DiscoverTab;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.Observer;

import static com.google.android.gms.common.internal.Preconditions.checkNotNull;

public class DiscoverPresenter implements BaseContract.BasePresenter {
    private final DiscoverFragment mDiscoverView;

    public DiscoverPresenter(DiscoverFragment discoverView) {
        mDiscoverView = checkNotNull(discoverView, "discoverView cannot be null!");
        mDiscoverView.setPresenter(this);
    }

    @Override
    public void start() {
        loadDiscoverTabs();
    }

    private void loadDiscoverTabs() {
        TabDatabase.getDiscoverInstance(mDiscoverView.getContext()).discoverDao().getAllLive()
                .observe(mDiscoverView, new Observer<List<DiscoverTab>>() {
                    @Override
                    public void onChanged(List<DiscoverTab> discoverTabs) {
                        mDiscoverView.showDefaultUi(new ArrayList<>(discoverTabs));
                    }
                });
    }
}
