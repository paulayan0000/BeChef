package com.paula.android.bechef.data.database;

import android.content.Context;
import android.os.AsyncTask;

import com.paula.android.bechef.data.Material;
import com.paula.android.bechef.data.Step;
import com.paula.android.bechef.data.dao.BookmarkItemDao;
import com.paula.android.bechef.data.dao.ReceiptItemDao;
import com.paula.android.bechef.data.entity.BookmarkItem;
import com.paula.android.bechef.data.entity.ReceiptItem;
import com.paula.android.bechef.utils.Constants;
import com.paula.android.bechef.utils.Utils;

import java.util.ArrayList;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {BookmarkItem.class, ReceiptItem.class}, version = 1, exportSchema = false)
public abstract class ItemDatabase extends RoomDatabase {
    public abstract BookmarkItemDao bookmarkDao();

    public abstract ReceiptItemDao receiptDao();

    private static volatile ItemDatabase mBookmarkItemDatabaseInstance;
    private static volatile ItemDatabase mReceiptItemDatabaseInstance;

    public static ItemDatabase getBookmarkInstance(Context context) {
        if (mBookmarkItemDatabaseInstance == null) {
            synchronized (ItemDatabase.class) {
                if (mBookmarkItemDatabaseInstance == null) {
                    mBookmarkItemDatabaseInstance = Room.databaseBuilder(context,
                            ItemDatabase.class,
                            Constants.BOOKMARK_ITEM_TABLE)
                            .build();

                    if (Utils.doesDatabaseExist(context, Constants.BOOKMARK_ITEM_TABLE))
                        return mBookmarkItemDatabaseInstance;

                    // TODO: remove fake default data
                    final ArrayList<BookmarkItem> bookmarkItems = new ArrayList<>();
                    for (int j = 0; j <= 1; j++) {
                        for (int i = 0; i < 10; i++) {
                            BookmarkItem bookmarkItem = new BookmarkItem(j + 1,
                                    "title_" + j + i,
                                    "image_url_" + j + i,
                                    i % 10,
                                    "#tag" + j + " #tag" + i,
                                    -1,
                                    "20" + j + i + "年12月12日",
                                    "description_" + j + i,
//                                    "",
                                    "video_id_" + j + i);
                            bookmarkItems.add(bookmarkItem);
                        }
                    }
                    // default tab with empty title
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... voids) {
                            mBookmarkItemDatabaseInstance.bookmarkDao().insert(bookmarkItems);
                            return null;
                        }
                    }.execute();
                }
            }
        }
        return mBookmarkItemDatabaseInstance;
    }

    public static ItemDatabase getReceiptInstance(Context context) {
        if (mReceiptItemDatabaseInstance == null) {
            synchronized (ItemDatabase.class) {
                if (mReceiptItemDatabaseInstance == null) {
                    mReceiptItemDatabaseInstance = Room.databaseBuilder(context,
                            ItemDatabase.class,
                            Constants.RECEIPT_ITEM_TABLE)
                            .build();

                    if (Utils.doesDatabaseExist(context, Constants.RECEIPT_ITEM_TABLE))
                        return mReceiptItemDatabaseInstance;

                    ArrayList<Step> steps = new ArrayList<>();
                    steps.add(new Step(1, "將麵粉加入水中，將麵粉加入水中，將麵粉加入水中，將麵粉加入水中", new ArrayList<String>() {
                    }));
                    steps.add(new Step(2, "攪拌至麵糊表面光滑，攪拌至麵糊表面光滑，攪拌至麵糊表面光滑，攪拌至麵糊表面光滑", new ArrayList<String>()));

                    ArrayList<Material> materials = new ArrayList<>();
                    materials.add(new Material(0, "", "水", "500 c.c."));
                    materials.add(new Material(1, "", "麵粉", "500 g"));
                    materials.add(new Material(0, "調料", "鹽", "少許"));

                    // TODO: remove fake default data
                    final ArrayList<ReceiptItem> receiptItems = new ArrayList<>();
                    for (int i = 0; i < 10; i++) {
                        ReceiptItem receiptItem = new ReceiptItem(
                                steps,
                                materials,
                                "1" + i + "分鐘",
                                i % 10,
                                1,
                                "title_" + i,
                                "image_url_" + i,
                                i % 10,
                                "#tag" + i,
                                -1,
                                "202" + i + "年12月12日",
                                "description_" + i,
                                "video_id_" + i);
                        receiptItems.add(receiptItem);
                    }
                    // default tab with empty title
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... voids) {
                            mReceiptItemDatabaseInstance.receiptDao().insert(receiptItems);
                            return null;
                        }
                    }.execute();
                }
            }
        }
        return mReceiptItemDatabaseInstance;
    }
}
