package com.paula.android.bechef.data.database;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.paula.android.bechef.BeChef;
import com.paula.android.bechef.R;
import com.paula.android.bechef.data.dao.BookmarkTabDao;
import com.paula.android.bechef.data.dao.DiscoverTabDao;
import com.paula.android.bechef.data.dao.RecipeTabDao;
import com.paula.android.bechef.data.entity.BookmarkTab;
import com.paula.android.bechef.data.entity.DiscoverTab;
import com.paula.android.bechef.data.entity.RecipeTab;
import com.paula.android.bechef.thread.BeChefRunnableInterface;
import com.paula.android.bechef.thread.BeChefRunnable;
import com.paula.android.bechef.utils.Constants;
import com.paula.android.bechef.utils.Utils;

import java.util.ArrayList;

@Database(entities = {DiscoverTab.class, BookmarkTab.class, RecipeTab.class}, version = 1, exportSchema = false)
public abstract class TabDatabase extends RoomDatabase {
    public abstract DiscoverTabDao discoverDao();

    public abstract BookmarkTabDao bookmarkDao();

    public abstract RecipeTabDao recipeDao();

    private static volatile TabDatabase mTabDatabaseInstance;

    public static TabDatabase getTabInstance() {
        if (mTabDatabaseInstance == null) {
            synchronized (TabDatabase.class) {
                if (mTabDatabaseInstance == null) {
                    mTabDatabaseInstance = Room.databaseBuilder(BeChef.getAppContext(),
                            TabDatabase.class,
                            Constants.TAB_TABLE)
                            .build();

                    if (Utils.doesDatabaseExist(Constants.TAB_TABLE))
                        return mTabDatabaseInstance;
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

                    // add default data
                    String defaultTabName = BeChef.getAppContext().getString(R.string.default_tab_name);
                    final BookmarkTab bookmarkTab = new BookmarkTab(defaultTabName);
                    final RecipeTab recipeTab = new RecipeTab(defaultTabName);
                    new Thread(new BeChefRunnable(new BeChefRunnableInterface() {
                        @Override
                        public void runTasksOnNewThread() {
                            mTabDatabaseInstance.discoverDao().insert(discoverTabs);
                            mTabDatabaseInstance.bookmarkDao().insert(bookmarkTab);
                            mTabDatabaseInstance.recipeDao().insert(recipeTab);
                        }
                    })).start();
                }
            }
        }
        return mTabDatabaseInstance;
    }
}
