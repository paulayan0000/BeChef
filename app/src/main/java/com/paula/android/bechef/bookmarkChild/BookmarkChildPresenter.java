package com.paula.android.bechef.bookmarkChild;

import static com.google.android.gms.common.internal.Preconditions.checkNotNull;

public class BookmarkChildPresenter implements BookmarkChildFragmentContract.Presenter {
    private static final String LOG_TAG = BookmarkChildPresenter.class.getSimpleName();

    private BookmarkChildFragmentContract.View mBookmarkChildFragmentView;
    private String mTableName = "";

    BookmarkChildPresenter(BookmarkChildFragmentContract.View bookmarkChildFragmentView, String tableName) {
        mBookmarkChildFragmentView = checkNotNull(bookmarkChildFragmentView, "mainFragmentView cannot be null!");
        bookmarkChildFragmentView.setPresenter(this);
        mTableName = tableName;
    }

    @Override
    public void start() {

    }

    @Override
    public void scrollTo(int position) {
        mBookmarkChildFragmentView.scrollViewTo(position);
    }
}
