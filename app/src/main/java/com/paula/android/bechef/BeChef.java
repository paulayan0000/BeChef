package com.paula.android.bechef;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.paula.android.bechef.user.UserManager;

public class BeChef extends Application {
    @Override
    public void onTerminate() {
        super.onTerminate();
        UserManager.getInstance().clearGoogleSignInAccount();
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
