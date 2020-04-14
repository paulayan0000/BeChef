package com.paula.android.bechef;

import android.net.Uri;

public interface BeChefContract {
    interface View extends BaseView<Presenter> {
        void showDiscoverUi();
        void showBookmarkUi();
        void showReceiptUi();
        void showTodayUi();
    }
    interface Presenter extends BasePresenter{
        void transToDiscover();
        void transToBookmark();
        void transToReceipt();
        void transToToday();
        void transToDetail(Uri uri);
    }
}
