package com.paula.android.bechef.receipt;

import com.paula.android.bechef.BaseContract;
import com.paula.android.bechef.data.entity.ReceiptItem;

public interface ReceiptContract extends BaseContract {
    interface View extends CustomView<Presenter, ReceiptItem> {
    }

    interface Presenter extends CustomPresenter<ReceiptItem> {
    }
}