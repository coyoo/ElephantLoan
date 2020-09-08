package com.india.elephantloan.base;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;

import com.india.elephantloan.constant.Constants;


/**
 * Created by zhangzc on 2017/4/27.
 * 全局变量
 */
public class MyApplication extends Application {


    private static MyApplication mInstance;
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


    }

    public static MyApplication getInstance() {
        return mInstance;
    }


}
