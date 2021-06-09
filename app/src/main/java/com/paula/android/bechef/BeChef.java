package com.paula.android.bechef;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

public class BeChef extends Application {
    private static Context mAppContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mAppContext = this;
        initLruCache();
    }

    public static Context getAppContext() {
        return mAppContext;
    }

    private void initLruCache() {
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1204);
        int cacheSize = maxMemory / 8;

        new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount() / 1024;
            }
        };
    }

//    private int getAppVersion(Context context) {
//        try {
//            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
//            return info.versionCode;
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
//        return 1;
//    }
}
