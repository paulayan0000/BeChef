package com.paula.android.bechef.data.database;

import android.content.Context;
import android.os.AsyncTask;
import com.paula.android.bechef.data.dao.BookmarkTabDao;
import com.paula.android.bechef.data.entity.BookmarkTab;
import java.util.ArrayList;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {BookmarkTab.class}, version = 1, exportSchema = false)
public abstract class BookmarkTabDatabase extends RoomDatabase {
    public abstract BookmarkTabDao bookmarkDao();

    private static volatile BookmarkTabDatabase mBookmarkTabDatabaseInstance;

    public static BookmarkTabDatabase getInstance(Context context) {
        if (mBookmarkTabDatabaseInstance == null) {
            synchronized (BookmarkTabDatabase.class) {
                if (mBookmarkTabDatabaseInstance == null) {
                    mBookmarkTabDatabaseInstance = Room.databaseBuilder(context,
                            BookmarkTabDatabase.class,
                            "bookmark-tab-database")
                            .build();
                    // add default data
                    BookmarkTab bookmarkTab1 = new BookmarkTab(0,
                            "未命名書籤");
                    final ArrayList<BookmarkTab> bookmarkTabs = new ArrayList<>();
                    bookmarkTabs.add(bookmarkTab1);

                    // TODO: remove fake default data
                    BookmarkTab bookmarkTab2 = new BookmarkTab(1,
                            "Default tab2");
                    bookmarkTabs.add(bookmarkTab2);

                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... voids) {
                            for (int i = 0; i < bookmarkTabs.size(); i++) {
                                mBookmarkTabDatabaseInstance.bookmarkDao().insert(bookmarkTabs);
                            }
                            return null;
                        }
                    }.execute();
                }
            }
        }
        return mBookmarkTabDatabaseInstance;
    }
}
