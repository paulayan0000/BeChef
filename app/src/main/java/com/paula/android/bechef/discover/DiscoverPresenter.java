package com.paula.android.bechef.discover;

import com.paula.android.bechef.data.LoadDataTask;
import com.paula.android.bechef.data.LoadDataCallback;
import com.paula.android.bechef.data.dao.DiscoverTabDao;
import com.paula.android.bechef.data.database.DiscoverTabDatabase;
import java.util.ArrayList;
import androidx.room.RoomDatabase;

import static com.google.android.gms.common.internal.Preconditions.checkNotNull;

public class DiscoverPresenter implements DiscoverContract.Presenter{
    private final DiscoverContract.View mDiscoverView;
    private ArrayList<String> mTabTitles = new ArrayList<>();
    private LoadDataTask mLoadDataTask;

    public DiscoverPresenter(DiscoverContract.View discoverView) {
        mDiscoverView = checkNotNull(discoverView, "discoverView cannot be null!");
        mDiscoverView.setPresenter(this);
    }

    @Override
    public void start() {
        mTabTitles.clear();
        DiscoverTabDatabase db = DiscoverTabDatabase.getInstance(mDiscoverView.getContext());
        mLoadDataTask = new LoadDataTask(db, new LoadDataCallback() {
            private ArrayList<String> mGotTabTitles;
            private  ArrayList<String> mGotChannelIds;

            @Override
            public void doInBackground(RoomDatabase database) {
                DiscoverTabDao discoverTabDao = ((DiscoverTabDatabase) database).discoverDao();
                mGotTabTitles = new ArrayList<>(discoverTabDao.getAllTabTitles());
                mGotChannelIds = new ArrayList<>(discoverTabDao.getAllChannelIds());
            }

            @Override
            public void onCompleted() {
                mTabTitles.addAll(mGotTabTitles);
                mDiscoverView.showDiscoverUi(mTabTitles, mGotChannelIds);
            }
        });
        mLoadDataTask.execute();
    }
}
