package com.india.elephantloan.ui.activity;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.india.elephantloan.R;
import com.india.elephantloan.base.BaseActivity;
import com.india.elephantloan.utils.UserUtil;
import com.india.elephantloan.utils.AppUtil;

import java.util.Timer;
import java.util.TimerTask;

import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.util.LinkProperties;

public class SplashActivity extends BaseActivity {
    private Branch branch;

    private boolean isFirstEnter = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startMainActivity();
        isFirstEnter = true;
    }


    @Override
    protected void init() {
        super.init();
        AppUtil.setTranslucentStatus(this);

    }

    @Override
    protected int initContentView() {
        return R.layout.activity_welcome;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void onStart() {
        super.onStart();

        if (!UserUtil.isFirstEnter()) {
            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.METHOD, "install_app");
            FirebaseAnalytics.getInstance(this).logEvent("install_app", bundle);
            UserUtil.setIsFirstEnter(true);
        }
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.METHOD, "open_app");
        FirebaseAnalytics.getInstance(SplashActivity.this).logEvent(FirebaseAnalytics.Event.APP_OPEN, bundle);

        Branch.sessionBuilder(this).withCallback(branchUniversalReferralInitListener).withData(getIntent() != null ? getIntent().getData() : null).init();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        Branch.sessionBuilder(this).withCallback(branchUniversalReferralInitListener).reInit();

    }

    private Branch.BranchUniversalReferralInitListener branchUniversalReferralInitListener = new Branch.BranchUniversalReferralInitListener() {
        @Override
        public void onInitFinished(@Nullable BranchUniversalObject branchUniversalObject, @Nullable LinkProperties linkProperties, @Nullable BranchError error) {
            String campaign = linkProperties.getCampaign();
            UserUtil.setCampaign(campaign);
        }
    };

    private void startMainActivity() {

        TimerTask delayTask = new TimerTask() {
            @Override
            public void run() {
                Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(mainIntent);
                SplashActivity.this.finish();
            }
        };
        Timer timer = new Timer();
        timer.schedule(delayTask, 3000);//延时两秒执行 run 里面的操作
    }
}
