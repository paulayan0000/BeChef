package com.paula.android.bechef.data.database;

import android.content.Context;
import android.os.AsyncTask;
import com.paula.android.bechef.data.entity.BookmarkItem;
import com.paula.android.bechef.data.dao.BookmarkItemDao;
import java.util.ArrayList;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {BookmarkItem.class}, version = 1, exportSchema = false)
public abstract class BookmarkItemDatabase extends RoomDatabase {
    public abstract BookmarkItemDao bookmarkDao();

    private static volatile BookmarkItemDatabase mBookmarkItemDatabaseInstance;

    public static BookmarkItemDatabase getInstance(Context context) {
        if (mBookmarkItemDatabaseInstance == null) {
            synchronized (BookmarkItemDatabase.class) {
                if (mBookmarkItemDatabaseInstance == null) {
                    mBookmarkItemDatabaseInstance = Room.databaseBuilder(context,
                            BookmarkItemDatabase.class,
                            "bookmark-item-database")
                            .build();
                    // TODO: remove fake default data
                    final ArrayList<BookmarkItem> bookmarkItems = new ArrayList<>();
                    for (int j = 0; j <= 1; j++) {
                        for (int i = 0; i < 10; i++) {
                            BookmarkItem bookmarkItem = new BookmarkItem(j * 10 + i,
                                    "20" + j + i + "年12月12日",
                                    j,
                                    "title_" + j + i,
                                    "image_url_" + j + i,
                                    i % 10,
                                    "#tag" + j + " #tag" + i,
                                    -1,
                                    i,
//                                    "description_" + j + i,
                                    "",
                                    "video_id_" + j + i);
                            bookmarkItems.add(bookmarkItem);
                        }
                    }
                    // default tab with empty title
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... voids) {
                            for (int i = 0; i < bookmarkItems.size(); i++) {
                                mBookmarkItemDatabaseInstance.bookmarkDao().insert(bookmarkItems);
                            }
                            return null;
                        }
                    }.execute();
                }
            }
        }
        return mBookmarkItemDatabaseInstance;
    }
}
