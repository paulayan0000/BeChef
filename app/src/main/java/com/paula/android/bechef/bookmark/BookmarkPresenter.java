package com.paula.android.bechef.bookmark;

import com.paula.android.bechef.api.beans.GetSearchList;

import java.util.ArrayList;

import static com.google.android.gms.common.internal.Preconditions.checkNotNull;

public class BookmarkPresenter implements BookmarkContract.Presenter {

    private final BookmarkContract.View mBookmarkView;
    private ArrayList<String> mTabtitles = new ArrayList<>();
    private ArrayList<GetSearchList> mBookmarkItems = new ArrayList<>();

    public BookmarkPresenter(BookmarkContract.View bookmarkView) {
        mBookmarkView = checkNotNull(bookmarkView, "bookmarkView cannot be null!");
        mBookmarkView.setPresenter(this);
    }

    @Override
    public void start() {
        mTabtitles.add("bookmark one");
        mTabtitles.add("bookmark two");
        GetSearchList tableOne = new GetSearchList();
        tableOne.setTableName("bookmark one");
        GetSearchList tableTwo = new GetSearchList();
        tableTwo.setTableName("bookmark two");
        mBookmarkItems.add(tableOne);
        mBookmarkItems.add(tableTwo);
        mBookmarkView.showDefaultUi(mTabtitles, mBookmarkItems);
    }
}
