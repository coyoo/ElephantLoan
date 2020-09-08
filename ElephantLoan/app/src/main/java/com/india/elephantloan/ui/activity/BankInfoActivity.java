package com.india.elephantloan.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.india.elephantloan.R;
import com.india.elephantloan.base.BaseActivity;
import com.india.elephantloan.constant.Constants;
import com.india.elephantloan.net.HttpParams;
import com.india.elephantloan.utils.MyToast;
import com.india.elephantloan.utils.UserUtil;
import com.india.elephantloan.utils.appUtil;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.android.volley.VolleyLog.TAG;

public class BankInfoActivity extends BaseActivity {

    Button btnNext;
    EditText edtIFSC,edtBranchName,editBankNunber;
    String strIFSC="",strBranchName="",strBankNunber="";
    String strInfoAuthStatus="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appUtil.setTranslucentStatus(this);
    }

    @Override
    protected int initContentView() {
        return R.layout.activity_bank_info;
    }

    @Override
    protected void initView() {
        setBack();
        setCusTomeTitle("Bank Information");
        btnNext=findViewById(R.id.btn_bank_next);
        edtIFSC=findViewById(R.id.edt_bank_info_ifsc);
        edtBranchName=findViewById(R.id.edt_bank_info_brach);
        editBankNunber=findViewById(R.id.edt_bank_info_account);
    }

    @Override
    protected void initData() {
        getBankMessage();
    }

    @Override
    protected void initListener() {
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                putBankInfo();

            }
        });

    }

    private Boolean isNull(String str){
        if(str.isEmpty()||str.equals("Please enter")||str.equals("Please choose")) {
            return true;
        }else {
            return false;
        }
    }

    private void getBankMessage(){
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
                            if((!objectData.getString("ifscCode").isEmpty())&&(!objectData.getString("ifscCode").equals("null"))){
                                strIFSC=objectData.getString("ifscCode");
                                strBranchName=objectData.getString("branchName");
                                strBankNunber=objectData.getString("bankAccountNo");
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
                if(!strIFSC.isEmpty()){
                    edtIFSC.setText(strIFSC);
                    edtIFSC.setTextColor(getResources().getColor(R.color.main_color));
                }
                if(!strBranchName.isEmpty()){
                    edtBranchName.setText(strBranchName);
                    edtBranchName.setTextColor(getResources().getColor(R.color.main_color));
                }
                if(!strBankNunber.isEmpty()){
                    editBankNunber.setText(strBankNunber);
                    editBankNunber.setTextColor(getResources().getColor(R.color.main_color));
                }
            }
        });

    }

    private void putBankInfo(){
        String info1=edtIFSC.getText().toString();
        String info2=edtBranchName.getText().toString();
        String info3=editBankNunber.getText().toString();
        if(isNull(info1)||isNull(info2)||isNull(info3)){
            MyToast.show(getApplication(),"Please complete the information.");
        }else{
                HttpParams param = new HttpParams();
                param.put("ifscCode", info1);
                param.put("branchName", info2);
                param.put("bankAccountNo", info3);
                param.put("infoAuthStatus", "2");//(0:未认证 1:已认证 2:审核中 3：开始认证)
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
                                    JSONObject objectData =object.getJSONObject("data");
                                    if((!objectData.getString("infoAuthStatus").isEmpty())&&(!objectData.getString("infoAuthStatus").equals("null"))){
                                        strInfoAuthStatus=objectData.getString("infoAuthStatus");
                                        if (strInfoAuthStatus.equals("1")){
                                            Intent intent=new Intent(BankInfoActivity.this, GetLoanActivity.class);
                                            startActivity(intent);
                                        }else{
                                            Intent intent=new Intent(BankInfoActivity.this, ApprovalActivity.class);
                                            startActivity(intent);
                                        }

                                    }

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
