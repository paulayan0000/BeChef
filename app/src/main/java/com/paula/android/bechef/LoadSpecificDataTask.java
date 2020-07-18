package com.paula.android.bechef;

import android.os.AsyncTask;

import com.paula.android.bechef.objects.AppDatabase;

import java.util.ArrayList;
import java.util.List;

public class LoadSpecificDataTask extends AsyncTask<Void, Void, List<String>> {

    private AppDatabase mAppDatabase;
    private LoadDiscoverDataCallback mCallback;

    public LoadSpecificDataTask(AppDatabase appDatabase, LoadDiscoverDataCallback callback) {
        mAppDatabase = appDatabase;
        mCallback = callback;
    }

    @Override
    protected List<String> doInBackground(Void... voids) {
        return mCallback.doInBackground(mAppDatabase);
    }

    @Override
    protected void onPostExecute(List<String> result) {
        super.onPostExecute(result);
        mCallback.onCompleted(result);
    }
}