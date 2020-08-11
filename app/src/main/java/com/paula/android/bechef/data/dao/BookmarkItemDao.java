package com.paula.android.bechef.data.dao;

import com.paula.android.bechef.data.entity.BookmarkItem;
import java.util.ArrayList;
import java.util.List;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface BookmarkItemDao {
    @Insert (onConflict = OnConflictStrategy.IGNORE)
    void insert(ArrayList<BookmarkItem> bookmarkItems);

    @Delete
    void deleteItem(BookmarkItem bookmarkItem);

    @Delete
    void deleteItems(BookmarkItem... bookmarkItems);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateItem(BookmarkItem bookmarkItem);

    @Query("UPDATE bookmark_item SET item_index = :newItemIndex WHERE uid = :itemId")
    void updateItemIndex(int itemId, int newItemIndex);

    @Query("SELECT * FROM bookmark_item WHERE tab_uid = :tabIndex ORDER BY item_index ASC")
    List<BookmarkItem> getAllWithTab(int tabIndex);
}
