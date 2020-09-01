package com.paula.android.bechef.dialog;

import android.content.DialogInterface;

public interface AlertDialogItemsCallback {
    String[] getItems();

    DialogInterface.OnClickListener getItemOnClickListener();
}
