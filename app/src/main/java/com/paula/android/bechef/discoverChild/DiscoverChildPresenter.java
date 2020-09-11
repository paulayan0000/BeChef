package com.paula.android.bechef.discoverChild;

import android.util.Log;
import android.widget.Toast;

import com.paula.android.bechef.api.BeChefApiHelper;
import com.paula.android.bechef.api.GetYouTubeDataTask;
import com.paula.android.bechef.api.beans.GetSearchList;
import com.paula.android.bechef.api.callbacks.GetYouTubeDataCallback;
import com.paula.android.bechef.api.exceptions.NoResourceException;
import com.paula.android.bechef.data.entity.BaseTab;
import com.paula.android.bechef.data.entity.DiscoverTab;

import java.util.HashMap;
import java.util.Map;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.google.android.gms.common.internal.Preconditions.checkNotNull;

public class DiscoverChildPresenter implements DiscoverChildFragmentContract.Presenter {

    private static final String LOG_TAG = DiscoverChildPresenter.class.getSimpleName();

    private DiscoverChildFragmentContract.View mDiscoverChildFragmentView;
    private int mlastVisibleItemPosition;
    private boolean mLoading = false;
    private String mNextPagingId = "";
    private String mChannelId;

//    DiscoverChildPresenter(DiscoverChildFragmentContract.View discoverChildFragmentView, String channelId) {
//        mDiscoverChildFragmentView = checkNotNull(discoverChildFragmentView, "discoverChildView cannot be null!");
//        discoverChildFragmentView.setPresenter(this);
//        mChannelId = channelId;
//    }
    public DiscoverChildPresenter(DiscoverChildFragmentContract.View discoverChildFragmentView, DiscoverTab discoverTab) {
        mDiscoverChildFragmentView = checkNotNull(discoverChildFragmentView, "discoverChildView cannot be null!");
        discoverChildFragmentView.setPresenter(this);
        mChannelId = discoverTab.getChannelId();
    }

    @Override
    public void start() {
        if (mChannelId != null) {
            Map<String, String> queryParameters = new HashMap<>();

            //        queryParameters.put("q", "wecook123");
            //        queryParameters.put("type", "channel");

            queryParameters.put("pageToken", "");
            queryParameters.put("channelId", mChannelId);

            loadDiscoverItems(queryParameters);
        }
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

            new GetYouTubeDataTask(queryParameters, new GetYouTubeDataCallback() {
                Exception error;

                @Override
                public GetSearchList doInBackground(Map<String, String> queryParameters) {
                    GetSearchList bean = null;
                    try {
                        bean = BeChefApiHelper.SearchYoutubeData(queryParameters);
                    } catch (Exception e) {
                        error = e;
                    }
                    return bean;
                }

                @Override
                public void onCompleted(GetSearchList bean) {
                    if (bean != null && error == null) {
                        mDiscoverChildFragmentView.updateSearchItems(bean);
                        mNextPagingId = bean.getNextPageToken();
                        mLoading = false;
                    } else {
                        onError(error);
                    }
                }

                @Override
                public void onError(Exception e) {
                    Log.d(LOG_TAG, "Error: " + e.getMessage());
                    if (e instanceof NoResourceException)
                        Toast.makeText(mDiscoverChildFragmentView.getContext(),
                                "此資源不存在！", Toast.LENGTH_LONG).show();
                    mLoading = false;
                }
            }).execute();
        }
    }

    @Override
    public void onScrolled(RecyclerView.LayoutManager layoutManager) {
        mlastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
    }

    @Override
    public void openDetail(Object content, boolean isBottomShown) {
        mDiscoverChildFragmentView.showDetailUi(content, isBottomShown);
    }
}
