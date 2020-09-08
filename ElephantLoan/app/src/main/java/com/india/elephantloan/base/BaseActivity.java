package com.india.elephantloan.base;

import android.content.pm.ActivityInfo;
import android.os.Bundle;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

//import com.chengyi.jidianban.R;
//import com.chengyi.jidianban.business.caipiao.Caipiao;
//import com.chengyi.jidianban.business.caipiao.IPop;
//import com.chengyi.jidianban.business.wanfa.AbsWanfa;
//import com.chengyi.jidianban.constant.CaipiaoConst;
//import com.chengyi.jidianban.model.mode.BaseMode;
//import com.chengyi.jidianban.model.mode.PopMode;
//import com.chengyi.jidianban.ui.view.LoadingPagerManager.ErrorLayout;
//import com.chengyi.jidianban.ui.view.LoadingPagerManager.OnRetryListener;
//import com.chengyi.jidianban.ui.view.LoadingPagerManager.OnShowHideViewListener;
//import com.chengyi.jidianban.ui.view.LoadingPagerManager.StatusLayoutManager;
//import com.chengyi.jidianban.utils.AnimaUtils;
//import com.chengyi.jidianban.utils.AppUtil;
//import com.chengyi.jidianban.utils.LogUtils;
//import com.chengyi.jidianban.utils.MyToast;
//import com.chengyi.jidianban.utils.Num2Str;
//import com.chengyi.jidianban.utils.UIUtils;

import androidx.fragment.app.FragmentActivity;

import com.india.elephantloan.R;
import com.india.elephantloan.ui.view.loadingpager.ErrorLayout;
import com.india.elephantloan.ui.view.loadingpager.OnRetryListener;
import com.india.elephantloan.ui.view.loadingpager.OnShowHideViewListener;
import com.india.elephantloan.ui.view.loadingpager.StatusLayoutManager;
import com.india.elephantloan.utils.appUtil;

import java.lang.reflect.Method;


/**
 * Created by ChengYi
 * Copyright 2014-2016 丞易软件 All rights reserved.
 */
public abstract class BaseActivity extends FragmentActivity{


    public StatusLayoutManager mLoadingPager;

    @Override
    protected void onResume() {
        super.onResume();
        appUtil.closeSoftKeybord(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        preInit();
        initLoadingPager();
        setContentView(mLoadingPager.getRootLayout());
        init();
        initView();
        mLoadingPager.showContent();
        initData();
        initListener();
    }

    protected void setCusTomeTitle(String s) {

        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(s);
    }

    private void initLoadingPager() {

        mLoadingPager = StatusLayoutManager.newBuilder(this)
                .contentView(initContentView())
                .emptyDataView(R.layout.no_data)
                .emptyDataRetryViewId(R.id.no_data)
                .errorLayout(new ErrorLayout(this))
                .errorRetryViewId(R.layout.error)
                .loadingView(R.layout.loading)
                .netWorkErrorView(R.layout.no_net)
                .netWorkErrorRetryViewId(R.id.net_error)
                .onRetryListener(new OnRetryListener() {
                    @Override
                    public void onRetry() {
                        BaseActivity.this.onRetry();
                    }
                })
                .onShowHideViewListener(new OnShowHideViewListener() {
                    @Override
                    public void onShowView(View view, int id) {
                        BaseActivity.this.onShowView(view, id);
                    }

                    @Override
                    public void onHideView(View view, int id) {
                        BaseActivity.this.onHideView(view, id);
                    }
                })
                .build();

    }


    /**
     * 需要在setContentView完成的
     * 特殊任务的初始化
     */
    protected void preInit() {

    }

    /**
     * 普通任务的初始化
     */
    protected void init() {

    }

    /**
     * 初始化contentView
     *
     * @return 返回contentView的layout id
     */
    protected abstract int initContentView();

    /**
     * 初始化View，执行findViewById操作
     */
    protected abstract void initView();

    /**
     * 初始化数据
     */
    protected abstract void initData();

    /**
     * 初始化监听器
     */
    protected abstract void initListener();

    /**
     * 设置返回键
     */
    public void setBack() {
        findViewById(R.id.tv_jiantou).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 重试的回调
     */
    protected void onRetry() {
    }


    /**
     * 根据ID显示隐藏布局
     *
     * @param id
     */
    protected void onShowView(View view, int id) {

    }

    /**
     * 根据ID显示隐藏布局
     *
     * @param id
     */
    protected void onHideView(View view, int id) {

    }
    /**
     * 获取loadingpager
     *
     * @return
     */
    public StatusLayoutManager getLoadingPager() {
        return mLoadingPager;
    }



//    // 必须紧挨在startActivity或finish之后调用
    public void pullDownActivityAnim() {
        try {
            Method method = this.getClass().getMethod("overridePendingTransition", int.class, int.class);
            if (method != null) {
                method.invoke(this, R.anim.push_down_in, R.anim.push_down_out);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void showLoading(String s) {
        mLoadingPager.showLoading();
        mLoadingPager.getRootLayout().setClickable(false);

    }



    /**
     * 关闭加载dialog
     */
    public void hideLoading() {
        mLoadingPager.showContent();
        mLoadingPager.getRootLayout().setClickable(true);
    }


    // /开启加载数据动画
    protected RelativeLayout progressBarLayout;

    public void startLoadAnim() {
        progressBarLayout = (RelativeLayout) findViewById(R.id.loaddata);
        progressBarLayout.setVisibility(View.VISIBLE);
    }

}
