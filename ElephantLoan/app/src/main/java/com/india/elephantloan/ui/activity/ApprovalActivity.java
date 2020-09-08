package com.india.elephantloan.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.india.elephantloan.R;
import com.india.elephantloan.base.BaseActivity;
import com.india.elephantloan.utils.appUtil;

public class ApprovalActivity extends BaseActivity {
    LinearLayout tvback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appUtil.setTranslucentStatus(this);
    }

    @Override
    protected int initContentView() {
        return R.layout.activity_approval;
    }

    @Override
    protected void initView() {
        setCusTomeTitle("Loan Qualification Approval");
        tvback=findViewById(R.id.tv_jiantou);

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
        tvback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(ApprovalActivity.this,MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
}
