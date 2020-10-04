package com.paula.android.bechef.bookmark;

import com.paula.android.bechef.customMain.CustomMainFragment;
import com.paula.android.bechef.customMain.CustomMainPresenter;
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
        TabDatabase.getBookmarkInstance(mCustomView.getContext()).bookmarkDao().getAllLive()
                .observe(mCustomView, new Observer<List<BookmarkTab>>() {
                    @Override
                    public void onChanged(List<BookmarkTab> bookmarkTabs) {
                        mBookmarkTabs.clear();
                        mBookmarkTabs.addAll(bookmarkTabs);
                        setTabs(mBookmarkTabs);
                        mCustomView.showDefaultUi(new ArrayList<>(bookmarkTabs));
                    }
                });
    }
}
