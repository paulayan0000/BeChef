package com.paula.android.bechef;

import android.net.Uri;

import java.util.ArrayList;

public interface BeChefContract {
    interface View extends BaseView<Presenter> {
        void showDiscoverUi(ArrayList<String> tabTitles, ArrayList<String> recyclerViewContents);
        void showBookmarkUi(ArrayList<String> tabTitles, ArrayList<String> recyclerViewCotents);
        void showReceiptUi(ArrayList<String> tabTitles, ArrayList<String> recyclerViewContents);
        void showTodayUi();

        void showMenuListUi(ArrayList<String> tabTitles, ArrayList<String> recyclerViewContents);
        void showBuyListUi(ArrayList<String> tabTitles, ArrayList<String> recyclerViewContents);
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
