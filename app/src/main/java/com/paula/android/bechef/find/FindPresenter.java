package com.paula.android.bechef.find;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.paula.android.bechef.api.beans.YouTubeData;
import com.paula.android.bechef.api.BeChefApiHelper;
import com.paula.android.bechef.api.callbacks.GetYouTubeDataCallback;
import com.paula.android.bechef.api.exceptions.NoResourceException;
import com.paula.android.bechef.api.GetYouTubeDataTask;
import com.paula.android.bechef.data.dao.BookmarkItemDao;
import com.paula.android.bechef.data.dao.RecipeItemDao;
import com.paula.android.bechef.data.database.ItemDatabase;
import com.paula.android.bechef.data.entity.BaseItem;
import com.paula.android.bechef.data.entity.BaseTab;
import com.paula.android.bechef.data.entity.BookmarkTab;
import com.paula.android.bechef.R;
import com.paula.android.bechef.utils.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.google.android.gms.common.internal.Preconditions.checkNotNull;

public class FindPresenter implements FindContract.Presenter {
    private final FindContract.View mFindFragment;
    private ArrayList<BaseTab> mBaseTabs = null;
    private GetYouTubeDataTask mGetYouTubeDataTask;
    private String mNextPagingId = "";
    private boolean mLoading = false;
    private int mLastVisibleItemPosition;

    public FindPresenter(FindContract.View findFragment) {
        mFindFragment = checkNotNull(findFragment, "findView cannot be null!");
        mFindFragment.setCustomMainPresenter(this);
    }

    public void setBaseTabs(ArrayList<BaseTab> baseTabs) {
        mBaseTabs = new ArrayList<>(baseTabs);
    }

    @Override
    public boolean isFromDiscover() {
        return mBaseTabs == null;
    }

    @Override
    public void onScrollStateChanged(int visibleItemCount, int totalItemCount, int newState) {
        if (newState == RecyclerView.SCROLL_STATE_IDLE && visibleItemCount > 0) {
            if (mLastVisibleItemPosition == totalItemCount - 1 && !"".equals(mNextPagingId)) {
                Map<String, String> queryParameters = new HashMap<>();
                queryParameters.put("pageToken", mNextPagingId);
                loadYouTubeItems(queryParameters);
            }
        }
    }

    @Override
    public void onScrolled(RecyclerView.LayoutManager layoutManager) {
        mLastVisibleItemPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
    }

    @Override
    public void start() {
        mFindFragment.setFindConditions(mBaseTabs);
    }

    @Override
    public Context getContext() {
        return mFindFragment.getContext();
    }

    @Override
    public void openDetail(Object content, boolean isBottomShown) {
        mFindFragment.showDetailUi(content, isBottomShown);
    }

    @Override
    public void cancelTask() {
        if (mGetYouTubeDataTask != null && !mGetYouTubeDataTask.isCancelled() &&
                mGetYouTubeDataTask.getStatus() == AsyncTask.Status.RUNNING) {
            mGetYouTubeDataTask.cancel(true);
            mLoading = false;
        }
    }

    @Override
    public void loadResults() {
        ArrayList<BaseTab> chosenTabs = mFindFragment.getChosenTabs();
        if (chosenTabs.size() == 0) return;
        if (isFromDiscover()) {
            loadYouTubeItems(new HashMap<String, String>());
        } else {
            showLoading();
            FilterFromDataBase();
        }
    }

    private void FilterFromDataBase() {
        final long tabUid = mFindFragment.getChosenTabUid();
        final String filterRange = mFindFragment.getChosenFilterRange();
        final String keyword = mFindFragment.getCurrentKeyword();
        final boolean isFromBookmark = mBaseTabs.get(0) instanceof BookmarkTab;
        Runnable runnableOnNewThread;
        runnableOnNewThread = new Runnable() {
            @Override
            public void run() {
                final ArrayList<BaseItem> baseItems;
                if (isFromBookmark) {
                    BookmarkItemDao dao = ItemDatabase.getItemInstance(getContext()).bookmarkDao();
                    switch (filterRange) {
                        case Constants.VARIABLE_NAME_TITLE:
                            baseItems = new ArrayList<BaseItem>(dao.findRelatedTitles(keyword, tabUid));
                            break;
                        case Constants.VARIABLE_NAME_TAGS:
                            baseItems = new ArrayList<BaseItem>(dao.findRelatedTags(keyword, tabUid));
                            break;
                        default:
                            baseItems = new ArrayList<BaseItem>(dao.findRelatedDescriptions(keyword, tabUid));
                            break;
                    }
                } else {
                    RecipeItemDao dao = ItemDatabase.getItemInstance(getContext()).recipeDao();
                    switch (filterRange) {
                        case Constants.VARIABLE_NAME_TITLE:
                            baseItems = new ArrayList<BaseItem>(dao.findRelatedTitles(keyword, tabUid));
                            break;
                        case Constants.VARIABLE_NAME_TAGS:
                            baseItems = new ArrayList<BaseItem>(dao.findRelatedTags(keyword, tabUid));
                            break;
                        default:
                            baseItems = new ArrayList<BaseItem>(dao.findRelatedDescriptions(keyword, tabUid));
                            break;
                    }
                }
                if (getContext() != null) {
                    ((Activity) getContext()).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mFindFragment.updateFilterResult(baseItems);
                            mLoading = false;
                        }
                    });
                }
            }
        };
        new Thread(runnableOnNewThread).start();
    }

    private void showLoading() {
        mLoading = true;
        mFindFragment.showLoadingUi();
    }

    private void loadYouTubeItems(final Map<String, String> queryParameters) {
        if (!mLoading) {
            showLoading();
            queryParameters.put("type", mFindFragment.getChosenVideoType());
            queryParameters.put("part", Constants.API_PART_SNIPPET);
            queryParameters.put("maxResults", Constants.API_MAX_RESULTS);
            queryParameters.put("q", mFindFragment.getCurrentKeyword());

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
                        mFindFragment.updateSearchResult(bean);
                        mNextPagingId = bean.getNextPageToken();
                        mLoading = false;
                    } else {
                        onError(error);
                    }
                }

                @Override
                public void onError(Exception error) {
                    mFindFragment.updateSearchResult(setErrorMsg(error));
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
            bean.setErrorMsg(getContext().getResources().getString(R.string.adapter_search_nothing));
        } else {
            bean.setErrorMsg(getContext().getResources().getString(R.string.adapter_error));
        }
        return bean;
    }

    public void startQuery() {
        mFindFragment.reQuery();
    }
}
