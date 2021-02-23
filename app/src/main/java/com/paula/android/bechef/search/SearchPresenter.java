package com.paula.android.bechef.search;

import android.os.AsyncTask;
import android.util.Log;

import com.paula.android.bechef.api.BeChefApiHelper;
import com.paula.android.bechef.api.GetYouTubeDataTask;
import com.paula.android.bechef.api.beans.GetSearchList;
import com.paula.android.bechef.api.callbacks.GetYouTubeDataCallback;
import com.paula.android.bechef.api.exceptions.NoResourceException;
import com.paula.android.bechef.data.LoadDataCallback;
import com.paula.android.bechef.data.LoadDataTask;
import com.paula.android.bechef.data.database.ItemDatabase;
import com.paula.android.bechef.data.entity.BaseItem;
import com.paula.android.bechef.data.entity.BaseTab;
import com.paula.android.bechef.data.entity.BookmarkTab;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.google.android.gms.common.internal.Preconditions.checkNotNull;

public class SearchPresenter implements SearchContract.Presenter {
    private SearchContract.View mSearchFragment;
    private ArrayList<BaseTab> mBaseTabs = null;
    private boolean mIsInDiscover = true;
    private LoadDataTask<ItemDatabase> mLoadDataTask;
    private GetYouTubeDataTask mGetYouTubeDataTask;
    private int mLastVisibleItemPosition;
    private String mNextPagingId = "";
    private boolean mLoading = false;
    private String mVideoType;

    public SearchPresenter(SearchContract.View searchFragment, ArrayList<BaseTab> baseTabs) {
        mSearchFragment = checkNotNull(searchFragment, "searchView cannot be null!");
        mSearchFragment.setPresenter(this);
        mBaseTabs = new ArrayList<>(baseTabs);
        mIsInDiscover = false;
        mLoading = false;
    }

    public SearchPresenter(SearchContract.View searchFragment) {
        mSearchFragment = checkNotNull(searchFragment, "searchView cannot be null!");
        mSearchFragment.setPresenter(this);
    }

    @Override
    public boolean isInDiscover() {
        return mIsInDiscover;
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
        mSearchFragment.updateFilterView(mBaseTabs);
    }

    public void setBaseTabs(ArrayList<BaseTab> baseTabs) {
        mBaseTabs = baseTabs;
        mIsInDiscover = mBaseTabs == null;
        setLoading(false);
    }

    @Override
    public void openDetail(Object content, boolean isBottomShown) {
        mSearchFragment.showDetailUi(content, isBottomShown);
    }

    @Override
    public void cancelTask() {
        if (mLoadDataTask != null && !mLoadDataTask.isCancelled() && mLoadDataTask.getStatus() == AsyncTask.Status.RUNNING) {
            mLoadDataTask.cancel(true);
            mLoadDataTask = null;
        }
        if (mGetYouTubeDataTask != null && !mGetYouTubeDataTask.isCancelled() && mGetYouTubeDataTask.getStatus() == AsyncTask.Status.RUNNING) {
            mGetYouTubeDataTask.cancel(true);
            mGetYouTubeDataTask = null;
            mLoading = false;
        }
    }

    @Override
    public void loadResults(final ArrayList<BaseTab> chosenTabs) {
        if (chosenTabs.size() == 0) return;
        cancelTask();
        if (mBaseTabs == null) {
            Map<String, String> queryParameters = new HashMap<>();
            mVideoType = chosenTabs.get(0).getTabName();
            queryParameters.put("pageToken", "");
            loadYouTubeItems(queryParameters);
        } else {
            setLoading(true);
            mLoadDataTask = new LoadDataTask<>(new LoadDataCallback<ItemDatabase>() {
                private BaseTab baseTab = mBaseTabs.get(0);
                private ArrayList<BaseItem> baseItems = new ArrayList<>();
                private String keyword = mSearchFragment.getCurrentKeyword();

                @Override
                public ItemDatabase getDao() {
                    if (baseTab instanceof BookmarkTab)
                        return ItemDatabase.getBookmarkInstance(mSearchFragment.getContext());
                    return ItemDatabase.getReceiptInstance(mSearchFragment.getContext());
                }

                @Override
                public void doInBackground(ItemDatabase database) {
                    baseItems.clear();
                    long tabUid = chosenTabs.get(0).getUid();
                    String searchRange = chosenTabs.get(1).getTabName();
                    if (baseTab instanceof BookmarkTab) {
                        switch (searchRange) {
                            case "標題":
                                baseItems.addAll(database.bookmarkDao().searchRelatedTitles(keyword, tabUid));
                                break;
                            case "標籤":
                                baseItems.addAll(database.bookmarkDao().searchRelatedTags(keyword, tabUid));
                                break;
                            default:
                                baseItems.addAll(database.bookmarkDao().searchRelatedDescriptions(keyword, tabUid));
                                break;
                        }
                     } else {
                        switch (searchRange) {
                            case "標題":
                                baseItems.addAll(database.receiptDao().searchRelatedTitles(keyword, tabUid));
                                break;
                            case "標籤":
                                baseItems.addAll(database.receiptDao().searchRelatedTags(keyword, tabUid));
                                break;
                            default:
                                baseItems.addAll(database.receiptDao().searchRelatedDescriptions(keyword, tabUid));
                                break;
                        }
                    }
                }

                @Override
                public void onCompleted() {
                    mSearchFragment.updateResultView(baseItems);
                }
            });
            if (!mLoadDataTask.isCancelled()) mLoadDataTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }

    private void setLoading(boolean isLoading) {
        mLoading = isLoading;
        mSearchFragment.showLoading(isLoading);
    }

    private void loadYouTubeItems(final Map<String, String> queryParameters) {
        if (!mLoading) {
            setLoading(true);
            Log.d("SearchPresenter", "start load...");

            if ("頻道".equals(mVideoType)) queryParameters.put("type", "channel");
            else queryParameters.put("type", "video");
            queryParameters.put("part", "snippet");
            queryParameters.put("maxResults", "10");
            queryParameters.put("q", mSearchFragment.getCurrentKeyword());

            mGetYouTubeDataTask = new GetYouTubeDataTask(queryParameters, new GetYouTubeDataCallback() {
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
                    Log.d("SearchPresenter", "complete " + queryParameters.get("q"));
                    if (bean != null && error == null) {
                        mSearchFragment.updateSearchItems(bean);
                        mNextPagingId = bean.getNextPageToken();
                        setLoading(false);
                    } else {
                        onError(error);
                    }
                }

                @Override
                public void onError(Exception e) {
                    Log.d("SearchPresenter", "Error: " + e.getMessage());
                    GetSearchList bean = new GetSearchList();
                    if (e instanceof NoResourceException)
                        bean.setErrorMsg("找不到結果\n請修改關鍵字或調整篩選條件！");
                    else
                        bean.setErrorMsg("發生錯誤\n請檢查網路連線！");
                    mSearchFragment.updateSearchItems(bean);
                    setLoading(false);
                }
            });
            if (!mGetYouTubeDataTask.isCancelled()) mGetYouTubeDataTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            else Log.d("SearchPresenter", "cancelled");
        }
    }

    public void startQuery() {
        mSearchFragment.reQuery();
    }
}
