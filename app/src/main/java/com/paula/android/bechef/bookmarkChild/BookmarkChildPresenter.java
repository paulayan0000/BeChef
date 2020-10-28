package com.paula.android.bechef.bookmarkChild;

import com.paula.android.bechef.customChild.CustomChildPresenter;
import com.paula.android.bechef.data.database.ItemDatabase;
import com.paula.android.bechef.data.entity.BaseTab;
import com.paula.android.bechef.data.entity.BookmarkItem;
import com.paula.android.bechef.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

public class BookmarkChildPresenter extends CustomChildPresenter<BookmarkItem> {
    BookmarkChildPresenter(BookmarkChildFragment bookmarkChildFragment, BaseTab baseTab) {
        super(bookmarkChildFragment, baseTab);
    }

    @Override
    public void loadSpecificItems(int type) {
        mDataFilterType = type;
        switch (mDataFilterType) {
            case Constants.FILTER_WITH_TIME_ASC:
                ItemDatabase.getBookmarkInstance(mCustomChildFragment.getContext()).bookmarkDao()
                        .getAllWithTimeAscLive(mTabUid).observe((LifecycleOwner) mCustomChildFragment, new Observer<List<BookmarkItem>>() {
                    @Override
                    public void onChanged(List<BookmarkItem> bookmarkItems) {
                        mCustomChildFragment.updateItems(new ArrayList<>(bookmarkItems));
                    }
                });
                break;
            case Constants.FILTER_WITH_TIME_DESC:
                ItemDatabase.getBookmarkInstance(mCustomChildFragment.getContext()).bookmarkDao()
                        .getAllWithTimeDescLive(mTabUid).observe((LifecycleOwner) mCustomChildFragment, new Observer<List<BookmarkItem>>() {
                    @Override
                    public void onChanged(List<BookmarkItem> bookmarkItems) {
                        mCustomChildFragment.updateItems(new ArrayList<>(bookmarkItems));
                    }
                });
                break;
            case Constants.FILTER_WITH_RATING_ASC:
                ItemDatabase.getBookmarkInstance(mCustomChildFragment.getContext()).bookmarkDao()
                        .getAllWithRatingAscLive(mTabUid).observe((LifecycleOwner) mCustomChildFragment, new Observer<List<BookmarkItem>>() {
                    @Override
                    public void onChanged(List<BookmarkItem> bookmarkItems) {
                        mCustomChildFragment.updateItems(new ArrayList<>(bookmarkItems));
                    }
                });
                break;
            case Constants.FILTER_WITH_RATING_DESC:
                ItemDatabase.getBookmarkInstance(mCustomChildFragment.getContext()).bookmarkDao()
                        .getAllWithRatingDescLive(mTabUid).observe((LifecycleOwner) mCustomChildFragment, new Observer<List<BookmarkItem>>() {
                    @Override
                    public void onChanged(List<BookmarkItem> bookmarkItems) {
                        mCustomChildFragment.updateItems(new ArrayList<>(bookmarkItems));
                    }
                });
                break;
        }
    }
}
