package com.paula.android.bechef.utils;

import android.content.Context;

import com.paula.android.bechef.data.entity.BaseItem;

public interface EditCallback {
    Context getContext();

    boolean isFromBookmark();

    void onSaveDataComplete(BaseItem baseItem);

    void onChooseImages(int stepPosition, int imagePosition);

    void scrollByY(int y);

    void scrollToBottom();
}
