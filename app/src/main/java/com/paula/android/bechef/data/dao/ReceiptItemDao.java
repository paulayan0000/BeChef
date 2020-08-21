package com.paula.android.bechef.data.dao;

import com.paula.android.bechef.data.entity.ReceiptItem;
import java.util.ArrayList;
import java.util.List;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface ReceiptItemDao {
    @Insert (onConflict = OnConflictStrategy.IGNORE)
    void insert(ArrayList<ReceiptItem> receiptItems);

    @Delete
    void deleteItem(ReceiptItem receiptItem);

    @Delete
    void deleteItems(ReceiptItem... receiptItems);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateItem(ReceiptItem receiptItem);

    @Query("UPDATE receipt_item SET item_index = :newItemIndex WHERE uid = :itemId")
    void updateItemIndex(int itemId, int newItemIndex);

    @Query("SELECT * FROM receipt_item WHERE tab_uid = :tabIndex ORDER BY item_index ASC")
    List<ReceiptItem> getAllWithTab(int tabIndex);
}
