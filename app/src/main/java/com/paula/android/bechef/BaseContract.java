package com.paula.android.bechef;

import android.content.Context;

public interface BaseContract {
    interface BaseView<T> {
        void setPresenter(T presenter);

        Context getContext();
    }
    interface BasePresenter {
        void start();
    }
}
