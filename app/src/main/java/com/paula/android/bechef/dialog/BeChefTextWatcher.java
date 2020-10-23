package com.paula.android.bechef.dialog;

import android.text.Editable;
import android.text.TextWatcher;

public class BeChefTextWatcher implements TextWatcher {
    private int position;
    private EditTextChangeCallback mCallback;

    public BeChefTextWatcher(EditTextChangeCallback callback) {
        mCallback = callback;
    }

    public void bindPosition(int position) {
        this.position = position;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        mCallback.afterTextChanged(position, s.toString());
    }
}
