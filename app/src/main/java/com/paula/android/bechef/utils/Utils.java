package com.paula.android.bechef.utils;

import android.content.Context;

public class Utils {

    public static float convertDpToPixel(float dp, Context context){
        return dp * context.getResources().getDisplayMetrics().density;
    }
}
