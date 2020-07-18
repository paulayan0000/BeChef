package com.paula.android.bechef;

import android.net.Uri;
import com.paula.android.bechef.bookmark.BookmarkFragment;
import com.paula.android.bechef.bookmark.BookmarkPresenter;
import com.paula.android.bechef.discover.DiscoverFragment;
import com.paula.android.bechef.discover.DiscoverPresenter;
import com.paula.android.bechef.receipt.ReceiptFragment;
import com.paula.android.bechef.receipt.ReceiptPresenter;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.StringDef;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import static com.google.android.gms.common.internal.Preconditions.checkNotNull;

public class BeChefPresenter implements BeChefContract.Presenter {

    private static final String LOG_TAG = BeChefPresenter.class.getSimpleName();
    private final BeChefContract.View mMainView;
    private FragmentManager mFragmentManager;

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({
            DISCOVER, BOOKMARK, RECEIPT, TODAY, DETAIL
    })
    @interface FragmentType {}
    static final String DISCOVER = "DISCOVER";
    static final String BOOKMARK = "BOOKMARK";
    static final String RECEIPT  = "RECEIPT";
    static final String TODAY    = "TODAY";
    static final String DETAIL   = "DETAIL";

    private DiscoverFragment mDiscoverFragment;
    private BookmarkFragment mBookmarkFragment;
    private ReceiptFragment mReceiptFragment;
//    private TodayFragment mTodayFragment;

    private DiscoverPresenter mDiscoverPresenter;
    private BookmarkPresenter mBookmarkPresenter;
    private ReceiptPresenter mReceiptPresenter;
//    private TodayPresenter mTodayPresenter;

    public BeChefPresenter(BeChefContract.View mainView, FragmentManager fragmentManager) {
        mMainView = checkNotNull(mainView, "mainView cannot be null!");
        mMainView.setPresenter(this);
        mFragmentManager = fragmentManager;
    }

    @FragmentType
    @Override
    public void transToDiscover() {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();

        if (mFragmentManager.findFragmentByTag(DETAIL) != null) mFragmentManager.popBackStack();
        if (mDiscoverFragment == null) mDiscoverFragment = DiscoverFragment.newInstance();
        if (mBookmarkFragment != null) transaction.hide(mBookmarkFragment);
        if (mReceiptFragment != null) transaction.hide(mReceiptFragment);
//        if (mTodayFragment != null) transaction.hide(mTodayFragment);
        if (!mDiscoverFragment.isAdded()) {
            transaction.add(R.id.linearlayout_main_container, mDiscoverFragment, DISCOVER);
        } else {
            transaction.show(mDiscoverFragment);
        }
        transaction.commit();

        if (mDiscoverPresenter == null) {
            mDiscoverPresenter = new DiscoverPresenter(mDiscoverFragment);
        }

        mMainView.showDiscoverUi();
    }

    @FragmentType
    @Override
    public void transToBookmark() {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();

        if (mFragmentManager.findFragmentByTag(DETAIL) != null) mFragmentManager.popBackStack();
        if (mBookmarkFragment == null) mBookmarkFragment = BookmarkFragment.newInstance();
        if (mDiscoverFragment != null) transaction.hide(mDiscoverFragment);
        if (mReceiptFragment != null) transaction.hide(mReceiptFragment);
//        if (mTodayFragment != null) transaction.hide(mTodayFragment);
        if (!mBookmarkFragment.isAdded()) {
            transaction.add(R.id.linearlayout_main_container, mBookmarkFragment, BOOKMARK);
        } else {
            transaction.show(mBookmarkFragment);
        }
        transaction.commit();

        if (mBookmarkPresenter == null) {
            mBookmarkPresenter = new BookmarkPresenter(mBookmarkFragment);
        }

        mMainView.showBookmarkUi();
    }

    @FragmentType
    @Override
    public void transToReceipt() {

        FragmentTransaction transaction = mFragmentManager.beginTransaction();

        if (mFragmentManager.findFragmentByTag(DETAIL) != null) mFragmentManager.popBackStack();
        if (mReceiptFragment == null) mReceiptFragment = ReceiptFragment.newInstance();
        if (mDiscoverFragment != null) transaction.hide(mDiscoverFragment);
        if (mBookmarkFragment != null) transaction.hide(mBookmarkFragment);
//        if (mTodayFragment != null) transaction.hide(mTodayFragment);
        if (!mReceiptFragment.isAdded()) {
            transaction.add(R.id.linearlayout_main_container, mReceiptFragment, RECEIPT);
        } else {
            transaction.show(mReceiptFragment);
        }
        transaction.commit();

        if (mReceiptPresenter == null) {
            mReceiptPresenter = new ReceiptPresenter(mReceiptFragment);
        }

        mMainView.showReceiptUi();
    }

    @FragmentType
    @Override
    public void transToToday() {
        transToMenuList();
        mMainView.showTodayUi();
    }

    @FragmentType
    @Override
    public void transToDetail(Uri uri) {
    }

    @FragmentType
    @Override
    public void transToMenuList() {
    }

    @FragmentType
    @Override
    public void transToBuyList() {
    }

    @Override
    public void start() {
        transToDiscover();
    }
}
