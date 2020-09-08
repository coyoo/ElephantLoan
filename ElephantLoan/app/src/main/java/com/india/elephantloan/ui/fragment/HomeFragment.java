package com.india.elephantloan.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.india.elephantloan.R;
import com.india.elephantloan.base.BaseFragment;
import com.india.elephantloan.base.MyApplication;
import com.india.elephantloan.constant.Constants;
import com.india.elephantloan.ui.activity.GetLoanActivity;
import com.india.elephantloan.ui.activity.LoginActivity;
import com.india.elephantloan.ui.activity.MainActivity;
import com.india.elephantloan.utils.MyToast;
import com.india.elephantloan.utils.UIUtils;
import com.india.elephantloan.utils.UserUtil;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.android.volley.VolleyLog.TAG;

public class HomeFragment extends BaseFragment {

    Button btnApply;

    @Override
    protected int setContentViewLayout() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        btnApply=view.findViewById(R.id.home_apply_btn);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(UIUtils.isLogin()){
                    getMsg();

                }else{
                    Intent intent=new Intent(MyApplication.getInstance(), LoginActivity.class);
                    startActivity(intent);
                }
            }
        });

    }
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {// 不在最前端界面显示
        } else {// 重新显示到最前端中
            initData();
        }

    }

    private void getMsg(){
            String url = Constants.GET_USER_MSG;
            OkHttpClient okHttpClient = new OkHttpClient();
            final Request request = new Request.Builder()
                    .url(url)
                    .get()//默认就是GET请求，可以不写
                    .addHeader(Constants.CLIENT_USER_SESSION, UserUtil.getSession())
                    .build();
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
                                if((!objectData.getString("infoAuthStatus").isEmpty())&&(!objectData.getString("infoAuthStatus").equals("null"))){
                                   String strInfoAuthStatus=objectData.getString("infoAuthStatus");
                                    if (strInfoAuthStatus.equals("1")){
                                        Intent intent=new Intent(getActivity(), GetLoanActivity.class);
                                        startActivity(intent);
                                    }else{
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                MainActivity mainActivity= (MainActivity) getActivity();
                                                mainActivity.mBottomBarLayout.setCurrentItem(1);
                                            }});
                                    }
                                }
                            }else{
                                MyToast.show(getContext(),"Network exception, please try again later.");
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                    Log.d(TAG, "onResponse: GET_USER_MSG" +content);
                }
            });
        }
}
