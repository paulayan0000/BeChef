package com.paula.android.bechef.detail;

import android.util.Log;
import android.widget.Toast;

import com.paula.android.bechef.api.BeChefApiHelper;
import com.paula.android.bechef.api.GetYouTubeDataTask;
import com.paula.android.bechef.api.beans.GetSearchList;
import com.paula.android.bechef.api.callbacks.GetYouTubeDataCallback;
import com.paula.android.bechef.api.exceptions.NoResourceException;
import com.paula.android.bechef.data.LoadDataCallback;
import com.paula.android.bechef.data.LoadDataTask;
import com.paula.android.bechef.data.dao.BookmarkItemDao;
import com.paula.android.bechef.data.database.ItemDatabase;
import com.paula.android.bechef.data.entity.BaseItem;
import com.paula.android.bechef.data.entity.BookmarkItem;

import java.util.HashMap;
import java.util.Map;

import static com.google.android.gms.common.internal.Preconditions.checkNotNull;

public class DetailPresenter implements DetailContract.Presenter {
    private static final String LOG_TAG = DetailPresenter.class.getSimpleName();
    private final DetailContract.View mDetailView;
    private Object mDataContent;

    public DetailPresenter(DetailContract.View detailView, Object dataContent) {
        mDetailView = checkNotNull(detailView, "detailView cannot be null!");
        mDetailView.setPresenter(this);
        mDataContent = dataContent;
    }

    @Override
    public void start() {
        if (mDataContent instanceof String) {
            new LoadDataTask<>(new LoadDataCallback<BookmarkItemDao>() {
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
                        mDetailView.showDetailUi(mBookmarkItem);
                    } else {
                        Map<String, String> queryParameters = new HashMap<>();
                        queryParameters.put("pageToken", "");
                        queryParameters.put("id", (String) mDataContent);
                        loadVideo(queryParameters);
                    }
                }
            }).execute();
        } else {
            mDetailView.showDetailUi(mDataContent);
        }
    }

    private void loadVideo(Map<String, String> queryParameters) {
        queryParameters.put("part", "snippet,contentDetails,statistics");
        queryParameters.put("maxResults", "10");

        new GetYouTubeDataTask(queryParameters, new GetYouTubeDataCallback() {
            private Exception error;

            @Override
            public GetSearchList doInBackground(Map<String, String> queryParameters) {
                GetSearchList bean = null;
                try {
                    bean = BeChefApiHelper.GetYoutubeVideos(queryParameters);
                } catch (Exception e) {
                    error = e;
                }
                return bean;
            }

            @Override
            public void onCompleted(GetSearchList bean) {
                if (bean != null && error == null) {
                    mDataContent = bean.getDiscoverItems().get(0);
                    mDetailView.showDetailUi(mDataContent);
                } else {
                    onError(error);
                }
            }

            @Override
            public void onError(Exception e) {
                Log.d(LOG_TAG, "Error: " + e.getMessage());
                if (e instanceof NoResourceException)
                    Toast.makeText(mDetailView.getContext(),
                            "此資源不存在！", Toast.LENGTH_LONG).show();
            }
        }).execute();
    }

    public void transDetailUi(BaseItem baseItem) {
        mDataContent = baseItem;
        mDetailView.showDetailUi(baseItem);
    }

    public Object getDataContent() {
        return mDataContent;
    }

    @Override
    public void refreshData(BaseItem baseItem) {
        mDetailView.updateUi(baseItem);
    }
}
