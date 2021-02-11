package com.paula.android.bechef.api;

import android.os.AsyncTask;

import com.paula.android.bechef.api.beans.GetSearchList;
import com.paula.android.bechef.api.callbacks.GetYouTubeDataCallback;

import java.util.Map;

public class GetYouTubeDataTask extends AsyncTask<Void, Void, GetSearchList> {
    private GetYouTubeDataCallback mCallback;
    private Map<String, String> mQueryParameters;

    public GetYouTubeDataTask(Map<String, String> queryParameters, GetYouTubeDataCallback callback) {
        mQueryParameters = queryParameters;
        mCallback = callback;
    }

    @Override
    protected GetSearchList doInBackground(Void... voids) {
        if (this.isCancelled()) return null;
        return mCallback.doInBackground(mQueryParameters);
    }

    @Override
    protected void onPostExecute(GetSearchList bean) {
        super.onPostExecute(bean);
        mCallback.onCompleted(bean);
    }
}
