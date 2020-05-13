package com.paula.android.bechef;

import android.net.Uri;
import android.util.Log;

import java.util.ArrayList;
import androidx.fragment.app.FragmentManager;

import static com.google.android.gms.common.internal.Preconditions.checkNotNull;

public class BeChefPresenter implements BeChefContract.Presenter {

    private static final String LOG_TAG = BeChefPresenter.class.getSimpleName();
    private final BeChefContract.View mMainView;

    private ArrayList<String> mTabtitles = new ArrayList<>();
    private ArrayList<String> mRecyclerViewContents = new ArrayList<>();

    public BeChefPresenter(BeChefContract.View mainView, FragmentManager fragmentManager) {
        mMainView = checkNotNull(mainView, "mainView cannot be null!");
        mMainView.setPresenter(this);
    }

    @Override
    public void transToDiscover() {
        mTabtitles.clear();
        mTabtitles.add("discover one");
        mTabtitles.add("discover two");
        mTabtitles.add("discover three");
        mTabtitles.add("discover four");

        mRecyclerViewContents.clear();
        mRecyclerViewContents.add("discover content 1");
        mRecyclerViewContents.add("discover content 2");
        mRecyclerViewContents.add("discover content 3");
        mRecyclerViewContents.add("discover content 4");
        mRecyclerViewContents.add("discover content 5");
        mRecyclerViewContents.add("discover content 6");

        mMainView.showDiscoverUi(mTabtitles, mRecyclerViewContents);
    }

    @Override
    public void transToBookmark() {
        mTabtitles.clear();
        mTabtitles.add("bookmark one");
        mTabtitles.add("bookmark two");

        mRecyclerViewContents.clear();
        mRecyclerViewContents.add("bookmark content 1");
        mRecyclerViewContents.add("bookmark content 2");
        mRecyclerViewContents.add("bookmark content 3");

        mMainView.showBookmarkUi(mTabtitles, mRecyclerViewContents);
    }

    @Override
    public void transToReceipt() {
        mTabtitles.clear();
        mTabtitles.add("Receipt one");
        mTabtitles.add("Receipt two");
        mTabtitles.add("Receipt three");

        mRecyclerViewContents.clear();

        mMainView.showReceiptUi(mTabtitles, mRecyclerViewContents);
    }

    @Override
    public void transToToday() {
        transToMenuList();
        mMainView.showTodayUi();
    }

    @Override
    public void transToDetail(Uri uri) {
        Log.d(LOG_TAG, "Uri is: " + uri);
    }

    @Override
    public void transToMenuList() {
        mTabtitles.clear();
        mTabtitles.add("Menu one");
        mTabtitles.add("Menu two");

        mRecyclerViewContents.clear();

        mMainView.showMenuListUi(mTabtitles, mRecyclerViewContents);
    }

    @Override
    public void transToBuyList() {
        mTabtitles.clear();
        mTabtitles.add("Buy one");
        mTabtitles.add("Buy two");
        mTabtitles.add("Buy three");

        mRecyclerViewContents.clear();

        mMainView.showBuyListUi(mTabtitles, mRecyclerViewContents);
    }

    @Override
    public void start() {
        transToDiscover();
    }
}
