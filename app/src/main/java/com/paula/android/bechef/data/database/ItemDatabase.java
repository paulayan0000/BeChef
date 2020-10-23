package com.paula.android.bechef.data.database;

import android.content.Context;
import android.os.AsyncTask;

import com.paula.android.bechef.data.MaterialGroup;
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
                    ArrayList<String> imageUrls1 = new ArrayList<>();
                    imageUrls1.add("file:///storage/sdcard/1603454320654.jpg");
                    steps.add(new Step("將麵粉加入水中，將麵粉加入水中，將麵粉加入水中，將麵粉加入水中", imageUrls1));

                    ArrayList<String> imageUrls2 = new ArrayList<>();
                    imageUrls2.add("https://i.ytimg.com/vi/oqGVLj2fAnc/mqdefault.jpg");
                    imageUrls2.add("https://i.ytimg.com/vi/oqGVLj2fAnc/mqdefault.jpg");
                    steps.add(new Step("攪拌至麵糊表面光滑，攪拌至麵糊表面光滑，攪拌至麵糊表面光滑，攪拌至麵糊表面光滑", imageUrls2));

                    ArrayList<String> imageUrls3 = new ArrayList<>();
                    imageUrls3.add("https://i.ytimg.com/vi/oqGVLj2fAnc/mqdefault.jpg");
                    imageUrls3.add("https://i.ytimg.com/vi/oqGVLj2fAnc/mqdefault.jpg");
                    imageUrls3.add("https://i.ytimg.com/vi/oqGVLj2fAnc/mqdefault.jpg");
                    steps.add(new Step("攪拌至麵糊表面光滑，攪拌至麵糊表面光滑，攪拌至麵糊表面光滑，攪拌至麵糊表面光滑", imageUrls3));

                    ArrayList<String> imageUrls4 = new ArrayList<>();
                    imageUrls4.add("https://i.ytimg.com/vi/oqGVLj2fAnc/mqdefault.jpg");
                    imageUrls4.add("https://i.ytimg.com/vi/oqGVLj2fAnc/mqdefault.jpg");
                    imageUrls4.add("https://i.ytimg.com/vi/oqGVLj2fAnc/mqdefault.jpg");
                    imageUrls4.add("https://i.ytimg.com/vi/oqGVLj2fAnc/mqdefault.jpg");
                    steps.add(new Step("攪拌至麵糊表面光滑，攪拌至麵糊表面光滑，攪拌至麵糊表面光滑，攪拌至麵糊表面光滑", imageUrls4));

//                    materials.add(new Material(0, "", "水", "500 c.c."));
//                    materials.add(new Material(1, "", "麵粉", "500 g"));
//                    materials.add(new Material(0, "調料", "鹽", "少許"));
                    ArrayList<String> materialContents1 = new ArrayList<>();
                    materialContents1.add("水 500 c.c.");
                    MaterialGroup materialGroup1 = new MaterialGroup("預備食材", materialContents1);

                    ArrayList<String> materialContents2 = new ArrayList<>();
                    materialContents2.add("鹽 少許");
                    materialContents2.add("麵粉 500 g");
                    MaterialGroup materialGroup2 = new MaterialGroup("調味料", materialContents2);

                    ArrayList<MaterialGroup> materialGroups = new ArrayList<>();
                    materialGroups.add(materialGroup1);
                    materialGroups.add(materialGroup2);

                    // TODO: remove fake default data
                    final ArrayList<ReceiptItem> receiptItems = new ArrayList<>();
                    for (int j = 0; j <= 1; j++) {
                        for (int i = 0; i < 10; i++) {
                            ReceiptItem receiptItem = new ReceiptItem(
                                    steps,
                                    materialGroups,
                                    j + i + "分鐘",
                                    i,
                                    j + 1,
                                    "title_" + j + i,
                                    "https://i.ytimg.com/vi/oqGVLj2fAnc/mqdefault.jpg",
                                    i % 10,
                                    "#tag" + j + " #tag" + i,
                                    -1,
                                    "20" + j + i + "年12月12日",
                                    "description_" + j + i,
                                    "");
                            receiptItems.add(receiptItem);
                        }
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
