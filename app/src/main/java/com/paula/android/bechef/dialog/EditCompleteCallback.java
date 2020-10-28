package com.paula.android.bechef.dialog;

import com.paula.android.bechef.data.entity.BaseItem;

public interface EditCompleteCallback {
    String getDialogTag();

    void onEditComplete(BaseItem baseItem);

    void onInsertComplete(int position);

    void onChooseImages(int editAdapterPosition, int stepImagePosition);
}
