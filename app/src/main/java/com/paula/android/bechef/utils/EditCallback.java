package com.paula.android.bechef.utils;

public interface EditCallback {
    boolean isFromBookmark();

    void onChooseImages(int stepPosition, int imagePosition);

    void scrollByY(int y);

    void scrollToBottom();
}