package com.paula.android.bechef.find;

import android.app.Activity;
import android.os.AsyncTask;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.paula.android.bechef.BeChef;
import com.paula.android.bechef.api.beans.YouTubeData;
import com.paula.android.bechef.api.BeChefApiHelper;
import com.paula.android.bechef.thread.GetDataTaskCallback;
import com.paula.android.bechef.api.exceptions.NoResourceException;
import com.paula.android.bechef.thread.GetDataAsyncTask;
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
    private GetDataAsyncTask<YouTubeData> mGetDataAsyncTask;
    private GetDataAsyncTask<ArrayList<BaseItem>> mGetItemDataTask;
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
    public Activity getActivity() {
        return ((FindFragment) mFindFragment).getActivity();
    }

    @Override
    public void openDetail(Object content, boolean isBottomShown) {
        mFindFragment.showDetailUi(content, isBottomShown);
    }

    @Override
    public void cancelTask() {
        if (mGetDataAsyncTask != null && !mGetDataAsyncTask.isCancelled() &&
                mGetDataAsyncTask.getStatus() == AsyncTask.Status.RUNNING) {
            mGetDataAsyncTask.cancel(true);
            mGetDataAsyncTask = null;
        }
        if (mGetItemDataTask != null && !mGetItemDataTask.isCancelled() &&
                mGetItemDataTask.getStatus() == AsyncTask.Status.RUNNING) {
            mGetItemDataTask.cancel(true);
            mGetItemDataTask = null;
        }
        mLoading = false;
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

        mGetItemDataTask = new GetDataAsyncTask<>(new GetDataTaskCallback<ArrayList<BaseItem>>() {
            @Override
            public ArrayList<BaseItem> doInBackground() {
                ArrayList<BaseItem> baseItems;
                if (isFromBookmark) {
                    BookmarkItemDao dao = ItemDatabase.getItemInstance().bookmarkDao();
                    switch (filterRange) {
                        case Constants.VARIABLE_NAME_TITLE:
                            baseItems = new ArrayList<BaseItem>(dao
                                    .findRelatedTitles(keyword, tabUid));
                            break;
                        case Constants.VARIABLE_NAME_TAGS:
                            baseItems = new ArrayList<BaseItem>(dao
                                    .findRelatedTags(keyword, tabUid));
                            break;
                        default:
                            baseItems = new ArrayList<BaseItem>(dao
                                    .findRelatedDescriptions(keyword, tabUid));
                            break;
                    }
                } else {
                    RecipeItemDao dao = ItemDatabase.getItemInstance().recipeDao();
                    switch (filterRange) {
                        case Constants.VARIABLE_NAME_TITLE:
                            baseItems = new ArrayList<BaseItem>(dao
                                    .findRelatedTitles(keyword, tabUid));
                            break;
                        case Constants.VARIABLE_NAME_TAGS:
                            baseItems = new ArrayList<BaseItem>(dao
                                    .findRelatedTags(keyword, tabUid));
                            break;
                        default:
                            baseItems = new ArrayList<BaseItem>(dao
                                    .findRelatedDescriptions(keyword, tabUid));
                            break;
                    }
                }
                return baseItems;
            }

            @Override
            public void onCompleted(ArrayList<BaseItem> baseItems) {
                if (mGetItemDataTask.isCancelled()) {
                    mLoading = false;
                    return;
                }
                mFindFragment.updateFilterResult(baseItems);
                mLoading = false;
            }

            @Override
            public void onError(Exception error) {
            }
        });
        if (!mGetItemDataTask.isCancelled()) {
            mGetItemDataTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
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
            if (!mGetDataAsyncTask.isCancelled()) {
                mGetDataAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        }
    }

    private YouTubeData setErrorMsg(Exception error) {
        YouTubeData bean = new YouTubeData();
        if (error instanceof NoResourceException) {
            bean.setErrorMsg(BeChef.getAppContext().getResources().getString(R.string.adapter_search_nothing));
        } else {
            bean.setErrorMsg(BeChef.getAppContext().getResources().getString(R.string.adapter_error));
        }
        return bean;
    }

    public void startQuery() {
        mFindFragment.reQuery();
    }
}