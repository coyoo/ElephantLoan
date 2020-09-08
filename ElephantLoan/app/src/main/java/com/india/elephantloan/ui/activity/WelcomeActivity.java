package com.india.elephantloan.ui.activity;


import android.content.Intent;
import android.os.Bundle;

import com.india.elephantloan.R;
import com.india.elephantloan.base.BaseActivity;
import com.india.elephantloan.utils.appUtil;

import java.util.Timer;
import java.util.TimerTask;

import io.branch.referral.Branch;

public class WelcomeActivity extends BaseActivity {
    private Branch branch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startMainActivity();
    }






    @Override
    protected void init() {
        super.init();
        appUtil.setTranslucentStatus(this);
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
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        this.setIntent(intent);
    }

    private void startMainActivity(){

        TimerTask delayTask = new TimerTask() {
            @Override
            public void run() {
                Intent mainIntent = new Intent(WelcomeActivity.this,MainActivity.class);
                startActivity(mainIntent);
                WelcomeActivity.this.finish();
            }
        };
        Timer timer = new Timer();
        timer.schedule(delayTask,3000);//延时两秒执行 run 里面的操作
    }
}
