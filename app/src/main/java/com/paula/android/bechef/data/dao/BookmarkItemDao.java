package com.paula.android.bechef.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.paula.android.bechef.data.entity.BookmarkItem;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface BookmarkItemDao extends BaseDao<BookmarkItem> {
    @Query("UPDATE bookmark_item SET tab_uid = :newTabUid WHERE uid = :uid")
    void setNewTabUid(long uid, long newTabUid);

    @Query("SELECT * FROM bookmark_item WHERE tab_uid = :tabUid ORDER BY created_time ASC")
    LiveData<List<BookmarkItem>> getAllWithTimeAscLive(long tabUid);

    @Query("SELECT * FROM bookmark_item WHERE tab_uid = :tabUid ORDER BY created_time DESC")
    LiveData<List<BookmarkItem>> getAllWithTimeDescLive(long tabUid);

    @Query("SELECT * FROM bookmark_item WHERE tab_uid = :tabUid ORDER BY rating ASC")
    LiveData<List<BookmarkItem>> getAllWithRatingAscLive(long tabUid);

    @Query("SELECT * FROM bookmark_item WHERE tab_uid = :tabUid ORDER BY rating DESC")
    LiveData<List<BookmarkItem>> getAllWithRatingDescLive(long tabUid);

    @Query("SELECT * FROM bookmark_item WHERE video_id = :videoId")
    BookmarkItem getItemWithVideoId(String videoId);

    @Query("DELETE FROM bookmark_item WHERE uid IN (:uids)")
    void deleteItemsWithUid(ArrayList<Long> uids);

    @Query("DELETE FROM bookmark_item WHERE tab_uid = :tabUid")
    void deleteItemWithTabUid(long tabUid);

    @Query("SELECT * FROM bookmark_item WHERE (tab_uid = :tabUid OR :tabUid = 0) AND title LIKE '%' || :keyword || '%'")
    List<BookmarkItem> findRelatedTitles(String keyword, long tabUid);

    @Query("SELECT * FROM bookmark_item WHERE (tab_uid = :tabUid OR :tabUid = 0) AND tags LIKE '%' || :keyword || '%'")
    List<BookmarkItem> findRelatedTags(String keyword, long tabUid);

    @Query("SELECT * FROM bookmark_item WHERE (tab_uid = :tabUid OR :tabUid = 0) AND description LIKE '%' || :keyword || '%'")
    List<BookmarkItem> findRelatedDescriptions(String keyword, long tabUid);
}