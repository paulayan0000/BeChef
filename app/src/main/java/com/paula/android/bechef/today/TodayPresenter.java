package com.paula.android.bechef.today;

import static com.google.android.gms.common.internal.Preconditions.checkNotNull;

public class TodayPresenter implements TodayContract.Presenter {

    private final TodayContract.View mTodayView;

    public TodayPresenter(TodayContract.View todayView) {
        mTodayView = checkNotNull(todayView, "todayView cannot be null!");
        mTodayView.setPresenter(this);
    }

    @Override
    public void start() {

    }
}
