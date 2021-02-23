package com.paula.android.bechef.detail;

import android.os.AsyncTask;
import android.util.Log;

import com.paula.android.bechef.api.BeChefApiHelper;
import com.paula.android.bechef.api.GetYouTubeDataTask;
import com.paula.android.bechef.api.beans.GetSearchList;
import com.paula.android.bechef.api.callbacks.GetYouTubeDataCallback;
import com.paula.android.bechef.api.exceptions.NoResourceException;
import com.paula.android.bechef.data.LoadDataCallback;
import com.paula.android.bechef.data.LoadDataTask;
import com.paula.android.bechef.data.dao.BookmarkItemDao;
import com.paula.android.bechef.data.dao.DiscoverTabDao;
import com.paula.android.bechef.data.database.ItemDatabase;
import com.paula.android.bechef.data.database.TabDatabase;
import com.paula.android.bechef.data.entity.BaseItem;
import com.paula.android.bechef.data.entity.BookmarkItem;
import com.paula.android.bechef.data.entity.DiscoverItem;
import com.paula.android.bechef.data.entity.DiscoverTab;
import com.paula.android.bechef.data.entity.ReceiptItem;

import java.util.HashMap;
import java.util.Map;

import static com.google.android.gms.common.internal.Preconditions.checkNotNull;

public class DetailPresenter implements DetailContract.Presenter {
    private static final String LOG_TAG = DetailPresenter.class.getSimpleName();
    private DetailContract.View mDetailView;
    private Object mDataContent;
    private LoadDataTask<BookmarkItemDao> mLoadBookmarkItemTask;
    private LoadDataTask<DiscoverTabDao> mLoadDiscoverTabTask;
    private GetYouTubeDataTask mGetYouTubeDataTask;

    public DetailPresenter(DetailContract.View detailView, Object dataContent) {
        mDetailView = checkNotNull(detailView, "detailView cannot be null!");
        mDetailView.setPresenter(this);
        mDataContent = dataContent;
    }

