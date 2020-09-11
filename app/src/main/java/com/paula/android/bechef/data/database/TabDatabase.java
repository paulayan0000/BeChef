package com.paula.android.bechef.data.database;

import android.content.Context;
import android.os.AsyncTask;

import com.paula.android.bechef.data.dao.BookmarkTabDao;
import com.paula.android.bechef.data.dao.DiscoverTabDao;
import com.paula.android.bechef.data.dao.ReceiptTabDao;
import com.paula.android.bechef.data.entity.BookmarkTab;
import com.paula.android.bechef.data.entity.DiscoverTab;
import com.paula.android.bechef.data.entity.ReceiptTab;
import com.paula.android.bechef.utils.Constants;
import com.paula.android.bechef.utils.Utils;

import java.util.ArrayList;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {DiscoverTab.class, BookmarkTab.class, ReceiptTab.class}, version = 1, exportSchema = false)
public abstract class TabDatabase extends RoomDatabase {
    public abstract DiscoverTabDao discoverDao();

    public abstract BookmarkTabDao bookmarkDao();

    public abstract ReceiptTabDao receiptDao();

    private static volatile TabDatabase mDiscoverTabDatabaseInstance;
    private static volatile TabDatabase mBookmarkTabDatabaseInstance;
    private static volatile TabDatabase mReceiptTabDatabaseInstance;

    public static TabDatabase getDiscoverInstance(Context context) {
        if (mDiscoverTabDatabaseInstance == null) {
            synchronized (TabDatabase.class) {
                if (mDiscoverTabDatabaseInstance == null) {
                    mDiscoverTabDatabaseInstance = Room.databaseBuilder(context,
                            TabDatabase.class,
                            Constants.DISCOVER_TAB_TABLE)
                            .build();

                    if (Utils.doesDatabaseExist(context, Constants.DISCOVER_TAB_TABLE))
                        return mDiscoverTabDatabaseInstance;

                    // TODO: read Json file (DefaultChannelId.json),
                    // add default data
                    DiscoverTab discoverTab1 = new DiscoverTab(
                            "UCQGVzUNv0UTn-t0Xzd06E4Q",
                            "Wecook123 料理123");
                    DiscoverTab discoverTab2 = new DiscoverTab(
                            "UCr90FXGOO8nAE9B6FAUeTNA",
                            "MASAの料理ABC");
                    DiscoverTab discoverTab3 = new DiscoverTab(
                            "UCReIdTavsve16EJlilnTPNg", "iCook 愛料理");
                    DiscoverTab discoverTab4 = new DiscoverTab(
                            "UCctVKh07hVAyQtqpl75pxYA", "楊桃美食網");
                    DiscoverTab discoverTab5 = new DiscoverTab(
                            "UCOJDuGX9SqzPkureXZfS60w", "乾杯與小菜的日常");

                    final ArrayList<DiscoverTab> discoverTabs = new ArrayList<>();
                    discoverTabs.add(discoverTab1);
                    discoverTabs.add(discoverTab2);
                    discoverTabs.add(discoverTab3);
                    discoverTabs.add(discoverTab4);
                    discoverTabs.add(discoverTab5);


                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... voids) {
                            mDiscoverTabDatabaseInstance.discoverDao().insert(discoverTabs);
                            return null;
                        }
                    }.execute();
                }
            }
        }
        return mDiscoverTabDatabaseInstance;
    }

    public static TabDatabase getBookmarkInstance(Context context) {
        if (mBookmarkTabDatabaseInstance == null) {
            synchronized (TabDatabase.class) {
                if (mBookmarkTabDatabaseInstance == null) {
                    mBookmarkTabDatabaseInstance = Room.databaseBuilder(context,
                            TabDatabase.class,
                            Constants.BOOKMARK_TAB_TABLE)
                            .build();

                    if (Utils.doesDatabaseExist(context, Constants.BOOKMARK_TAB_TABLE))
                        return mBookmarkTabDatabaseInstance;

                    // add default data
                    BookmarkTab bookmarkTab1 = new BookmarkTab(
                            "未命名書籤");
                    final ArrayList<BookmarkTab> bookmarkTabs = new ArrayList<>();
                    bookmarkTabs.add(bookmarkTab1);

                    // TODO: remove fake default data
                    BookmarkTab bookmarkTab2 = new BookmarkTab(
                            "Default tab2");
                    bookmarkTabs.add(bookmarkTab2);

                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... voids) {
                            mBookmarkTabDatabaseInstance.bookmarkDao().insert(bookmarkTabs);
                            return null;
                        }
                    }.execute();
                }
            }
        }
        return mBookmarkTabDatabaseInstance;
    }

    public static TabDatabase getReceiptInstance(Context context) {
        if (mReceiptTabDatabaseInstance == null) {
            synchronized (TabDatabase.class) {
                if (mReceiptTabDatabaseInstance == null) {
                    mReceiptTabDatabaseInstance = Room.databaseBuilder(context,
                            TabDatabase.class,
                            Constants.RECEIPT_TAB_TABLE)
                            .build();

                    if (Utils.doesDatabaseExist(context, Constants.RECEIPT_TAB_TABLE))
                        return mReceiptTabDatabaseInstance;

                    // add default data
                    ReceiptTab receiptTab1 = new ReceiptTab(
                            "未命名書籤");
                    final ArrayList<ReceiptTab> receiptTabs = new ArrayList<>();
                    receiptTabs.add(receiptTab1);

                    // TODO: remove fake default data
                    ReceiptTab receiptTab2 = new ReceiptTab(
                            "Receipt 2");
                    receiptTabs.add(receiptTab2);

                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... voids) {
                            mReceiptTabDatabaseInstance.receiptDao().insert(receiptTabs);
                            return null;
                        }
                    }.execute();
                }
            }
        }
        return mReceiptTabDatabaseInstance;
    }
}
