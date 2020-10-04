package com.paula.android.bechef.data.dao;

import com.paula.android.bechef.data.entity.ReceiptItem;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

@Dao
public interface ReceiptItemDao extends BaseDao<ReceiptItem> {
    @Query("UPDATE receipt_item SET tab_uid = :newTabUid WHERE uid = :uid")
    void setNewTabUid(int uid, long newTabUid);

    @Query("SELECT * FROM receipt_item WHERE tab_uid = :tabUid ORDER BY created_time ASC")
    LiveData<List<ReceiptItem>> getAllWithTimeAscLive(int tabUid);

    @Query("SELECT * FROM receipt_item WHERE tab_uid = :tabUid ORDER BY created_time DESC")
    LiveData<List<ReceiptItem>> getAllWithTimeDescLive(int tabUid);

    @Query("SELECT * FROM receipt_item WHERE tab_uid = :tabUid ORDER BY rating ASC")
    LiveData<List<ReceiptItem>> getAllWithRatingAscLive(int tabUid);

    @Query("SELECT * FROM receipt_item WHERE tab_uid = :tabUid ORDER BY rating DESC")
    LiveData<List<ReceiptItem>> getAllWithRatingDescLive(int tabUid);

}
