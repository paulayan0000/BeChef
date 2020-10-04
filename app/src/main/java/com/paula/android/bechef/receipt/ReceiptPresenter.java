package com.paula.android.bechef.receipt;

import com.paula.android.bechef.customMain.CustomMainFragment;
import com.paula.android.bechef.customMain.CustomMainPresenter;
import com.paula.android.bechef.data.database.TabDatabase;
import com.paula.android.bechef.data.entity.ReceiptItem;
import com.paula.android.bechef.data.entity.ReceiptTab;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.Observer;

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
        TabDatabase.getReceiptInstance(mCustomView.getContext()).receiptDao().getAllLive()
                .observe(mCustomView, new Observer<List<ReceiptTab>>() {
                    @Override
                    public void onChanged(List<ReceiptTab> receiptTabs) {
                        mReceiptTabs.clear();
                        mReceiptTabs.addAll(receiptTabs);
                        setTabs(mReceiptTabs);
                        mCustomView.showDefaultUi(new ArrayList<>(receiptTabs));
                    }
                });
    }
}
