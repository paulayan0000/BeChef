package com.paula.android.bechef.receipt;

import com.paula.android.bechef.data.LoadDataCallback;
import com.paula.android.bechef.data.LoadDataTask;
import com.paula.android.bechef.data.dao.ReceiptTabDao;
import com.paula.android.bechef.data.database.TabDatabase;
import com.paula.android.bechef.data.entity.ReceiptItem;
import com.paula.android.bechef.data.entity.ReceiptTab;

import java.util.ArrayList;

import static com.google.android.gms.common.internal.Preconditions.checkNotNull;

public class ReceiptPresenter implements ReceiptContract.Presenter {

    private final ReceiptContract.View mReceiptView;
//    private ArrayList<String> mTabTitles = new ArrayList<>();
    private ArrayList<ReceiptTab> mReceiptTabs = new ArrayList<>();

    public ReceiptPresenter(ReceiptContract.View receiptView) {
        mReceiptView = checkNotNull(receiptView, "receiptView cannot be null!");
        mReceiptView.setPresenter(this);
    }

    @Override
    public void start() {
        loadReceiptTabs();
    }

    private void loadReceiptTabs() {
//        mTabTitles.clear();
        mReceiptTabs.clear();

        new LoadDataTask<>(new LoadDataCallback<ReceiptTabDao>() {
//            private ArrayList<String> mGotTabTitles;
            private ArrayList<ReceiptTab> mGotReceiptTabs;

            @Override
            public ReceiptTabDao getDao() {
                return TabDatabase.getReceiptInstance(mReceiptView.getContext()).receiptDao();
            }

            @Override
            public void doInBackground(ReceiptTabDao receiptTabDao) {
//                mGotTabTitles = new ArrayList<>(receiptTabDao.getAllTabTitles());
                mGotReceiptTabs = new ArrayList<>(receiptTabDao.getAll());
            }

            @Override
            public void onCompleted() {
//                mTabTitles.addAll(mGotTabTitles);
                mReceiptTabs.addAll(mGotReceiptTabs);
                mReceiptView.showDefaultUi(mGotReceiptTabs);
            }
        }).execute();
    }

    @Override
    public void refreshCurrentData() {

    }

    @Override
    public void refreshData(int tabIndex) {

    }

    @Override
    public void leaveChooseDialog() {

    }

    @Override
    public int getChosenItemsCount() {
        return getChosenItems().size();
    }

    @Override
    public ArrayList<ReceiptItem> getChosenItems() {
        return null;
    }

    @Override
    public ArrayList<?> getOtherTabs() {
        return mReceiptTabs;
    }

    @Override
    public int getCurrentTabIndex() {
        return 0;
    }

//    @Override
//    public ArrayList<String> getTabTitles() {
//        return null;
//    }
}
