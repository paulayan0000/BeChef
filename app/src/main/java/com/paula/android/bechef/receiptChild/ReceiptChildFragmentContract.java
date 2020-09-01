package com.paula.android.bechef.receiptChild;

import com.paula.android.bechef.ChildContract;

public interface ReceiptChildFragmentContract extends ChildContract {
    interface View extends CustomChildView<Presenter> {
    }

    interface Presenter extends CustomChildPresenter {
    }
}
