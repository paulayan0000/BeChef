package com.paula.android.bechef.discover;

import static com.google.android.gms.common.internal.Preconditions.checkNotNull;

public class DiscoverPresenter implements DiscoverContract.Presenter {

    private final DiscoverContract.View mDiscoverView;

    public DiscoverPresenter(DiscoverContract.View discoverView) {
        mDiscoverView = checkNotNull(discoverView, "discoverView cannot be null!");
        mDiscoverView.setPresenter(this);
    }

    @Override
    public void start() {

    }
}
