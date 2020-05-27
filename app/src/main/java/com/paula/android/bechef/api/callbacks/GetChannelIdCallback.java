package com.paula.android.bechef.api.callbacks;

import com.paula.android.bechef.api.beans.GetSearchList;

public interface GetChannelIdCallback {
    void onCompleted(GetSearchList bean);
    void onError(String errorMessage);
}
