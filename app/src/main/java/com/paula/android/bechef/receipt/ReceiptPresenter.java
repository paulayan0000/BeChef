package com.paula.android.bechef.receipt;

import com.paula.android.bechef.customMain.CustomMainFragment;
import com.paula.android.bechef.customMain.CustomMainPresenter;
import com.paula.android.bechef.data.LoadDataCallback;
import com.paula.android.bechef.data.LoadDataTask;
import com.paula.android.bechef.data.dao.ReceiptTabDao;
import com.paula.android.bechef.data.database.TabDatabase;
import com.paula.android.bechef.data.entity.ReceiptItem;
import com.paula.android.bechef.data.entity.ReceiptTab;

import java.util.ArrayList;

public class ReceiptPresenter extends CustomMainPresenter<ReceiptTab, ReceiptItem> {
    private ArrayList<ReceiptTab> mReceiptTabs = new ArrayList<>();

    public ReceiptPresenter(CustomMainFragment<ReceiptTab, ReceiptItem> customView) {
        super(customView);
    }

    @Override
    public void start() {
        loadReceiptTabs();
    }

    private void loadReceiptTabs() {
        mReceiptTabs.clear();
        new LoadDataTask<>(new LoadDataCallback<ReceiptTabDao>() {
            private ArrayList<ReceiptTab> mGotReceiptTabs;

            @Override
            public ReceiptTabDao getDao() {
                return TabDatabase.getReceiptInstance(mCustomView.getContext()).receiptDao();
            }

            @Override
            public void doInBackground(ReceiptTabDao receiptTabDao) {
                mGotReceiptTabs = new ArrayList<>(receiptTabDao.getAll());
            }

            @Override
            public void onCompleted() {
                mReceiptTabs.addAll(mGotReceiptTabs);
                setTabs(mReceiptTabs);
                mCustomView.showDefaultUi(mGotReceiptTabs);
            }
        }).execute();
    }
}
