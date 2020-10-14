package com.india.elephantloan.ui.activity;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.facebook.appevents.AppEventsConstants;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.india.elephantloan.R;
import com.india.elephantloan.base.BaseActivity;
import com.india.elephantloan.constant.Constants;
import com.india.elephantloan.net.HttpParams;
import com.india.elephantloan.net.NetUtil;
import com.india.elephantloan.utils.BranchEventHelper;
import com.india.elephantloan.utils.LogUtils;
import com.india.elephantloan.utils.MyToast;
import com.india.elephantloan.utils.UserUtil;
import com.india.elephantloan.utils.AppUtil;


import org.json.JSONObject;

import java.io.IOException;

import io.branch.referral.util.BRANCH_STANDARD_EVENT;
import io.branch.referral.util.BranchEvent;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.android.volley.VolleyLog.TAG;
import static com.facebook.appevents.AppEventsConstants.EVENT_NAME_COMPLETED_REGISTRATION;

public class LoginActivity extends BaseActivity {
    TextView tvOTP, tvUnClickOTP, tvUserAgreement;
    Button btnApply;
    EditText editPhone, editOTP;
    String mPhoneNumber, mOTP, getmOTP;
    RelativeLayout loading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppUtil.setTranslucentStatus(this);


    }

    @Override
    protected int initContentView() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        tvOTP = findViewById(R.id.tv_login_otp);
        tvUnClickOTP = findViewById(R.id.tv_login_otp2);
        btnApply = findViewById(R.id.login_apply_btn);
        editPhone = findViewById(R.id.edt_login_phone);
        editOTP = findViewById(R.id.edt_login_otp);
        loading = findViewById(R.id.loaddata);
        tvUserAgreement = findViewById(R.id.tv_user_agreement);
        tvUnClickOTP.setVisibility(View.GONE);
        tvOTP.setVisibility(View.VISIBLE);
        loading.setVisibility(View.GONE);

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
        tvOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prepareSendOTP();
            }
        });
        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prepareLogin();
            }
        });
        tvUserAgreement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, WebActivity.class);
                intent.putExtra("url", "https://www.jq62.com/useragreement/user.html");
                intent.putExtra("title", "User Login Protocol");
                startActivity(intent);
            }
        });
    }


    public void prepareSendOTP() {
        mPhoneNumber = editPhone.getText().toString();
        if (mPhoneNumber.isEmpty()) {
            Toast.makeText(LoginActivity.this, "Please input the phone number,cannot be empty.", Toast.LENGTH_SHORT).show();
        } else if (mPhoneNumber.length() != 10) {
            Toast.makeText(LoginActivity.this, "Please enter the correct phone number.", Toast.LENGTH_SHORT).show();
        } else {
            loading.setVisibility(View.VISIBLE);
            getOTP();
        }
    }

    public void isRegister() {
        HttpParams param = new HttpParams();
        param.put("phone", mPhoneNumber);
        String url = Constants.IS_REGISTER + param.toGetParams();

        Call call = request(param, url);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(Constants.DOMAIN, "onFailure: ");
                changeUI(2);
                MyToast.show(LoginActivity.this, "Network exception, please try again later.");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response != null) {
                    try {
                        JSONObject object = new JSONObject(response.body().string());
                        int yes = object.getInt("status");
                        if (yes == 200) {
                            boolean isRegister = object.getBoolean("data");
                            if (!isRegister) {
                                //branch注册事件跟踪
                                BranchEventHelper.obtainBranchEvent(LoginActivity.this,
                                        BRANCH_STANDARD_EVENT.COMPLETE_REGISTRATION, "register_event");
                                //fb注册事件跟踪
                                AppEventsLogger.newLogger(LoginActivity.this).logEvent(AppEventsConstants.EVENT_NAME_COMPLETED_REGISTRATION);
                                Bundle bundle = new Bundle();
                                bundle.putString(FirebaseAnalytics.Param.METHOD, "register");
                                FirebaseAnalytics.getInstance(LoginActivity.this).logEvent(FirebaseAnalytics.Event.SIGN_UP, bundle);
                            } else {
                                Log.e("TAG", "register");
                            }
                        }
                    } catch (Exception e) {
                        changeUI(2);
                        MyToast.show(LoginActivity.this, "Network exception, please try again later.");
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void getOTP() {
        HttpParams param = new HttpParams();
        param.put("phone", mPhoneNumber);
        String url = Constants.GET_OTP + param.toGetParams();

        Call call = request(param, url);


        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(Constants.DOMAIN, "onFailure: ");
                changeUI(2);
                MyToast.show(LoginActivity.this, "Network exception, please try again later.");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response != null) {
                    try {
                        JSONObject object = new JSONObject(response.body().string());
                        int yes = object.getInt("status");
                        if (yes == 200) {
                            getmOTP = object.getString("data");
                            changeUI(1);
                        } else {
                            changeUI(2);
                            MyToast.show(LoginActivity.this, "Network exception, please try again later.");
                        }
                    } catch (Exception e) {
                        changeUI(2);
                        MyToast.show(LoginActivity.this, "Network exception, please try again later.");
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void prepareLogin() {
        mOTP = editOTP.getText().toString();
        if (mOTP.isEmpty()) {
            Toast.makeText(LoginActivity.this, "Verification code cannot be empty.", Toast.LENGTH_SHORT).show();
        } else if (!mOTP.equals(tvUnClickOTP.getText().toString())) {
            Toast.makeText(LoginActivity.this, "Please enter the correct verification code.", Toast.LENGTH_SHORT).show();
        } else {
            isRegister();
            gotoLogin();

        }
    }

    public void gotoLogin() {
        HttpParams param = new HttpParams();
        param.put("phone", mPhoneNumber);
        param.put("verCode", mOTP);
        param.put("channelNum", UserUtil.getCampaign());
        String url = Constants.LOGIN + param.toGetParams();
        Call call = request(param, url);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(Constants.DOMAIN, "onFailure: ");
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d(TAG, response.protocol() + " " + response.code() + " " + response.message());
                String content = response.body().string();
                if (response != null) {
                    try {
                        JSONObject object = new JSONObject(content);
                        int yes = object.getInt("status");
                        if (yes == 200) {
                            Log.d(Constants.DOMAIN, "登录成功");
                            MyToast.show(LoginActivity.this, "Login successful!");
                            UserUtil.setSession(object.getString("data"));
                            UserUtil.setPhoneNum(mPhoneNumber);
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            MyToast.show(LoginActivity.this, "Network exception, please try again later.");

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                Log.d(TAG, "onResponse: " + content);
            }
        });
    }

    private Call request(HttpParams params, String url) {
        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(url)
                .get()//默认就是GET请求，可以不写
                .build();
        return okHttpClient.newCall(request);
    }

    private void changeUI(int type) {
        if (type == 1) {//接收成功otp，tv显示，且不可点击
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    loading.setVisibility(View.GONE);
                    tvOTP.setVisibility(View.GONE);
                    tvUnClickOTP.setVisibility(View.VISIBLE);
                    tvUnClickOTP.setText(getmOTP);
                }
            });
        } else if (type == 2) {//返回OTP失败，需要重新发送
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    loading.setVisibility(View.GONE);
                    tvUnClickOTP.setVisibility(View.GONE);
                    tvOTP.setVisibility(View.VISIBLE);
                }
            });
        }


    }


}
