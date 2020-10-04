package com.paula.android.bechef.receipt;

import com.paula.android.bechef.customMain.CustomMainFragment;
import com.paula.android.bechef.R;
import com.paula.android.bechef.data.entity.ReceiptItem;
import com.paula.android.bechef.data.entity.ReceiptTab;
import com.paula.android.bechef.receiptChild.ReceiptChildFragment;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;

public class ReceiptFragment extends CustomMainFragment<ReceiptTab, ReceiptItem> {
    public static ReceiptFragment newInstance() {
        return new ReceiptFragment();
    }

    @Override
    protected int getTitleText() {
        return R.string.title_receipt;
    }

    @Override
    public ArrayList<ReceiptItem> getChosenItems() {
        Fragment childFragment = getChildFragment(mCurrentTabIndex);
        if (childFragment != null) return ((ReceiptChildFragment)childFragment).getChosenItems();
        return new ArrayList<>();
    }
}