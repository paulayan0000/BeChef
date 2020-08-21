package com.paula.android.bechef.data.database;

import android.content.Context;
import android.os.AsyncTask;
import com.paula.android.bechef.data.Material;
import com.paula.android.bechef.data.Step;
import com.paula.android.bechef.data.converter.MaterialsConverter;
import com.paula.android.bechef.data.converter.StepsConverter;
import com.paula.android.bechef.data.dao.ReceiptItemDao;
import com.paula.android.bechef.data.entity.ReceiptItem;
import java.util.ArrayList;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {ReceiptItem.class}, version = 1, exportSchema = false)
@TypeConverters({StepsConverter.class, MaterialsConverter.class})
public abstract class ReceiptItemDatabase extends RoomDatabase {
    public abstract ReceiptItemDao receiptDao();

    private static volatile ReceiptItemDatabase mReceiptItemDatabaseInstance;

    public static ReceiptItemDatabase getInstance(Context context) {
        if (mReceiptItemDatabaseInstance == null) {
            synchronized (ReceiptItemDatabase.class) {
                if (mReceiptItemDatabaseInstance == null) {
                    mReceiptItemDatabaseInstance = Room.databaseBuilder(context,
                            ReceiptItemDatabase.class,
                            "receipt-item-database")
                            .build();
                    ArrayList<Step> steps = new ArrayList<>();
                    steps.add(new Step(1, "將麵粉加入水中，將麵粉加入水中，將麵粉加入水中，將麵粉加入水中", new ArrayList<String>(){}));
                    steps.add(new Step(2, "攪拌至麵糊表面光滑，攪拌至麵糊表面光滑，攪拌至麵糊表面光滑，攪拌至麵糊表面光滑", new ArrayList<String>()));

                    ArrayList<Material> materials = new ArrayList<>();
                    materials.add(new Material(0,"", "水", "500 c.c."));
                    materials.add(new Material(1, "", "麵粉", "500 g"));
                    materials.add(new Material(0, "調料", "鹽", "少許"));

                    // TODO: remove fake default data
                    final ArrayList<ReceiptItem> receiptItems = new ArrayList<>();
                    for (int i = 0; i < 10; i++) {
                        ReceiptItem receiptItem = new ReceiptItem(i,
                                steps,
                                materials,
                                "1" + i + "分鐘",
                                i % 10,
                                0,
                                "title_" + i,
                                "image_url_" + i,
                                i % 10,
                                "#tag" + i,
                                -1,
                                i,
                                "description_" + i,
                                "video_id_" + i);
                        receiptItems.add(receiptItem);
                    }
                    // default tab with empty title
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... voids) {
                            for (int i = 0; i < receiptItems.size(); i++) {
                                mReceiptItemDatabaseInstance.receiptDao().insert(receiptItems);
                            }
                            return null;
                        }
                    }.execute();
                }
            }
        }
        return mReceiptItemDatabaseInstance;
    }
}
