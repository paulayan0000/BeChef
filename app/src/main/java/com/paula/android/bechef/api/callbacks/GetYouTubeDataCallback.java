package com.paula.android.bechef.api.callbacks;

import com.paula.android.bechef.api.beans.YouTubeData;

import java.util.Map;

public interface GetYouTubeDataCallback {
    YouTubeData doInBackground(Map<String, String> queryParameters);

    void onCompleted(YouTubeData bean);

    void onError(Exception e);
}
