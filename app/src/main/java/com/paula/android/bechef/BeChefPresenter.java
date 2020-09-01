package com.paula.android.bechef;

import com.paula.android.bechef.bookmark.BookmarkFragment;
import com.paula.android.bechef.bookmark.BookmarkPresenter;
import com.paula.android.bechef.detail.DetailFragment;
import com.paula.android.bechef.detail.DetailPresenter;
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
    private final BeChefContract.View mMainView;
    private FragmentManager mFragmentManager;

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({
            DISCOVER, BOOKMARK, RECEIPT, DETAIL
    })
    @interface FragmentType {
    }

    static final String DISCOVER = "DISCOVER";
    static final String BOOKMARK = "BOOKMARK";
    static final String RECEIPT = "RECEIPT";
    static final String DETAIL = "DETAIL";

    private DiscoverFragment mDiscoverFragment;
    private BookmarkFragment mBookmarkFragment;
    private ReceiptFragment mReceiptFragment;

    private DiscoverPresenter mDiscoverPresenter;
    private BookmarkPresenter mBookmarkPresenter;
    private ReceiptPresenter mReceiptPresenter;

    public BeChefPresenter(BeChefContract.View mainView, FragmentManager fragmentManager) {
        mMainView = checkNotNull(mainView, "mainView cannot be null!");
        mMainView.setPresenter(this);
        mFragmentManager = fragmentManager;
    }

    @FragmentType
    @Override
    public void transToDiscover() {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();

//        if (mFragmentManager.findFragmentByTag(DETAIL) != null) mFragmentManager.popBackStack();
        if (mDiscoverFragment == null) mDiscoverFragment = DiscoverFragment.newInstance();
        if (mBookmarkFragment != null) transaction.hide(mBookmarkFragment);
        if (mReceiptFragment != null) transaction.hide(mReceiptFragment);
        if (!mDiscoverFragment.isAdded()) {
            transaction.add(R.id.linearlayout_main_container, mDiscoverFragment, DISCOVER);
        } else {
            transaction.show(mDiscoverFragment);
        }
        transaction.commit();

        if (mDiscoverPresenter == null) {
            mDiscoverPresenter = new DiscoverPresenter(mDiscoverFragment);
        }
        mMainView.setMenuId(R.id.navigation_discover);
    }

    @FragmentType
    @Override
    public void transToBookmark() {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();

//        if (mFragmentManager.findFragmentByTag(DETAIL) != null) mFragmentManager.popBackStack();
        if (mBookmarkFragment == null) mBookmarkFragment = BookmarkFragment.newInstance();
        if (mDiscoverFragment != null) transaction.hide(mDiscoverFragment);
        if (mReceiptFragment != null) transaction.hide(mReceiptFragment);
        if (!mBookmarkFragment.isAdded()) {
            transaction.add(R.id.linearlayout_main_container, mBookmarkFragment, BOOKMARK);
        } else {
            transaction.show(mBookmarkFragment);
        }
        transaction.commit();

        if (mBookmarkPresenter == null) {
            mBookmarkPresenter = new BookmarkPresenter(mBookmarkFragment);
        }
        mMainView.setMenuId(R.id.navigation_bookmark);
    }

    @FragmentType
    @Override
    public void transToReceipt() {

        FragmentTransaction transaction = mFragmentManager.beginTransaction();

//        if (mFragmentManager.findFragmentByTag(DETAIL) != null) mFragmentManager.popBackStack();
        if (mReceiptFragment == null) mReceiptFragment = ReceiptFragment.newInstance();
        if (mDiscoverFragment != null) transaction.hide(mDiscoverFragment);
        if (mBookmarkFragment != null) transaction.hide(mBookmarkFragment);
        if (!mReceiptFragment.isAdded()) {
            transaction.add(R.id.linearlayout_main_container, mReceiptFragment, RECEIPT);
        } else {
            transaction.show(mReceiptFragment);
        }
        transaction.commit();

        if (mReceiptPresenter == null) {
            mReceiptPresenter = new ReceiptPresenter(mReceiptFragment);
        }
        mMainView.setMenuId(R.id.navigation_receipt);
    }

    @FragmentType
    @Override
    public void transToDetail(Object content, boolean isBottomShown) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();

        if (mDiscoverFragment != null && !mDiscoverFragment.isHidden()) {
            transaction.hide(mDiscoverFragment);
            transaction.addToBackStack(DISCOVER);
        }
        if (mBookmarkFragment != null && !mBookmarkFragment.isHidden()) {
            transaction.hide(mBookmarkFragment);
            transaction.addToBackStack(BOOKMARK);
        }
        if (mReceiptFragment != null && !mReceiptFragment.isHidden()) {
            transaction.hide(mReceiptFragment);
            transaction.addToBackStack(RECEIPT);
        }
        DetailFragment detailFragment = DetailFragment.newInstance(isBottomShown);
        transaction.add(R.id.linearlayout_main_container, detailFragment, DETAIL);
        transaction.commit();

        DetailPresenter detailPresenter = new DetailPresenter(detailFragment, content);
    }

    @Override
    public void showToolbar(int itemId) {
        switch (itemId) {
            case R.id.navigation_discover:
                mDiscoverFragment.setToolbar();
                break;
            case R.id.navigation_bookmark:
                mBookmarkFragment.setToolbar(true);
                break;
            case R.id.navigation_receipt:
                mReceiptFragment.setToolbar(true);
                break;
            default:
        }
    }

    @Override
    public void start() {
        transToDiscover();
    }
}
