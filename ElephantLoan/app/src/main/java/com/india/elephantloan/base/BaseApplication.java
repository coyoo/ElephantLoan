package com.india.elephantloan.base;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;

import com.facebook.FacebookSdk;
import com.facebook.LoggingBehavior;
import com.india.elephantloan.constant.Constants;

import io.branch.referral.Branch;


/**
 * Created by zhangzc on 2017/4/27.
 * 全局变量
 */
public class BaseApplication extends Application {


    private static BaseApplication mInstance;
    private static Context mContext;
    //    主线程的handler
    public Handler mMainThreadHandler;
    //    主线程
    private static int mMainThreadId;
    //屏幕的宽高
    public static int H, W;


    private static SharedPreferences mSharedPreferences;

    /**
     * 得到Context
     *
     * @return
     */
    public static Context getContext() {
        return mContext;
    }

    /**
     * 得到主线程里面的创建的一个hanlder
     *
     * @return
     */
    public Handler getMainThreadHandler() {
        return mMainThreadHandler;
    }

    /**
     * 得到主线程的线程id
     *
     * @return
     */
    public static int getMainThreadId() {
        return mMainThreadId;
    }

    /**
     * 得到全局的SharedPreferences
     *
     * @return
     */
    public static SharedPreferences getSharedPreferences() {
        return mSharedPreferences;
    }

    @Override
    public void onCreate() {
        mInstance = this;
        mContext = getApplicationContext();
        mMainThreadHandler = new Handler();
        mMainThreadId = android.os.Process.myTid();
        mSharedPreferences = getSharedPreferences(Constants.NAME, MODE_PRIVATE);
        super.onCreate();
        init();
    }

    private void init() {
        // Branch logging for debugging
        Branch.enableLogging();
        // Branch object initialization
        Branch.getAutoInstance(this);
        Branch.enableLogging();
        Branch.setPlayStoreReferrerCheckTimeout(3000);

        FacebookSdk.setIsDebugEnabled(true);
        FacebookSdk.addLoggingBehavior(LoggingBehavior.APP_EVENTS);



    }

    public static BaseApplication getInstance() {
        return mInstance;
    }


}
