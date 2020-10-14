package com.india.elephantloan.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.view.inputmethod.InputMethodManager;

import com.india.elephantloan.base.BaseApplication;
import com.india.elephantloan.constant.Constants;

/**
 * Created by zhangzc on 2017/4/28.
 */
public class UIUtils {
    /**
     * 得到Context
     */
    public static Context getContext() {
        return BaseApplication.getContext();
    }

    /**
     * 得到Resource对象
     */
    public static Resources getResources() {
        return getContext().getResources();
    }

    /**
     * 得到String.xml中的字符串信息
     */
    public static String getString(int resId) {
        return getResources().getString(resId);
    }

    /**
     * 得到String.xml中的字符串数组信息
     */
    public static String[] getStrings(int resId) {
        return getResources().getStringArray(resId);
    }

    /**
     * 得到Color.xml中的颜色信息
     */
    public static int getColor(int resId) {
        return getResources().getColor(resId);
    }

    /**
     * 得到应用程序包名
     *
     * @return
     */
    public static String getPackageName() {
        return getContext().getPackageName();
    }

    /**
     * 得到版本名
     *
     * @return
     */
    public static String getVersion() {

        PackageManager manager = getContext().getPackageManager();
        try {
            PackageInfo packageInfo = manager.getPackageInfo(getContext().getPackageName(), 0);
            String version = packageInfo.versionName;
            return version;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 判断用户登录状态
     *
     * @return
     */
    public static boolean isLogin() {
        String usersession = getSharedPreferences().getString(Constants.CLIENT_USER_SESSION, "");
        if (usersession == null || usersession.length() <= 0) {
            return false;
        }
        return true;

    }

    /**
     * 得到全局SharedPreferences
     *
     * @return
     */
    public static SharedPreferences getSharedPreferences() {
        return BaseApplication.getSharedPreferences();
    }

    /**
     * 隐藏软键盘
     *
     * @param activity
     */
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (activity.getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }


    /**
     * 改变loading显示
     *
     */


}
