package com.paula.android.bechef.discover;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.paula.android.bechef.R;
import com.paula.android.bechef.activities.BeChefActivity;
import com.paula.android.bechef.BaseContract;
import com.paula.android.bechef.BaseMainFragment;
import com.paula.android.bechef.dialog.EditTabAlertDialogBuilder;
import com.paula.android.bechef.discoverChild.DiscoverChildFragment;

import static com.google.android.gms.common.internal.Preconditions.checkNotNull;

public class DiscoverFragment extends BaseMainFragment
        implements BaseContract.BaseView<DiscoverPresenter> {
    private DiscoverPresenter mPresenter;

    public static DiscoverFragment newInstance() {
        return new DiscoverFragment();
    }

    @Override
    public void setCustomMainPresenter(DiscoverPresenter customMainPresenter) {
        mPresenter = checkNotNull(customMainPresenter);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.start();
    }

    @Override
    protected void editTab() {
        new EditTabAlertDialogBuilder(getContext(), mPresenter.getTabs()).create().show();
    }

    @Override
    protected void setRefreshButton(View view) {
        view.setVisibility(View.VISIBLE);
        view.setOnClickListener(this);
    }

    @Override
    protected void find() {
        ((BeChefActivity) getActivity()).showSearchUi(mPresenter);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.imagebutton_refresh) {
            Fragment childFragment = getChildFragment(getCurrentTabIndex());
            if (childFragment != null) {
                ((DiscoverChildFragment) childFragment).refresh();
            }
        }
    }
}