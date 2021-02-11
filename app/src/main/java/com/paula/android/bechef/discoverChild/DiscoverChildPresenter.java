package com.paula.android.bechef.discoverChild;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.paula.android.bechef.api.BeChefApiHelper;
import com.paula.android.bechef.api.GetYouTubeDataTask;
import com.paula.android.bechef.api.beans.GetSearchList;
import com.paula.android.bechef.api.callbacks.GetYouTubeDataCallback;
import com.paula.android.bechef.api.exceptions.NoResourceException;
import com.paula.android.bechef.data.entity.DiscoverTab;

import java.util.HashMap;
import java.util.Map;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.google.android.gms.common.internal.Preconditions.checkNotNull;

public class DiscoverChildPresenter implements DiscoverChildFragmentContract.Presenter {

    private static final String LOG_TAG = DiscoverChildPresenter.class.getSimpleName();

    private DiscoverChildFragmentContract.View mDiscoverChildFragmentView;
    private int mLastVisibleItemPosition;
    private boolean mLoading = false;
    private String mNextPagingId = "";
    private String mChannelId;

    public DiscoverChildPresenter(DiscoverChildFragmentContract.View discoverChildFragmentView, DiscoverTab discoverTab) {
        mDiscoverChildFragmentView = checkNotNull(discoverChildFragmentView, "discoverChildView cannot be null!");
        discoverChildFragmentView.setPresenter(this);
        mChannelId = discoverTab.getChannelId();
    }

    @Override
    public void start() {
        if (mChannelId != null) {
            Map<String, String> queryParameters = new HashMap<>();
            queryParameters.put("pageToken", "");
            queryParameters.put("channelId", mChannelId);
            loadDiscoverItems(queryParameters);
        }
    }

    @Override
    public void onScrollStateChanged(int visibleItemCount, int totalItemCount, int newState) {
        if (newState == RecyclerView.SCROLL_STATE_IDLE && visibleItemCount > 0 && !"".equals(mChannelId)) {
            if (mLastVisibleItemPosition == totalItemCount - 1 && !"".equals(mNextPagingId)) {
                Map<String, String> queryParameters = new HashMap<>();
                queryParameters.put("pageToken", mNextPagingId);
                queryParameters.put("channelId", mChannelId);
                loadDiscoverItems(queryParameters);
            }
        }
    }

    private void setLoading(boolean isLoading) {
        mLoading = isLoading;
        mDiscoverChildFragmentView.setLoading(isLoading);
    }

    private void loadDiscoverItems(Map<String, String> queryParameters) {
        if (!mLoading) {
//            mLoading = true;
            setLoading(true);
            Log.d(LOG_TAG, "Loading..." + queryParameters.get("channelId"));
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
//                        mLoading = false;
                        setLoading(false);
                    } else {
                        onError(error);
                    }
                }

                @Override
                public void onError(Exception e) {
                    Log.d(LOG_TAG, "Error: " + e.getMessage());
                    GetSearchList bean = new GetSearchList();
                    if (e instanceof NoResourceException)
                        bean.setErrorMsg("此資源不存在！");
                    else
                        bean.setErrorMsg("發生錯誤\n請檢查網路連線！");
                    mDiscoverChildFragmentView.updateSearchItems(bean);
                    setLoading(false);
                }
            }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//            }).execute();
        }
    }

    @Override
    public void onScrolled(RecyclerView.LayoutManager layoutManager) {
        mLastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
    }

    @Override
    public void openDetail(Object content, boolean isBottomShown) {
        mDiscoverChildFragmentView.showDetailUi(content, isBottomShown);
    }
}
