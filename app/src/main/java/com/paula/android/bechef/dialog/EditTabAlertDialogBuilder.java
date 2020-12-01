package com.paula.android.bechef.dialog;

import android.content.Context;
import android.view.View;

import com.paula.android.bechef.BaseContract;
import com.paula.android.bechef.R;
import com.paula.android.bechef.adapters.EditTabAdapter;
import com.paula.android.bechef.discover.DiscoverPresenter;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class EditTabAlertDialogBuilder extends BeChefAlertDialogBuilder {
    private EditTabAdapter mEditTabAdapter;

    public EditTabAlertDialogBuilder(@NonNull Context context, BaseContract.MainPresenter presenter) {
        super(context, R.style.SmallDialogTheme);
        if (presenter instanceof DiscoverPresenter) {
            mEditTabAdapter = new EditTabAdapter(new ArrayList<>(presenter.getTabs()));
        } else {
            mEditTabAdapter = new EditTabAdapter(new ArrayList<>(presenter.getTabs()));
        }
        initBuilder(mContext);
    }

    private void initBuilder(@NonNull Context context) {
        mContext = context;
        setCancelable(true);
        setTitle("編輯書籤");
        setButtons(new AlertDialogClickCallback() {
            @Override
            public void onPositiveButtonClick() {
                mEditTabAdapter.onCompleteClicked();
//                mMainPresenter.updateTabs(mEditTabAdapter.getBaseTabs());
            }
        });
    }

    @Override
    protected String getPositiveWord() {
        return "完成";
    }

    @Override
    protected String getNegativeWord() {
        return "取消";
    }

    @Override
    protected void setCustomView(AlertDialog alertDialog) {
        View view = alertDialog.getLayoutInflater().inflate(R.layout.edit_tab_alert_dialog, null);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerview_edit);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerView.setAdapter(mEditTabAdapter);
        alertDialog.setView(view);
    }
}
