package com.india.elephantloan.utils;

import com.india.elephantloan.constant.Constants;

/**
 * Created by zhangzc on 2017/6/8.
 */

public class UserUtil {


    /**
     * 得到登录用户唯一标识
     *
     * @return
     */
    public static String getSession() {
        return UIUtils.getSharedPreferences().getString(Constants.CLIENT_USER_SESSION, "");
    }

    public static void setSession(String userSession) {
         UIUtils.getSharedPreferences().edit().putString(Constants.CLIENT_USER_SESSION, userSession).commit();
    }

    /**
     * 获取绑定手机号
     * @return
     */
    public static String getPhoneNum() {
        return UIUtils.getSharedPreferences().getString(Constants.PHONE_NUM,"");
    }

    public static void setPhoneNum(String phoneNum) {
        UIUtils.getSharedPreferences().edit().putString(Constants.PHONE_NUM,phoneNum).commit();
    }

    /**
     * 获取用户生日
     * @return
     */

    public static String getBirthday() {
        return UIUtils.getSharedPreferences().getString(Constants.BIRTHDAY,"");
    }

    public static void setBirthday(String birthday) {
        UIUtils.getSharedPreferences().edit().putString(Constants.BIRTHDAY,birthday).commit();
    }

    /**
     * 获取用户性别
     * @return
     */
    public static String geGender() {
        return UIUtils.getSharedPreferences().getString(Constants.GENDER,"");
    }

    public static void setGender(String gender) {
        UIUtils.getSharedPreferences().edit().putString(Constants.GENDER,gender).commit();
    }


}
