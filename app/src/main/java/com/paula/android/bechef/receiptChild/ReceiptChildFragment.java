package com.paula.android.bechef.receiptChild;

import com.paula.android.bechef.customChild.CustomChildFragment;
import com.paula.android.bechef.customMain.CustomMainFragment;
import com.paula.android.bechef.data.entity.ReceiptItem;
import com.paula.android.bechef.data.entity.ReceiptTab;
import androidx.fragment.app.Fragment;

public class ReceiptChildFragment extends CustomChildFragment<ReceiptItem> {
    private ReceiptChildFragment(ReceiptTab receiptTab, Fragment fragment) {
        mCustomChildPresenter = new ReceiptChildPresenter(this, receiptTab);
        mCustomMainFragment = (CustomMainFragment) fragment;
    }

    public static ReceiptChildFragment newInstance(ReceiptTab receiptTab, Fragment fragment) {
        return new ReceiptChildFragment(receiptTab, fragment);
    }

    public long getTabUid() {
        return mCustomChildPresenter.mTabUid;
    }
}
