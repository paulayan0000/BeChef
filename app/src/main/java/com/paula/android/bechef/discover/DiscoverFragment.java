package com.paula.android.bechef.discover;

import android.os.Bundle;
import android.view.View;

import com.paula.android.bechef.BaseContract;
import com.paula.android.bechef.BaseMainFragment;
import com.paula.android.bechef.data.entity.DiscoverTab;
import com.paula.android.bechef.dialog.EditTabAlertDialogBuilder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static com.google.android.gms.common.internal.Preconditions.checkNotNull;

public class DiscoverFragment extends BaseMainFragment<DiscoverTab> implements BaseContract.BaseView<DiscoverPresenter>{
    private DiscoverPresenter mPresenter;

    public static DiscoverFragment newInstance() {
        return new DiscoverFragment();
    }

    @Override
    public void setPresenter(DiscoverPresenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.start();
    }

    @Override
    protected void editTab() {
        EditTabAlertDialogBuilder builder = new EditTabAlertDialogBuilder(mContext, mPresenter);
        builder.create().show();
//        EditTabDialog editTabDialog = new EditTabDialog(mPresenter.getDiscoverTabs());
//        editTabDialog.show(getChildFragmentManager(), "edit");
    }
}
