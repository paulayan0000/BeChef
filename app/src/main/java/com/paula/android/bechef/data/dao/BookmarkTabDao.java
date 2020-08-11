package com.paula.android.bechef.data.dao;

import com.paula.android.bechef.data.entity.BookmarkTab;
import java.util.ArrayList;
import java.util.List;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface BookmarkTabDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(ArrayList<BookmarkTab> bookmarkTabs);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(BookmarkTab bookmarkTab);

    @Delete
    void deleteItem(BookmarkTab bookmarkTab);

    @Delete
    void deleteItems(BookmarkTab... bookmarkTabs);

    @Query("SELECT tab_name FROM bookmark_tab ORDER BY uid ASC")
    List<String> getAllTabTitles();
}
