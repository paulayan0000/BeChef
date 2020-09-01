package com.paula.android.bechef.discover;

import com.paula.android.bechef.baseMain.BaseMainFragment;
import com.paula.android.bechef.data.LoadDataCallback;
import com.paula.android.bechef.data.LoadDataTask;
import com.paula.android.bechef.data.dao.DiscoverTabDao;
import com.paula.android.bechef.data.database.TabDatabase;
import com.paula.android.bechef.data.entity.BaseTab;
import com.paula.android.bechef.data.entity.DiscoverTab;

import java.util.ArrayList;

import static com.google.android.gms.common.internal.Preconditions.checkNotNull;

public class DiscoverPresenter implements DiscoverContract.Presenter {
    private final DiscoverContract.View mDiscoverView;
//    private ArrayList<String> mTabTitles = new ArrayList<>();
//    private ArrayList<BaseTab> mBaseTabs = new ArrayList<>();

    public DiscoverPresenter(DiscoverContract.View discoverView) {
        mDiscoverView = checkNotNull(discoverView, "discoverView cannot be null!");
        mDiscoverView.setPresenter(this);
    }

    @Override
    public void start() {
        loadDiscoverTabs();
    }

    private void loadDiscoverTabs() {
//        mTabTitles.clear();
//        mBaseTabs.clear();
        new LoadDataTask<>(new LoadDataCallback<DiscoverTabDao>() {
//            private ArrayList<String> mGotTabTitles;
//            private ArrayList<String> mGotChannelIds;
            private ArrayList<DiscoverTab> mGotBaseTabs;

            @Override
            public DiscoverTabDao getDao() {
                return TabDatabase.getDiscoverInstance(mDiscoverView.getContext()).discoverDao();
            }

            @Override
            public void doInBackground(DiscoverTabDao discoverTabDao) {
//                mGotTabTitles = new ArrayList<>(discoverTabDao.getAllTabTitles());
//                mGotChannelIds = new ArrayList<>(discoverTabDao.getAllChannelIds());
                mGotBaseTabs = new ArrayList<>(discoverTabDao.getAll());
            }

            @Override
            public void onCompleted() {
//                mTabTitles.addAll(mGotTabTitles);
//                ((BaseMainFragment) mDiscoverView).showDefaultUi(mTabTitles, mGotChannelIds);
//                mBaseTabs.addAll(mGotTabs);
                ((BaseMainFragment) mDiscoverView).showDefaultUi(mGotBaseTabs);
            }
        }).execute();
    }
}
