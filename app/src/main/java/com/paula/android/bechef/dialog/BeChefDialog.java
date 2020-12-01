package com.paula.android.bechef.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.paula.android.bechef.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;

public class BeChefDialog extends DialogFragment implements View.OnClickListener {
    protected Context mContext;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.SmallDialogTheme);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bechef_dialog, container, false);
        mContext = view.getContext();

        ((TextView) view.findViewById(R.id.textview_dialog_title)).setText(getTitleText());
        view.findViewById(R.id.button_negative).setOnClickListener(this);
        view.findViewById(R.id.button_positive).setOnClickListener(this);

        ConstraintLayout mainLayout = view.findViewById(R.id.constraintlayout_dialog_container);
        inflater.inflate(getLayoutResource(), mainLayout, true);
        setView(view);

        setCancelable(true);
        return view;
    }

    protected String getTitleText() {
        return "";
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setCanceledOnTouchOutside(true);
        return dialog;
    }

    protected void setView(View view) {
    }

    protected int getLayoutResource() {
        return 0;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_negative:
                setNegativeButton();
                break;
            case R.id.button_positive:
                setPositiveButton();
                break;
            default:
                setOtherButtons(v.getId());
                break;
        }
    }

    protected void setPositiveButton() {
    }

    protected void setNegativeButton() {
        dismiss();
    }

    protected void setOtherButtons(int viewId) {
    }
}
