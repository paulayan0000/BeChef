package com.paula.android.bechef.data.database;

import android.content.Context;
import android.os.AsyncTask;
import com.paula.android.bechef.data.entity.DiscoverTab;
import com.paula.android.bechef.data.dao.DiscoverTabDao;
import java.util.ArrayList;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

// TODO: export schema to check with
@Database(entities = {DiscoverTab.class}, version = 1, exportSchema = false)
public abstract class DiscoverTabDatabase extends RoomDatabase {
    public abstract DiscoverTabDao discoverDao();

    private static volatile DiscoverTabDatabase mDiscoverTabDatabaseInstance;

    public static DiscoverTabDatabase getInstance(Context context) {
        if (mDiscoverTabDatabaseInstance == null) {
            synchronized (DiscoverTabDatabase.class) {
                if (mDiscoverTabDatabaseInstance == null) {
                    mDiscoverTabDatabaseInstance = Room.databaseBuilder(context,
                            DiscoverTabDatabase.class,
                            "discover-tab-database")
                            .build();
                    // TODO: read Json file (DefaultChannelId.json),
                    // add default data
                    DiscoverTab discoverTab1 = new DiscoverTab(0,
                            "UCQGVzUNv0UTn-t0Xzd06E4Q",
                            "Wecook123 料理123");
                    DiscoverTab discoverTab2 = new DiscoverTab(1,
                            "UCr90FXGOO8nAE9B6FAUeTNA",
                            "MASAの料理ABC");
                    DiscoverTab discoverTab3 = new DiscoverTab(2,
                            "UCReIdTavsve16EJlilnTPNg", "iCook 愛料理");
                    DiscoverTab discoverTab4 = new DiscoverTab(3,
                            "UCctVKh07hVAyQtqpl75pxYA", "楊桃美食網");
                    DiscoverTab discoverTab5 = new DiscoverTab(4,
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
                            for (int i = 0; i < discoverTabs.size(); i++) {
                                mDiscoverTabDatabaseInstance.discoverDao().insert(discoverTabs);
                            }
                            return null;
                        }
                    }.execute();
                }
            }
        }
        return mDiscoverTabDatabaseInstance;
    }
}
