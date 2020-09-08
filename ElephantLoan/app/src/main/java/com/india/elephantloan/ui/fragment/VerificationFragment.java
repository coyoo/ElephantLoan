package com.india.elephantloan.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;

import cn.qqtheme.framework.picker.DatePicker;
import cn.qqtheme.framework.picker.SinglePicker;
import cn.qqtheme.framework.util.ConvertUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.alibaba.fastjson.JSON;
import com.india.elephantloan.R;
import com.india.elephantloan.base.BaseFragment;
import com.india.elephantloan.constant.Constants;
import com.india.elephantloan.model.entity.GoodsCategory;
import com.india.elephantloan.net.HttpParams;
import com.india.elephantloan.ui.activity.LoginActivity;
import com.india.elephantloan.ui.activity.MainActivity;
import com.india.elephantloan.ui.activity.WorkInfoActivity;
import com.india.elephantloan.utils.MyToast;
import com.india.elephantloan.utils.UIUtils;
import com.india.elephantloan.utils.UserUtil;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.android.volley.VolleyLog.TAG;

public class VerificationFragment extends BaseFragment {

    Button btnNext;
    LinearLayout tvJianTou;
    TextView tvBirthdayDate,tvGender,tvMarried;
    LinearLayout layoutBirthday,layoutGender,layoutMarried;
    EditText edtFullName,edtEmail;
    String strBirthdayDate="",strGender="",strMarried="",strFullName="",strEmail="";
    RelativeLayout loading;


