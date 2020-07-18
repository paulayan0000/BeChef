package com.paula.android.bechef.discover;

import android.os.AsyncTask;
import android.util.Log;

import com.paula.android.bechef.LoadDiscoverDataCallback;
import com.paula.android.bechef.LoadSpecificDataTask;
import com.paula.android.bechef.objects.AppDatabase;
import com.paula.android.bechef.objects.DiscoverItem;

import java.util.ArrayList;
import java.util.List;

import androidx.room.Room;

import static com.google.android.gms.common.internal.Preconditions.checkNotNull;

public class DiscoverPresenter implements DiscoverContract.Presenter{

    private static final String LOG_TAG = DiscoverPresenter.class.getSimpleName();

    private final DiscoverContract.View mDiscoverView;
    private ArrayList<String> mTabtitles = new ArrayList<>();

    public DiscoverPresenter(DiscoverContract.View discoverView) {
        mDiscoverView = checkNotNull(discoverView, "discoverView cannot be null!");
        mDiscoverView.setPresenter(this);
    }

    @Override
    public void start() {
        boolean isDatabaseExist = ((DiscoverFragment) mDiscoverView).getContext().getDatabasePath("database-name").exists();
        mTabtitles.clear();

        DiscoverFragment fragment = (DiscoverFragment) mDiscoverView;
        final AppDatabase db = Room.databaseBuilder(fragment.getContext(),
                AppDatabase.class, "database-name").build();
        if (isDatabaseExist) {
            // read from database
            new LoadSpecificDataTask(db, new LoadDiscoverDataCallback() {
                @Override
                public void onCompleted(List<String> result) {
                    mTabtitles.addAll(result);
                    mDiscoverView.showDiscoverUi(mTabtitles);
                }

                @Override
                public List<String> doInBackground(AppDatabase database) {
                    return new ArrayList<>(database.userDao().getAllTabTitles());
                }
            }).execute();

        } else {
            // TODO: read Json file (DefaultChannelId.json),
            // add default data
            DiscoverItem discoverItem1 = new DiscoverItem(0,
                    "UCQGVzUNv0UTn-t0Xzd06E4Q",
                    "Wecook123 料理123");
            DiscoverItem discoverItem2 = new DiscoverItem(1,
                    "UCr90FXGOO8nAE9B6FAUeTNA",
                    "MASAの料理ABC");
            DiscoverItem discoverItem3 = new DiscoverItem(2,
                    "UCReIdTavsve16EJlilnTPNg", "iCook 愛料理");
            DiscoverItem discoverItem4 = new DiscoverItem(3,
                    "UCctVKh07hVAyQtqpl75pxYA", "楊桃美食網");
            DiscoverItem discoverItem5 = new DiscoverItem(4,
                    "UCOJDuGX9SqzPkureXZfS60w", "乾杯與小菜的日常");

            mTabtitles.add("Wecook123 料理123"); //UCQGVzUNv0UTn-t0Xzd06E4Q
            mTabtitles.add("MASAの料理ABC"); //UCr90FXGOO8nAE9B6FAUeTNA
            mTabtitles.add("iCook 愛料理"); //UCReIdTavsve16EJlilnTPNg
            mTabtitles.add("楊桃美食網"); //UCctVKh07hVAyQtqpl75pxYA
            mTabtitles.add("乾杯與小菜的日常"); //UCOJDuGX9SqzPkureXZfS60w

            mDiscoverView.showDiscoverUi(mTabtitles);

            final ArrayList<DiscoverItem> discoverItems = new ArrayList<>();
            discoverItems.add(discoverItem1);
            discoverItems.add(discoverItem2);
            discoverItems.add(discoverItem3);
            discoverItems.add(discoverItem4);
            discoverItems.add(discoverItem5);

            AsyncTask asyncTask = new AsyncTask() {
                @Override
                protected Object doInBackground(Object[] objects) {
                    for (int i = 0; i < discoverItems.size(); i++) {
                        db.userDao().insert(discoverItems);
                    }
                    return null;
                }
            };

            asyncTask.execute();

        }
    }
}
