package com.paula.android.bechef.data.dao;

import com.paula.android.bechef.data.entity.ReceiptTab;
import java.util.ArrayList;
import java.util.List;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface ReceiptTabDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(ArrayList<ReceiptTab> receiptTabs);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(ReceiptTab receiptTab);

    @Delete
    void deleteItem(ReceiptTab receiptTab);

    @Delete
    void deleteItems(ReceiptTab... receiptTabs);

    @Query("SELECT tab_name FROM receipt_tab ORDER BY uid ASC")
    List<String> getAllTabTitles();
}
