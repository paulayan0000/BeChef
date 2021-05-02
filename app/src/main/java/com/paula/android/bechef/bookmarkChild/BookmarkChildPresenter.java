package com.paula.android.bechef.bookmarkChild;

import androidx.lifecycle.LiveData;

import com.paula.android.bechef.customChild.CustomChildPresenter;
import com.paula.android.bechef.data.database.ItemDatabase;
import com.paula.android.bechef.data.entity.BookmarkItem;
import com.paula.android.bechef.utils.Constants;

import java.util.List;

public class BookmarkChildPresenter extends CustomChildPresenter<BookmarkItem> {

    BookmarkChildPresenter(BookmarkChildFragment bookmarkChildFragment, long tabUid) {
        super(bookmarkChildFragment, tabUid);
    }

    @Override
    public void loadSpecificItems(int type) {
        mDataFilterType = type;
        LiveData<List<BookmarkItem>> liveData;
        switch (mDataFilterType) {
            case Constants.FILTER_WITH_TIME_ASC:
                liveData = ItemDatabase.getItemInstance(getContext()).bookmarkDao()
                        .getAllWithTimeAscLive(mTabUid);
                break;
            case Constants.FILTER_WITH_TIME_DESC:
                liveData = ItemDatabase.getItemInstance(getContext()).bookmarkDao()
                        .getAllWithTimeDescLive(mTabUid);
                break;
            case Constants.FILTER_WITH_RATING_ASC:
                liveData = ItemDatabase.getItemInstance(getContext()).bookmarkDao()
                        .getAllWithRatingAscLive(mTabUid);
                break;
            case Constants.FILTER_WITH_RATING_DESC:
                liveData = ItemDatabase.getItemInstance(getContext()).bookmarkDao()
                        .getAllWithRatingDescLive(mTabUid);
                break;
            default:
                return;
        }
        addDataObserver(liveData);
    }
}
