package com.paula.android.bechef.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;

public class BeChefEditText extends androidx.appcompat.widget.AppCompatEditText {

    public BeChefEditText(Context context) {
        super(context);
    }

    public BeChefEditText(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BeChefEditText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    // Fix memory leak
    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (changedView instanceof EditText) ((EditText) changedView)
                .setCursorVisible(visibility == VISIBLE);
    }
}