package com.paula.android.bechef.bookmarkChild;

import com.paula.android.bechef.data.LoadDataCallback;
import com.paula.android.bechef.data.LoadDataTask;
import com.paula.android.bechef.data.dao.BookmarkItemDao;
import com.paula.android.bechef.data.database.ItemDatabase;
import com.paula.android.bechef.data.entity.BaseTab;
import com.paula.android.bechef.utils.Constants;

import java.util.ArrayList;

import static com.google.android.gms.common.internal.Preconditions.checkNotNull;

public class BookmarkChildPresenter implements BookmarkChildFragmentContract.Presenter {
    private BookmarkChildFragmentContract.View mBookmarkChildFragmentView;
    private int mTabUid;
    private ArrayList<?> mBookmarkItems = new ArrayList<>();
    private int mDataFilterType = Constants.FILTER_WITH_TIME_DESC;

//    BookmarkChildPresenter(BookmarkChildFragmentContract.View bookmarkChildFragmentView, int tabIndex) {
//        mBookmarkChildFragmentView = checkNotNull(bookmarkChildFragmentView, "bookmarkChildView cannot be null!");
//        bookmarkChildFragmentView.setPresenter(this);
//        mTabIndex = tabIndex;
//    }
    BookmarkChildPresenter(BookmarkChildFragmentContract.View bookmarkChildFragmentView, BaseTab baseTab) {
        mBookmarkChildFragmentView = checkNotNull(bookmarkChildFragmentView, "bookmarkChildView cannot be null!");
        bookmarkChildFragmentView.setPresenter(this);
        mTabUid = baseTab.getUid();
    }

    @Override
    public void start() {
        loadSpecificItems(Constants.FILTER_WITH_TIME_DESC);
    }

    @Override
    public void loadSpecificItems(int type) {
        mDataFilterType = type;
        new LoadDataTask<>(new LoadDataCallback<BookmarkItemDao>() {
            @Override
            public BookmarkItemDao getDao() {
//                return BookmarkItemDatabase.getInstance(mBookmarkChildFragmentView.getContext()).bookmarkDao();
                return ItemDatabase.getBookmarkInstance(mBookmarkChildFragmentView.getContext()).bookmarkDao();
            }

            @Override
            public void doInBackground(BookmarkItemDao bookmarkItemDao) {
                switch (mDataFilterType) {
                    case Constants.FILTER_WITH_TIME_ASC:
                        mBookmarkItems = new ArrayList<>(bookmarkItemDao.getAllWithTimeAsc(mTabUid));
                        break;
                    case Constants.FILTER_WITH_RATING_DESC:
                        mBookmarkItems = new ArrayList<>(bookmarkItemDao.getAllWithRatingDesc(mTabUid));
                        break;
                    case Constants.FILTER_WITH_RATING_ASC:
                        mBookmarkItems = new ArrayList<>(bookmarkItemDao.getAllWithRatingAsc(mTabUid));
                        break;
                    default:
                    case Constants.FILTER_WITH_TIME_DESC:
                        mBookmarkItems = new ArrayList<>(bookmarkItemDao.getAllWithTimeDesc(mTabUid));
                        break;
                }
            }

            @Override
            public void onCompleted() {
                mBookmarkChildFragmentView.updateItems(mBookmarkItems);
            }
        }).execute();
    }

    @Override
    public void loadItems() {
        loadSpecificItems(mDataFilterType);
    }

    @Override
    public void openDetail(Object content, boolean isBottomShown) {
        mBookmarkChildFragmentView.showDetailUi(content, isBottomShown);
    }

    @Override
    public void transToSelectable() {
        mBookmarkChildFragmentView.showSelectableUi(true);
    }
}
