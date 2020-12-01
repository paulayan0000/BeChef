package com.paula.android.bechef.receipt;

import com.paula.android.bechef.customMain.CustomMainFragment;
import com.paula.android.bechef.customMain.CustomMainPresenter;
import com.paula.android.bechef.data.database.TabDatabase;
import com.paula.android.bechef.data.entity.BaseTab;
import com.paula.android.bechef.data.entity.ReceiptTab;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.Observer;

public class ReceiptPresenter extends CustomMainPresenter<ReceiptTab> {
    private ArrayList<BaseTab> mReceiptTabs = new ArrayList<>();

    public ReceiptPresenter(CustomMainFragment<ReceiptTab> customView) {
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
                        mCustomView.showDefaultUi(mReceiptTabs);
                    }
                });
    }
}
