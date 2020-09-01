package com.paula.android.bechef.data.dao;

import com.paula.android.bechef.data.entity.ReceiptItem;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Query;

@Dao
public interface ReceiptItemDao extends BaseDao<ReceiptItem> {
    @Query("UPDATE receipt_item SET tab_uid = :newTabUid WHERE uid = :uid")
    void setNewTabUid(int uid, int newTabUid);

    @Query("SELECT * FROM receipt_item WHERE tab_uid = :tabUid ORDER BY created_time ASC")
    List<ReceiptItem> getAllWithTimeAsc(int tabUid);

    @Query("SELECT * FROM receipt_item WHERE tab_uid = :tabUid ORDER BY created_time DESC")
    List<ReceiptItem> getAllWithTimeDesc(int tabUid);

    @Query("SELECT * FROM receipt_item WHERE tab_uid = :tabUid ORDER BY rating ASC")
    List<ReceiptItem> getAllWithRatingAsc(int tabUid);

    @Query("SELECT * FROM receipt_item WHERE tab_uid = :tabUid ORDER BY rating DESC")
    List<ReceiptItem> getAllWithRatingDesc(int tabUid);

}
