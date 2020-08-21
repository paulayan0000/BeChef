package com.paula.android.bechef.receiptChild;

import com.paula.android.bechef.data.LoadDataCallback;
import com.paula.android.bechef.data.LoadDataTask;
import com.paula.android.bechef.data.dao.ReceiptItemDao;
import com.paula.android.bechef.data.database.ReceiptItemDatabase;
import com.paula.android.bechef.data.entity.ReceiptItem;
import java.util.ArrayList;
import androidx.room.RoomDatabase;

import static com.google.android.gms.common.internal.Preconditions.checkNotNull;

public class ReceiptChildPresenter implements ReceiptChildFragmentContract.Presenter {
    private ReceiptChildFragmentContract.View mReceiptChildFragmentView;
    private int mTabIndex;
    private ArrayList<ReceiptItem> mReceiptItems = new ArrayList<>();

    ReceiptChildPresenter(ReceiptChildFragmentContract.View receiptChildFragmentView, int tabIndex) {
        mReceiptChildFragmentView = checkNotNull(receiptChildFragmentView, "receiptChildView cannot be null!");
        receiptChildFragmentView.setPresenter(this);
        mTabIndex = tabIndex;
    }

    @Override
    public void start() {
        loadReceiptItems();
    }

    private void loadReceiptItems() {
        ReceiptItemDatabase db = ReceiptItemDatabase.getInstance(mReceiptChildFragmentView.getContext());
        LoadDataTask loadDataTask = new LoadDataTask(db, new LoadDataCallback() {
            private ArrayList<ReceiptItem> mGotReceiptItems;

            @Override
            public void doInBackground(RoomDatabase database) {
                ReceiptItemDao receiptItemDao = ((ReceiptItemDatabase) database).receiptDao();
                mGotReceiptItems = new ArrayList<>(receiptItemDao.getAllWithTab(mTabIndex));
            }

            @Override
            public void onCompleted() {
                mReceiptItems.addAll(mGotReceiptItems);
                mReceiptChildFragmentView.updateData(mReceiptItems);
            }
        });
        loadDataTask.execute();
    }

    @Override
    public void openDetail(Object content) {
        mReceiptChildFragmentView.showDetailUi(content);
    }
}