    @Override
    protected int setContentViewLayout() {
        return R.layout.fragment_verification;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        setCusTomeTitle(view,"Personal Information");
        btnNext=view.findViewById(R.id.btn_basic_next);
        tvJianTou=view.findViewById(R.id.tv_jiantou);
        tvJianTou.setVisibility(View.INVISIBLE);
        tvBirthdayDate=view.findViewById(R.id.tv_birthday_date);
        tvGender=view.findViewById(R.id.tv_basic_gender);
        tvMarried=view.findViewById(R.id.tv_basic_married);
        layoutBirthday=view.findViewById(R.id.layout_basicinfo_birthday);
        layoutGender=view.findViewById(R.id.layout_basicinfo_gender);
        layoutMarried=view.findViewById(R.id.layout_basicinfo_married);
        edtFullName=view.findViewById(R.id.edit_basicinfo_fullName);
        edtEmail=view.findViewById(R.id.edit_basicinfo_email);
        loading=view.findViewById(R.id.loaddata);
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
    protected void initData() {
        if(!UIUtils.isLogin()){
            Intent intent=new Intent(getActivity(),LoginActivity.class);
            startActivity(intent);
            MainActivity mainActivity= (MainActivity) getActivity();
            mainActivity.mBottomBarLayout.setCurrentItem(0);
        }else{
            getBasicMessage();
        }



    }




    @Override
    protected void initListener() {
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    gotoNext();
            }
        });
        layoutBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    onYearMonthDayPicker();
            }
        });
        layoutGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    onGenderPicker();
            }
        });
        layoutMarried.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    onMarriedPicker();
            }
        });
    }

    public void onYearMonthDayPicker() {
        final DatePicker picker = new DatePicker(getActivity());
        picker.setCanceledOnTouchOutside(true);
        picker.setUseWeight(true);
        picker.setTopPadding(ConvertUtils.toPx(getActivity(), 10));
        picker.setRangeEnd(2020, 12, 31);
        picker.setRangeStart(1900, 1, 1);
        picker.setSelectedItem(1995, 1, 1);
        picker.setResetWhileWheel(false);
        picker.setShadowVisible(true);
        picker.setShadowColor(0Xffeefff0);
        picker.setDividerVisible(false);
        picker.setTopLineVisible(false);
        picker.setCancelText("Cancel");
        picker.setCancelTextColor(0Xff000000);
        picker.setTitleTextColor(0Xff3AB64A);
        picker.setSubmitTextColor(0Xffffffff);
        picker.setSubmitText("Sure");
        picker.setSubmitBgColor(R.drawable.redcolor_bt_bg);
        picker.setTextColor(0Xff3AB64A);


        picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
            @Override
            public void onDatePicked(String year, String month, String day) {
                tvBirthdayDate.setText(year + "-" + month + "-" + day);
                tvBirthdayDate.setTextColor(getActivity().getResources().getColor(R.color.main_color));
//                MyToast.show(getActivity(),year + "-" + month + "-" + day);
            }
        });
        picker.setOnWheelListener(new DatePicker.OnWheelListener() {
            @Override
            public void onYearWheeled(int index, String year) {
                picker.setTitleText(year + "-" + picker.getSelectedMonth() + "-" + picker.getSelectedDay());
            }

            @Override
            public void onMonthWheeled(int index, String month) {
                picker.setTitleText(picker.getSelectedYear() + "-" + month + "-" + picker.getSelectedDay());
            }

            @Override
            public void onDayWheeled(int index, String day) {
                picker.setTitleText(picker.getSelectedYear() + "-" + picker.getSelectedMonth() + "-" + day);
            }
        });
        picker.show();
    }
    public void onGenderPicker() {
        List<GoodsCategory> data = new ArrayList<>();
        data.add(new GoodsCategory(1, "Male"));
        data.add(new GoodsCategory(2, "Female"));
        SinglePicker<GoodsCategory> picker = new SinglePicker<>(getActivity(), data);
        picker.setCanceledOnTouchOutside(true);
        picker.setSelectedIndex(1);
        picker.setCycleDisable(true);
        picker.setShadowVisible(true);
        picker.setShadowColor(0Xffeefff0);
        picker.setDividerVisible(false);
        picker.setTopLineVisible(false);
        picker.setCancelText("Cancel");
        picker.setCancelTextColor(0Xff000000);
        picker.setTitleTextColor(0Xff3AB64A);
        picker.setSubmitTextColor(0Xffffffff);
        picker.setSubmitText("Sure");
        picker.setSubmitBgColor(R.drawable.redcolor_bt_bg);
        picker.setTextColor(0Xff3AB64A);
        picker.setOnItemPickListener(new SinglePicker.OnItemPickListener<GoodsCategory>() {
            @Override
            public void onItemPicked(int index, GoodsCategory item) {
                tvGender.setText(item.getName());
                tvGender.setTextColor(getActivity().getResources().getColor(R.color.main_color));
//                MyToast.show(getActivity(),"index=" + index + ", id=" + item.getId() + ", name=" + item.getName());
            }
        });
        picker.show();
    }
    public void onMarriedPicker() {
        List<GoodsCategory> data = new ArrayList<>();
        data.add(new GoodsCategory(1, "Unmarried"));
        data.add(new GoodsCategory(2, "Married"));
        data.add(new GoodsCategory(3, "Divorce"));
        SinglePicker<GoodsCategory> picker = new SinglePicker<>(getActivity(), data);
        picker.setCanceledOnTouchOutside(true);
        picker.setSelectedIndex(1);
        picker.setCycleDisable(true);
        picker.setShadowVisible(true);
        picker.setShadowColor(0Xffeefff0);
        picker.setDividerVisible(false);
        picker.setTopLineVisible(false);
        picker.setCancelText("Cancel");
        picker.setCancelTextColor(0Xff000000);
        picker.setTitleTextColor(0Xff3AB64A);
        picker.setSubmitTextColor(0Xffffffff);
        picker.setSubmitText("Sure");
        picker.setSubmitBgColor(R.drawable.redcolor_bt_bg);
        picker.setTextColor(0Xff3AB64A);
        picker.setOnItemPickListener(new SinglePicker.OnItemPickListener<GoodsCategory>() {
            @Override
            public void onItemPicked(int index, GoodsCategory item) {
                tvMarried.setText(item.getName());
                tvMarried.setTextColor(getActivity().getResources().getColor(R.color.main_color));
//                MyToast.show(getActivity(),"index=" + index + ", id=" + item.getId() + ", name=" + item.getName());
            }
        });
        picker.show();
    }
    private Boolean isNull(String str){
        if(str.isEmpty()||str.equals("Please enter")||str.equals("Please choose")) {
           return true;
        }else {
            return false;
        }
    }
    private void gotoNext(){
        String info1=tvBirthdayDate.getText().toString();
        String info2=tvGender.getText().toString();
        String info3=tvMarried.getText().toString();
        String info4=edtFullName.getText().toString();
        String info5=edtEmail.getText().toString();
        if(isNull(info1)||isNull(info2)||isNull(info3)||isNull(info4)||isNull(info5)){
            MyToast.show(getActivity(),"Please complete the information.");
        }else if(!isEmailNO(info5)) {
            MyToast.show(getActivity(),"Please enter the correct email address.");
        }else{
            HttpParams param = new HttpParams();
            param.put("brithday", info1);
            param.put("gender",info2);
            param.put("married",info3);
            param.put("fullName",info4);
            param.put("email",info5);
            param.put("infoAuthStatus","3");//(0:未认证 1:已认证 2:审核中 3：开始认证)
            String content= param.toGetJson();
            com.alibaba.fastjson.JSONObject object = JSON.parseObject(content);
            String url = Constants.SUBMIT_USER_MEG;
            OkHttpClient okHttpClient = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/json;charset=UTF-8");
            RequestBody requestBody = RequestBody.create(mediaType,object.toJSONString());
            final Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .addHeader(Constants.CLIENT_USER_SESSION,UserUtil.getSession())
                    .build();

            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d(TAG, "onFailure: " + e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Log.d(TAG, response.protocol() + " " +response.code() + " " + response.message());
                    String connect =response.body().string();
                    if (response != null) {
                        try {
                            JSONObject object = new JSONObject(connect);
                            int yes = object.getInt("status");
                            if(yes==200){
                                Intent intent=new Intent(getActivity(), WorkInfoActivity.class);
                                startActivity(intent);
                            }else{
                                MyToast.show(getActivity(),"Network exception, please try again later.");
                            }
                        }catch (Exception e){
                            MyToast.show(getActivity(),"Network exception, please try again later.");
                            e.printStackTrace();
                        }
                    }
                    Log.d(TAG, "onResponse: " + connect);
                }
            });
        }
    }
    private void getBasicMessage(){
        String url = Constants.GET_USER_MSG;
        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(url)
                .get()//默认就是GET请求，可以不写
                .addHeader(Constants.CLIENT_USER_SESSION,UserUtil.getSession())
                .build();
        loading.setVisibility(View.VISIBLE);
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d(TAG, response.protocol() + " " +response.code() + " " + response.message());
                String content=response.body().string();
                if (response != null) {
                    try {
                        JSONObject object = new JSONObject(content);
                        int yes = object.getInt("status");
                        if(yes==200){
                            JSONObject objectData =object.getJSONObject("data");
                            if((!objectData.getString("brithday").isEmpty())&&(!objectData.getString("brithday").equals("null"))) {
                                strBirthdayDate = objectData.getString("brithday");
                                strGender = objectData.getString("gender");
                                strMarried = objectData.getString("married");
                                strFullName = objectData.getString("fullName");
                                strEmail = objectData.getString("email");
                                setMSG();
                            }
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    loading.setVisibility(View.GONE);
                                }

                            });
                        }else{
                            MyToast.show(getActivity(), "Network exception, please try again later.");
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                        MyToast.show(getActivity(), "Network exception, please try again later.");
                    }
                }
                Log.d(TAG, "onResponse: " + content);
            }
        });
    }


    private void setMSG(){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(!strBirthdayDate.isEmpty()){
                    tvBirthdayDate.setText(strBirthdayDate);
                    tvBirthdayDate.setTextColor(getActivity().getResources().getColor(R.color.main_color));
                }
                if(!strGender.isEmpty()){
                    tvGender.setText(strGender);
                    tvGender.setTextColor(getActivity().getResources().getColor(R.color.main_color));
                }
                if(!strMarried.isEmpty()){
                    tvMarried.setText(strMarried);
                    tvMarried.setTextColor(getActivity().getResources().getColor(R.color.main_color));
                }
                if(!strFullName.isEmpty()){
                    edtFullName.setText(strFullName);
                }
                if(!strEmail.isEmpty()){
                    edtEmail.setText(strEmail);
                }


            }
        });

    }
    public static boolean isEmailNO(String email){
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            // 错误的邮箱格式
            return false;
        } else {
            // 校验通过
            return true;
        }
    }

}
