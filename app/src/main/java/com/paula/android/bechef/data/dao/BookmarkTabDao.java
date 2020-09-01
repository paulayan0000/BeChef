package com.paula.android.bechef.data.dao;

import com.paula.android.bechef.data.entity.BookmarkTab;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Query;

@Dao
public interface BookmarkTabDao extends BaseDao<BookmarkTab> {
    @Query("SELECT tab_name FROM bookmark_tab ORDER BY uid ASC")
    List<String> getAllTabTitles();

    @Query("SELECT * FROM bookmark_tab ORDER BY uid ASC")
    List<BookmarkTab> getAll();
}
