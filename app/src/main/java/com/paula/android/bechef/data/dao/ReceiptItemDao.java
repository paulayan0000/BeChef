package com.paula.android.bechef.data.dao;

import com.paula.android.bechef.data.entity.ReceiptItem;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

@Dao
public interface ReceiptItemDao extends BaseDao<ReceiptItem> {
    @Query("UPDATE receipt_item SET tab_uid = :newTabUid WHERE uid = :uid")
    void setNewTabUid(long uid, long newTabUid);

    @Query("SELECT * FROM receipt_item WHERE tab_uid = :tabUid ORDER BY created_time ASC")
    LiveData<List<ReceiptItem>> getAllWithTimeAscLive(long tabUid);

    @Query("SELECT * FROM receipt_item WHERE tab_uid = :tabUid ORDER BY created_time DESC")
    LiveData<List<ReceiptItem>> getAllWithTimeDescLive(long tabUid);

    @Query("SELECT * FROM receipt_item WHERE tab_uid = :tabUid ORDER BY rating ASC")
    LiveData<List<ReceiptItem>> getAllWithRatingAscLive(long tabUid);

    @Query("SELECT * FROM receipt_item WHERE tab_uid = :tabUid ORDER BY rating DESC")
    LiveData<List<ReceiptItem>> getAllWithRatingDescLive(long tabUid);

    @Query("SELECT * FROM receipt_item WHERE uid = :uid")
    LiveData<ReceiptItem> getItemWithUid(int uid);

    @Query("DELETE FROM receipt_item WHERE uid = :uid")
    void deleteItemWithUid(long uid);

    @Query("DELETE FROM receipt_item WHERE tab_uid = :tabUid")
    void deleteItemWithTabUid(long tabUid);

    @Query("SELECT * FROM receipt_item WHERE (tab_uid = :tabUid OR :tabUid = 0) AND title LIKE '%' || :keyword || '%'")
    List<ReceiptItem> searchRelatedTitles(String keyword, Long tabUid);

    @Query("SELECT * FROM receipt_item WHERE (tab_uid = :tabUid OR :tabUid = 0) AND tags LIKE '%' || :keyword || '%'")
    List<ReceiptItem> searchRelatedTags(String keyword, Long tabUid);

    @Query("SELECT * FROM receipt_item WHERE (tab_uid = :tabUid OR :tabUid = 0) AND description LIKE '%' || :keyword || '%'")
    List<ReceiptItem> searchRelatedDescriptions(String keyword, Long tabUid);
}
