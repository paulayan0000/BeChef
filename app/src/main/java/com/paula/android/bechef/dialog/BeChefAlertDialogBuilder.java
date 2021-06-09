package com.paula.android.bechef.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.paula.android.bechef.BeChef;
import com.paula.android.bechef.R;

import static android.util.TypedValue.COMPLEX_UNIT_PX;

public class BeChefAlertDialogBuilder extends AlertDialog.Builder {
    Context mContext;
    private AlertDialogClickCallback mAlertDialogClickCallback;

    public BeChefAlertDialogBuilder(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        mContext = context;
        setCancelable(true);
    }

    public BeChefAlertDialogBuilder(@NonNull Context context) {
        this(context, R.style.AlertDialogTheme);
    }

    public BeChefAlertDialogBuilder setButtons(AlertDialogClickCallback clickCallback) {
        if (clickCallback != null) {
            mAlertDialogClickCallback = clickCallback;
            setPositiveButton(getPositiveWord(), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
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
        return BeChef.getAppContext().getString(R.string.no);
    }

    protected String getPositiveWord() {
        return BeChef.getAppContext().getString(R.string.yes);
    }

    @NonNull
    @Override
    public AlertDialog create() {
        final AlertDialog alertDialog = super.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button negativeButton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                negativeButton.setTextAppearance(BeChef.getAppContext(),
                        R.style.MyAlertButtonTextStyle);

                Button positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                positiveButton.setTextAppearance(BeChef.getAppContext(),
                        R.style.MyAlertButtonTextStyle);
                positiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mAlertDialogClickCallback.onPositiveButtonClick())
                            alertDialog.dismiss();
                    }
                });
            }
        });
        setCustomView(alertDialog);
        return alertDialog;
    }

    @Override
    public AlertDialog.Builder setTitle(@Nullable CharSequence title) {
        TextView tvTitle = new TextView(BeChef.getAppContext());
        tvTitle.setText(title);
        tvTitle.setGravity(Gravity.CENTER_HORIZONTAL);
        tvTitle.setTextColor(BeChef.getAppContext().getResources().getColor(R.color.black));
        float textSize = BeChef.getAppContext().getResources().getDimension(R.dimen.title_text_size);
        tvTitle.setTextSize(COMPLEX_UNIT_PX, textSize);
        int padding = (int) BeChef.getAppContext().getResources().getDimension(R.dimen.dialog_padding);
        tvTitle.setPadding(0, padding, 0, padding);
        return setCustomTitle(tvTitle);
    }

    protected void setCustomView(AlertDialog alertDialog) {
    }
}
