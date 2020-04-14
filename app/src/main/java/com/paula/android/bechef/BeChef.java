package com.paula.android.bechef;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.paula.android.bechef.user.UserManager;

public class BeChef extends Application {

    private static LruCache mLruCache;

    @Override
    public void onCreate() {
        super.onCreate();
        initLruCache();
    }

    private void initLruCache() {
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1204);
        int cacheSize = maxMemory / 8;

        mLruCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount() / 1024;
            }
        };
    }

    public static LruCache getLruCache() {
        return mLruCache;
    }

    private int getAppVersion(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }
}
