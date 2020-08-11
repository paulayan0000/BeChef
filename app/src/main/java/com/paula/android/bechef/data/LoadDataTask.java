package com.paula.android.bechef.data;

import android.os.AsyncTask;
import androidx.room.RoomDatabase;

public class LoadDataTask extends AsyncTask<Void, Void, Void> {
    private RoomDatabase mDatabase;
    private LoadDataCallback mCallback;

    public LoadDataTask(RoomDatabase database, LoadDataCallback callback) {
        mDatabase = database;
        mCallback = callback;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        mCallback.doInBackground(mDatabase);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        mCallback.onCompleted();
    }
}