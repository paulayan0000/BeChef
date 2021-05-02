package com.paula.android.bechef.data.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.paula.android.bechef.data.dao.BookmarkItemDao;
import com.paula.android.bechef.data.dao.RecipeItemDao;
import com.paula.android.bechef.data.entity.BookmarkItem;
import com.paula.android.bechef.data.entity.RecipeItem;
import com.paula.android.bechef.utils.Constants;

@Database(entities = {BookmarkItem.class, RecipeItem.class}, version = 1, exportSchema = false)
public abstract class ItemDatabase extends RoomDatabase {
    public abstract BookmarkItemDao bookmarkDao();

    public abstract RecipeItemDao recipeDao();

    private static volatile ItemDatabase mItemDatabaseInstance;

    public static ItemDatabase getItemInstance(Context context) {
        if (mItemDatabaseInstance == null) {
            synchronized (ItemDatabase.class) {
                if (mItemDatabaseInstance == null) {
                    mItemDatabaseInstance = Room.databaseBuilder(context,
                            ItemDatabase.class,
                            Constants.ITEM_TABLE)
                            .build();
                }
            }
        }
        return mItemDatabaseInstance;
    }
}
