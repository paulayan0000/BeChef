package com.paula.android.bechef.receiptChild;

import com.paula.android.bechef.customChild.CustomChildPresenter;
import com.paula.android.bechef.data.database.ItemDatabase;
import com.paula.android.bechef.data.entity.BaseTab;
import com.paula.android.bechef.data.entity.ReceiptItem;
import com.paula.android.bechef.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

public class ReceiptChildPresenter extends CustomChildPresenter<ReceiptItem> {
    ReceiptChildPresenter(ReceiptChildFragment receiptChildFragment, BaseTab baseTab) {
        super(receiptChildFragment, baseTab);
    }

    @Override
    public void loadSpecificItems(int type) {
        mDataFilterType = type;
        switch (mDataFilterType) {
            case Constants.FILTER_WITH_TIME_ASC:
                ItemDatabase.getReceiptInstance(mCustomChildFragment.getContext()).receiptDao()
                        .getAllWithTimeAscLive(mTabUid).observe((LifecycleOwner) mCustomChildFragment, new Observer<List<ReceiptItem>>() {
                    @Override
                    public void onChanged(List<ReceiptItem> receiptItems) {
                        mCustomChildFragment.updateItems(new ArrayList<>(receiptItems));
                    }
                });
                break;

            case Constants.FILTER_WITH_TIME_DESC:
                ItemDatabase.getReceiptInstance(mCustomChildFragment.getContext()).receiptDao()
                        .getAllWithTimeDescLive(mTabUid).observe((LifecycleOwner) mCustomChildFragment, new Observer<List<ReceiptItem>>() {
                    @Override
                    public void onChanged(List<ReceiptItem> receiptItems) {
                        mCustomChildFragment.updateItems(new ArrayList<>(receiptItems));
                    }
                });
                break;

            case Constants.FILTER_WITH_RATING_ASC:
                ItemDatabase.getReceiptInstance(mCustomChildFragment.getContext()).receiptDao()
                        .getAllWithRatingAscLive(mTabUid).observe((LifecycleOwner) mCustomChildFragment, new Observer<List<ReceiptItem>>() {
                    @Override
                    public void onChanged(List<ReceiptItem> receiptItems) {
                        mCustomChildFragment.updateItems(new ArrayList<>(receiptItems));
                    }
                });
                break;

            case Constants.FILTER_WITH_RATING_DESC:
                ItemDatabase.getReceiptInstance(mCustomChildFragment.getContext()).receiptDao()
                        .getAllWithRatingDescLive(mTabUid).observe((LifecycleOwner) mCustomChildFragment, new Observer<List<ReceiptItem>>() {
                    @Override
                    public void onChanged(List<ReceiptItem> receiptItems) {
                        mCustomChildFragment.updateItems(new ArrayList<>(receiptItems));
                    }
                });
                break;
        }
    }
}
