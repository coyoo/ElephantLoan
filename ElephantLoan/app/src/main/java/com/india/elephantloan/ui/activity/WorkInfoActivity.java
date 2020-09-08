package com.india.elephantloan.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.india.elephantloan.R;
import com.india.elephantloan.base.BaseActivity;
import com.india.elephantloan.constant.Constants;
import com.india.elephantloan.model.entity.GoodsCategory;
import com.india.elephantloan.net.HttpParams;
import com.india.elephantloan.utils.MyToast;
import com.india.elephantloan.utils.UserUtil;
import com.india.elephantloan.utils.appUtil;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

import static com.android.volley.VolleyLog.TAG;


public class WorkInfoActivity extends BaseActivity {
    Button btnNext;
    TextView tvEmployment,tvMonthly,tvTime;
    LinearLayout layoutEmployment,layoutMonthly,layoutTime;
    String []  sEmploymentType={"Full Time","Part Time","Freelancer","No Work"};
    String []  sMonthly={"10,000～15,000","15,000～20,000","20,000～30,000","More Than 30,000"};
    String strEmployment="",strMonthly="",strTime="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appUtil.setTranslucentStatus(this);

    }

    @Override
    protected int initContentView() {
        return R.layout.activity_work_info;
    }

    @Override
    protected void initView() {
        btnNext=findViewById(R.id.btn_work_next);
        setBack();
        setCusTomeTitle("Employment Information");
        tvEmployment=findViewById(R.id.tv_workinfo_employment_type);
        layoutEmployment=findViewById(R.id.layout_workinfo_employment_type);
        tvMonthly=findViewById(R.id.tv_workinfo_month);
        layoutMonthly=findViewById(R.id.layout_workinfo_month);
        tvTime=findViewById(R.id.tv_workinfo_since);
        layoutTime=findViewById(R.id.layout_workinfo_since);
    }

    @Override
    protected void initData() {
        getWorkInfoMessage();
    }

    @Override
    protected void initListener() {
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoBankInfo();
            }
        });
        layoutEmployment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPicker(tvEmployment,sEmploymentType);
            }
        });
        layoutMonthly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPicker(tvMonthly,sMonthly);
            }
        });
        layoutTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onYearMonthDayPicker();
            }
        });
    }

    public void onPicker(final TextView textView, String[] strings) {
        List<GoodsCategory> data = new ArrayList<>();
        for(int i=0;i<strings.length;i++){
            data.add(new GoodsCategory(i, strings[i]));
        }
        SinglePicker<GoodsCategory> picker = new SinglePicker<>(this, data);
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
                textView.setText(item.getName());
                textView.setTextColor(getApplication().getResources().getColor(R.color.main_color));
//                MyToast.show(getApplication(),"index=" + index + ", id=" + item.getId() + ", name=" + item.getName());
            }
        });
        picker.show();
    }
    public void onYearMonthDayPicker() {
        final DatePicker picker = new DatePicker(this);
        picker.setCanceledOnTouchOutside(true);
        picker.setUseWeight(true);
        picker.setTopPadding(ConvertUtils.toPx(this, 10));
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
                tvTime.setText(year + "-" + month + "-" + day);
                tvTime.setTextColor(getApplication().getResources().getColor(R.color.main_color));
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


    private Boolean isNull(String str){
        if(str.isEmpty()||str.equals("Please enter")||str.equals("Please choose")) {
            return true;
        }else {
            return false;
        }
    }

    private void getWorkInfoMessage(){
        String url = Constants.GET_USER_MSG;
        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(url)
                .get()//默认就是GET请求，可以不写
                .addHeader(Constants.CLIENT_USER_SESSION, UserUtil.getSession())
                .build();
        mLoadingPager.showLoading();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(Constants.DOMAIN, "onFailure: ");
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
                            if((!objectData.getString("employmentType").isEmpty())&&(!objectData.getString("employmentType").equals("null"))){
                                strEmployment=objectData.getString("employmentType");
                                strMonthly=objectData.getString("unconmeMonthly");
                                strTime=objectData.getString("workingSince");
                                setMSG();
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mLoadingPager.showContent();
                                }

                            });
                        }
                    }catch (Exception e){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mLoadingPager.showError();
                            }

                        });
                        e.printStackTrace();
                    }
                }
                Log.d(TAG, "onResponse: " + content);
            }
        });
    }

    private void setMSG(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(!strEmployment.isEmpty()){
                    tvEmployment.setText(strEmployment);
                    tvEmployment.setTextColor(getResources().getColor(R.color.main_color));
                }
                if(!strMonthly.isEmpty()){
                    tvMonthly.setText(strMonthly);
                    tvMonthly.setTextColor(getResources().getColor(R.color.main_color));
                }
                if(!strTime.isEmpty()){
                    tvTime.setText(strTime);
                    tvTime.setTextColor(getResources().getColor(R.color.main_color));
                }
            }
        });

    }
    private void gotoBankInfo(){
        String info1=tvEmployment.getText().toString();
        String info2=tvMonthly.getText().toString();
        String info3=tvTime.getText().toString();
        if(isNull(info1)||isNull(info2)||isNull(info3)){
            MyToast.show(getApplication(),"Please complete the information.");
        }else {
            HttpParams param = new HttpParams();
            param.put("employmentType", info1);
            param.put("unconmeMonthly", info2);
            param.put("workingSince", info3);
            String content = param.toGetJson();
            com.alibaba.fastjson.JSONObject object = JSON.parseObject(content);
            String url = Constants.SUBMIT_USER_MEG;
            OkHttpClient okHttpClient = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/json;charset=UTF-8");
            RequestBody requestBody = RequestBody.create(mediaType, object.toJSONString());
            final Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .addHeader(Constants.CLIENT_USER_SESSION, UserUtil.getSession())
                    .build();
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d(TAG, "onFailure: " + e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Log.d(TAG, response.protocol() + " " + response.code() + " " + response.message());
                    String connect = response.body().string();
                    if (response != null) {
                        try {
                            JSONObject object = new JSONObject(connect);
                            int yes = object.getInt("status");
                            if (yes == 200) {
                                Intent intent = new Intent(WorkInfoActivity.this, BankInfoActivity.class);
                                startActivity(intent);
                            } else {
                                MyToast.show(getApplication(), "Network exception, please try again later.");
                            }
                        } catch (Exception e) {
                            MyToast.show(getApplication(), "Network exception, please try again later.");
                            e.printStackTrace();
                        }
                    }
                    Log.d(TAG, "onResponse: " + connect);
                }
            });
        }
    }


}
