package com.paula.android.bechef.discoverChild;

import android.content.Context;
import android.util.Log;

import com.paula.android.bechef.LoadDiscoverDataCallback;
import com.paula.android.bechef.LoadSpecificDataTask;
import com.paula.android.bechef.api.GetChannelIdTask;
import com.paula.android.bechef.api.beans.GetSearchList;
import com.paula.android.bechef.api.callbacks.GetChannelIdCallback;
import com.paula.android.bechef.objects.AppDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import static com.google.android.gms.common.internal.Preconditions.checkNotNull;

public class DiscoverChildPresenter implements DiscoverChildFragmentContract.Presenter {
    private static final String LOG_TAG = DiscoverChildPresenter.class.getSimpleName();

    private DiscoverChildFragmentContract.View mDiscoverChildFragmentView;
    private int mlastVisibleItemPosition;
    private boolean mLoading = false;
    private String mNextPagingId = "";
    private String mChannelId = "";
    private Context mContext;
    private int mTabIndex;

    DiscoverChildPresenter(DiscoverChildFragmentContract.View discoverChildFragmentView, int index) {
        mDiscoverChildFragmentView = checkNotNull(discoverChildFragmentView, "mainFragmentView cannot be null!");
        discoverChildFragmentView.setPresenter(this);
        mTabIndex = index;
        mContext = ((DiscoverChildFragment) mDiscoverChildFragmentView).getContext();
    }

    @Override
    public void start() {
            AppDatabase db = Room.databaseBuilder(mContext,
                    AppDatabase.class, "database-name").build();

            new LoadSpecificDataTask(db, new LoadDiscoverDataCallback() {
                @Override
                public void onCompleted(List<String> channelId) {
                    mChannelId = channelId.get(0);
                    Map<String, String> queryParameters = new HashMap<>();

//        queryParameters.put("q", "wecook123");
//        queryParameters.put("type", "channel");

                    queryParameters.put("pageToken", "");
                    queryParameters.put("channelId", mChannelId);
                    loadDiscoverItems(queryParameters);
                }

                @Override
                public List<String> doInBackground(AppDatabase database) {
                    List<String> results = new ArrayList<>();
                    results.add(database.userDao().getChannelAt(mTabIndex));
                    return results;
                }
            }).execute();
        }

    @Override
    public void onScrollStateChanged(int visibleItemCount, int totalItemCount, int newState) {
        if (newState == RecyclerView.SCROLL_STATE_IDLE && visibleItemCount > 0 && !"".equals(mChannelId)) {
            if (!mLoading && mlastVisibleItemPosition == totalItemCount - 1 && !"".equals(mNextPagingId)) {

                    Map<String, String> queryParameters = new HashMap<>();
                queryParameters.put("pageToken", mNextPagingId);
                queryParameters.put("channelId", mChannelId);

                loadDiscoverItems(queryParameters);
            }
        }
    }

    private void loadDiscoverItems(Map<String, String> queryParameters) {
        if (!mLoading) {
            mLoading = true;
            Log.d(LOG_TAG, "Loading...");
            queryParameters.put("part", "snippet");
            queryParameters.put("maxResults", "10");
            queryParameters.put("order", "date");

            new GetChannelIdTask(queryParameters, new GetChannelIdCallback() {
                @Override
                public void onCompleted(GetSearchList bean) {
                    mDiscoverChildFragmentView.updateSearchItems(bean);
                    mNextPagingId = bean.getNextPageToken();
                    Log.d(LOG_TAG, "size: " + bean.getSearchItems().size());
                    mLoading = false;
                }

                @Override
                public void onError(String errorMessage) {
                    mLoading = false;
                    Log.d(LOG_TAG, "Error: " + errorMessage);
                }
            }).execute();
        }
    }

    @Override
    public void onScrolled(RecyclerView.LayoutManager layoutManager) {
        mlastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
    }
}
