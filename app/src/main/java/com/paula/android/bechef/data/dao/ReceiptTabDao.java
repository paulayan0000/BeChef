package com.paula.android.bechef.data.dao;

import com.paula.android.bechef.data.entity.ReceiptTab;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Query;

@Dao
public interface ReceiptTabDao extends BaseDao<ReceiptTab> {
    @Query("SELECT tab_name FROM receipt_tab ORDER BY uid ASC")
    List<String> getAllTabTitles();

    @Query("SELECT * FROM receipt_tab ORDER BY uid ASC")
    List<ReceiptTab> getAll();
}
