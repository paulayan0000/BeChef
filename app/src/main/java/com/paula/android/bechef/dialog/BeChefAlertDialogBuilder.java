package com.paula.android.bechef.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.widget.Button;

import com.paula.android.bechef.R;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

public class BeChefAlertDialogBuilder extends AlertDialog.Builder {
    protected Context mContext;

    public BeChefAlertDialogBuilder(@NonNull Context context) {
        super(context, R.style.SmallDialogTheme);
        mContext = context;
        setCancelable(true);
    }

    BeChefAlertDialogBuilder(@NonNull Context context, int style) {
        super(context, style);
        mContext = context;
        setCancelable(true);
    }

    public BeChefAlertDialogBuilder setButtons(final AlertDialogClickCallback clickCallback) {
        if (clickCallback != null) {
            setPositiveButton(getPositiveWord(), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    clickCallback.onPositiveButtonClick();
                    dialog.dismiss();
                }
            });
            setNegativeButton(getNegativeWord(), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
        }
        return this;
    }

    protected String getNegativeWord() {
        return "否";
    }

    protected String getPositiveWord() {
        return "是";
    }

    public BeChefAlertDialogBuilder setCustomItems(AlertDialogItemsCallback itemsCallback) {
        if (itemsCallback != null)
            setItems(itemsCallback.getItems(), itemsCallback.getItemOnClickListener());
        return this;
    }

    @NonNull
    @Override
    public AlertDialog create() {
        final AlertDialog alertDialog = super.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button negativeButton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                negativeButton.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
                Button positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                positiveButton.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
            }
        });
        setCustomView(alertDialog);
        return alertDialog;
    }

    protected void setCustomView(AlertDialog alertDialog) {
    }
}
