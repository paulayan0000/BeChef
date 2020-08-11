package com.paula.android.bechef.data;

import androidx.room.RoomDatabase;

public interface LoadDataCallback {

    void doInBackground(RoomDatabase database);

    void onCompleted();
}
