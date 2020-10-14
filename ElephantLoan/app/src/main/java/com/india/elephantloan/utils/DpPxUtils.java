package com.india.elephantloan.utils;

import android.content.Context;
import android.util.DisplayMetrics;

import static com.india.elephantloan.utils.UIUtils.getContext;


public class DpPxUtils {
    public static int dp2px( int dp) {

        DisplayMetrics displayMetrics = getContext().getResources()

                .getDisplayMetrics();

        float density = displayMetrics.density;

        int px = (int) (dp * density + 0.5f);

        return px;

    }




    public static int px2dp(Context context, int px) {

        DisplayMetrics displayMetrics = context.getResources()

                .getDisplayMetrics();

        float density = displayMetrics.density;

        int dp = (int) (px / density + 0.5f);

        return dp;

    }

}
