package com.india.elephantloan.ui.activity;
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

public class MessageBoardActivity extends BaseActivity {
    Button btnSubmit;
    EditText editText;
    String strContent="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appUtil.setTranslucentStatus(this);

    }

    @Override
    protected int initContentView() {
        return R.layout.activity_message_board;
    }

    @Override
    protected void initView() {
        setCusTomeTitle("Message Board");
        setBack();
        btnSubmit=findViewById(R.id.btn_feedback_submit);
        editText=findViewById(R.id.edt_feedback_content);

    }

    @Override
    protected void initData() {


    }

    @Override
    protected void initListener() {
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveFeedback();
            }
        });
    }

    private void saveFeedback(){
        strContent=editText.getText().toString();
        if((!strContent.isEmpty())&&(!strContent.equals("null"))){
            HttpParams param = new HttpParams();
            param.put("content", strContent);
            String content = param.toGetJson();
            com.alibaba.fastjson.JSONObject object = JSON.parseObject(content);
            String url = Constants.SAVE_FEEDBACK;
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
                                finish();
                                MyToast.show(getApplication(), "Sent successfully");
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
        }else {
            MyToast.show(getApplication(), "Content is empty");
        }

    }
}
