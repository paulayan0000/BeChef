package com.paula.android.bechef;

import android.net.Uri;

public interface BeChefContract {
    interface View extends BaseView<Presenter> {
        void showDiscoverUi();
        void showBookmarkUi();
        void showReceiptUi();
        void showTodayUi();
        void showMenuUi();
        void showBuyUi();
    }
    interface Presenter extends BasePresenter{
        void transToDiscover();
        void transToBookmark();
        void transToReceipt();
        void transToToday();
        void transToDetail(Uri uri);

        // Today title onClick method
        void transToMenuList();
        void transToBuyList();

    }
}
