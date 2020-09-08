package com.india.elephantloan.ui.view.loadingpager;

import android.content.Context;

import com.india.elephantloan.R;


/**
 * Created by ChengYi
 * Copyright 2014-2016 丞易软件 All rights reserved.
 */
public class ErrorLayout extends AbsLayout {

    public ErrorLayout(Context context) {
        initLayout(R.layout.error, context);

    }

    @Override
    protected void setData(Object... objects) {

        // TODO: 2017/7/14 对错误界面填充内容
        //例如:
        //        if (mContentView == null) return;

        //        button.setText((String) objects[0]);
    }
}
