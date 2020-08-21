package com.paula.android.bechef.data.database;

import android.content.Context;
import android.os.AsyncTask;

import com.paula.android.bechef.data.dao.ReceiptTabDao;
import com.paula.android.bechef.data.entity.BookmarkTab;
import com.paula.android.bechef.data.entity.ReceiptTab;

import java.util.ArrayList;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {ReceiptTab.class}, version = 1, exportSchema = false)
public abstract class ReceiptTabDatabase extends RoomDatabase {
    public abstract ReceiptTabDao receiptDao();

    private static volatile ReceiptTabDatabase mReceiptTabDatabaseInstance;

    public static ReceiptTabDatabase getInstance(Context context) {
        if (mReceiptTabDatabaseInstance == null) {
            synchronized (ReceiptTabDatabase.class) {
                if (mReceiptTabDatabaseInstance == null) {
                    mReceiptTabDatabaseInstance = Room.databaseBuilder(context,
                            ReceiptTabDatabase.class,
                            "receipt-tab-database")
                            .build();
                    // add default data
                    ReceiptTab receiptTab1 = new ReceiptTab(0,
                            "未命名書籤");
                    final ArrayList<ReceiptTab> receiptTabs = new ArrayList<>();
                    receiptTabs.add(receiptTab1);

                    // TODO: remove fake default data

                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... voids) {
                            for (int i = 0; i < receiptTabs.size(); i++) {
                                mReceiptTabDatabaseInstance.receiptDao().insert(receiptTabs);
                            }
                            return null;
                        }
                    }.execute();
                }
            }
        }
        return mReceiptTabDatabaseInstance;
    }
}
