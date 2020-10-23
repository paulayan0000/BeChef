package com.paula.android.bechef.dialog;

import com.paula.android.bechef.data.entity.ReceiptItem;

public interface EditCompleteCallback {
    void onEditComplete(ReceiptItem receiptItem);
    void onInsertComplete(int position);
    void onChooseImages(int editAdapterPosition, int stepImagePosition);
}
