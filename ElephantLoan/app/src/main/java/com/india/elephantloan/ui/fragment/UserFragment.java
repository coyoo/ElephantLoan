package com.india.elephantloan.ui.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.india.elephantloan.R;
import com.india.elephantloan.base.BaseFragment;
import com.india.elephantloan.ui.activity.LoginActivity;
import com.india.elephantloan.ui.activity.MainActivity;
import com.india.elephantloan.ui.activity.MessageBoardActivity;
import com.india.elephantloan.ui.activity.WebActivity;
import com.india.elephantloan.utils.MyToast;
import com.india.elephantloan.utils.UIUtils;
import com.india.elephantloan.utils.UserUtil;


public class UserFragment extends BaseFragment {

    LinearLayout layoutTitle;
    RelativeLayout layoutEmail,layoutMyProfile,layoutFeekback,layoutPremiunInsurance;
    TextView tvUserName,tvPhoneNumber;

    @Override
    protected int setContentViewLayout() {
        return R.layout.fragment_me;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        layoutTitle=view.findViewById(R.id.layout_mefragment_title);
        layoutEmail=view.findViewById(R.id.layout_me_credit_report);
        layoutMyProfile=view.findViewById(R.id.layout_me_my_profile);
        layoutFeekback=view.findViewById(R.id.layout_me_feedback);
        layoutPremiunInsurance=view.findViewById(R.id.layout_me_premiun_insurance);
        tvUserName=view.findViewById(R.id.tv_me_username);
        tvPhoneNumber=view.findViewById(R.id.tv_me_phone_number);
    }

    @Override
    protected void initData() {
        if (!UserUtil.getPhoneNum().equals("")){
            String str=UserUtil.getPhoneNum();
            String name="User"+UserUtil.getPhoneNum().substring(str.length()-4,str.length());
            tvUserName.setText(name);
            String number=UserUtil.getPhoneNum().substring(0,3)+"****"+UserUtil.getPhoneNum().substring(str.length()-4,str.length());
            tvPhoneNumber.setText(number);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {// 不在最前端界面显示
        } else {// 重新显示到最前端中
            initData();
        }

    }

    @Override
    protected void initListener() {
        layoutTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(UIUtils.isLogin()){
                    showMyDialog();
                }else{
                    Intent intent=new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
        layoutEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(UIUtils.isLogin()){
//                    MyToast.show(getActivity(),"信用报告开发中");
                    showEmailDialog();
                }else{
                    Intent intent=new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
        layoutMyProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(UIUtils.isLogin()){
//                    MyToast.show(getActivity(),"个人资料开发中");
                    MainActivity mainActivity= (MainActivity) getActivity();
                    mainActivity.mBottomBarLayout.setCurrentItem(1);
                }else{
                    Intent intent=new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
        layoutFeekback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(UIUtils.isLogin()){
//                    MyToast.show(getActivity(),"反馈开发中");
                    Intent intent=new Intent(getActivity(), MessageBoardActivity.class);
                    startActivity(intent);
                }else{
                    Intent intent=new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
        layoutPremiunInsurance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(UIUtils.isLogin()){
//                    MyToast.show(getActivity(),"保费保险开发中");
                    Intent intent=new Intent(getActivity(), WebActivity.class);
                    intent.putExtra("url","https://www.jq62.com/useragreement/refund.html");
                    intent.putExtra("title","Premiun Insurance");
                    startActivity(intent);
                }else{
                    Intent intent=new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
            }
        });

    }

    private void showMyDialog() {
        // 创建退出对话框
        AlertDialog isExit = new AlertDialog.Builder(getActivity()).create();
        // 设置对话框标题
        isExit.setTitle("Tips");
        // 设置对话框消息
        isExit.setMessage("Are you sure to logout?");
        // 添加选择按钮并注册监听
        isExit.setButton("OK", listener);
        isExit.setButton2("CANCEL", listener);
        // 显示对话框
        isExit.show();
    }

    DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case AlertDialog.BUTTON_POSITIVE:// "确认"按钮退出程序
                    MainActivity mainActivity= (MainActivity) getActivity();
                    mainActivity.mBottomBarLayout.setCurrentItem(0);
                    UserUtil.setSession("");
                    UserUtil.setPhoneNum("");
//                    UserUtil.saveUserData("", "");
                    break;
                case AlertDialog.BUTTON_NEGATIVE:// "取消"第二个按钮取消对话框
                    break;
                default:
                    break;
            }
        }
    };
    private void showEmailDialog() {
        // 创建退出对话框
        AlertDialog isExit = new AlertDialog.Builder(getActivity()).create();
        // 设置对话框标题
        isExit.setTitle("Email:xccxed@126.com");
        // 设置对话框消息
        isExit.setMessage("Please contact us if you have any questions.");
        // 添加选择按钮并注册监听
        isExit.setButton2("CANCEL", listener);
        // 显示对话框
        isExit.show();
    }




}
