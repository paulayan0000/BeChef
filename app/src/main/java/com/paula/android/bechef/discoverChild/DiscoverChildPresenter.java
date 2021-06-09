package com.paula.android.bechef.discoverChild;

import android.app.Activity;
import android.os.AsyncTask;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.paula.android.bechef.BeChef;
import com.paula.android.bechef.R;
import com.paula.android.bechef.api.BeChefApiHelper;
import com.paula.android.bechef.thread.GetDataAsyncTask;
import com.paula.android.bechef.api.beans.YouTubeData;
import com.paula.android.bechef.thread.GetDataTaskCallback;
import com.paula.android.bechef.api.exceptions.NoResourceException;
import com.paula.android.bechef.ChildContract;
import com.paula.android.bechef.utils.Constants;

import java.util.HashMap;
import java.util.Map;

import static com.google.android.gms.common.internal.Preconditions.checkNotNull;

public class DiscoverChildPresenter implements ChildContract.DiscoverChildPresenter {
    private final ChildContract.DiscoverChildView mDiscoverChildView;
    private final String mChannelId;
    private int mLastVisibleItemPosition;
    private boolean mLoading = false;
    private String mNextPagingId = "";
    private GetDataAsyncTask<YouTubeData> mGetDataAsyncTask;

    DiscoverChildPresenter(ChildContract.DiscoverChildView discoverChildView, String channelId) {
        mDiscoverChildView = checkNotNull(discoverChildView, "discoverChildView cannot be null!");
        discoverChildView.setCustomMainPresenter(this);
        mChannelId = channelId;
    }

    @Override
    public void start() {
        if (mChannelId != null) loadDiscoverItems();
    }

    @Override
    public Activity getActivity() {
        return ((DiscoverChildFragment) mDiscoverChildView).getActivity();
    }

    @Override
    public void onScrollStateChanged(int visibleItemCount, int totalItemCount, int newState) {
        if (newState == RecyclerView.SCROLL_STATE_IDLE
                && visibleItemCount > 0
                && !"".equals(mChannelId)) {
            if (mLastVisibleItemPosition == totalItemCount - 1 && !mNextPagingId.isEmpty()) {
                loadDiscoverItems();
            }
        }
    }

    @Override
    public void cancelTask() {
        mNextPagingId = "";
        if (mGetDataAsyncTask != null && !mGetDataAsyncTask.isCancelled() &&
                mGetDataAsyncTask.getStatus() == AsyncTask.Status.RUNNING) {
            mGetDataAsyncTask.cancel(true);
            mLoading = false;
        }
    }

    private void showLoading() {
        mLoading = true;
        mDiscoverChildView.showLoadingUi();
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
            mGetDataAsyncTask = new GetDataAsyncTask<>(new GetDataTaskCallback<YouTubeData>() {
                Exception error;

                @Override
                public YouTubeData doInBackground() {
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
                    if (mGetDataAsyncTask.isCancelled()) {
                        mLoading = false;
                        return;
                    }
                    if (bean != null && error == null) {
                        mDiscoverChildView.updateSearchItems(bean);
                        mNextPagingId = bean.getNextPageToken();
                        mLoading = false;
                    } else {
                        onError(error);
                    }
                }

                @Override
                public void onError(Exception error) {
                    mDiscoverChildView.updateSearchItems(setErrorMsg(error));
                    mLoading = false;
                }
            });
            if (!mGetDataAsyncTask.isCancelled()) {
                mGetDataAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        }
    }

    private YouTubeData setErrorMsg(Exception error) {
        YouTubeData bean = new YouTubeData();
        if (error instanceof NoResourceException) {
            bean.setErrorMsg(BeChef.getAppContext().getResources().getString(R.string.adapter_nothing));
        } else {
            bean.setErrorMsg(BeChef.getAppContext().getResources().getString(R.string.adapter_error));
        }
        return bean;
    }

    @Override
    public void onScrolled(RecyclerView.LayoutManager layoutManager) {
        mLastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
    }

    @Override
    public void openDetail(Object content, boolean isBottomShown) {
        mDiscoverChildView.showDetailUi(content, isBottomShown);
    }
}