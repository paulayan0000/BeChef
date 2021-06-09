package com.paula.android.bechef.bookmarkChild;

import androidx.fragment.app.Fragment;

import com.paula.android.bechef.customChild.CustomChildFragment;
import com.paula.android.bechef.data.entity.BookmarkItem;

public class BookmarkChildFragment extends CustomChildFragment<BookmarkItem> {
    public BookmarkChildFragment(long tabUid, Fragment fragment) {
        super(fragment);
        mCustomChildPresenter = new BookmarkChildPresenter(this, tabUid);
    }

    public static BookmarkChildFragment newInstance(long tabUid, Fragment fragment) {
        return new BookmarkChildFragment(tabUid, fragment);
    }
}