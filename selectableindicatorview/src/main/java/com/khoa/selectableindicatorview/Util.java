package com.khoa.selectableindicatorview;

import android.content.Context;

public class Util {
    public static float convertDpToPixel(float dp, Context context){
        return dp * context.getResources().getDisplayMetrics().densityDpi;
    }
}
