package com.paula.android.bechef.receipt;

import static com.google.android.gms.common.internal.Preconditions.checkNotNull;

public class ReceiptPresenter implements ReceiptContract.Presenter {

    private final ReceiptContract.View mReceiptView;

    public ReceiptPresenter(ReceiptContract.View receiptView) {
        mReceiptView = checkNotNull(receiptView, "receiptView cannot be null!");
        mReceiptView.setPresenter(this);
    }

    @Override
    public void start() {

    }
}
