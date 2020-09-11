package com.paula.android.bechef.receiptChild;

import com.paula.android.bechef.customChild.CustomChildPresenter;
import com.paula.android.bechef.data.LoadDataCallback;
import com.paula.android.bechef.data.LoadDataTask;
import com.paula.android.bechef.data.dao.ReceiptItemDao;
import com.paula.android.bechef.data.database.ItemDatabase;
import com.paula.android.bechef.data.entity.BaseTab;
import com.paula.android.bechef.data.entity.ReceiptItem;
import com.paula.android.bechef.utils.Constants;

import java.util.ArrayList;

public class ReceiptChildPresenter extends CustomChildPresenter<ReceiptItem> {
    ReceiptChildPresenter(ReceiptChildFragment receiptChildFragment, BaseTab baseTab) {
        super(receiptChildFragment, baseTab);
    }

    @Override
    public void loadSpecificItems(int type) {
        mDataFilterType = type;
        new LoadDataTask<>(new LoadDataCallback<ReceiptItemDao>() {
            @Override
            public ReceiptItemDao getDao() {
                return ItemDatabase.getReceiptInstance(mCustomChildFragment.getContext()).receiptDao();
            }

            @Override
            public void doInBackground(ReceiptItemDao receiptItemDao) {
                switch (mDataFilterType) {
                    case Constants.FILTER_WITH_TIME_ASC:
                        mDataArrayList = new ArrayList<>(receiptItemDao.getAllWithTimeAsc(mTabUid));
                        break;
                    case Constants.FILTER_WITH_RATING_DESC:
                        mDataArrayList = new ArrayList<>(receiptItemDao.getAllWithRatingDesc(mTabUid));
                        break;
                    case Constants.FILTER_WITH_RATING_ASC:
                        mDataArrayList = new ArrayList<>(receiptItemDao.getAllWithRatingAsc(mTabUid));
                        break;
                    default:
                    case Constants.FILTER_WITH_TIME_DESC:
                        mDataArrayList = new ArrayList<>(receiptItemDao.getAllWithTimeDesc(mTabUid));
                        break;
                }
            }

            @Override
            public void onCompleted() {
                mCustomChildFragment.updateItems(mDataArrayList);
            }
        }).execute();
    }
}
