package com.paula.android.bechef.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.paula.android.bechef.data.entity.BookmarkTab;

import java.util.List;

@Dao
public interface BookmarkTabDao extends BaseDao<BookmarkTab> {
    @Query("SELECT * FROM bookmark_tab ORDER BY uid ASC")
    LiveData<List<BookmarkTab>> getAllLive();

    @Query("SELECT * FROM bookmark_tab ORDER BY uid ASC")
    List<BookmarkTab> getAll();

    @Query("DELETE FROM bookmark_tab WHERE uid = :tabUid")
    void deleteItemWithTabUid(long tabUid);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateBaseTab(BookmarkTab baseTab);
}
