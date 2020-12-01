package com.paula.android.bechef.data.dao;

import com.paula.android.bechef.data.entity.BaseTab;
import com.paula.android.bechef.data.entity.ReceiptTab;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface ReceiptTabDao extends BaseDao<ReceiptTab> {
    @Query("SELECT tab_name FROM receipt_tab ORDER BY uid ASC")
    List<String> getAllTabTitles();

    @Query("SELECT * FROM receipt_tab ORDER BY uid ASC")
    LiveData<List<ReceiptTab>> getAllLive();

    @Query("SELECT * FROM receipt_tab ORDER BY uid ASC")
    List<ReceiptTab> getAll();

    @Query("DELETE FROM receipt_tab WHERE uid = :tabUid")
    void deleteItemWithTabUid(long tabUid);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateBaseTab(ReceiptTab baseTab);
}
