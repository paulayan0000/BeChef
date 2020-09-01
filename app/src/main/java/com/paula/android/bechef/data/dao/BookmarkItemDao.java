package com.paula.android.bechef.data.dao;

import com.paula.android.bechef.data.entity.BookmarkItem;

import java.util.ArrayList;
import java.util.List;

import androidx.room.Dao;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface BookmarkItemDao extends BaseDao<BookmarkItem> {
    @Query("UPDATE bookmark_item SET tab_uid = :newTabUid WHERE uid = :uid")
    void setNewTabUid(int uid, int newTabUid);

    @Query("SELECT * FROM bookmark_item WHERE tab_uid = :tabUid ORDER BY created_time ASC")
    List<BookmarkItem> getAllWithTimeAsc(int tabUid);

    @Query("SELECT * FROM bookmark_item WHERE tab_uid = :tabUid ORDER BY created_time DESC")
    List<BookmarkItem> getAllWithTimeDesc(int tabUid);

    @Query("SELECT * FROM bookmark_item WHERE tab_uid = :tabUid ORDER BY rating ASC")
    List<BookmarkItem> getAllWithRatingAsc(int tabUid);

    @Query("SELECT * FROM bookmark_item WHERE tab_uid = :tabUid ORDER BY rating DESC")
    List<BookmarkItem> getAllWithRatingDesc(int tabUid);
}
