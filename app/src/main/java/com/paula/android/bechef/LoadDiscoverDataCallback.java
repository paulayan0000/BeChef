package com.paula.android.bechef;

import com.paula.android.bechef.objects.AppDatabase;

import java.util.ArrayList;
import java.util.List;

public interface LoadDiscoverDataCallback {

    void onCompleted(List<String> result);

    List<String> doInBackground(AppDatabase database);
}
