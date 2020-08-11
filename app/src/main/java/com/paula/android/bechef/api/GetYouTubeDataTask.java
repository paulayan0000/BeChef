package com.paula.android.bechef.api;

import android.os.AsyncTask;
import android.util.Log;
import com.paula.android.bechef.api.beans.GetSearchList;
import com.paula.android.bechef.api.callbacks.GetYouTubeDataCallback;
import java.util.Map;

public class GetYouTubeDataTask extends AsyncTask<Void, Void, GetSearchList> {

    private GetYouTubeDataCallback mCallback;
//    private String mErrorMessage;
    private Map<String, String> mQueryParameters;

    public GetYouTubeDataTask(Map<String, String> queryParameters, GetYouTubeDataCallback callback) {
        mQueryParameters = queryParameters;
        mCallback = callback;
//        mErrorMessage = "";
    }

    @Override
    protected GetSearchList doInBackground(Void... voids) {
//        GetSearchList bean = null;
//        try {
//            bean = BeChefApiHelper.SearchYoutubeData(mQueryParameters);
        GetSearchList bean = mCallback.doInBackground(mQueryParameters);
//        } catch (Exception e) {
//            mErrorMessage = e.getMessage();
//            e.printStackTrace();
//        }
        return bean;
    }

    @Override
    protected void onPostExecute(GetSearchList bean) {
        super.onPostExecute(bean);
//        if (bean != null) {
            mCallback.onCompleted(bean);
//        } else if (!mErrorMessage.equals("")) {
//            mCallback.onError(mErrorMessage);
//        } else {
//            Log.d("GetChannelIdTask", "error: " + mErrorMessage);
//        }
    }
}