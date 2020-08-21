package com.paula.android.bechef.receipt;

import com.paula.android.bechef.data.LoadDataCallback;
import com.paula.android.bechef.data.LoadDataTask;
import com.paula.android.bechef.data.dao.ReceiptTabDao;
import com.paula.android.bechef.data.database.ReceiptTabDatabase;
import java.util.ArrayList;
import androidx.room.RoomDatabase;

import static com.google.android.gms.common.internal.Preconditions.checkNotNull;

public class ReceiptPresenter implements ReceiptContract.Presenter {

    private final ReceiptContract.View mReceiptView;
    private ArrayList<String> mTabTitles = new ArrayList<>();

    public ReceiptPresenter(ReceiptContract.View receiptView) {
        mReceiptView = checkNotNull(receiptView, "receiptView cannot be null!");
        mReceiptView.setPresenter(this);
    }

    @Override
    public void start() {
        loadReceiptTabs();
    }

    private void loadReceiptTabs() {
        mTabTitles.clear();
        ReceiptTabDatabase db = ReceiptTabDatabase.getInstance(mReceiptView.getContext());
        LoadDataTask loadDataTask = new LoadDataTask(db, new LoadDataCallback() {
            private ArrayList<String> mGotTabTitles;

            @Override
            public void doInBackground(RoomDatabase database) {
                ReceiptTabDao receiptTabDao = ((ReceiptTabDatabase) database).receiptDao();
                mGotTabTitles = new ArrayList<>(receiptTabDao.getAllTabTitles());
            }

            @Override
            public void onCompleted() {
                mTabTitles.addAll(mGotTabTitles);
                mReceiptView.showDefaultUi(mTabTitles);
            }
        });
        loadDataTask.execute();
    }
}
