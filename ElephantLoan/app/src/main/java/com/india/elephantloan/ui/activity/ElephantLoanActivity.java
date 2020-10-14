package com.india.elephantloan.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
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
import com.india.elephantloan.utils.BranchEventHelper;
import com.india.elephantloan.utils.CountDownUtils;
import com.india.elephantloan.utils.MyToast;
import com.india.elephantloan.utils.RazorPayHelper;
import com.india.elephantloan.utils.UIUtils;
import com.india.elephantloan.utils.UserUtil;
import com.india.elephantloan.utils.AppUtil;
import com.razorpay.Checkout;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import io.branch.referral.util.BRANCH_STANDARD_EVENT;
import io.branch.referral.util.BranchEvent;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.android.volley.VolleyLog.TAG;
import static com.android.volley.VolleyLog.e;

public class ElephantLoanActivity extends BaseActivity implements View.OnClickListener, PaymentResultWithDataListener {
    LinearLayout layoutSubmit, tvback;
    TextView tvTime;
    TextView tvMoney2000, tvMoney5000, tvMoney10000, tvMoney20000;
    TextView tvday7, tvday14, tvday30, tvday90;
    TextView tvDisbursal, tvInterest, tvRepayment, tvSmallSencurityDeposit, tvSencurityDeposit;
    int intMoney = 5000, intDay = 14;
    Timer timer1 = null;
    TimerTask timerTask = null;
    RelativeLayout loading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppUtil.setTranslucentStatus(this);
        Time();
        RazorPayHelper.init(this);
    }

    @Override
    protected int initContentView() {
        return R.layout.activity_get_loan;
    }

    @Override
    protected void initView() {
        setCusTomeTitle("Get Loan");
        tvback = findViewById(R.id.tv_jiantou);
        layoutSubmit = findViewById(R.id.layout_loan_borrow_submit);
        tvMoney2000 = findViewById(R.id.tv_loan_money2000);
        tvMoney5000 = findViewById(R.id.tv_loan_money5000);
        tvMoney10000 = findViewById(R.id.tv_loan_money10000);
        tvMoney20000 = findViewById(R.id.tv_loan_money20000);
        tvday7 = findViewById(R.id.tv_loan_day7);
        tvday14 = findViewById(R.id.tv_loan_day14);
        tvday30 = findViewById(R.id.tv_loan_day30);
        tvday90 = findViewById(R.id.tv_loan_day90);
        tvDisbursal = findViewById(R.id.tv_loan_disbursal_money);
        tvInterest = findViewById(R.id.tv_loan_interest_money);
        tvRepayment = findViewById(R.id.tv_loan_repayment);
        tvSmallSencurityDeposit = findViewById(R.id.tv_loan_sencurity_deposit_small);
        tvSencurityDeposit = findViewById(R.id.tv_loan_sencurity_deposit);
        tvTime = findViewById(R.id.tv_Loan_time);
        loading = findViewById(R.id.loaddata);
    }

    @Override
    protected void initData() {
        setAllInfo();
    }

    @Override
    protected void initListener() {
        layoutSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                MyToast.show(getApplication(),"调支付网页");
                createOrder();
            }
        });
        tvback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ElephantLoanActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        tvday7.setOnClickListener(this);
        tvday14.setOnClickListener(this);
        tvday30.setOnClickListener(this);
        tvday90.setOnClickListener(this);
        tvMoney2000.setOnClickListener(this);
        tvMoney5000.setOnClickListener(this);
        tvMoney10000.setOnClickListener(this);
        tvMoney20000.setOnClickListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopClock();
    }

    private void changeTvDayColor(TextView textView) {
        tvday7.setBackgroundResource(R.drawable.unclick_maincolor_bt_bg_small);
        tvday7.setTextColor(UIUtils.getColor(R.color.main_color));
        tvday14.setBackgroundResource(R.drawable.unclick_maincolor_bt_bg_small);
        tvday14.setTextColor(UIUtils.getColor(R.color.main_color));
        tvday30.setBackgroundResource(R.drawable.unclick_maincolor_bt_bg_small);
        tvday30.setTextColor(UIUtils.getColor(R.color.main_color));
        tvday90.setBackgroundResource(R.drawable.unclick_maincolor_bt_bg_small);
        tvday90.setTextColor(UIUtils.getColor(R.color.main_color));
        textView.setBackgroundResource(R.drawable.maincolor_bt_bg_small);
        textView.setTextColor(UIUtils.getColor(R.color.white));
    }

    private void changeTvMoneyColor(TextView textView) {
        tvMoney2000.setBackgroundResource(R.drawable.unclick_maincolor_bt_bg_small);
        tvMoney2000.setTextColor(UIUtils.getColor(R.color.main_color));
        tvMoney5000.setBackgroundResource(R.drawable.unclick_maincolor_bt_bg_small);
        tvMoney5000.setTextColor(UIUtils.getColor(R.color.main_color));
        tvMoney10000.setBackgroundResource(R.drawable.unclick_maincolor_bt_bg_small);
        tvMoney10000.setTextColor(UIUtils.getColor(R.color.main_color));
        tvMoney20000.setBackgroundResource(R.drawable.unclick_maincolor_bt_bg_small);
        tvMoney20000.setTextColor(UIUtils.getColor(R.color.main_color));
        textView.setBackgroundResource(R.drawable.maincolor_bt_bg_small);
        textView.setTextColor(UIUtils.getColor(R.color.white));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_loan_day7:
                changeTvDayColor(tvday7);
                intDay = 7;
                setAllInfo();
                break;
            case R.id.tv_loan_day14:
                changeTvDayColor(tvday14);
                intDay = 14;
                setAllInfo();
                break;
            case R.id.tv_loan_day30:
                changeTvDayColor(tvday30);
                intDay = 30;
                setAllInfo();
                break;
            case R.id.tv_loan_day90:
                changeTvDayColor(tvday90);
                intDay = 90;
                setAllInfo();
                break;
            case R.id.tv_loan_money2000:
                changeTvMoneyColor(tvMoney2000);
                intMoney = 3000;
                setAllInfo();
                break;
            case R.id.tv_loan_money5000:
                changeTvMoneyColor(tvMoney5000);
                intMoney = 5000;
                setAllInfo();
                break;
            case R.id.tv_loan_money10000:
                changeTvMoneyColor(tvMoney10000);
                intMoney = 10000;
                setAllInfo();
                break;
            case R.id.tv_loan_money20000:
                changeTvMoneyColor(tvMoney20000);
                intMoney = 20000;
                setAllInfo();
                break;
        }
    }

    private void setAllInfo() {
        if (intMoney == 3000) {
            int securityDeposit = 199;
            setMsg(securityDeposit);
        }
        if (intMoney == 5000) {
            int securityDeposit = 399;
            setMsg(securityDeposit);
        }
        if (intMoney == 10000) {
            int securityDeposit = 499;
            setMsg(securityDeposit);
        }
        if (intMoney == 20000) {
            int securityDeposit = 699;
            setMsg(securityDeposit);
        }
    }

    private void setMsg(int SencurityDeposit) {
        tvDisbursal.setText("₹" + intMoney);
        tvSmallSencurityDeposit.setText("(₹" + SencurityDeposit + ")");
        tvSencurityDeposit.setText("₹" + SencurityDeposit);
        tvInterest.setText("₹" + getInterest());
        tvRepayment.setText("₹" + getRepayment(SencurityDeposit));
    }

    public double getInterest() {
        double interest = intMoney * 0.0005 * intDay;
        return interest;
    }

    public double getRepayment(int securityDeposit) {
        double repayment = intMoney - securityDeposit + getInterest();
        return repayment;

    }

    private void Time() {
        CountDownUtils.initData(90 * 60);
        timer1 = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                if (CountDownUtils.secondNotAlready) {
                    CountDownUtils.startCount();
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            String Time = autoGenericCode(CountDownUtils.hour + "", 2)
                                    + " : " + autoGenericCode(CountDownUtils.minute + "", 2)
                                    + " : " + autoGenericCode(CountDownUtils.second + "", 2);
                            tvTime.setText(Time);
                        }
                    });

                }

            }
        };
        timer1.schedule(timerTask, 0, 1000);
    }

    private void stopClock() {

        if (timerTask != null) {//&& !timerTask.cancel()

            timerTask.cancel();

            timer1.cancel();

            Log.d("TAG", "timer.cancel() 执行。。。。。");

        }

    }

    private String autoGenericCode(String code, int num) {
        String result = "";
        // 保留num的位数
        // 0 代表前面补充0
        // num 代表长度为4
        // d 代表参数为正数型
        result = String.format("%0" + num + "d", Integer.parseInt(code));

        return result;
    }

    //创建订单
    private void createOrder() {
        loading.setVisibility(View.VISIBLE);
        String url = Constants.SEND_ORDER;
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("amount", tvSencurityDeposit.getText().toString().substring(1) + "00")
                .add("remark", "")
                .build();


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
                            JSONObject objectData = object.getJSONObject("data");
                            String amount = objectData.getString("amount");
                            String contact = objectData.getString("userPhone");
                            String email = objectData.getString("userEmail");
                            String orderId = objectData.getString("extOrderNo");
                            String userName = objectData.getString("userId");
                            RazorPayHelper.startPayment(ElephantLoanActivity.this, orderId, amount, email, contact);
                            BranchEventHelper.obtainBranchEvent(ElephantLoanActivity.this, BRANCH_STANDARD_EVENT.INITIATE_PURCHASE);
                        } else {
                            MyToast.show(getApplication(), "Network exception, please try again later.");
                        }
                    } catch (Exception e) {
                        MyToast.show(getApplication(), "Network exception, please try again later.");
                        e.printStackTrace();
                    }
                }
                Log.d(TAG, "createOrder: " + connect);
            }
        });
    }

    private void postPaySuccess(String orderId, String paymentId, String signature) {
        loading.setVisibility(View.GONE);
        String url = Constants.GET_PAY_SUCCESS;
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("orderId", orderId)
                .add("paymentId", paymentId)
                .add("signature", signature)
                .build();


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
                        Log.e("TAG", object.toString());
                        if (yes == 200) {
                            Intent intent = new Intent(ElephantLoanActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        } else {
                            MyToast.show(getApplication(), object.getString("msg"));
                        }
                    } catch (Exception e) {
                        MyToast.show(getApplication(), "Network exception, please try again later.");
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void postPayFailure(String orderId, String paymentId, String signature) {
        loading.setVisibility(View.GONE);
        String url = Constants.GET_PAY_FAILURE;
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("orderId", orderId)
                .add("paymentId", paymentId)
                .add("signature", signature)
                .build();


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
                        Log.e("TAG", object.toString());
                        if (yes == 200) {
                            Toast.makeText(ElephantLoanActivity.this, object.getString("msg"), Toast.LENGTH_SHORT);
                        } else {
                            MyToast.show(getApplication(), object.getString("msg"));
                        }
                    } catch (Exception e) {
                        MyToast.show(getApplication(), "Network exception, please try again later.");
                        e.printStackTrace();
                    }
                }

            }
        });
    }

    @Override
    public void onPaymentSuccess(String s, PaymentData paymentData) {
        try {
            postPaySuccess(paymentData.getOrderId(), paymentData.getPaymentId(), paymentData.getSignature());
            //branch支付事件跟踪
            BranchEventHelper.obtainBranchEvent(this, BRANCH_STANDARD_EVENT.PURCHASE);
            //fb支付事件跟踪
            AppEventsLogger.newLogger(this).logEvent(AppEventsConstants.EVENT_NAME_INITIATED_CHECKOUT);
            //google ads
            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.METHOD, "purchase");
            FirebaseAnalytics.getInstance(ElephantLoanActivity.this).logEvent(FirebaseAnalytics.Event.PURCHASE, bundle);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentError(int i, String s, PaymentData paymentData) {
        try {
            switch (i) {
                case Checkout.NETWORK_ERROR:
                    MyToast.showLong(ElephantLoanActivity.this, "There was a network error. For example, loss of internet connectivity");
                    break;
                case Checkout.INVALID_OPTIONS:
                    MyToast.showLong(ElephantLoanActivity.this, "An issue with options passed in checkout.open");
                    break;
                case Checkout.PAYMENT_CANCELED:
                    MyToast.showLong(ElephantLoanActivity.this, "User cancelled the payment");
                    break;
                case Checkout.TLS_ERROR:
                    MyToast.showLong(ElephantLoanActivity.this, "The device does not support TLS v1.1 or TLS v1.2.");
                    break;
            }
            postPayFailure(paymentData.getOrderId(), paymentData.getPaymentId(), paymentData.getSignature());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
