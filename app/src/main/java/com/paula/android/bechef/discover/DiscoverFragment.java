package com.paula.android.bechef.discover;

import android.os.Bundle;
import android.view.View;

import com.paula.android.bechef.BaseContract;
import com.paula.android.bechef.BaseMainFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static com.google.android.gms.common.internal.Preconditions.checkNotNull;

public class DiscoverFragment extends BaseMainFragment implements BaseContract.BaseView<DiscoverPresenter> {
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
}