    @Override
    public void start() {
        if (mDataContent instanceof String) {
            if (checkTaskIsCanceled(mLoadBookmarkItemTask)) mLoadBookmarkItemTask = null;
            mDetailView.showLoading(true);
            mLoadBookmarkItemTask = new LoadDataTask<>(new LoadDataCallback<BookmarkItemDao>() {
                private BookmarkItem mBookmarkItem = null;

                @Override
                public BookmarkItemDao getDao() {
                    return ItemDatabase.getBookmarkInstance(mDetailView.getContext()).bookmarkDao();
                }

                @Override
                public void doInBackground(BookmarkItemDao dao) {
                    mBookmarkItem = dao.getItemWithVideoId((String) mDataContent);
                }

                @Override
                public void onCompleted() {
                    if (mBookmarkItem != null) {
                        mDataContent = mBookmarkItem;
                        mDetailView.showDetailUi((BookmarkItem) mDataContent);
                    } else {
                        Map<String, String> queryParameters = new HashMap<>();
                        queryParameters.put("pageToken", "");
                        queryParameters.put("id", (String) mDataContent);
                        loadVideo(queryParameters, "videos");
                    }
                }
            });
            if (!mLoadBookmarkItemTask.isCancelled()) mLoadBookmarkItemTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else if (mDataContent instanceof DiscoverItem) {
            if (checkTaskIsCanceled(mLoadDiscoverTabTask)) mLoadDiscoverTabTask = null;
            mDetailView.showLoading(true);
            mLoadDiscoverTabTask = new LoadDataTask<>(new LoadDataCallback<DiscoverTabDao>() {
                private DiscoverTab mDiscoverTab = null;

                @Override
                public DiscoverTabDao getDao() {
                    return TabDatabase.getDiscoverInstance(mDetailView.getContext()).discoverDao();
                }

                @Override
                public void doInBackground(DiscoverTabDao dao) {
                    mDiscoverTab = dao.getTabWithChannelId(((DiscoverItem) mDataContent).getChannelId());
                }

                @Override
                public void onCompleted() {
                    DiscoverItem discoverItem = (DiscoverItem) mDataContent;
                    discoverItem.setInBeChef(mDiscoverTab != null);
                    mDataContent = discoverItem;
                    Map<String, String> queryParameters = new HashMap<>();
                    queryParameters.put("pageToken", "");
                    queryParameters.put("id", discoverItem.getChannelId());
                    loadVideo(queryParameters, "channels");
                }
            });
            if (!mLoadDiscoverTabTask.isCancelled()) mLoadDiscoverTabTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else if (mDataContent instanceof BookmarkItem) {
            mDetailView.showDetailUi((BookmarkItem) mDataContent);
        } else {
            mDetailView.showDetailUi((ReceiptItem) mDataContent);
        }
    }

    private boolean checkTaskIsCanceled(AsyncTask task) {
        if (task != null && !task.isCancelled() && task.getStatus() == AsyncTask.Status.RUNNING) {
            task.cancel(true);
            return true;
        }
        return false;
    }

    private void loadVideo(Map<String, String> queryParameters, final String apiTypeName) {
        queryParameters.put("part", "snippet,contentDetails,statistics");
        queryParameters.put("maxResults", "10");

        if (checkTaskIsCanceled(mGetYouTubeDataTask)) mGetYouTubeDataTask = null;
        mGetYouTubeDataTask = new GetYouTubeDataTask(queryParameters, new GetYouTubeDataCallback() {
            private Exception error;

            @Override
            public GetSearchList doInBackground(Map<String, String> queryParameters) {
                GetSearchList bean = null;
                try {
                    bean = BeChefApiHelper.GetYoutubeData(queryParameters, apiTypeName);
                } catch (Exception e) {
                    error = e;
                }
                return bean;
            }

            @Override
            public void onCompleted(GetSearchList bean) {
                if (bean != null && error == null) {
                    DiscoverItem discoverItem = bean.getDiscoverItems().get(0);
                    if (mDataContent instanceof DiscoverItem) discoverItem.setInBeChef(((DiscoverItem) mDataContent).isInBeChef());
                    mDataContent = discoverItem;
                    mDetailView.showDetailUi(discoverItem);
                } else {
                    onError(error);
                }
            }

            @Override
            public void onError(Exception e) {
                Log.d(LOG_TAG, "Error: " + e.getMessage());
                if (e instanceof NoResourceException)
                    mDetailView.showErrorUi("此資源不存在！");
                else
                    mDetailView.showErrorUi("發生錯誤\n請檢查網路連線！");
            }
        });
        if (!mGetYouTubeDataTask.isCancelled()) mGetYouTubeDataTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void updateData(BaseItem baseItem) {
        mDataContent = baseItem;
        mDetailView.updateButton(baseItem.getVideoId().isEmpty());
    }

    public Object getDataContent() {
        return mDataContent;
    }

    public void setDataContent(Object dataContent) {
        mDataContent = dataContent;
    }

    @Override
    public void refreshData(BaseItem baseItem) {
        mDetailView.updateUi(baseItem);
    }

    @Override
    public void stopAllAsyncTasks() {
        if (checkTaskIsCanceled(mLoadBookmarkItemTask)) mLoadBookmarkItemTask = null;
        if (checkTaskIsCanceled(mGetYouTubeDataTask)) mGetYouTubeDataTask = null;
    }

    @Override
    public void addToDiscover(final DiscoverItem discoverItem) {
        discoverItem.setInBeChef(true);
        if (checkTaskIsCanceled(mLoadDiscoverTabTask)) mLoadDiscoverTabTask = null;
        new LoadDataTask<>(new LoadDataCallback<DiscoverTabDao>() {
            @Override
            public DiscoverTabDao getDao() {
                return TabDatabase.getDiscoverInstance(mDetailView.getContext()).discoverDao();
            }

            @Override
            public void doInBackground(DiscoverTabDao dao) {
                dao.insert(new DiscoverTab(discoverItem.getChannelId(), discoverItem.getTitle()));
            }

            @Override
            public void onCompleted() {
                updateData(discoverItem);
            }
        }).execute();
    }
}
