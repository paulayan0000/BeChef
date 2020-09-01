package com.paula.android.bechef.data.entity;

import androidx.room.Entity;

@Entity(tableName = "receipt_tab")
public class ReceiptTab extends BaseTab {
    public ReceiptTab(String tabName) {
        super(tabName);
    }
}
