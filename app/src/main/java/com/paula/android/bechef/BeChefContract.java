package com.paula.android.bechef;

import android.net.Uri;

import com.paula.android.bechef.api.beans.GetSearchList;
import com.paula.android.bechef.objects.SearchItem;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

public interface BeChefContract {
    interface View extends BaseView<Presenter> {
        void showDiscoverUi(ArrayList<String> tabTitles, GetSearchList discoverContents);
        void showBookmarkUi(ArrayList<String> tabTitles, ArrayList<String> recyclerViewCotents);
        void showReceiptUi(ArrayList<String> tabTitles, ArrayList<String> recyclerViewContents);
        void showTodayUi();

        void showMenuListUi(ArrayList<String> tabTitles, ArrayList<String> recyclerViewContents);
        void showBuyListUi(ArrayList<String> tabTitles, ArrayList<String> recyclerViewContents);

        void updateSearchItems(GetSearchList searchContents);
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


        void onScrollStateChanged(int visibleItemCount, int totalItemCount, int newState);
        void onScrolled(RecyclerView.LayoutManager layoutManager);


    }
}
