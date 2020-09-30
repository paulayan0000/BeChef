package com.paula.android.bechef.bookmark;

import com.paula.android.bechef.customMain.CustomMainFragment;
import com.paula.android.bechef.customMain.CustomMainPresenter;
import com.paula.android.bechef.data.LoadDataCallback;
import com.paula.android.bechef.data.LoadDataTask;
import com.paula.android.bechef.data.dao.BookmarkTabDao;
import com.paula.android.bechef.data.database.TabDatabase;
import com.paula.android.bechef.data.entity.BookmarkItem;
import com.paula.android.bechef.data.entity.BookmarkTab;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.Observer;

public class BookmarkPresenter extends CustomMainPresenter<BookmarkTab, BookmarkItem> {
    private ArrayList<BookmarkTab> mBookmarkTabs = new ArrayList<>();

    public BookmarkPresenter(CustomMainFragment<BookmarkTab, BookmarkItem> customView) {
        super(customView);
    }

    @Override
    public void start() {
        loadBookmarkTabs();
    }

    private void loadBookmarkTabs() {
        mBookmarkTabs.clear();
        TabDatabase.getBookmarkInstance(mCustomView.getContext()).bookmarkDao().getAllLive()
                .observe(mCustomView, new Observer<List<BookmarkTab>>() {
                    @Override
                    public void onChanged(List<BookmarkTab> bookmarkTabs) {
                        mBookmarkTabs.addAll(bookmarkTabs);
                        setTabs(mBookmarkTabs);
                        mCustomView.showDefaultUi(new ArrayList<>(bookmarkTabs));
                    }
                });
//        new LoadDataTask<>(new LoadDataCallback<BookmarkTabDao>() {
//            private ArrayList<BookmarkTab> mGotBookmarkTabs;
//
//            @Override
//            public BookmarkTabDao getDao() {
//                return TabDatabase.getBookmarkInstance(mCustomView.getContext()).bookmarkDao();
//            }
//
//            @Override
//            public void doInBackground(BookmarkTabDao bookmarkTabDao) {
//                mGotBookmarkTabs = new ArrayList<>(bookmarkTabDao.getAll());
//            }
//
//            @Override
//            public void onCompleted() {
//                mBookmarkTabs.addAll(mGotBookmarkTabs);
//                setTabs(mBookmarkTabs);
//                mCustomView.showDefaultUi(mGotBookmarkTabs);
//            }
//        }).execute();
    }
}
