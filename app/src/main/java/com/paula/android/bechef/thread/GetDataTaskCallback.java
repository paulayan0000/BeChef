package com.paula.android.bechef.thread;

public interface GetDataTaskCallback<D> {
    D doInBackground();

    void onCompleted(D data);

    void onError(Exception e);
}