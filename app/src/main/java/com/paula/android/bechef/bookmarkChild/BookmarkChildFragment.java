package com.paula.android.bechef.bookmarkChild;

import com.paula.android.bechef.customChild.CustomChildFragment;
import com.paula.android.bechef.customMain.CustomMainFragment;
import com.paula.android.bechef.data.entity.BookmarkItem;
import com.paula.android.bechef.data.entity.BookmarkTab;
import androidx.fragment.app.Fragment;

public class BookmarkChildFragment extends CustomChildFragment<BookmarkItem> {
    private BookmarkChildFragment(BookmarkTab bookmarkTab, Fragment fragment) {
        mCustomChildPresenter = new BookmarkChildPresenter(this, bookmarkTab);
        mCustomMainFragment = (CustomMainFragment) fragment;
    }

    public static BookmarkChildFragment newInstance(BookmarkTab bookmarkTab, Fragment fragment) {
        return new BookmarkChildFragment(bookmarkTab, fragment);
    }
}
