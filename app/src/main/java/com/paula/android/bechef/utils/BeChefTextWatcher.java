package com.paula.android.bechef.utils;

import android.text.Editable;
import android.text.TextWatcher;

public class BeChefTextWatcher implements TextWatcher {
    private final EditTextChangeCallback mCallback;
    private int mPosition;

    public BeChefTextWatcher(EditTextChangeCallback callback) {
        mCallback = callback;
    }

    public void bindPosition(int position) {
        mPosition = position;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        mCallback.afterTextChanged(mPosition, s.toString());
    }
}