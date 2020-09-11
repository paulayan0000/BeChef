package com.paula.android.bechef.bookmarkChild;

import com.paula.android.bechef.customChild.CustomChildPresenter;
import com.paula.android.bechef.data.LoadDataCallback;
import com.paula.android.bechef.data.LoadDataTask;
import com.paula.android.bechef.data.dao.BookmarkItemDao;
import com.paula.android.bechef.data.database.ItemDatabase;
import com.paula.android.bechef.data.entity.BaseTab;
import com.paula.android.bechef.data.entity.BookmarkItem;
import com.paula.android.bechef.utils.Constants;

import java.util.ArrayList;

public class BookmarkChildPresenter extends CustomChildPresenter<BookmarkItem> {
    BookmarkChildPresenter(BookmarkChildFragment bookmarkChildFragment, BaseTab baseTab) {
        super(bookmarkChildFragment, baseTab);
    }

    @Override
    public void loadSpecificItems(int type) {
        mDataFilterType = type;
        new LoadDataTask<>(new LoadDataCallback<BookmarkItemDao>() {
            @Override
            public BookmarkItemDao getDao() {
                return ItemDatabase.getBookmarkInstance(mCustomChildFragment.getContext()).bookmarkDao();
            }

            @Override
            public void doInBackground(BookmarkItemDao bookmarkItemDao) {
                switch (mDataFilterType) {
                    case Constants.FILTER_WITH_TIME_ASC:
                        mDataArrayList = new ArrayList<>(bookmarkItemDao.getAllWithTimeAsc(mTabUid));
                        break;
                    case Constants.FILTER_WITH_RATING_DESC:
                        mDataArrayList = new ArrayList<>(bookmarkItemDao.getAllWithRatingDesc(mTabUid));
                        break;
                    case Constants.FILTER_WITH_RATING_ASC:
                        mDataArrayList = new ArrayList<>(bookmarkItemDao.getAllWithRatingAsc(mTabUid));
                        break;
                    default:
                    case Constants.FILTER_WITH_TIME_DESC:
                        mDataArrayList = new ArrayList<>(bookmarkItemDao.getAllWithTimeDesc(mTabUid));
                        break;
                }
            }

            @Override
            public void onCompleted() {
                mCustomChildFragment.updateItems(mDataArrayList);
            }
        }).execute();
    }
}
