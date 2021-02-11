package com.paula.android.bechef.data;

import android.os.AsyncTask;

public class LoadDataTask<T> extends AsyncTask<Void, Void, Void> {
    private T mDao;
    private LoadDataCallback<T> mCallback;

    public LoadDataTask(LoadDataCallback<T> callback) {
        mDao = callback.getDao();
        mCallback = callback;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        if (this.isCancelled()) return null;
        mCallback.doInBackground(mDao);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        mCallback.onCompleted();
    }
}
