package com.paula.android.bechef.bookmark;

import com.paula.android.bechef.R;
import com.paula.android.bechef.action.ActionChooseFragment;
import com.paula.android.bechef.data.LoadDataCallback;
import com.paula.android.bechef.data.LoadDataTask;
import com.paula.android.bechef.data.dao.BookmarkTabDao;
import com.paula.android.bechef.data.database.TabDatabase;
import com.paula.android.bechef.data.entity.BookmarkItem;
import com.paula.android.bechef.data.entity.BookmarkTab;

import java.util.ArrayList;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import static com.google.android.gms.common.internal.Preconditions.checkNotNull;

public class BookmarkPresenter implements BookmarkContract.Presenter {
    private final BookmarkContract.View mBookmarkView;
//    private ArrayList<String> mTabTitles = new ArrayList<>();
    private ArrayList<BookmarkTab> mBookmarkTabs = new ArrayList<>();
    private ActionChooseFragment mActionChooseFragment;
    private FragmentTransaction mTransaction;

    public BookmarkPresenter(BookmarkContract.View bookmarkView) {
        mBookmarkView = checkNotNull(bookmarkView, "bookmarkView cannot be null!");
        mBookmarkView.setPresenter(this);
    }

    @Override
    public void start() {
        loadBookmarkTabs();
    }

    private void loadBookmarkTabs() {
//        mTabTitles.clear();
        mBookmarkTabs.clear();
        new LoadDataTask<>(new LoadDataCallback<BookmarkTabDao>() {
//            private ArrayList<String> mGotTabTitles;
            private ArrayList<BookmarkTab> mGotBookmarkTabs;

            @Override
            public BookmarkTabDao getDao() {
                return TabDatabase.getBookmarkInstance(mBookmarkView.getContext()).bookmarkDao();
            }

            @Override
            public void doInBackground(BookmarkTabDao bookmarkTabDao) {
//                mGotTabTitles = new ArrayList<>(bookmarkTabDao.getAllTabTitles());
                mGotBookmarkTabs = new ArrayList<>(bookmarkTabDao.getAll());
            }

            @Override
            public void onCompleted() {
//                mTabTitles.addAll(mGotTabTitles);
                mBookmarkTabs.addAll(mGotBookmarkTabs);
//                mBookmarkView.showDefaultUi(mTabTitles);
                mBookmarkView.showDefaultUi(mGotBookmarkTabs);
            }
        }).execute();
    }

    @Override
    public void transToAction(boolean isTrans, FragmentManager fragmentManager) {
        if (fragmentManager != null) mTransaction = fragmentManager.beginTransaction();

        if (isTrans && mActionChooseFragment != null) {
            mTransaction.show(mActionChooseFragment).commit();
        } else if (isTrans) {
            mActionChooseFragment = new ActionChooseFragment<>(this);
            mTransaction.add(R.id.toolbar, mActionChooseFragment).commit();
        } else if (mActionChooseFragment != null) {
            mTransaction.hide(mActionChooseFragment).commit();
        }
    }

    @Override
    public void leaveChooseDialog() {
        mBookmarkView.showSelectable(false);
    }

    @Override
    public int getChosenItemsCount() {
        return getChosenItems().size();
    }

    @Override
    public ArrayList<BookmarkItem> getChosenItems() {
        return mBookmarkView.getChosenItems();
    }

    @Override
    public ArrayList<?> getOtherTabs() {
        ArrayList<BookmarkTab> otherBookmarkItems = new ArrayList<>(mBookmarkTabs);
        otherBookmarkItems.remove(getCurrentTabIndex());
        return otherBookmarkItems;
    }

    @Override
    public void refreshCurrentData() {
        mBookmarkView.refreshCurrentUi();
    }

    @Override
    public void refreshData(int tabIndex) {
        mBookmarkView.refreshUi(tabIndex);
    }

    @Override
    public int getCurrentTabIndex() {
        return mBookmarkView.getCurrentTabIndex();
    }
}
