package com.paula.android.bechef.data;

public interface LoadDataCallback<T> {

    T getDao();

    void doInBackground(T dao);

    void onCompleted();
}
