package com.paula.android.bechef.discoverChild;

import android.content.Context;
import android.os.AsyncTask;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.paula.android.bechef.R;
import com.paula.android.bechef.api.BeChefApiHelper;
import com.paula.android.bechef.api.GetYouTubeDataTask;
import com.paula.android.bechef.api.beans.YouTubeData;
import com.paula.android.bechef.api.callbacks.GetYouTubeDataCallback;
import com.paula.android.bechef.api.exceptions.NoResourceException;
import com.paula.android.bechef.ChildContract;
import com.paula.android.bechef.utils.Constants;

import java.util.HashMap;
import java.util.Map;

import static com.google.android.gms.common.internal.Preconditions.checkNotNull;

public class DiscoverChildPresenter implements ChildContract.DiscoverChildPresenter {
    private final ChildContract.DiscoverChildView mDiscoverChildFragmentView;
    private final String mChannelId;
    private int mLastVisibleItemPosition;
    private boolean mLoading = false;
    private String mNextPagingId = "";
    private GetYouTubeDataTask mGetYouTubeDataTask;

    DiscoverChildPresenter(ChildContract.DiscoverChildView discoverChildFragmentView, String channelId) {
        mDiscoverChildFragmentView = checkNotNull(discoverChildFragmentView, "discoverChildView cannot be null!");
        discoverChildFragmentView.setCustomMainPresenter(this);
        mChannelId = channelId;
    }

    @Override
    public void start() {
        if (mChannelId != null) loadDiscoverItems();
    }

    @Override
    public Context getContext() {
        return mDiscoverChildFragmentView.getContext();
    }

    @Override
    public void onScrollStateChanged(int visibleItemCount, int totalItemCount, int newState) {
        if (newState == RecyclerView.SCROLL_STATE_IDLE && visibleItemCount > 0 && !"".equals(mChannelId)) {
            if (mLastVisibleItemPosition == totalItemCount - 1 && !mNextPagingId.isEmpty()) {
                loadDiscoverItems();
            }
        }
    }

    @Override
    public void cancelTask() {
        mNextPagingId = "";
        if (mGetYouTubeDataTask != null && !mGetYouTubeDataTask.isCancelled() &&
                mGetYouTubeDataTask.getStatus() == AsyncTask.Status.RUNNING) {
            mGetYouTubeDataTask.cancel(true);
            mLoading = false;
        }
    }

    private void showLoading() {
        mLoading = true;
        mDiscoverChildFragmentView.showLoadingUi();
    }

    private void loadDiscoverItems() {
        if (!mLoading) {
            showLoading();
            final Map<String, String> queryParameters = new HashMap<>();
            queryParameters.put("pageToken", mNextPagingId);
            queryParameters.put("channelId", mChannelId);
            queryParameters.put("part", Constants.API_PART_SNIPPET);
            queryParameters.put("maxResults", Constants.API_MAX_RESULTS);
            queryParameters.put("order", Constants.API_DEFAULT_ORDER);

            mGetYouTubeDataTask = new GetYouTubeDataTask(queryParameters, new GetYouTubeDataCallback() {
                Exception error;

                @Override
                public YouTubeData doInBackground(Map<String, String> queryParameters) {
                    YouTubeData bean = null;
                    try {
                        bean = BeChefApiHelper.GetYoutubeData(queryParameters, Constants.API_SEARCH);
                    } catch (Exception e) {
                        error = e;
                    }
                    return bean;
                }

                @Override
                public void onCompleted(YouTubeData bean) {
                    if (bean != null && error == null) {
                        mDiscoverChildFragmentView.updateSearchItems(bean);
                        mNextPagingId = bean.getNextPageToken();
                        mLoading = false;
                    } else {
                        onError(error);
                    }
                }

                @Override
                public void onError(Exception error) {
                    mDiscoverChildFragmentView.updateSearchItems(setErrorMsg(error));
                    mLoading = false;
                }
            });
            if (!mGetYouTubeDataTask.isCancelled()) {
                mGetYouTubeDataTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        }
    }

    private YouTubeData setErrorMsg(Exception error) {
        YouTubeData bean = new YouTubeData();
        if (error instanceof NoResourceException) {
            bean.setErrorMsg(getContext().getResources().getString(R.string.adapter_nothing));
        } else {
            bean.setErrorMsg(getContext().getResources().getString(R.string.adapter_error));
        }
        return bean;
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
