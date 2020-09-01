package com.paula.android.bechef.receiptChild;

import com.paula.android.bechef.data.LoadDataCallback;
import com.paula.android.bechef.data.LoadDataTask;
import com.paula.android.bechef.data.dao.ReceiptItemDao;
import com.paula.android.bechef.data.database.ItemDatabase;
import com.paula.android.bechef.data.entity.BaseItem;
import com.paula.android.bechef.data.entity.BaseTab;
import com.paula.android.bechef.data.entity.ReceiptItem;
import com.paula.android.bechef.data.entity.ReceiptTab;

import java.util.ArrayList;

import static com.google.android.gms.common.internal.Preconditions.checkNotNull;

public class ReceiptChildPresenter implements ReceiptChildFragmentContract.Presenter {
    private ReceiptChildFragmentContract.View mReceiptChildFragmentView;
    private int mTabUid;
    private ArrayList<BaseItem> mReceiptItems = new ArrayList<>();

//    ReceiptChildPresenter(ReceiptChildFragmentContract.View receiptChildFragmentView, int tabIndex) {
//        mReceiptChildFragmentView = checkNotNull(receiptChildFragmentView, "receiptChildView cannot be null!");
//        receiptChildFragmentView.setPresenter(this);
//        mTabIndex = tabIndex;
//    }
    ReceiptChildPresenter(ReceiptChildFragmentContract.View receiptChildFragmentView, ReceiptTab receiptTab) {
        mReceiptChildFragmentView = checkNotNull(receiptChildFragmentView, "receiptChildView cannot be null!");
        receiptChildFragmentView.setPresenter(this);
        mTabUid = receiptTab.getUid();
    }

    @Override
    public void start() {
        loadReceiptItems();
    }

    private void loadReceiptItems() {
        new LoadDataTask<>(new LoadDataCallback<ReceiptItemDao>() {
            private ArrayList<ReceiptItem> mGotReceiptItems;

            @Override
            public ReceiptItemDao getDao() {
                return ItemDatabase.getReceiptInstance(mReceiptChildFragmentView.getContext()).receiptDao();
            }

            @Override
            public void doInBackground(ReceiptItemDao receiptItemDao) {
                mGotReceiptItems = new ArrayList<>(receiptItemDao.getAllWithTimeDesc(mTabUid));
            }

            @Override
            public void onCompleted() {
                mReceiptItems.addAll(mGotReceiptItems);
                mReceiptChildFragmentView.updateItems(mReceiptItems);
            }
        }).execute();
    }

    @Override
    public void openDetail(Object content, boolean isBottomShown) {
        mReceiptChildFragmentView.showDetailUi(content, isBottomShown);
    }

    @Override
    public void transToSelectable() {

    }

    @Override
    public void loadSpecificItems(int type) {

    }

    @Override
    public void loadItems() {

    }
}
