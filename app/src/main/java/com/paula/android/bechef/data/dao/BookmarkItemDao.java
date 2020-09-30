package com.paula.android.bechef.data.dao;

import com.paula.android.bechef.data.entity.BookmarkItem;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

@Dao
public interface BookmarkItemDao extends BaseDao<BookmarkItem> {
    @Query("UPDATE bookmark_item SET tab_uid = :newTabUid WHERE uid = :uid")
    void setNewTabUid(int uid, int newTabUid);

    @Query("SELECT * FROM bookmark_item WHERE tab_uid = :tabUid ORDER BY created_time ASC")
    LiveData<List<BookmarkItem>> getAllWithTimeAscLive(int tabUid);

    @Query("SELECT * FROM bookmark_item WHERE tab_uid = :tabUid ORDER BY created_time DESC")
    LiveData<List<BookmarkItem>> getAllWithTimeDescLive(int tabUid);

    @Query("SELECT * FROM bookmark_item WHERE tab_uid = :tabUid ORDER BY rating ASC")
    LiveData<List<BookmarkItem>> getAllWithRatingAscLive(int tabUid);

    @Query("SELECT * FROM bookmark_item WHERE tab_uid = :tabUid ORDER BY rating DESC")
    LiveData<List<BookmarkItem>> getAllWithRatingDescLive(int tabUid);

    @Query("SELECT * FROM bookmark_item WHERE video_id = :videoId")
    BookmarkItem getItemWithVideoId(String videoId);
}
