package com.paula.android.bechef.bookmark;

import com.paula.android.bechef.data.LoadDataTask;
import com.paula.android.bechef.data.LoadDataCallback;
import com.paula.android.bechef.data.dao.BookmarkTabDao;
import com.paula.android.bechef.data.database.BookmarkTabDatabase;
import java.util.ArrayList;
import androidx.room.RoomDatabase;

import static com.google.android.gms.common.internal.Preconditions.checkNotNull;

public class BookmarkPresenter implements BookmarkContract.Presenter {
    private final BookmarkContract.View mBookmarkView;
    private ArrayList<String> mTabTitles = new ArrayList<>();

    public BookmarkPresenter(BookmarkContract.View bookmarkView) {
        mBookmarkView = checkNotNull(bookmarkView, "bookmarkView cannot be null!");
        mBookmarkView.setPresenter(this);
    }

    @Override
    public void start() {
        loadBookmarkTabs();
    }

    public void loadBookmarkTabs() {
        mTabTitles.clear();
        BookmarkTabDatabase db = BookmarkTabDatabase.getInstance(mBookmarkView.getContext());
        LoadDataTask loadDataTask = new LoadDataTask(db, new LoadDataCallback() {
            private ArrayList<String> mGotTabTitles;

            @Override
            public void doInBackground(RoomDatabase database) {
                BookmarkTabDao bookmarkTabDao = ((BookmarkTabDatabase) database).bookmarkDao();
                mGotTabTitles = new ArrayList<>(bookmarkTabDao.getAllTabTitles());
            }

            @Override
            public void onCompleted() {
                mTabTitles.addAll(mGotTabTitles);
                mBookmarkView.showDefaultUi(mTabTitles);
            }
        });
        loadDataTask.execute();
    }
}
