package com.paula.android.bechef;

import android.util.Log;

import com.paula.android.bechef.bookmark.BookmarkFragment;
import com.paula.android.bechef.bookmark.BookmarkPresenter;
import com.paula.android.bechef.data.entity.BaseTab;
import com.paula.android.bechef.detail.DetailFragment;
import com.paula.android.bechef.detail.DetailPresenter;
import com.paula.android.bechef.discover.DiscoverFragment;
import com.paula.android.bechef.discover.DiscoverPresenter;
import com.paula.android.bechef.receipt.ReceiptFragment;
import com.paula.android.bechef.receipt.ReceiptPresenter;
import com.paula.android.bechef.search.SearchFragment;
import com.paula.android.bechef.search.SearchPresenter;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;

import androidx.annotation.StringDef;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import static com.google.android.gms.common.internal.Preconditions.checkNotNull;

public class BeChefPresenter implements BeChefContract.Presenter {
    private final BeChefContract.View mMainView;
    private FragmentManager mFragmentManager;

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({
            DISCOVER, BOOKMARK, RECEIPT, DETAIL, SEARCH
    })
    @interface FragmentType {
    }

    static final String DISCOVER = "DISCOVER";
    static final String BOOKMARK = "BOOKMARK";
    static final String RECEIPT = "RECEIPT";
    static final String DETAIL = "DETAIL";
    static final String SEARCH = "SEARCH";

    private DiscoverFragment mDiscoverFragment;
    private BookmarkFragment mBookmarkFragment;
    private ReceiptFragment mReceiptFragment;
    private DetailFragment mDetailFragment;
    private SearchFragment mSearchFragment;

    private DiscoverPresenter mDiscoverPresenter;
    private BookmarkPresenter mBookmarkPresenter;
    private ReceiptPresenter mReceiptPresenter;
    private DetailPresenter mDetailPresenter;
    private SearchPresenter mSearchPresenter;

    public BeChefPresenter(BeChefContract.View mainView, FragmentManager fragmentManager) {
        mMainView = checkNotNull(mainView, "mainView cannot be null!");
        mMainView.setPresenter(this);
        mFragmentManager = fragmentManager;
    }

    @FragmentType
    @Override
    public void transToDiscover() {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();

        if (mFragmentManager.findFragmentByTag(DETAIL) != null) {
            transaction.remove(mDetailFragment);
            mFragmentManager.popBackStack();
        }
        if (mFragmentManager.findFragmentByTag(SEARCH) != null) {
            transaction.remove(mSearchFragment);
            mFragmentManager.popBackStack();
        }

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

        if (mFragmentManager.findFragmentByTag(DETAIL) != null) {
//            Log.d("BechefPresenter", "1");
//            int count = mFragmentManager.getBackStackEntryCount();
//            StringBuilder fragmentName = new StringBuilder();
//            for (int i = 0; i < count; i++) {
//                fragmentName.append(mFragmentManager.getBackStackEntryAt(i));
//            }
//            Log.d("BechefPresenter", String.valueOf(fragmentName));
            transaction.remove(mDetailFragment);
            mFragmentManager.popBackStack();
        }
        if (mFragmentManager.findFragmentByTag(SEARCH) != null) {
            transaction.remove(mSearchFragment);
            mFragmentManager.popBackStack();
        }

//        StringBuilder fragmentName = new StringBuilder();
//        for (Fragment fragment :mFragmentManager.getFragments()) {
//            fragmentName.append(fragment.getTag());
//        }
//        Log.d("BechefPresenter", String.valueOf(fragmentName));

        if (mBookmarkFragment == null) {
            Log.d("BechefPresenter", "2");
            mBookmarkFragment = BookmarkFragment.newInstance();
        }
        if (mDiscoverFragment != null) {
            Log.d("BechefPresenter", "3");
            transaction.hide(mDiscoverFragment);
        }
        if (mReceiptFragment != null) {
            Log.d("BechefPresenter", "4");
            transaction.hide(mReceiptFragment);
        }
        if (!mBookmarkFragment.isAdded()) {
            Log.d("BechefPresenter", "5");
            transaction.add(R.id.linearlayout_main_container, mBookmarkFragment, BOOKMARK);
        } else {
            Log.d("BechefPresenter", "6");
            transaction.show(mBookmarkFragment);
        }
        transaction.commit();

        if (mBookmarkPresenter == null) {
            Log.d("BechefPresenter", "7");
            mBookmarkPresenter = new BookmarkPresenter(mBookmarkFragment);
        }
        mMainView.setMenuId(R.id.navigation_bookmark);
    }

    @FragmentType
    @Override
    public void transToReceipt() {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();

        if (mFragmentManager.findFragmentByTag(DETAIL) != null) {
            transaction.remove(mDetailFragment);
            mFragmentManager.popBackStack();
        }
        if (mFragmentManager.findFragmentByTag(SEARCH) != null) {
            transaction.remove(mSearchFragment);
            mFragmentManager.popBackStack();
        }

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

        if (mDetailFragment == null) {
            mDetailFragment = DetailFragment.newInstance();
        }
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
        if (mSearchFragment != null && !mSearchFragment.isHidden()) {
            transaction.hide(mSearchFragment);
            transaction.addToBackStack(SEARCH);
        }

        if (!mDetailFragment.isAdded()) {
            transaction.add(R.id.linearlayout_main_container, mDetailFragment, DETAIL);
        } else {
            transaction.show(mDetailFragment);
        }
        mDetailFragment.setBottomShown(isBottomShown);

        if (mDetailPresenter == null) mDetailPresenter = new DetailPresenter(mDetailFragment, content);
        else mDetailPresenter.setDataContent(content);
        transaction.commit();
    }

    @FragmentType
    @Override
    public void transToSearch(BaseContract.MainPresenter presenter) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();

        if (mFragmentManager.findFragmentByTag(DETAIL) != null) {
            transaction.remove(mDetailFragment);
            mFragmentManager.popBackStack();
        }

        if (mSearchFragment == null) mSearchFragment = SearchFragment.newInstance();
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
        if (!mSearchFragment.isAdded()) {
            transaction.add(R.id.linearlayout_main_container, mSearchFragment, SEARCH);
        } else {
            transaction.show(mSearchFragment);
        }

        if (presenter instanceof DiscoverPresenter) {
            if (mSearchPresenter == null) mSearchPresenter = new SearchPresenter(mSearchFragment);
            else mSearchPresenter.setBaseTabs(null);
        } else {
            if (mSearchPresenter == null) mSearchPresenter = new SearchPresenter(mSearchFragment, presenter.getTabs());
            else mSearchPresenter.setBaseTabs(presenter.getTabs());
        }
        mSearchFragment.initOldKeyword();
        transaction.commit();
    }

    @Override
    public void showToolbar(int itemId) {
        switch (itemId) {
            case R.id.navigation_discover:
                mDiscoverFragment.showToolbar();
                break;
            case R.id.navigation_bookmark:
                mBookmarkFragment.showToolbar();
                break;
            case R.id.navigation_receipt:
                mReceiptFragment.showToolbar();
                break;
            default:
        }
    }

    @Override
    public void start() {
        transToDiscover();
    }

//    public boolean isDetailShown() {
//        if (mDetailFragment != null && mDetailFragment.isVisible()) {
//            mFragmentManager.beginTransaction().remove(mDetailFragment).commit();
//            mFragmentManager.popBackStack();
//            return true;
//        }
//        return false;
//    }
}
