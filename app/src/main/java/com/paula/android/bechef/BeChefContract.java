package com.paula.android.bechef;

public interface BeChefContract extends BaseContract{
    interface View extends BaseView<Presenter> {
        void showDiscoverUi();
        void showBookmarkUi();
        void showReceiptUi();
    }
    interface Presenter extends BasePresenter{
        void transToDiscover();
        void transToBookmark();
        void transToReceipt();
        void transToDetail(Object content);

        Boolean isDetailShown();
    }
}
