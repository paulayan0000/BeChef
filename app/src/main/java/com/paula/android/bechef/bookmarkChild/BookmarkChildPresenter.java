package com.paula.android.bechef.bookmarkChild;

import com.paula.android.bechef.data.LoadDataCallback;
import com.paula.android.bechef.data.LoadDataTask;
import com.paula.android.bechef.data.dao.BookmarkItemDao;
import com.paula.android.bechef.data.database.BookmarkItemDatabase;
import com.paula.android.bechef.data.entity.BookmarkItem;
import java.util.ArrayList;
import androidx.room.RoomDatabase;

import static com.google.android.gms.common.internal.Preconditions.checkNotNull;

public class BookmarkChildPresenter implements BookmarkChildFragmentContract.Presenter {
    private BookmarkChildFragmentContract.View mBookmarkChildFragmentView;
    private int mTabIndex;
    private LoadDataTask mLoadDataTask;
    private  ArrayList<BookmarkItem> mBookmarkItems = new ArrayList<>();

    BookmarkChildPresenter(BookmarkChildFragmentContract.View bookmarkChildFragmentView, int tabIndex) {
        mBookmarkChildFragmentView = checkNotNull(bookmarkChildFragmentView, "bookmarkChildView cannot be null!");
        bookmarkChildFragmentView.setPresenter(this);
        mTabIndex = tabIndex;
    }

    @Override
    public void start() {
        BookmarkItemDatabase db = BookmarkItemDatabase.getInstance(mBookmarkChildFragmentView.getContext());
        mLoadDataTask = new LoadDataTask(db, new LoadDataCallback() {
            private ArrayList<BookmarkItem> mGotBookmarkItems;

            @Override
            public void doInBackground(RoomDatabase database) {
                BookmarkItemDao bookmarkItemDao = ((BookmarkItemDatabase) database).bookmarkDao();
                mGotBookmarkItems = new ArrayList<>(bookmarkItemDao.getAllWithTab(mTabIndex));
            }
            @Override
            public void onCompleted() {
                mBookmarkItems.addAll(mGotBookmarkItems);
                mBookmarkChildFragmentView.updateData(mBookmarkItems);
            }
        });
        mLoadDataTask.execute();
    }

    @Override
    public void scrollTo(int position) {
        mBookmarkChildFragmentView.scrollViewTo(position);
    }

    @Override
    public void openDetail(Object content) {
        mBookmarkChildFragmentView.showDetailUi(content);
    }
}
