package com.paula.android.bechef.api;

import android.os.AsyncTask;

import com.paula.android.bechef.api.beans.YouTubeData;
import com.paula.android.bechef.api.callbacks.GetYouTubeDataCallback;

import java.util.Map;

public class GetYouTubeDataTask extends AsyncTask<Void, Void, YouTubeData> {
    private final GetYouTubeDataCallback mCallback;
    private final Map<String, String> mQueryParameters;

    public GetYouTubeDataTask(Map<String, String> queryParameters, GetYouTubeDataCallback callback) {
        mQueryParameters = queryParameters;
        mCallback = callback;
    }

    @Override
    protected YouTubeData doInBackground(Void... voids) {
        if (this.isCancelled()) return null;
        return mCallback.doInBackground(mQueryParameters);
    }

    @Override
    protected void onPostExecute(YouTubeData bean) {
        super.onPostExecute(bean);
        mCallback.onCompleted(bean);
    }
}
