package com.paula.android.bechef.detail;

import android.util.Log;
import android.widget.Toast;

import com.paula.android.bechef.api.BeChefApiHelper;
import com.paula.android.bechef.api.GetYouTubeDataTask;
import com.paula.android.bechef.api.beans.GetSearchList;
import com.paula.android.bechef.api.callbacks.GetYouTubeDataCallback;
import com.paula.android.bechef.api.exceptions.NoResourceException;

import java.util.HashMap;
import java.util.Map;

import static com.google.android.gms.common.internal.Preconditions.checkNotNull;

public class DetailPresenter implements DetailContract.Presenter {
    private static final String LOG_TAG = DetailPresenter.class.getSimpleName();
    private final DetailContract.View mDetailView;
    private Object mContent;
    private boolean mLoading = false;

    public DetailPresenter(DetailContract.View detailView, Object content) {
        mDetailView = checkNotNull(detailView, "detailView cannot be null!");
        mDetailView.setPresenter(this);
        mContent = content;
    }

    @Override
    public void start() {
        if (mContent instanceof String) {
            Map<String, String> queryParameters = new HashMap<>();
            queryParameters.put("pageToken", "");
            queryParameters.put("id", (String) mContent);
            loadVideo(queryParameters);
        } else {
            mDetailView.showDetailUi(mContent);
        }
    }

    private void loadVideo(Map<String, String> queryParameters) {
        if (!mLoading) {
            mLoading = true;
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
                        mDetailView.showDetailUi(bean.getDiscoverItems().get(0));
                        mLoading = false;
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
                    mLoading = false;
                }
            }).execute();
        }
    }
}
