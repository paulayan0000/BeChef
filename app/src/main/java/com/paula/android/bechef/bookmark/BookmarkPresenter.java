package com.paula.android.bechef.bookmark;

import static com.google.android.gms.common.internal.Preconditions.checkNotNull;

public class BookmarkPresenter implements BookmarkContract.Presenter {

    private final BookmarkContract.View mBookmarkView;

    public BookmarkPresenter(BookmarkContract.View bookmarkView) {
        mBookmarkView = checkNotNull(bookmarkView, "bookmarkView cannot be null!");
        mBookmarkView.setPresenter(this);
    }

    @Override
    public void start() {

    }
}
