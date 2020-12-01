package com.paula.android.bechef.utils;

public interface EditCallback<T> {
    String getDialogTag();

    void onSaveDataComplete(T data);

    void onInsertComplete(int position);

    void onChooseImages(int editAdapterPosition, int stepImagePosition);
}
