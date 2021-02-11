package com.paula.android.bechef.discover;

import android.os.Bundle;
import android.view.View;

import com.paula.android.bechef.BaseContract;
import com.paula.android.bechef.BaseMainFragment;
import com.paula.android.bechef.activities.BeChefActivity;
import com.paula.android.bechef.data.entity.DiscoverTab;
import com.paula.android.bechef.dialog.EditTabAlertDialogBuilder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static com.google.android.gms.common.internal.Preconditions.checkNotNull;

public class DiscoverFragment extends BaseMainFragment implements BaseContract.BaseView<DiscoverPresenter>{
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

    @Override
    protected void find() {
        ((BeChefActivity) mContext).transToFind(mPresenter);

//        SearchItemDialog searchItemDialog = new SearchItemDialog(mPresenter);
//        searchItemDialog.show(getChildFragmentManager(), "search");
    }

    public void showDetailUi(Object content) {
        ((BeChefActivity) mContext).transToDetail(content, true);
//        Fragment searchFragment = getChildFragmentManager().findFragmentByTag("search");
//        if (searchFragment != null) ((SearchItemDialog) searchFragment).dismiss();
    }
}
