package com.paula.android.bechef.discover;

import com.paula.android.bechef.BaseContract;
import com.paula.android.bechef.BaseMainFragment;
import com.paula.android.bechef.data.LoadDataCallback;
import com.paula.android.bechef.data.LoadDataTask;
import com.paula.android.bechef.data.dao.DiscoverTabDao;
import com.paula.android.bechef.data.database.TabDatabase;
import com.paula.android.bechef.data.entity.DiscoverTab;

import java.util.ArrayList;

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
        new LoadDataTask<>(new LoadDataCallback<DiscoverTabDao>() {
            private ArrayList<DiscoverTab> mGotBaseTabs;

            @Override
            public DiscoverTabDao getDao() {
                return TabDatabase.getDiscoverInstance(mDiscoverView.getContext()).discoverDao();
            }

            @Override
            public void doInBackground(DiscoverTabDao discoverTabDao) {
                mGotBaseTabs = new ArrayList<>(discoverTabDao.getAll());
            }

            @Override
            public void onCompleted() {
                mDiscoverView.showDefaultUi(mGotBaseTabs);
            }
        }).execute();
    }
}
