package com.india.elephantloan.base;

import android.os.Bundle;




import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.india.elephantloan.R;
import com.india.elephantloan.ui.view.loadingpager.ErrorLayout;
import com.india.elephantloan.ui.view.loadingpager.OnRetryListener;
import com.india.elephantloan.ui.view.loadingpager.OnShowHideViewListener;
import com.india.elephantloan.ui.view.loadingpager.StatusLayoutManager;


/**
 * Created by zhangzc on 2017/5/5.
 */

public abstract class BaseFragment extends Fragment {

    public StatusLayoutManager mLoadingPager;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return initLoadingPager();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view, savedInstanceState);
        mLoadingPager.showContent();
        initListener();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initData();
    }

    /**
     * 初始化操作
     */
    protected void init() {

    }


    protected abstract int setContentViewLayout();

//    /**
//     * 初始化布局
//     *
//     * @param inflater
//     * @param container
//     * @param savedInstanceState
//     * @return
//     */
//    protected abstract View setContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    /**
     * 初始化控件
     */
    protected abstract void initView(View view, Bundle savedInstanceState);

    /**
     * 初始化数据
     */
    protected abstract void initData();

    /**
     * 初始化监听
     */
    protected abstract void initListener();

    private View initLoadingPager() {
        mLoadingPager = StatusLayoutManager.newBuilder(getActivity())
                .contentView(setContentViewLayout())
                .emptyDataView(R.layout.no_data)
                .emptyDataRetryViewId(R.id.no_data)
                .errorLayout(new ErrorLayout(getActivity()))
                .errorRetryViewId(R.id.error)
                .loadingView(R.layout.loading)
                .netWorkErrorView(R.layout.no_net)
                .netWorkErrorRetryViewId(R.id.net_error)
                .onRetryListener(new OnRetryListener() {
                    @Override
                    public void onRetry() {
                        BaseFragment.this.onRetry();
                    }
                })
                .onShowHideViewListener(new OnShowHideViewListener() {
                    @Override
                    public void onShowView(View view, int id) {
                        BaseFragment.this.onShowView(view, id);
                    }

                    @Override
                    public void onHideView(View view, int id) {
                        BaseFragment.this.onHideView(view, id);
                    }
                })
                .build();

        return mLoadingPager.getRootLayout();

    }

    protected void setCusTomeTitle(View view,String s) {

        TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
        tv_title.setText(s);
    }

    protected void onHideView(View view, int id) {

    }

    protected void onShowView(View view, int id) {

    }

    protected void onRetry() {
        mLoadingPager.showLoading();
    }




}
