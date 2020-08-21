package com.paula.android.bechef.api.callbacks;

import com.paula.android.bechef.api.beans.GetSearchList;
import java.util.Map;

public interface GetYouTubeDataCallback {
    GetSearchList doInBackground(Map<String, String> queryParameters);
    void onCompleted(GetSearchList bean);
    void onError(Exception e);
}
